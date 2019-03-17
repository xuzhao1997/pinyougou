package com.pinyougou.user.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName: 类名
 * @Description: 认证服务类
 * @Author: XuZhao
 * @CreateDate: 19/03/17$ 下午 04:39$
 */
public class UserDetailServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    //认证服务类,做认证和授权操作
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List< GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //基于商家名称,查询商家信息
        TbSeller seller = sellerService.findOne(username);
        if(seller!=null)
        {
            if(seller.getStatus().equals("1"))
            {
                return new User(username,seller.getPassword(),authorities);
            }else
                {
                return null;
                }
        }else
            {
            return null;
            }
    }
}
