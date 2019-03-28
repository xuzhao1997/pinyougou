package com.pinyougou.page.listener;

import com.pinyougou.page.service.pageService;
import com.pinyougou.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AddItemPageMessageListener
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/28$ 下午 08:59$
 */
public class AddItemPageMessageListener implements MessageListener {


    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;

    @Autowired
    private pageService pageService;

    /**
    * @Description: 商品上架同步生成静态页
    * @Author:      XuZhao
    * @CreateDate:  19/03/28 下午 09:02
    */
    @Override
    public void onMessage(Message message) {

        try {
            TextMessage textMessage = (TextMessage) message;
            String goodsId = textMessage.getText();
            //创建一个configuration对象
            Configuration configuration = freemarkerConfig.getConfiguration();
            //加载一个个模板
            Template template = configuration.getTemplate("item.ftl");
            //创建一个模板使用的数据集
            Goods goods = pageService.findOne(Long.parseLong(goodsId));
            //创建一个writer对象,指定生成的文件名
            List<TbItem> itemList = goods.getItemList();
            for (TbItem item : itemList) {
                Map<String,Object> map = new HashMap<>();
                map.put("goods",goods);
                map.put("item",item);
                Writer out = new FileWriter("C:\\E\\item\\"+item.getId()+".html");
                //调用模板对象的 process 方法输出文件。
                template.process(map,out);
                //释放资源
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
