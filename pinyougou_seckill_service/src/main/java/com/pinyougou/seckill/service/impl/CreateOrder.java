package com.pinyougou.seckill.service.impl;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import util.IdWorker;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName: CreateOrder
 * @Description: 创建订单的线程类
 * @Author: XuZhao
 * @CreateDate: 19/04/07$ 下午 08:26$
 */
@Component
public class CreateOrder implements Runnable{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Override
    public void run() {
        //从redis队列中获取秒杀下单任务
        Map<String,Object> map  = (Map<String, Object>) redisTemplate.boundListOps("seckill_order_queue").rightPop();
        Long seckillGoodsId = (Long) map.get("seckillGoodsId");
        String userId = (String) map.get("userId");

        //获取秒杀商品   超卖  5  50
        TbSeckillGoods seckillGoods= (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(seckillGoodsId);
        //生成秒杀订单
        TbSeckillOrder seckillOrder = new TbSeckillOrder();
        //id
        long seckillOrderId = idWorker.nextId();
        seckillOrder.setId(seckillOrderId);
        //金额
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        //user_id
        seckillOrder.setUserId(userId);
        //seller_id
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        //创建时间
        seckillOrder.setCreateTime(new Date());
        //状态
        seckillOrder.setStatus("1");//1未支付

        //秒杀下单保存到数据库
        seckillOrderMapper.insert(seckillOrder);
        //秒杀下单,要将redis中秒杀商品库存减1
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);

        //在redis中,记录当前用户购买过当前商品
        redisTemplate.boundSetOps("seckill_goods_"+seckillGoodsId).add(userId);

        //同步数据库的时机,当库存为0或者秒杀结束
        if(seckillGoods.getStockCount()==0 || (new Date().getTime() > seckillGoods.getEndTime().getTime())){
            //更新秒杀商品库存到数据库
            seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
            //清除redis中秒杀商品
            redisTemplate.boundHashOps("seckill_goods").delete(seckillGoodsId);
        }else{
            //秒杀下单,秒杀还没有结束,此时要更新redis缓存中秒杀商品库存
            redisTemplate.boundHashOps("seckill_goods").put(seckillGoodsId,seckillGoods);
        }
    }
}
