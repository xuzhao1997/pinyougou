package com.pinyougou.page.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.page.service.pageService;
import com.pinyougou.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: pageController
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/26$ 下午 04:22$
 */
@RestController
@RequestMapping("/page")
public class pageController {

    @Reference
    private pageService pageService;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;

    /**
    * @Description: 生成商品静态页
    * @Author:      XuZhao
    * @CreateDate:  19/03/26 下午 04:27
    */
    @RequestMapping("/genHtml")
    public String genHtml(Long goodsId){
        try {
            //创建一个configuration对象
            Configuration configuration = freemarkerConfig.getConfiguration();
            //加载一个个模板
            Template template = configuration.getTemplate("item.ftl");
            //创建一个模板使用的数据集
            Goods goods = pageService.findOne(goodsId);
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
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail.....";
        }
    }


}
