package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.WechatActiveFactoryDao;
import com.rongdu.p2psys.crowdfunding.domain.WechatActiveFactory;
import com.rongdu.p2psys.crowdfunding.service.WechatActiveFactoryService;

@Service("wechatActiveFactoryService")
public class WechatActiveFactoryServiceImpl implements WechatActiveFactoryService{

	private StringBuffer buffer;
	
	@Resource
	private WechatActiveFactoryDao wechatActiveFactoryDao;
	
	public WechatActiveFactory getByWechatId(Integer wechatId) {
		buffer = new StringBuffer();
		buffer.append(" FROM WechatActiveFactory WHERE wechatId =");
		buffer.append(wechatId);
		List<WechatActiveFactory> list = wechatActiveFactoryDao.getByHql(buffer.toString());
		if(null!=list&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<WechatActiveFactory> findAll() {
		return wechatActiveFactoryDao.findAll();
	}

	public void save(WechatActiveFactory wechatActiveFactory) {
		wechatActiveFactoryDao.save(wechatActiveFactory);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public StringBuffer getBuffer() {
		return buffer;
	}
	public void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}
}
