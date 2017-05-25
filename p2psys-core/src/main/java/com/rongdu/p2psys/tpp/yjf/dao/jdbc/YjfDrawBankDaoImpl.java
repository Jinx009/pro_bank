package com.rongdu.p2psys.tpp.yjf.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.tpp.domain.YjfDrawBank;
import com.rongdu.p2psys.tpp.yjf.dao.YjfDrawBankDao;


@Repository("yjfDrawBankDao")
public class YjfDrawBankDaoImpl extends BaseDaoImpl<YjfDrawBank> implements YjfDrawBankDao {
	@Override
	public YjfDrawBank getYjfDrawBankByBankCode(String bankCode) {
		String sql = " from YjfDrawBank where bankCode = ?1";
		Query query = em.createQuery(sql);
		query.setParameter(1, bankCode);
		List<YjfDrawBank> list = query.getResultList();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	@Override
	public YjfDrawBank getYjfDrawBankByBankName(String bankName){
		String sql = " from YjfDrawBank where bankName = ?1";
		Query query = em.createQuery(sql);
		query.setParameter(1, bankName);
		List<YjfDrawBank> list = query.getResultList();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
