package com.pinyougou.seckill.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: seckillTask
 * @Description: 秒杀定时任务
 * @Author: XuZhao
 * @CreateDate: 19/04/06$ 下午 08:39$
 */
@Component
public class seckillTask {


    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
    * @Description: 将数据库数据同步到redis缓存中
    * @Author:      XuZhao
    * @CreateDate:  19/04/06 下午 08:40
    */
    @Scheduled(cron = "0/10 * * * * ?")//每个十秒执行一次
    public void synchronizeSeckillGoodsToRedis(){

        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        example.createCriteria().andStatusEqualTo("1").//审核通过
                andStockCountGreaterThan(0).//有库存
                andStartTimeLessThanOrEqualTo(new Date()).//当前时间大于开始时间
                andEndTimeGreaterThan(new Date());//当前时间小于秒杀结束时间
        List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
        //将秒杀商品一个个存入redis,为了方便基于商品id获取秒杀商品内容,展示在秒杀 商品详情页
        for (TbSeckillGoods seckillGoods : seckillGoodsList) {
            redisTemplate.boundHashOps("seckill_goods").put(seckillGoods.getId(),seckillGoods);
            //List list = redisTemplate.boundHashOps("seckill_goods").values();
        }
        System.out.println("synchronizeSeckillGoodsToRedis finish....");

    }

}
