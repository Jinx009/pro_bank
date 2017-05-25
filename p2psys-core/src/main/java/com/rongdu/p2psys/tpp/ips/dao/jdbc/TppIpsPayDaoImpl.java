package com.rongdu.p2psys.tpp.ips.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.ips.dao.TppIpsPayDao;

/**
 * 环迅资金操作日志DAO接口
 * @author hcb
 * @version 1.0
 * @since 2014-08-20
 */
@Repository("tppIpsPayDao")
public class TppIpsPayDaoImpl extends BaseDaoImpl<TppIpsPay> implements TppIpsPayDao {

    @Override
    public Boolean editTppIpsPay(TppIpsPay pay, byte status) {
        StringBuffer sql = new StringBuffer("UPDATE TppIpsPay SET ips_time = :ipsTime,");
        sql.append(" ips_bill_no = :ipsBillNo, status = :status, ips_fee = :ipsFee, end_time = now()");
        sql.append(" WHERE status = :ipsStatus AND mer_bill_no = :merBillNo");
        int result = this.updateByJpql(sql.toString(), 
                new String[]{"ipsTime", "ipsBillNo", "status", "ipsFee", "ipsStatus", "merBillNo"}, 
                new Object[] {pay.getIpsTime(), pay.getIpsBillNo(), pay.getStatus(), pay.getIpsFee(), status, pay.getMerBillNo()});
        if (result < 1) {
            return false;
        }
        // 更新缓存
        em.refresh(getTppIpsPay(pay.getMerBillNo()));
        return true;
    }
    
    @Override
    public Boolean editTppIpsColl(TppIpsPay pay, byte status, long repaymentId) {
        // TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer("UPDATE TppIpsPay SET ips_time = :ipsTime,");
        sql.append(" ips_bill_no = :ipsBillNo, status = :status, ips_fee = :ipsFee, end_time = now()");
        sql.append(" WHERE type = :type AND status = :ipsStatus AND repayment_id = :repaymentId AND mer_bill_no = :merBillNo");
        int result = this.updateByJpql(sql.toString(), 
                new String[]{"ipsTime", "ipsBillNo", "status", "ipsFee", "type", "ipsStatus", "repaymentId", "merBillNo"}, 
                new Object[] {pay.getIpsTime(), pay.getIpsBillNo(), pay.getStatus(), pay.getIpsFee(), pay.getType(), status, repaymentId, pay.getMerBillNo()});
        if (result < 1) {
            return false;
        }
        // 更新缓存
        em.refresh(getTppIpsPay(pay.getMerBillNo()));
        return true;
    }
    

    @Override
    public List<TppIpsPay> getTppIpsPay(String merBillNo, String type) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("merBillNo", merBillNo);
        param.addParam("type", type);
        List<TppIpsPay> payList = this.findByCriteria(param);
        return payList;
    }

    public TppIpsPay getTppIpsPay(String merBillNo) {
        return this.findObjByProperty("merBillNo", merBillNo);
    }
    
    @Override
    public TppIpsPay getTppIpsPayByOriMerBillNo(String oriMerBillNo){
        return this.findObjByProperty("oriMerBillNo", oriMerBillNo);
    }
    @Override
    public TppIpsPay getTppIpsPayByMerBillNo(String merBillNo){
        return this.findObjByProperty("merBillNo", merBillNo);
    }
    @Override
    public TppIpsPay getTppIpsPayByRepayId(long repayId,byte status) {
        // TODO Auto-generated method stub
        QueryParam param = QueryParam.getInstance();
        param.addParam("repaymentId", repayId);
        param.addParam("type", TppIpsPay.TYPE_DO_RAPAY);
        param.addParam("status", status);
        List<TppIpsPay> payList = this.findByCriteria(param);
        if(payList != null && payList.size() > 0){
            return payList.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TppIpsPay> getTppIpsPayList(long repayId) {
        StringBuffer jpql = new StringBuffer("select p1.* from tpp_ips_pay p1 where p1.type = '" + TppIpsPay.TYPE_DO_RAPAY);
        jpql.append("' AND p1.status IN ( " + TppIpsPay.STATUS_WAIT + " , " + TppIpsPay.STATUS_SUCCESS + " )");
        jpql.append(" AND p1.repayment_id = :repayId");
        Query query = em.createNativeQuery(jpql.toString(), TppIpsPay.class);
        query.setParameter("repayId", repayId);
        List<TppIpsPay> list = (List<TppIpsPay>) query.getResultList();
        return list;
    }
     
    @SuppressWarnings("unchecked")
    @Override
    public List<BorrowCollection> getCollByIpsNo(String merBillNo,byte status) {
        StringBuffer jpql = new StringBuffer("SELECT p2.* FROM  tpp_ips_pay p1, rd_borrow_collection p2 WHERE");
        jpql.append(" p1.collection_id = p2.id AND p1.type = '"+TppIpsPay.TYPE_DO_COLLECTION+"' AND p1.status = :status AND p1.mer_bill_no = :merBillNo");
        Query query = em.createNativeQuery(jpql.toString(), BorrowCollection.class);
        query.setParameter("merBillNo", merBillNo);
        query.setParameter("status", status);
        List<BorrowCollection> list = (List<BorrowCollection>) query.getResultList();
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BorrowCollection> getWaitColl(long borrowId, int period) {
        StringBuffer jpql = new StringBuffer("SELECT p2.* FROM  tpp_ips_pay p1 right join rd_borrow_collection p2");
        jpql.append(" on  p1.collection_id = p2.id WHERE ( (p1.type is null and p1.status is null)");
        jpql.append(" or  (p1.type = 'do_collection' and p1.status != 0 and p1.status != 1) )");
        jpql.append(" and p2.borrow_id = :borrowId and p2.period = :period");
        Query query = em.createNativeQuery(jpql.toString(), BorrowCollection.class);
        query.setParameter("period", period);
        query.setParameter("borrowId", borrowId);
        List<BorrowCollection> list = (List<BorrowCollection>) query.getResultList();
        return list;
    }

    
}
