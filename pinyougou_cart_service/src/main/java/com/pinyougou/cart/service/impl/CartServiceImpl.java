package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import groupEntity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/04/01$ 下午 06:18$
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
    * @Description: 添加商品到购物车
    * @Author:      XuZhao
    * @CreateDate:  19/04/01 下午 06:19
    */
    @Override
    public List<Cart> addItemToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //根据商品Id查询该商品关联的商家
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if(item==null){
            throw new RuntimeException("不好意思,商品走丢了!!!");
        }
        if(!item.getStatus().equals("1")){
            throw new RuntimeException("不好意思,商品状态无效!!!");
        }
        String sellerId = item.getSellerId();
        //判断商家对应的购物车是否存在购物车列表中
        Cart cart = serachCartBySellerId(cartList,sellerId);
        if(cart==null){//购物车列表存在该商家
            //创建购物车对象
            cart = new Cart();
            //商家id(需要指定的该购物车商家信息)
            cart.setSellerId(sellerId);
            //商家店铺名
            cart.setSellerName(item.getSeller());
            //构建购物车商品明细列表
            List<TbOrderItem> orderItemList = new ArrayList<>();
            //构建购物车商品对象
            TbOrderItem orderItem = createOrderItem(item,num);
            //将购物车明细对象添加到购物车商品明细列表
            orderItemList.add(orderItem);
            //将购物车明细列表添加到购物车对象
            cart.setOrderItemList(orderItemList);
            //将购物车添加到购物车列表中
            cartList.add(cart);

        }else{//说明该商家对应的购物车存在
            //判断该商品是否存在于购物车商品明细列表,如果商品存在,返回该商品
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            TbOrderItem orderItem = searchOrderItemByItemId(orderItemList,itemId);
            if( orderItem==null){//如果该商品不存在购物车明细列表中
                //创建购物车商品对象,在添加到购物车明细列表中
                orderItem = createOrderItem(item,num);
                orderItemList.add(orderItem);
            }else{//如果该商品存在购物车明细列表
                //修改购物车明细商品对象的数量和小计金额
                orderItem.setNum(orderItem.getNum()+num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));
                //商品有可能加或减,当商品数量减至0时,将商品从商品列表中移除
                if(orderItem.getNum()<=0){
                    orderItemList.remove(orderItem);
                }
                //当购物车中,商品列表中没有商品时,从购物车列表中移除该购物车对象.
                if(orderItemList.size()==0){
                    //从购物车列表中移除该购物车对象.
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }


    /**
    * @Description: 判断该商品是否存在于购物车商品明细列表,如果商品存在,返回该商品
    * @Author:      XuZhao
    * @CreateDate:  19/04/01 下午 07:41
    */

    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {

        for (TbOrderItem orderItem : orderItemList) {
            if(orderItem.getItemId().longValue()==itemId.longValue()){
                //说明购物车列表中存在添加的商品,返回该商品
                return orderItem;
            }
        }
        return null;
    }


    /**
    * @Description: 构建购物车商品对象
    * @Author:      XuZhao
    * @CreateDate:  19/04/01 下午 07:24
    */

    private TbOrderItem createOrderItem(TbItem item, Integer num) {

        if(num<1){
            throw new RuntimeException("添加购物车的商品不能少于一个!!!");
        }
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num);
        orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));//当前商品小计=商品价格*商品数量
        orderItem.setPicPath(item.getImage());
        orderItem.setSellerId(orderItem.getSellerId());
        return orderItem;
    }


    /**
    * @Description: 判断商家对应的购物车是否存在购物车列表中
    * @Author:      XuZhao
    * @CreateDate:  19/04/01 下午 07:09
    */
    private Cart serachCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if(sellerId.equals(cart.getSellerId())){
                //说明购物车列表存在该商家
                return cart;
            }
        }
        return null;
    }

    /**
    * @Description: 基于sessionId记录购物车列表数据一周时间
    * @Author:      XuZhao
    * @CreateDate:  19/04/01 下午 09:07
    */
    @Override
    public void saveCartListToRedisBySessionId(String sessionId, List<Cart> cartList) {
        redisTemplate.boundValueOps(sessionId).set(cartList,7L,TimeUnit.DAYS);
    }

    /**
    * @Description: 基于cookie记录sessionId一周时间
    * @Author:      XuZhao
    * @CreateDate:  19/04/01 下午 09:45
    */
    @Override
    public List<Cart> selectCartListByKey(String key) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundValueOps(key).get();
        //获取不到购物车列表时
        if(cartList==null){
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    /**
    * @Description: 基于用户名保存商品到购物车
     *  清除登陆后基于username保存的购物车列表数据的时间,当将购物车列表商品提交保存成订单后
    * @Author:      XuZhao
    * @CreateDate:  19/04/02 下午 04:41
    */
    @Override
    public void saveCartListToRedisByUsername(String username, List<Cart> cartList) {
        redisTemplate.boundValueOps(username).set(cartList);
    }

    /**
    * @Description: 将登录前的购物车列表合并到登录后的购物车列表
    * @Author:      XuZhao
    * @CreateDate:  19/04/02 下午 05:13
    */
    @Override
    public List<Cart> mergeCartList(List<Cart> cartList_sessionId, List<Cart> cartList_username) {
        for (Cart cart : cartList_sessionId) {
            //获取登录前的购物车数据
            //获取登录前的购物车商品id和商品数量,添加到登录后的购物车列表中
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            for (TbOrderItem orderItem : orderItemList) {
                Integer num = orderItem.getNum();
                Long itemId = orderItem.getItemId();
                cartList_username = addItemToCartList(cartList_username,itemId,num);
            }
        }
        return cartList_username;
    }


    /**
    * @Description: 清除登录前的购物车列表
    * @Author:      XuZhao
    * @CreateDate:  19/04/02 下午 05:13
    */
    @Override
    public void deleteCartList(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
