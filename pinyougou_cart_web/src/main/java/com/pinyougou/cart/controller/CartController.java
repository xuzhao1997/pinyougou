package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.cart.service.CartService;
import entity.Result;
import groupEntity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName: CartController
 * @Description: 购物车表现层
 * @Author: XuZhao
 * @CreateDate: 19/04/01$ 下午 08:45$
 */
@RestController
@RequestMapping("/cart")
public class CartController {

   @Reference
    private CartService cartService;

   @Autowired
   private HttpSession session;

   @Autowired
   private HttpServletRequest request;

   @Autowired
   private HttpServletResponse response;

   /**
   * @Description: 基于cookie记录sessionId一周时间
   * @Author:      XuZhao
   * @CreateDate:  19/04/01 下午 09:29
   */
    public String getSessionId(){
        //基于系统cookie,获取响应的sessionId值
        String sessionId = CookieUtil.getCookieValue(request, "cartCookie");
        if(sessionId ==null){
            //系统中记录的sessionId失效,获取浏览器重新传递的sessionId
            sessionId = session.getId();
            //系统中记录的sessionId 失效
            CookieUtil.setCookie(request,response,"cartCookie",sessionId,3600*24*7,"utf-8");
        }
        return sessionId;
    }


   /**
   * @Description: 查询购物车列表功能
   * @Author:      XuZhao
   * @CreateDate:  19/04/01 下午 08:58
   */
   @RequestMapping("/findCartList")
   public List<Cart> findCartList(){
       //获取用户名
       String username = SecurityContextHolder.getContext().getAuthentication().getName();

       //从redis中获取购物车列表
       //未登录,基于session作为key获取值
       String sessionId = getSessionId();
       //登录后,基于username作为key,获取购物车列表
       //未登录时,基于sessionId作为key获取购物车列表
       List<Cart> cartList_sessionId = cartService.selectCartListByKey(sessionId);
       if("anonymousUser".equals(username)){//未登录
           System.out.println("get cartList by sessionId......");
           return cartList_sessionId;
       }else{//已登录
           System.out.println("get cartList by username......");
           List<Cart> cartList_username = cartService.selectCartListByKey(username);
           //判断登录前是否添加商品到购物车列表
            if(cartList_sessionId.size()>0){//说明登录前添加过商品到购物车
                //需要将登录前的购物车列表合并到登录后的购物车列表
                cartList_username = cartService.mergeCartList(cartList_sessionId,cartList_username);
                //清除登录前的购物车列表
                cartService.deleteCartList(sessionId);
                //将合并后的购物车列表从新存入redis
                cartService.saveCartListToRedisByUsername(username,cartList_username);
            }

           return cartList_username;
       }

   }

   /**
   * @Description: 添加商品到购物车
   * @Author:      XuZhao
   * @CreateDate:  19/04/01 下午 08:48
   */
   @RequestMapping("/addItemToCartList")
    public Result addItemToCartList(Long itemId,Integer num){
       try {
           //获取用户名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(username);
           //获取sessionId
           String sessionId = getSessionId();
           //1.从redis中获取购物车列表
           List<Cart> cartList = findCartList();
           //2.添加商品到购物车
           //3.将添加商品到购物车列表后的购物车从新存入redis
           cartList = cartService.addItemToCartList(cartList, itemId, num);
           if("anonymousUser".equals(username)){//未登录
               System.out.println("save cartList by sessionId......");
                cartService.saveCartListToRedisBySessionId(sessionId,cartList);
            }else{
               System.out.println("save cartList by username......");
                //登录后,基于用户名作为key,将购物车列表存入redis
               cartService.saveCartListToRedisByUsername(username,cartList);
            }
            return new Result(true,"添加商品购物车成功!!!");
       } catch (RuntimeException e) {
           e.printStackTrace();
           return new Result(false,e.getMessage());
       }catch (Exception e) {
           e.printStackTrace();
           return new Result(false,e.getMessage());
       }
   }
}
