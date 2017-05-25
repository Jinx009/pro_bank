package com.rongdu.p2psys.borrow.dao.jdbc;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.borrow.dao.BorrowMortgageDao;
import com.rongdu.p2psys.borrow.domain.BorrowMortgage;

@Repository("borrowMortgageDao")
public class BorrowMortgageDaoImpl extends BaseDaoImpl<BorrowMortgage> implements BorrowMortgageDao {
    @Override
    public double getTotalAssessPriceByBorrowId(long id) {
        String sql = "select sum(assessPrice) from BorrowMortgage where borrow.id=? and status=1";
        Query query = em.createQuery(sql).setParameter(1, id);
        double totalAssessPrice = 0;
        Object obj = query.getSingleResult();
        if (obj != null) {
            totalAssessPrice = (Double) obj;
        }
        return totalAssessPrice;
    }
    @Override
    public double getTotalMortgagePriceByBorrowId(long borrowId) {
        String sql = "select sum(mortgagePrice) from BorrowMortgage where borrow.id=? and status=1";
        Query query = em.createQuery(sql).setParameter(1, borrowId);
        double totalMortgagePrice = 0;
        Object obj = query.getSingleResult();
        if (obj != null) {
            totalMortgagePrice = (Double) obj;
        }
        return totalMortgagePrice;
    }
    @Override
    public double getTotalAssessPriceByBorrowIdAndNum(long id, int num) {
        String sql = "select sum(assessPrice) from BorrowMortgage where borrow.id=? and num=? and status=1";
        Query query = em.createQuery(sql).setParameter(1, id).setParameter(2, num);
        double totalAssessPrice = 0;
        Object obj = query.getSingleResult();
        if (obj != null) {
            totalAssessPrice = (Double) obj;
        }
        return totalAssessPrice;
    }
    @Override
    public double getTotalMortgagePriceByMortgageIds(long[] ids) {
        StringBuffer sb = new StringBuffer("select sum(mortgage_price) from rd_borrow_mortgage where id in (");
        for (int i=0;i<ids.length;i++) {
            sb.append("?,");
        }
        double totalMortgagePrice = 0;
        Query query = em.createNativeQuery(sb.substring(0, sb.length()-1) + ") and status=1");
        for (int i=0;i<ids.length;i++) {
            query.setParameter(i+1, ids[i]);
        }
        Object obj = query.getSingleResult();
        if (obj != null) {
            totalMortgagePrice = (Double) obj;
        }
        return totalMortgagePrice;
    }
    @Override
    public int getMaxNumByBorrowId(long borrowId) {
        String jpql = "select max(num) from BorrowMortgage where borrow.id = ?";
        Query query = em.createQuery(jpql).setParameter(1, borrowId);
        return (Integer) query.getSingleResult();
    }
    
    @Override
    public Object[] getTotalPriceByBorrowIdAndNum(long id, int num) {
        String sql = "select sum(assessPrice),sum(mortgagePrice) from BorrowMortgage where borrow.id=? and num=?";
        Query query = em.createQuery(sql).setParameter(1, id).setParameter(2, num);
        Object[] totalPrice = null;
        Object obj = query.getSingleResult();
        if (obj != null) {
            totalPrice = (Object[]) obj;
        }
        return totalPrice;
    }
}
