package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.p2psys.crowdfunding.domain.WechatActiveFactory;

public interface WechatActiveFactoryService {

	public WechatActiveFactory getByWechatId(Integer wechatId);
	
	public List<WechatActiveFactory> findAll();

	public void save(WechatActiveFactory wechatActiveFactory);
	
}
