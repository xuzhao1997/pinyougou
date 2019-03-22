package com.pinyougou.content.service.impl;
import java.util.List;

import com.pinyougou.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
	    //当增加广告时,要清空缓存中的数据
        Long categoryId = content.getCategoryId();
        redisTemplate.boundHashOps("content").delete(categoryId);
        contentMapper.insert(content);
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
	    //修改广告时,不管有没有修改广告分类ID,需要清除修改前对应得广告列表数
        Long oldCategoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
        redisTemplate.boundHashOps("content").delete(oldCategoryId);
        contentMapper.updateByPrimaryKey(content);

        //判断广告分类ID是否发生改变
        if(oldCategoryId.longValue()!= content.getCategoryId()){
            //如果广告类型ID发生改变,需要清空改变后的广告类型列表数据
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }

	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
		    //当删除广告时,要清空缓存中的数据
            Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
            redisTemplate.boundHashOps("content").delete(categoryId);
            contentMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	* @Description: 根据广告分类,查询相关广告列表数据
	* @Author:      XuZhao
	* @CreateDate:  19/03/22 下午 09:03
	*/
	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {

		//从redis中获取广告列表数据
		List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
		//如果从redis中找不到,就查询数据库
		if(contentList==null){
            System.out.println("from mysql..........");
			//基于广告分类id查询有效状态广告数据
			TbContentExample example = new TbContentExample();
			Criteria criteria = example.createCriteria();
			criteria.andCategoryIdEqualTo(categoryId);
			criteria.andStatusEqualTo("1");
			example.setOrderByClause("sort_order");
            contentList= contentMapper.selectByExample(example);
            //把查询结果存到缓存中
            redisTemplate.boundHashOps("content").put(categoryId,contentList);
		}else{
            System.out.println("from redis..........");
        }

		return contentList;
	}

}
