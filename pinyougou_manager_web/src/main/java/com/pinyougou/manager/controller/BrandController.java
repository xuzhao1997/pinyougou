package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
    public PageResult findPage(Integer pageNum, Integer pageSize){
        return brandService.findPage(pageNum,pageSize);
    }

    /**
     * @Description: 新增品牌
     * @Author:      XuZhao
     * @CreateDate:  19/03/11 下午 04:39
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand){
        try {
            brandService.add(tbBrand);
            return new Result(true,"新增成功!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"新增失败!!");
        }
    }
    /**
    * @Description: 根据id回显数据
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 04:59
    */
    @RequestMapping("/findOne")
    public TbBrand fidOne(Long id){
        return brandService.findOne(id);
    }

    /**
    * @Description: 品牌修改
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 05:01
    */
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand){
        try {
            brandService.update(tbBrand);
            return new Result(true,"修改成功!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败!!!");
        }
    }

    /**
    * @Description: 批量删除品牌
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 05:31
    */
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            brandService.delete(ids);
            return new Result(true,"删除成功!!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"删除失败!!!");
        }
    }
}
