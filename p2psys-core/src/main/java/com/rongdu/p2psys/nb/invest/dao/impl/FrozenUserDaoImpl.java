package com.rongdu.p2psys.nb.invest.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.invest.dao.FrozenUserDao;
import com.rongdu.p2psys.nb.invest.domain.FrozenUser;

@Repository("frozenUserDao")
public class FrozenUserDaoImpl extends BaseDaoImpl<FrozenUser> implements FrozenUserDao
{

	@SuppressWarnings("unchecked")
	public double getLockUseMoney(Integer userId)
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" from FrozenUser where status = 0  and user.userId = "+userId+"  ");
		
		Query query = (Query) em.createQuery(buffer.toString());
		
		List<FrozenUser> list = query.getResultList();
		
		double money = 0;
		
		if(list.size()>0&&!list.isEmpty())
		{
			for(int i = 0;i<list.size();i++)
			{
				money += list.get(i).getMoney();
			}
			
			return money;
		}
		
		
		return 0;
	}
	@Override
	public int unLockUserMoney(Long productId,Long productBasicId,Long userId)
	{
		String nativeSql = "UPDATE nb_frozen_user SET status = :status  WHERE user_id = :userId and product_id =:productId and product_basic_id =:productBasicId";
		String[] names = new String[] { "status", "productId", "productBasicId", "userId" };
		Object[] values = new Object[] { 1, productId, productBasicId, userId };
		int result = updateBySql(nativeSql, names, values);
		return result;
	}
}
