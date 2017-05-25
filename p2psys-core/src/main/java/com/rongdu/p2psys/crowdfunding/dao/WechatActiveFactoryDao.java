package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.WechatActiveFactory;

public interface WechatActiveFactoryDao extends BaseDao<WechatActiveFactory>{

	public List<WechatActiveFactory> getByHql(String hql);
	
}
