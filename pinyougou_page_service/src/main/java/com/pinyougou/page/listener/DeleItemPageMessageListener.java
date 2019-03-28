package com.pinyougou.page.listener;

import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.util.List;

/**
 * @ClassName: DeleItemPageMessageListener
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/28$ 下午 08:59$
 */
public class DeleItemPageMessageListener implements MessageListener {

    @Autowired
    private TbItemMapper itemMapper;


    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage)message;
            String goodsId = textMessage.getText();
            //商品下架时,同步删除商品静态页
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(Long.parseLong(goodsId));
            List<TbItem> itemList = itemMapper.selectByExample(example);
            for (TbItem item : itemList) {
                new File("C:\\E\\item\\"+item.getId()+".html").delete();
            }




        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
