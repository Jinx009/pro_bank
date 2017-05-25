package com.rongdu.p2psys.bond.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.bond.dao.BondCollectionDao;
import com.rongdu.p2psys.bond.domain.BondCollection;

/**
 * 债权待收DAO接口
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Repository("bondCollectionDao")
public class BondCollectionDaoImpl extends BaseDaoImpl<BondCollection> implements BondCollectionDao {

    @SuppressWarnings("unchecked")
    @Override
    public BondCollection getBondCollectionById(long id) {
        String jpql = "from BondCollection where id = ?1";
        Query query = em.createQuery(jpql);
        query.setParameter(1, id);
        List<BondCollection> list = query.getResultList();
        if (list != null && list.size() >= 0) {
            return (BondCollection) list.get(0);
        } else {
            return null;
        }
    }
    
	@Override
	public BondCollection getCollectionByTenderAndPeriod(long bondTenderId, int period) {
		String jpql = "from BondCollection bc where bc.status=0  and bc.bondTenderId= ?1 and bc.period = ?2";
		Query query =em.createQuery(jpql);
		query.setParameter(1, bondTenderId);
		query.setParameter(2,(byte)period);
		@SuppressWarnings("unchecked")
		List<BondCollection> list = query.getResultList();
		
        if (list != null && list.size() >= 0) {
            return (BondCollection) list.get(0);
        }
        
		return null;
	}    
    
	@Override
	public double getRemainderCapital(long bondTenderId) {
		String sql = "select sum(capital) from BondCollection bc where bc.status=0 and bc.collectionTime >= now() and bc.bondTenderId= ?1 ";
		Query q = em.createQuery(sql).setParameter(1, bondTenderId);
		Object ret = q.getSingleResult();
		if (ret == null)
			return 0;
		return Double.parseDouble(ret.toString());
	}
	
	@Override
	public boolean isLatedByBorrowId(long borrowId) {
		String sql = "select count(bc.id) from BondCollection bc where bc.borrow.id= :borrowId and ((bc.status=0 and bc.collectionTime < now()) or (bc.status=1 and bc.collectionTime < bc.collectionYesTime)) ";
        Query query = em.createQuery(sql);
        query.setParameter("borrowId", borrowId);
		Object ret = query.getSingleResult();
		int count = 0;
		if (ret != null){
			count = Integer.parseInt(ret.toString());
		}
		return (count>0);
	}
	
	@Override
	public List<BondCollection> getBondCollectionList(long repaymentId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 0);
		param.addParam("borrowRepaymentId", repaymentId);
		return super.findByCriteria(param);
	}

	@Override
	public Object[] getSumBondCollection(long userId) {
		StringBuffer buffer = new StringBuffer("select sum(capital - bondCapital),sum(interest - bondInterest) from BondCollection where status = 0 and user.userId = :userId ");
		Query query = em.createQuery(buffer.toString());
		query.setParameter("userId", userId);
		Object[] object = (Object[])query.getSingleResult();
		return object;
	}
}
