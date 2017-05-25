package com.rongdu.p2psys.user.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.dao.UserCreditApplyDao;
import com.rongdu.p2psys.user.domain.UserCreditApply;

@Repository("userCreditApplyDao")
public class UserCreditApplyDaoImpl extends BaseDaoImpl<UserCreditApply> implements UserCreditApplyDao {

	@Override
	public int count(long userId) {
		QueryParam param = QueryParam.getInstance().addParam("user.userId", userId);
		return countByCriteria(param);
	}

	@Override
	public int count(long userId, int status) {
		QueryParam param = QueryParam.getInstance();
		if (userId > 0) {
			param.addParam("user.userId", userId);
		}
		param.addParam("status", status);
		return countByCriteria(param);
	}

}
