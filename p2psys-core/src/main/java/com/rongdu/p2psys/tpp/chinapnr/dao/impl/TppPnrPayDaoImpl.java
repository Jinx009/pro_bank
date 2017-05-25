package com.rongdu.p2psys.tpp.chinapnr.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.tpp.chinapnr.dao.TppPnrPayDao;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;

@Repository("tppPnrPayDao")
public class TppPnrPayDaoImpl extends BaseDaoImpl<TppPnrPay> implements TppPnrPayDao {
	
	@Override
	public TppPnrPay findChinapnrModelByOrd(String ordNo) {
		String jqpl = " from TppPnrPay where ordId=?1";
		Query query = em.createQuery(jqpl);
		query.setParameter(1, ordNo);
		List<TppPnrPay> list = query.getResultList();
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
