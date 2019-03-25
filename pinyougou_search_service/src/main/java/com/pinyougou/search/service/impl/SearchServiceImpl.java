package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/25$ 下午 04:24$
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {


    @Autowired
    private SolrTemplate solrTemplate;


    /**
    * @Description: 商品搜索
    * @Author:      XuZhao
    * @CreateDate:  19/03/25 下午 04:24
    */
    @Override
    public Map<String, Object> search(Map searchMap) {
        //构建高亮查询对象
        HighlightQuery query = new SimpleHighlightQuery();
        //获取页面输入的关键字条件
        String keywords = (String) searchMap.get("keywords");
        //构建关键字搜索条件
        Criteria criteria = null;
        if(keywords!=null && !"".equals(keywords)){
            //输入了关键字的查询
            criteria = new Criteria("item_keywords").is(keywords);
        }else{
            //没输入关键字的查询方法,默认搜索所有
            criteria = new Criteria().expression("*:*");
        }
        //将关键字所搜条件赋予查询对象
        query.addCriteria(criteria);

        //品牌过滤条件查询
        String brand = (String) searchMap.get("brand");
        if(brand!=null && !"".equals(brand)){
            //设置品牌筛选条件
            Criteria brandcriteria1 = new Criteria("item_brand").is(brand);
            FilterQuery filterQuery = new SimpleFacetQuery(brandcriteria1);
            //将过滤条件查询对象关联到主查询对象
            query.addFilterQuery(filterQuery);
        }
        //分类过滤条件查询
        String category = (String) searchMap.get("category");
        if(category!=null && !"".equals(category)){
            //设置分类筛选条件
            Criteria categorycriteria1 = new Criteria("item_category").is(category);
            FilterQuery filterQuery = new SimpleFacetQuery(categorycriteria1);
            //将过滤条件查询对象关联到主查询对象
            query.addFilterQuery(filterQuery);
        }

        //规格过滤条件查询
        Map<String,String> specMap = (Map<String, String>) searchMap.get("spec");
        if(searchMap!=null){
            //从map中获取规格名称和选择的规格选项值
            for (String key : specMap.keySet()) {
                //设置规格筛选条件
                Criteria specCriteria1 = new Criteria("item_spec_" + key).is(specMap.get(key));
                FilterQuery filterQuery = new SimpleFacetQuery(specCriteria1);
                //将过滤条件查询对象关联到主对象
                query.addFilterQuery(filterQuery);
            }
        }

        //价格区间过滤条件筛选
        String price = (String) searchMap.get("price");
        if(price!=null && !"".equals(price)){
            //设置价格区间筛选条件
            String[] prices = price.split("-");
            if(!prices[0].equals("0")){
                //设置价格区间筛选条件
                Criteria pricecriteria1 = new Criteria("item_price").greaterThanEqual(prices[0]);
                FilterQuery filterQuery = new SimpleFacetQuery(pricecriteria1);
                query.addFilterQuery(filterQuery);
            }
            if(!prices[1].equals("*")){
                //设置价格区间筛选条件
                Criteria pricecriteria1 = new Criteria("item_price").lessThanEqual(prices[1]);
                FilterQuery filterQuery = new SimpleFacetQuery(pricecriteria1);
                query.addFilterQuery(filterQuery);
            }
        }

        //排序查询
        String sortField = (String) searchMap.get("sortField");
        String sort = (String) searchMap.get("sort");
        if(sortField!=null && !"".equals(sortField)){
            //设置排序条件
            if(sort.equals("ASC")){//升序
                query.addSort(new Sort(Sort.Direction.ASC,"item_"+sortField));
            }else{
                //降序
                query.addSort(new Sort(Sort.Direction.DESC,"item_"+sortField));
            }
        }

        //分页条件查询
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        query.setOffset((pageNo-1)*pageSize);//分页起始值
        query.setRows(pageSize);//每条记录数

        //设置高亮处理
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置高亮字段
        highlightOptions.addField("item_title");
        //设置高亮前后缀
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");
        query.setHighlightOptions(highlightOptions);

        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //获取当前页商品列表数据
        List<TbItem> content = page.getContent();
        //高亮结果处理
        for (TbItem item : content) {
            List<HighlightEntry.Highlight> highlights = page.getHighlights(item);
            //判断有没有高亮内容
            if(highlights.size()>0){
                HighlightEntry.Highlight highlight = highlights.get(0);
                //获取高亮内容
                List<String> snipplets = highlight.getSnipplets();
               //如果集合有内容,就进行替换高亮内容
                if(snipplets.size()>0){
                    item.setTitle(snipplets.get(0));
                }
            }
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rows",content);
        resultMap.put("totalPages",page.getTotalPages());
        resultMap.put("pageNo",pageNo);
        return resultMap;
    }
}
