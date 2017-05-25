package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;

public interface VirtualAccountDao extends BaseDao<VirtualAccount>{
	
	public List<VirtualAccount> getByHql(String hql);
	
}
