package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: SpecificationServiceImpl
 * @Description: 商品规格实现类
 * @Author: XuZhao
 * @CreateDate: 19/03/12$ 下午 07:18$
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {


    @Autowired
    private TbSpecificationMapper specificationMapper;

    /**
    * @Description: 分页查询所有规格
    * @Author:      XuZhao
    * @CreateDate:  19/03/12 下午 07:19
    */
    @Override
    public PageResult search(TbSpecification specification, Integer pageNum, Integer pageSize) {
        //设置分页条件
        PageHelper.startPage(pageNum,pageSize);
        //设置查询条件
        TbSpecificationExample example = new TbSpecificationExample();//组装查询条件对象
        //获取封装查询条件对象
        TbSpecificationExample.Criteria criteria = example.createCriteria();

        //获取页面查询条件参数
        if(specification!=null){
            //获取规格名称条件
            String specName = specification.getSpecName();
            //如果走就说明有字符串
            if(specName != null && !"".equals(specName)){
                criteria.andSpecNameLike("%"+specName+"%");
            }

        }

        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);

        return new PageResult(page.getTotal(),page.getResult());
    }
}
