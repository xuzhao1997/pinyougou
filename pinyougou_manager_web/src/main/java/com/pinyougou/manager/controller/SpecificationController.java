package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: SpecificationController
 * @Description: 产品规格控制层
 * @Author: XuZhao
 * @CreateDate: 19/03/12$ 下午 07:09$
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;


    /**
    * @Description: 条件分页查询
    * @Author:      XuZhao
    * @CreateDate:  19/03/12 下午 07:10
    */
    @RequestMapping("/search")
    public PageResult search (@RequestBody TbSpecification specification,Integer pageNum,Integer pageSize){
        return specificationService.search(specification,pageNum,pageSize);
    }


}
