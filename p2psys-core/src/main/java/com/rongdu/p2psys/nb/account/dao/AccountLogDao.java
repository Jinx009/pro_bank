package com.rongdu.p2psys.nb.account.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.account.domain.AccountLog;

public interface AccountLogDao extends BaseDao<AccountLog>
{

	public List<AccountLog> findByHql(String hql);

}
