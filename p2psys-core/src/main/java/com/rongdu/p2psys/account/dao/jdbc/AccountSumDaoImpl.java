package com.rongdu.p2psys.account.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.SearchParam;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.model.AccountSumModel;

@Repository("accountSumDao")
public class AccountSumDaoImpl extends BaseDaoImpl<AccountSum> implements AccountSumDao {

	@Override
	public void update(String type, double value, long userId) {
		String sql = "update rd_account_sum set " + type + "= ifnull(" + type
				+ " , 0 )+ :value where user_id = :user_id";
		String names[] = { "value", "user_id" };
		Object values[] = { value, userId };
		super.updateBySql(sql, names, values);
	}

	/**
	 * 根据用户ID查询信息
	 * 
	 * @param userId
	 * @return
	 */
	public AccountSum findByUserId(long userId) {
		return (AccountSum) findObjByProperty("user.userId", userId);
	}

	/**
	 * 资金合计分页汇总
	 * 
	 * @param p
	 * @return
	 */
	public int count(SearchParam p) {
		String sql = "SELECT count(1)  FROM  rd_account_sum AS account,rd_user AS user on account.userId=user.userId  where 1=1";
		Query query = em.createNativeQuery(sql);
		return (Integer) query.getSingleResult();
	}

	/**
	 * 根据搜索条件,获取资金合计列表
	 * 
	 * @param page ,max,SearchParam
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountSumModel> findByUserId(int page, int max, SearchParam p) {
		StringBuffer buffer = new StringBuffer(
				"select accountSum,user.userName from AccountSum accountSum,User user where accountSum.user.userId=user.userId");
		Query query = em.createQuery(buffer.toString());
		return query.getResultList();
	}


}
