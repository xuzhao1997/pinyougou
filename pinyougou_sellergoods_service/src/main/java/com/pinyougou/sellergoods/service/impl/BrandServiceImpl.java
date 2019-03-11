package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName: 类名
 * @Description: java类作用描述
 * @Author: XuZhao
 * @CreateDate: 19/03/09$ 下午 07:43$
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.findAll();
    }

    /**
    * @Description: 分页查询品牌列表
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 03:40
    */
    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.findAll();
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
    * @Description: 新增品牌
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 04:42
    */
    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.add(tbBrand);
    }

    /**
    * @Description: 根据id查询
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 05:04
    */
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
    * @Description: 修改品牌
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 05:04
    */
    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
    * @Description: 批量删除
    * @Author:      XuZhao
    * @CreateDate:  19/03/11 下午 05:35
    */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }


}
