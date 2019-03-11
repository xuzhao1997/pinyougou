package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: BrandController
 * @Description: 品牌列表表现层
 * @Author: XuZhao
 * @CreateDate: 19/03/09$ 下午 07:45$
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
    * @Description: 查询全部列表
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 03:30
    */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    /**
    * @Description: 分页查询品牌列表
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 03:31
    */
    @RequestMapping("/findPage")
    public PageResult findPage(Integer pageNum,Integer pageSize){
        return brandService.findPage(pageNum,pageSize);
    }

}
