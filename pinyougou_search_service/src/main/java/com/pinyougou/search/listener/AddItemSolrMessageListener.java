package com.pinyougou.search.listener;

import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

/**
 * @ClassName: AddItemSolrMessageListener
 * @Description: 商品上架监听类
 * @Author: XuZhao
 * @CreateDate: 19/03/28$ 下午 07:52$
 */
public class AddItemSolrMessageListener implements MessageListener {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
    * @Description: 商品上架,同步上架商品到索引库
    * @Author:      XuZhao
    * @CreateDate:  19/03/28 下午 07:54

    */
    @Override
    public void onMessage(Message message) {

        try {
            TextMessage textMessage = (TextMessage)message;
            //上架消息是商品id值
            String goodsId = textMessage.getText();
            //查询商家商品列表
            TbItemExample exampe =  new TbItemExample();
            TbItemExample.Criteria criteria = exampe.createCriteria();
            criteria.andGoodsIdEqualTo(Long.parseLong(goodsId));

            List<TbItem> itemList = itemMapper.selectByExample(exampe);

            //同步上架商品到索引库
            solrTemplate.saveBeans(itemList);

            solrTemplate.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
