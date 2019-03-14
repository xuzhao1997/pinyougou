package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import groupEntity.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

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

    /**
    * @Description: 新增规格
    * @Author:      XuZhao
    * @CreateDate:  19/03/12 下午 09:43
    */
    @Override
    public void insert(Specification specification) {

        TbSpecification tbSpecification = specification.getSpecification();
        //保存规格
        specificationMapper.insert(tbSpecification);
        //保存规格选项
        List<TbSpecificationOption> specificationOptions = specification.getSpecificationOptions();
        for (TbSpecificationOption specificationOption : specificationOptions) {
            //规格选项关联规格id
            specificationOption.setSpecId(tbSpecification.getId());
            specificationOptionMapper.insert(specificationOption);
        }

    }

    /**
    * @Description: 修改时的数据回显操作
    * @Author:      XuZhao
    * @CreateDate:  19/03/12 下午 10:34
    */
    @Override
    public Specification findOne(Long id) {
        Specification specification = new Specification();
        //规格数据对象
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        specification.setSpecification(tbSpecification);
        //规格选项列表数据
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<TbSpecificationOption> tbSpecificationOptions = specificationOptionMapper.selectByExample(example);
        specification.setSpecificationOptions(tbSpecificationOptions);
        return specification;
    }

    /**
    * @Description: 修改规格
    * @Author:      XuZhao
    * @CreateDate:  19/03/12 下午 10:35
    */
    @Override
    public void update(Specification specification) {
        //修改规格数据
        TbSpecification tbspecification1 = specification.getSpecification();
        specificationMapper.updateByPrimaryKey(tbspecification1);
        //修改规格选项,先删除之前的规格选项列表,在重新保存页面提交的

        //删除之前的
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(tbspecification1.getId());
        specificationOptionMapper.deleteByExample(example);

        //删完之后再保存页面新添加的数据
        List<TbSpecificationOption> specificationOptions = specification.getSpecificationOptions();
        for (TbSpecificationOption specificationOption : specificationOptions) {
            specificationOption.setSpecId(tbspecification1.getId());
            specificationOptionMapper.insert(specificationOption);
        }


    }

    /**
    * @Description: 删除规格
    * @Author:      XuZhao
    * @CreateDate:  19/03/14 下午 04:17
    */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //删除规格的同时,也同时删除关联的规格选项数据
            specificationMapper.deleteByPrimaryKey(id);
            //删除关联的规格选项数据
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);

        }
    }

    /**
    * @Description: 模板关联规格列表查询
    * @Author:      XuZhao
    * @CreateDate:  19/03/14 下午 08:34
    */
    @Override
    public List<Map> selectSpecOptions() {
        return specificationMapper.selectSpecOptions();
    }
}
