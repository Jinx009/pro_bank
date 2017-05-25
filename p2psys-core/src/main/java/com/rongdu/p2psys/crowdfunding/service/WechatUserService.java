package com.rongdu.p2psys.crowdfunding.service;

import com.rongdu.p2psys.crowdfunding.domain.WechatUser;

public interface WechatUserService {

	public WechatUser getByOpenIdAndAppId(String openId,String appId);

	public WechatUser save(WechatUser wechatUser);
	
}
