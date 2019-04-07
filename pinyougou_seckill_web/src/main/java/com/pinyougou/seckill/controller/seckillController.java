package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/04/06$ 下午 09:41$
 */
@RestController
@RequestMapping("/seckill")
public class seckillController {

   @Reference
   private SeckillService seckillService;

    /**
    * @Description: 查询秒杀商品列表
    * @Author:      XuZhao
    * @CreateDate:  19/04/06 下午 09:42
    */
    @RequestMapping("/selectSeckillGoodsList")
    public List<TbSeckillGoods> selectSeckillGoodsList(){

        return seckillService.selectSeckillGoodsFromRedis();
    }



}
