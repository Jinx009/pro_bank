package com.rongdu.p2psys.account.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.account.dao.PayOfflinebankDao;
import com.rongdu.p2psys.account.domain.PayOfflinebank;

@Repository
public class PayOfflinebankDaoImpl extends BaseDaoImpl<PayOfflinebank> implements PayOfflinebankDao {

	@Override
	public List<PayOfflinebank> list(int status) {
		QueryParam param = QueryParam.getInstance();
		if(status!=0){
			param.addParam("status", status);
		}
		param.addOrder(OrderType.DESC, "id");
		return findByCriteria(param);
	}

}
