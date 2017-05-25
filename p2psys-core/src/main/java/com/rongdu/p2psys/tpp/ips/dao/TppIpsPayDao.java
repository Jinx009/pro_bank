package com.rongdu.p2psys.tpp.ips.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;

/**
 * 环迅资金操作日志DAO接口
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
public interface TppIpsPayDao extends BaseDao<TppIpsPay> {
    
    /**
     * 环迅日志处理
     * @param pay
     * @param status
     * @return
     */
    Boolean editTppIpsPay(TppIpsPay pay, byte status);
    
   /**
    * 环迅日志处理
    * @param pay
    * @param status
    * @param repaymentId
    * @return
    */
    Boolean editTppIpsColl(TppIpsPay pay, byte status, long repaymentId);
    
   /**
    * 环迅日志处理:根据流水号和类型查询日子信息
    * @param merBillNo 流水号
    * @param type 类型查询
    * @return 环迅日志
    */
    List<TppIpsPay> getTppIpsPay(String merBillNo, String type);
    /**
     * 通过原商户号获取调用接口的记录
     * @param oriMerBillNo 原商户号
     * @return 环迅调接口记录
     */
    TppIpsPay getTppIpsPayByOriMerBillNo(String oriMerBillNo);
    
    /**
     * 查询环迅资金日志
     * @param repayId
     * @param status
     * @return
     */
    TppIpsPay getTppIpsPayByRepayId(long repayId, byte status);
    /**
     * 通过订单号获取调用接口的记录
     * @param merBillNo
     * @return
     */
    TppIpsPay getTppIpsPayByMerBillNo(String merBillNo);
    
    /**
     * 查询环迅已经还款成功的日志
     * @param repayId
     * @return list
     */
    List<TppIpsPay> getTppIpsPayList(long repayId);
    
    /**
     * 根据流水号查询待收信息
     * @param merBillNo
     * @param status
     * @return
     */
    List<BorrowCollection> getCollByIpsNo(String merBillNo, byte status);
    
    /**
     * 根据标ID和期数，查询本期可以还款的待收
     * @param borrowId
     * @param period
     * @return
     */
    List<BorrowCollection> getWaitColl(long borrowId, int period);
}
