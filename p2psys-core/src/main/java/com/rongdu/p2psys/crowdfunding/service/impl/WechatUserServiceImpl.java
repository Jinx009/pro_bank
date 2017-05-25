package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.WechatUserDao;
import com.rongdu.p2psys.crowdfunding.domain.WechatUser;
import com.rongdu.p2psys.crowdfunding.service.WechatUserService;

@Service("wechatUserService")
public class WechatUserServiceImpl implements WechatUserService{
	
	private StringBuffer buffer;
	
	@Resource
	private WechatUserDao wechatUserDao;
	

	public WechatUser getByOpenIdAndAppId(String openId, String appId) {
		buffer = new StringBuffer();
		buffer.append(" FROM WechatUser WHERE openId ='");
		buffer.append(openId);
		buffer.append("' AND appId  ='");
		buffer.append(appId);
		buffer.append("' ");
		List<WechatUser> list =wechatUserDao.getByHql(buffer.toString());
		if(null!=list&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	public WechatUser save(WechatUser wechatUser) {
		return wechatUserDao.save(wechatUser);
	}




	
	
	public void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}
}
