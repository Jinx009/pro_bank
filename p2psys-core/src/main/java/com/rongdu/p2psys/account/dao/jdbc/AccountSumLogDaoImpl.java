package com.rongdu.p2psys.account.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchParam;
import com.rongdu.p2psys.account.dao.AccountSumLogDao;
import com.rongdu.p2psys.account.domain.AccountSumLog;

@Repository("accountSumLogDao")
public class AccountSumLogDaoImpl extends BaseDaoImpl<AccountSumLog> implements AccountSumLogDao {

	@Override
	public List<AccountSumLog> getAccountSumLogPage(int page, int max, long userId, SearchParam p) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addOrder(OrderType.DESC, "id");
		return findByCriteria(param, page, max);
	}

	public int count(SearchParam p, long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		return countByCriteria(param);
	}

}
