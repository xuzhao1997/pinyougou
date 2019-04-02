package com.pinyougou.cart.service;

import groupEntity.Cart;

import java.util.List;

/**
 * @ClassName: CartService
 * @Description: 购物车接口
 * @Author: XuZhao
 * @CreateDate: 19/04/01$ 下午 06:15$
 */
public interface CartService {

    /**
    * @Description: 添加商品到购物车
    * @Author:      XuZhao
    * @CreateDate:  19/04/01 下午 06:15
    */
    public List<Cart> addItemToCartList(List<Cart> cartList,Long itemId,Integer num);

    void saveCartListToRedisBySessionId(String sessionId, List<Cart> cartList);

    List<Cart> selectCartListByKey(String key);

    void saveCartListToRedisByUsername(String username, List<Cart> cartList);

    //将登录前的购物车列表合并到登录后的购物车列表
    List<Cart> mergeCartList(List<Cart> cartList_sessionId, List<Cart> cartList_username);

    //清除登录前的购物车列表
    void deleteCartList(String sessionId);
}
