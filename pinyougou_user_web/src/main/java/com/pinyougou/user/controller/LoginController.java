package com.pinyougou.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/16$ 下午 07:43$
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    /**
    * @Description: 获取登录人用户名进行页面展示
    * @Author:      XuZhao
    * @CreateDate:  19/03/16 下午 07:43
    */

    @RequestMapping("/getLoginName")
    public Map<String,String> getLoginName(){
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,String> map = new HashMap<>();
        map.put("loginName",loginName);
        return map;
    }



}
