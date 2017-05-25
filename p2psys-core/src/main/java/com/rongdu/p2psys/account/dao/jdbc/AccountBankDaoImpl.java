package com.rongdu.p2psys.account.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.account.dao.AccountBankDao;
import com.rongdu.p2psys.account.domain.AccountBank;

@Repository("accountBankDao")
public class AccountBankDaoImpl extends BaseDaoImpl<AccountBank> implements AccountBankDao {

	@Override
	public List<AccountBank> list(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addParam("status", 1);
		return findByCriteria(param);
	}
	
	@Override
	public List<AccountBank> list(long userId,String channelKey) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addParam("status", 1);
		param.addParam("channelKey",channelKey);
		return findByCriteria(param);
	}

	@Override
	public List<AccountBank> listAll(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId).addOrder(OrderType.DESC, "status");
		return findByCriteria(param);
	}

	@Override
	public int count(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 1);
		param.addParam("user.userId", userId);
		return countByCriteria(param);
	}

	@Override
	public void disable(long userId, long id) {
		Query query = em.createQuery("UPDATE AccountBank set status = 0 WHERE user.userId = :userId AND id = :id");
		query.setParameter("userId", userId);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public AccountBank find(long userId, String bankNo,String channlKey) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addParam("bankNo", bankNo);
		param.addParam("status", 1);
		param.addParam("channelKey", channlKey);
		return findByCriteriaForUnique(param);
	}

	@Override
	public AccountBank findByDDY(long userId, String hidcard,String channlKey) {
		String sql = "select ab.* from rd_account_bank ab WHERE ab.user_id="+userId+" and ab.bank_no LIKE '%"+hidcard+"%' and ab.channel_key='"+channlKey+"' order by ab.id desc";
		Query query = em.createNativeQuery(sql.toString(),AccountBank.class);
		List<AccountBank> list = query.getResultList();
		if(list != null && list.size() > 0){
			AccountBank ab = list.get(0);
			return ab;
		}
		return null;
	}
	
	@Override
	public AccountBank find(long userId,String bank, String hidcard,String channlKey) {
		String sql = "select ab.* from rd_account_bank ab WHERE ab.user_id="+userId+" and ab.bank_no LIKE '%"+hidcard+"%' and ab.bank LIKE '%"+bank+"%' and ab.channel_key='"+channlKey+"' order by ab.id desc";
		Query query = em.createNativeQuery(sql.toString(),AccountBank.class);
		List<AccountBank> list = query.getResultList();
		if(list != null && list.size() > 0){
			AccountBank ab = list.get(0);
			return ab;
		}
		return null;
	}

	@Override
	public AccountBank findById(long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("id", id);
		List<AccountBank> list = super.findByCriteria(param);
		if (list != null && list.size() > 0) {
			return (AccountBank) list.get(0);
		} else {
			return null;
		}
	}
}
