package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import groupEntity.Specification;
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
    /**
    * @Description: 新增规格
    * @Author:      XuZhao
    * @CreateDate:  19/03/12 下午 09:40
    */

    @RequestMapping("/add")
    public Result insert(@RequestBody Specification specification){

        try {
            specificationService.insert(specification);
            return new Result(true,"新增成功!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"新增不成功!!");
        }
    }

    /**
    * @Description: 修改的数据回显
    * @Author:      XuZhao
    * @CreateDate:  19/03/12 下午 10:31
    */
    @RequestMapping("/findOne")
    public Specification findOne(Long id){
        return specificationService.findOne(id);
    }
    /**
    * @Description: 修改规格
    * @Author:      XuZhao
    * @CreateDate:  19/03/12 下午 10:32
    */
    @RequestMapping("/update")
    public Result update(@RequestBody Specification specification){

        try {
            specificationService.update(specification);
            return new Result(true,"修改成功!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"修改失败!!");
        }
    }
    /**
    * @Description: 删除规格
    * @Author:      XuZhao
    * @CreateDate:  19/03/14 下午 04:16
    */

    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            specificationService.delete(ids);
            return new Result(true,"删除成功!!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"删除失败!!!");
        }
    }
}
