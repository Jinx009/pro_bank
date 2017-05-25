package com.rongdu.p2psys.nb.invest.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.invest.dao.FrozenProductDao;
import com.rongdu.p2psys.nb.invest.domain.FrozenProduct;

@Repository("frozenProductDao")
public class FrozenProductDaoImpl extends BaseDaoImpl<FrozenProduct> implements FrozenProductDao
{
	@SuppressWarnings("unchecked")
	public double getLockMoney(Integer productId)
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" from FrozenProduct where status = 0  and productId = "+productId+"  ");
		
		Query query = (Query) em.createQuery(buffer.toString());
		
		
		List<FrozenProduct> list = query.getResultList();
		
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
	public int unLockProjectMoney(Long productId,Long productBasicId,Long userId)
	{
		String nativeSql = "UPDATE nb_frozen_product SET status = :status  WHERE user_id = :userId and product_id =:productId and product_basic_id =:productBasicId";
		String[] names = new String[] { "status", "productId", "productBasicId", "userId" };
		Object[] values = new Object[] { 1, productId, productBasicId, userId };
		int result = updateBySql(nativeSql, names, values);
		return result;
	}
	@Override
	public int unLockProjectMoney(Long productId,Long userId)
	{
		String nativeSql = "UPDATE nb_frozen_product SET status = :status  WHERE user_id = :userId and product_id =:productId";
		String[] names = new String[] { "status", "productId", "userId" };
		Object[] values = new Object[] { 1, productId,  userId };
		int result = updateBySql(nativeSql, names, values);
		return result;
	}
}
