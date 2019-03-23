package com.pinyougou.solr.util;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SolrUtil
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/23$ 下午 08:47$
 */
@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
    * @Description: 导入数据库满足条件的商品数据到索引库中
     * 商品上架,而且是有效状态
    * @Author:      XuZhao
    * @CreateDate:  19/03/23 下午 08:49
    */
    public void dataImport(){
        List<TbItem> itemList = itemMapper.findAllGrounding();
        for (TbItem item : itemList) {
            //为索引库动态赋值
            String spec = item.getSpec();
            Map<String,String> specMap = JSON.parseObject(spec, Map.class);
            item.setSpecMap(specMap);
        }
        
        //导入索引库
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }


}
