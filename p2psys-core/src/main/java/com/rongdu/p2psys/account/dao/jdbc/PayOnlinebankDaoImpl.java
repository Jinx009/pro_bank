package com.rongdu.p2psys.account.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.dao.PayOnlinebankDao;
import com.rongdu.p2psys.account.domain.PayOnlinebank;
import com.rongdu.p2psys.account.model.PayOnlinebankModel;

@Repository("payOnlinebankDao")
public class PayOnlinebankDaoImpl extends BaseDaoImpl<PayOnlinebank> implements PayOnlinebankDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PayOnlinebankModel> list(long pay_id) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT onlinebank.* FROM rd_pay_onlinebank onlinebank, rd_pay pay WHERE onlinebank.pay_id = pay.id"
				+ " and pay.enable = 1 AND pay.enable_direct = 1 and onlinebank.enable = 1 ");
		if (pay_id != 0) {
			sb.append(" AND pay.id = " + pay_id + " ");
		}
		Query query = em.createNativeQuery(sb.toString(),PayOnlinebank.class);
		List<PayOnlinebank> list = query.getResultList();
		List<PayOnlinebankModel> modelList = new ArrayList<PayOnlinebankModel>();
		if(list!=null&&list.size()>0){
			for (PayOnlinebank onlinebank : list) {
				PayOnlinebankModel model = PayOnlinebankModel.instance(onlinebank);
				model.setPayName(onlinebank.getPay().getName());
				modelList.add(model);
			}
		}
		return modelList;
	}

}
