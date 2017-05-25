package com.rongdu.p2psys.tpp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 环迅资金操作日志
 * @author zhangyz
 * @version 1.0
 * @since 2014-08-20
 */
@Entity
@Table(name = "tpp_ips_pay")
public class TppIpsPay implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 环迅资金操作日志:type值，充值 */
	public static final String TYPE_DO_RECHARGE = "do_recharge";
	
	/** 环迅资金操作日志:type值，提现 */
	public static final String TYPE_DO_CASH = "do_cash";
	
	/** 环迅资金操作日志:type值，投资 */
	public static final String TYPE_DO_TENDER = "do_tender";
	
	/** 环迅资金操作日志:type值，还款:待还 */
	public static final String TYPE_DO_RAPAY = "do_rapay";
	
	/** 环迅资金操作日志:type值，还款:待收 */
    public static final String TYPE_DO_COLLECTION = "do_collection";
    
    /** 环迅资金操作日志:type值，转账 */
    public static final String TYPE_DO_TRANSFER = "do_transfer";
    
    /** 环迅资金操作日志:type值，登记担保方 */
    public static final String TYPE_REGISTER_GUARANTOR = "register_guarantor";
    
    /** 环迅资金操作日志:type值，代偿 */
    public static final String TYPE_DO_COMPENSATE = "do_compensate";
	
	/** 环迅资金操作日志:status值，待处理 */
    public static final byte STATUS_WAIT = 0;
    /** 环迅资金操作日志:status值，1处理成功 */
    public static final byte STATUS_SUCCESS = 1;
    /** 环迅资金操作日志:status值，-1处理失败 */
    public static final byte STATUS_FAIL = -1;
	
    /**
     * 
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    /**
     * 转入用户id
     */
    private long inUserId;
    /**
     * 转出用户id
     */
    private long outUserId;
    /**
     * 转入IPS账号
     */
    private String inAcctNo;
    /**
     * 转出IPS账号
     */
    private String outAcctNo;
    /**
     * 类型,相当于nid
     */
    private String type;
    /**
     * IPS交易类型
     */
    private String transferType;
    /**
     * 交易流水号订单号
     */
    private String merBillNo;
    /**
     * 环迅交易订单号
     */
    private String ipsBillNo;
    /**
     * 环讯转账 原商户订单号
     */
    private String oriMerBillNo;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 环迅处理日期
     */
    private Date ipsTime;
    /**
     * 处理结束时间
     */
    private Date endTime;
    /**
     * 标的ID
     */
    private long borrowId;
    /**
     * 投标ID
     */
    private long tenderId;
    /**
     * 还款ID
     */
    private long repaymentId;
    /**
     * 投标ID
     */
    private long collectionId;
    /**
     * 日志
     */
    private String message;
    /**
     * 状态:默认0未处理,1处理成功，-1处理失败
     */
    private byte status;
    /**
     * 发生的金额
     */
    private double account;
    /**
     * 系统收取管理费
     */
    private double manageFee;
    /**
     * 环迅收取手续费
     */
    private double ipsFee;
    
    
    public TppIpsPay() {
	}
    
	public TppIpsPay(long id, long inUserId, long outUserId, String inAcctNo, String outAcctNo, String type,
			String transferType, String merBillNo, String ipsBillNo, Date addTime, Date ipsTime, Date endTime,
			int borrowId, int tenderId, int collectionId, String message, byte status, double account,
			double manageFee, double ipsFee) {
		super();
		this.id = id;
		this.inUserId = inUserId;
		this.outUserId = outUserId;
		this.inAcctNo = inAcctNo;
		this.outAcctNo = outAcctNo;
		this.type = type;
		this.transferType = transferType;
		this.merBillNo = merBillNo;
		this.ipsBillNo = ipsBillNo;
		this.addTime = addTime;
		this.ipsTime = ipsTime;
		this.endTime = endTime;
		this.borrowId = borrowId;
		this.tenderId = tenderId;
		this.collectionId = collectionId;
		this.message = message;
		this.status = status;
		this.account = account;
		this.manageFee = manageFee;
		this.ipsFee = ipsFee;
	}

	/**
     * 获取
     * 
     * @return 
     */
    public long getId(){
        return id;
    }

    /**
     * 设置
     * 
     * @param id 要设置的
     */
    public void setId(long id){
        this.id = id;
    }
    /**
     * 获取转入用户id
     * 
     * @return 转入用户id
     */
    public long getInUserId(){
        return inUserId;
    }

    /**
     * 设置转入用户id
     * 
     * @param inUserId 要设置的转入用户id
     */
    public void setInUserId(long inUserId){
        this.inUserId = inUserId;
    }
    /**
     * 获取转出用户id
     * 
     * @return 转出用户id
     */
    public long getOutUserId(){
        return outUserId;
    }

    /**
     * 设置转出用户id
     * 
     * @param outUserId 要设置的转出用户id
     */
    public void setOutUserId(long outUserId){
        this.outUserId = outUserId;
    }
    /**
     * 获取转入IPS账号
     * 
     * @return 转入IPS账号
     */
    public String getInAcctNo(){
        return inAcctNo;
    }

    /**
     * 设置转入IPS账号
     * 
     * @param inAcctNo 要设置的转入IPS账号
     */
    public void setInAcctNo(String inAcctNo){
        this.inAcctNo = inAcctNo;
    }
    /**
     * 获取转出IPS账号
     * 
     * @return 转出IPS账号
     */
    public String getOutAcctNo(){
        return outAcctNo;
    }

    /**
     * 设置转出IPS账号
     * 
     * @param outAcctNo 要设置的转出IPS账号
     */
    public void setOutAcctNo(String outAcctNo){
        this.outAcctNo = outAcctNo;
    }
    /**
     * 获取类型,相当于nid
     * 
     * @return 类型,相当于nid
     */
    public String getType(){
        return type;
    }

    /**
     * 设置类型,相当于nid
     * 
     * @param type 要设置的类型,相当于nid
     */
    public void setType(String type){
        this.type = type;
    }
    /**
     * 获取IPS交易类型
     * 
     * @return IPS交易类型
     */
    public String getTransferType(){
        return transferType;
    }

    /**
     * 设置IPS交易类型
     * 
     * @param transferType 要设置的IPS交易类型
     */
    public void setTransferType(String transferType){
        this.transferType = transferType;
    }
    /**
     * 获取交易流水号订单号
     * 
     * @return 交易流水号订单号
     */
    public String getMerBillNo(){
        return merBillNo;
    }

    /**
     * 设置交易流水号订单号
     * 
     * @param merBillNo 要设置的交易流水号订单号
     */
    public void setMerBillNo(String merBillNo){
        this.merBillNo = merBillNo;
    }
    /**
     * 获取环迅交易订单号
     * 
     * @return 环迅交易订单号
     */
    public String getIpsBillNo(){
        return ipsBillNo;
    }

    /**
     * 设置环迅交易订单号
     * 
     * @param ipsBillNo 要设置的环迅交易订单号
     */
    public void setIpsBillNo(String ipsBillNo){
        this.ipsBillNo = ipsBillNo;
    }
    /**
     * 获取添加时间
     * 
     * @return 添加时间
     */
    public Date getAddTime(){
        return addTime;
    }

    /**
     * 设置添加时间
     * 
     * @param addTime 要设置的添加时间
     */
    public void setAddTime(Date addTime){
        this.addTime = addTime;
    }
    /**
     * 获取环迅处理日期
     * 
     * @return 环迅处理日期
     */
    public Date getIpsTime(){
        return ipsTime;
    }

    /**
     * 设置环迅处理日期
     * 
     * @param ipsTime 要设置的环迅处理日期
     */
    public void setIpsTime(Date ipsTime){
        this.ipsTime = ipsTime;
    }
    /**
     * 获取处理结束时间
     * 
     * @return 处理结束时间
     */
    public Date getEndTime(){
        return endTime;
    }

    /**
     * 设置处理结束时间
     * 
     * @param endTime 要设置的处理结束时间
     */
    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }
    /**
     * 获取标的ID
     * 
     * @return 标的ID
     */
    public long getBorrowId(){
        return borrowId;
    }

    /**
     * 设置标的ID
     * 
     * @param borrowId 要设置的标的ID
     */
    public void setBorrowId(long borrowId){
        this.borrowId = borrowId;
    }
    /**
     * 获取投标ID
     * 
     * @return 投标ID
     */
    public long getTenderId(){
        return tenderId;
    }

    /**
     * 设置投标ID
     * 
     * @param tenderId 要设置的投标ID
     */
    public void setTenderId(long tenderId){
        this.tenderId = tenderId;
    }
    public long getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(long repaymentId) {
        this.repaymentId = repaymentId;
    }

    /**
     * 获取投标ID
     * 
     * @return 投标ID
     */
    public long getCollectionId(){
        return collectionId;
    }

    /**
     * 设置投标ID
     * 
     * @param collectionId 要设置的投标ID
     */
    public void setCollectionId(long collectionId){
        this.collectionId = collectionId;
    }
    /**
     * 获取日志
     * 
     * @return 日志
     */
    public String getMessage(){
        return message;
    }

    /**
     * 设置日志
     * 
     * @param message 要设置的日志
     */
    public void setMessage(String message){
        this.message = message;
    }
    /**
     * 获取状态:默认0未处理,1处理成功，-1处理失败
     * 
     * @return 状态:默认0未处理,1处理成功，-1处理失败
     */
    public byte getStatus(){
        return status;
    }

    /**
     * 设置状态:默认0未处理,1处理成功，-1处理失败
     * 
     * @param status 要设置的状态:默认0未处理,1处理成功，-1处理失败
     */
    public void setStatus(byte status){
        this.status = status;
    }
    /**
     * 获取发生的金额
     * 
     * @return 发生的金额
     */
    public double getAccount(){
        return account;
    }

    /**
     * 设置发生的金额
     * 
     * @param account 要设置的发生的金额
     */
    public void setAccount(double account){
        this.account = account;
    }
    /**
     * 获取系统收取管理费
     * 
     * @return 系统收取管理费
     */
    public double getManageFee(){
        return manageFee;
    }

    /**
     * 设置系统收取管理费
     * 
     * @param manageFee 要设置的系统收取管理费
     */
    public void setManageFee(double manageFee){
        this.manageFee = manageFee;
    }
    /**
     * 获取环迅收取手续费
     * 
     * @return 环迅收取手续费
     */
    public double getIpsFee(){
        return ipsFee;
    }

    /**
     * 设置环迅收取手续费
     * 
     * @param ipsFee 要设置的环迅收取手续费
     */
    public void setIpsFee(double ipsFee){
        this.ipsFee = ipsFee;
    }

	public String getOriMerBillNo() {
		return oriMerBillNo;
	}

	public void setOriMerBillNo(String oriMerBillNo) {
		this.oriMerBillNo = oriMerBillNo;
	}
}


