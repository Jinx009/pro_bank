package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFlag;

public interface LeaderFlagDao extends BaseDao<LeaderFlag> {

	public List<LeaderFlag> getByHql(String hql);

}
