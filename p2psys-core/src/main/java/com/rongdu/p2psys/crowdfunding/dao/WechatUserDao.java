package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.WechatUser;

public interface WechatUserDao extends BaseDao<WechatUser>{

	public List<WechatUser> getByHql(String hql);
	
}
