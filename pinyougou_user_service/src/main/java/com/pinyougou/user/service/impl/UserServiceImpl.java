package com.pinyougou.user.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.pinyougou.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.pojo.TbUserExample;
import com.pinyougou.pojo.TbUserExample.Criteria;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import util.HttpClient;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbUser> findAll() {
		return userMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbUser> page=   (Page<TbUser>) userMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbUser user) {
		//密码加密
		String password = DigestUtils.md5Hex(user.getPassword());
		user.setPassword(password);
		//创建时间
		user.setCreated(new Date());
		//	更新时间
		user.setUpdated(new Date());
		//会员来源
		user.setSourceType("1");
		//使用状态
		user.setStatus("Y");
		//短信是否验证
		user.setIsMobileCheck("1");
		userMapper.insert(user);
        //注册成功后,要清空缓存的验证码
        redisTemplate.delete(user.getPhone());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbUser user){
		userMapper.updateByPrimaryKey(user);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbUser findOne(Long id){
		return userMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			userMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbUser user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		if(user!=null){			
						if(user.getUsername()!=null && user.getUsername().length()>0){
				criteria.andUsernameLike("%"+user.getUsername()+"%");
			}
			if(user.getPassword()!=null && user.getPassword().length()>0){
				criteria.andPasswordLike("%"+user.getPassword()+"%");
			}
			if(user.getPhone()!=null && user.getPhone().length()>0){
				criteria.andPhoneLike("%"+user.getPhone()+"%");
			}
			if(user.getEmail()!=null && user.getEmail().length()>0){
				criteria.andEmailLike("%"+user.getEmail()+"%");
			}
			if(user.getSourceType()!=null && user.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+user.getSourceType()+"%");
			}
			if(user.getNickName()!=null && user.getNickName().length()>0){
				criteria.andNickNameLike("%"+user.getNickName()+"%");
			}
			if(user.getName()!=null && user.getName().length()>0){
				criteria.andNameLike("%"+user.getName()+"%");
			}
			if(user.getStatus()!=null && user.getStatus().length()>0){
				criteria.andStatusLike("%"+user.getStatus()+"%");
			}
			if(user.getHeadPic()!=null && user.getHeadPic().length()>0){
				criteria.andHeadPicLike("%"+user.getHeadPic()+"%");
			}
			if(user.getQq()!=null && user.getQq().length()>0){
				criteria.andQqLike("%"+user.getQq()+"%");
			}
			if(user.getIsMobileCheck()!=null && user.getIsMobileCheck().length()>0){
				criteria.andIsMobileCheckLike("%"+user.getIsMobileCheck()+"%");
			}
			if(user.getIsEmailCheck()!=null && user.getIsEmailCheck().length()>0){
				criteria.andIsEmailCheckLike("%"+user.getIsEmailCheck()+"%");
			}
			if(user.getSex()!=null && user.getSex().length()>0){
				criteria.andSexLike("%"+user.getSex()+"%");
			}
	
		}
		
		Page<TbUser> page= (Page<TbUser>)userMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	* @Description: 发送短信验证码
	* @Author:      XuZhao
	* @CreateDate:  19/03/29 下午 08:30
	*/
	@Override
	public void sendSmsCode(String phone) throws Exception {
		//生成随机六位验证码
		//随机取1到9的数字
		int num = (int)(Math.random() * 9 + 1);
		String smsCode = num + RandomStringUtils.randomNumeric(5);
		//将验证码保存在redis缓存中,时长为10 分钟
		redisTemplate.boundValueOps(phone).set(smsCode,10L,TimeUnit.MINUTES);
		//将验证码发送到用户手机中
        HttpClient httpClient = new HttpClient("http://localhost:9999/sms/sendSms.do");
        //设置请求参数
        httpClient.addParameter("phoneNumbers",phone);
        httpClient.addParameter("signName","品优购网上购物系统");
        httpClient.addParameter("templateCode","SMS_162520844");
        httpClient.addParameter("param","{\"code\":"+smsCode+"}");
        httpClient.post();
        String content = httpClient.getContent();
        System.out.println(content);
        //Map map = JSON.parseObject(content, Map.class);
    }


    /**
    * @Description: 验证码校验
    * @Author:      XuZhao
    * @CreateDate:  19/03/29 下午 09:49
    */
    @Override
    public boolean checkSmsCode(String phone, String smsCode) {
       //获取系统验证码
        String sysCode = (String) redisTemplate.boundValueOps(phone).get();
        if(sysCode == null){
            return false;
        }

        if(!sysCode.equals(smsCode)){
            return false;
        }
        return true;
    }

}
