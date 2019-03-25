package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
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

        //高亮条件分页查询

        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //获取当前页商品列表数据
        List<TbItem> content = page.getContent();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rows",content);
        return resultMap;
    }
}
