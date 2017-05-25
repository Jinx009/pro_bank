package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.AttentionList;

public interface AttentionListDao extends BaseDao<AttentionList>{

	public List<AttentionList> getByHql(String hql);

}
