package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/04/06$ 下午 09:38$
 */
@Service
@Transactional
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private CreateOrder createOrder;

    /**
    * @Description: 查询秒杀商品列表
    * @Author:      XuZhao
    * @CreateDate:  19/04/06 下午 09:39
    */
    @Override
    public List<TbSeckillGoods> selectSeckillGoodsFromRedis() {
        return redisTemplate.boundHashOps("seckill_goods").values();
    }

    /**
    * @Description: 基于秒杀商品id查询秒杀商品详情
    * @Author:      XuZhao
    * @CreateDate:  19/04/07 下午 04:05
    */
    @Override
    public TbSeckillGoods findOne(Long seckillGoodsId) {
        return (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(seckillGoodsId);
    }

    /**
    * @Description: 保存秒杀订单
    * @Author:      XuZhao
    * @CreateDate:  19/04/07 下午 05:02
    */
    @Override
    public void submitSeckillOrder(Long seckillGoodsId,String userId) {
        //判断当前用户是否购买过当前商品
        Boolean member = redisTemplate.boundSetOps("seckill_goods_" + seckillGoodsId).isMember(userId);

        if(member){
            throw  new RuntimeException("不好意思,秒杀商品只能购买一个呦!!!");
        }

        //进入一个线程,秒杀排队人数加1
        redisTemplate.boundValueOps("seckill_goods_queue_"+seckillGoodsId).increment(1);

        //限制库存,解决超卖问题,先从redis队列中获取信息
        Object obj = redisTemplate.boundListOps("seckill_goods_queue_" + seckillGoodsId).rightPop();
        if(obj==null){
            throw  new RuntimeException("不好意思,您手慢了,商品已经售罄了");
        }
        //获取秒杀商品
        TbSeckillGoods seckillGoods =  (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(seckillGoodsId);
        //判断是否还有商品
        /*if(seckillGoods == null || seckillGoods.getStockCount() <= 0){
            throw  new RuntimeException("不好意思,您手慢了,商品已经售罄了");
        }*/
        //获取排队人数
        Long size = redisTemplate.boundValueOps("seckill_goods_queue_" + seckillGoodsId).size();
        //当排队人数大于秒杀商品库存指定值（例如：20），提醒排队人数过多
        Integer stockCount = seckillGoods.getStockCount();
        if(size>(stockCount+20)){
            throw new RuntimeException("排队人数过多");
        }

        Map<String,Object> map = new HashMap<>();
        map.put("seckillGoodsId",seckillGoodsId);
        map.put("userId",userId);
        //将秒杀下单的操作,作为任务存入到redis队列中
        redisTemplate.boundListOps("seckill_order_queue").leftPush(map);
        //开启多线程执行秒杀订单参数
        executor.execute(createOrder);

    }
}
