package com.rongdu.p2psys.core.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;

@Repository("operationLogDao")
public class OperationLogDaoImpl extends BaseDaoImpl<OperationLog> implements OperationLogDao {

	@Override
	public OperationLog getOperationLogInfo(String orderNo) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("orderNo", orderNo);
		return findByCriteriaForUnique(param);
	}

}
