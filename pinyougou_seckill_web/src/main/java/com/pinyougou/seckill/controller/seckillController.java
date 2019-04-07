package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
    * @Description: 基于秒杀商品id查询秒杀商品详情
    * @Author:      XuZhao
    * @CreateDate:  19/04/07 下午 04:06
    */
    @RequestMapping("/findOne")
    public TbSeckillGoods findOne(Long seckillGoodsId){
        return seckillService.findOne(seckillGoodsId);
    }

    /**
    * @Description: 保存秒杀订单
    * @Author:      XuZhao
    * @CreateDate:  19/04/07 下午 04:59
    */
    @RequestMapping("/submitSeckillOrder")
    public Result submitSeckillOrder(Long seckillGoodsId){
        try {
            //获取登录用户
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            if(userId.equals("anonymousUser")){
                return new Result(false,"抢购前，请先登录");
            }
            seckillService.submitSeckillOrder(seckillGoodsId,userId);
            return new Result(true,"保存订单成功!!!");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"保存订单失败!!!");
        }
    }
}
