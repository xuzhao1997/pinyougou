package com.pinyougou.search.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @ClassName: DeleItemSolrMessageListener
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/28$ 下午 08:01$
 */
public class DeleItemSolrMessageListener implements MessageListener {


    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {
        try {
            //商品下架,同步下架商品从索引库中删除
            TextMessage textMessage = (TextMessage)message;
            String goodsId = textMessage.getText();
            //商品下架,同步删除索引库中的商品信息
            SolrDataQuery query = new SimpleQuery("item_goodsid:"+goodsId);
            solrTemplate.delete(query);
            solrTemplate.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
