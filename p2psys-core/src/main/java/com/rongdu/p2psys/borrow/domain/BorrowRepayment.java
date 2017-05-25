package com.rongdu.p2psys.borrow.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.user.domain.User;

/**
 * 还款表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_borrow_repayment")
public class BorrowRepayment implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/** 还款状态：未还款 */
	public static final int STATUS_WAIT_REPAY = 0;
	/** 还款状态：已还款 */
	public static final int STATUS_YES_REPAY = 1;
	/** 还款状态：网站垫付 */
	public static final int STATUS_INSTEAD_REPAY = 2;
	/** 还款状态：还款处理中 */
	public static final int STATUS_MANAGE_REPAY = 3;
    /** 还款状态：代偿处理中 */
    public static final int STATUS_MANAGE_COMPENSATE = 4;
    /** 还款状态：部分还款 */
    public static final int STATUS_PART_REPAY = 5;   
	
	/** 网站待还状态：正常还款 */
	public static final int WEB_STATUS_NORMAL = 1;
	/** 网站待还状态：网站垫付 */
    public static final int WEB_STATUS_INSTEAD = 3;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 还款状态 0未还；1已还 ;2网站垫付,3还款处理中,4代偿处理中
	 */
	private int status;
	/**
	 * 网站待还状态 1普通还款 3网站垫付
	 */
	private int webStatus;
	/**
	 * 期数
	 */
	private int period;
	/**
	 * 借款人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 借款标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borrow_id")
	private Borrow borrow;
	/**
	 * 投标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tender_id")
	private BorrowTender tender;
	/**
	 * 预计还款时间
	 */
	private Date repaymentTime;
	/**
	 * 实际还款时间
	 */
	private Date repaymentYesTime;
	/**
	 * 预还金额
	 */
	private double repaymentAccount;
	/**
	 * 已还金额
	 */
	private double repaymentYesAccount;
	/**
	 * 本金
	 */
	private double capital;
	/**
	 * 利息
	 */
	private double interest;
	/**
	 * 逾期天数
	 */
	private int lateDays;
	/**
	 * 逾期利息
	 */
	private double lateInterest;
	/**
	 * 实际展期天数
	 */
	private int realExtensionDay;
	/**
	 * 展期费率
	 */
	private double extensionInterest;
	/**
	 * 滞纳金
	 */
	private double forfeit;
	/**
	 * 催收费
	 */
	private double reminderFee;
	/**
	 * 时间
	 */
	private Date addTime;
	/**
	 * ip
	 */
	private String addIp;
	
    /**
     * 本期还款类型
     */
    private int type;
    
    /**
     * 实际还款者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repayment_user_id")
    private User realRepayer; 

    /**
     * 第三方还款流水号（数据库不需要这个字段）
     */
    private String merBillNo;
    
    /**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取还款状态 0未还；1已还 ;2网站垫付
	 * 
	 * @return 还款状态 0未还；1已还 ;2网站垫付
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置还款状态 0未还；1已还 ;2网站垫付
	 * 
	 * @param status 要设置的还款状态 0未还；1已还 ;2网站垫付
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取网站待还状态 1普通还款 3网站垫付
	 * 
	 * @return 网站待还状态 1普通还款 3网站垫付
	 */
	public int getWebStatus() {
		return webStatus;
	}

	/**
	 * 设置网站待还状态 1普通还款 3网站垫付
	 * 
	 * @param webStatus 要设置的网站待还状态 1普通还款 3网站垫付
	 */
	public void setWebStatus(int webStatus) {
		this.webStatus = webStatus;
	}

	/**
	 * 获取期数
	 * 
	 * @return 期数
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * 设置期数
	 * 
	 * @param period 要设置的期数
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

	public BorrowTender getTender() {
		return tender;
	}

	public void setTender(BorrowTender tender) {
		this.tender = tender;
	}

	/**
	 * 获取预计还款时间
	 * 
	 * @return 预计还款时间
	 */
	public Date getRepaymentTime() {
		return repaymentTime;
	}

	/**
	 * 设置预计还款时间
	 * 
	 * @param repaymentTime 要设置的预计还款时间
	 */
	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	/**
	 * 获取实际还款时间
	 * 
	 * @return 实际还款时间
	 */
	public Date getRepaymentYesTime() {
		return repaymentYesTime;
	}

	/**
	 * 设置实际还款时间
	 * 
	 * @param repaymentYesTime 要设置的实际还款时间
	 */
	public void setRepaymentYesTime(Date repaymentYesTime) {
		this.repaymentYesTime = repaymentYesTime;
	}

	/**
	 * 获取预还金额
	 * 
	 * @return 预还金额
	 */
	public double getRepaymentAccount() {
		return repaymentAccount;
	}

	/**
	 * 设置预还金额
	 * 
	 * @param repaymentAccount 要设置的预还金额
	 */
	public void setRepaymentAccount(double repaymentAccount) {
		this.repaymentAccount = repaymentAccount;
	}

	/**
	 * 获取已还金额
	 * 
	 * @return 已还金额
	 */
	public double getRepaymentYesAccount() {
		return repaymentYesAccount;
	}

	/**
	 * 设置已还金额
	 * 
	 * @param repaymentYesAccount 要设置的已还金额
	 */
	public void setRepaymentYesAccount(double repaymentYesAccount) {
		this.repaymentYesAccount = repaymentYesAccount;
	}

	/**
	 * 获取本金
	 * 
	 * @return 本金
	 */
	public double getCapital() {
		return capital;
	}

	/**
	 * 设置本金
	 * 
	 * @param capital 要设置的本金
	 */
	public void setCapital(double capital) {
		this.capital = capital;
	}

	/**
	 * 获取利息
	 * 
	 * @return 利息
	 */
	public double getInterest() {
		return interest;
	}

	/**
	 * 设置利息
	 * 
	 * @param interest 要设置的利息
	 */
	public void setInterest(double interest) {
		this.interest = interest;
	}

	/**
	 * 获取逾期天数
	 * 
	 * @return 逾期天数
	 */
	public int getLateDays() {
		return lateDays;
	}

	/**
	 * 设置逾期天数
	 * 
	 * @param lateDays 要设置的逾期天数
	 */
	public void setLateDays(int lateDays) {
		this.lateDays = lateDays;
	}

	/**
	 * 获取逾期利息
	 * 
	 * @return 逾期利息
	 */
	public double getLateInterest() {
		return lateInterest;
	}

	/**
	 * 设置逾期利息
	 * 
	 * @param lateInterest 要设置的逾期利息
	 */
	public void setLateInterest(double lateInterest) {
		this.lateInterest = lateInterest;
	}

	/**
	 * 获取实际展期天数
	 * 
	 * @return 实际展期天数
	 */
	public int getRealExtensionDay() {
		return realExtensionDay;
	}

	/**
	 * 设置实际展期天数
	 * 
	 * @param realExtensionDay 要设置的实际展期天数
	 */
	public void setRealExtensionDay(int realExtensionDay) {
		this.realExtensionDay = realExtensionDay;
	}

	/**
	 * 获取展期费率
	 * 
	 * @return 展期费率
	 */
	public double getExtensionInterest() {
		return extensionInterest;
	}

	/**
	 * 设置展期费率
	 * 
	 * @param extensionInterest 要设置的展期费率
	 */
	public void setExtensionInterest(double extensionInterest) {
		this.extensionInterest = extensionInterest;
	}

	/**
	 * 获取滞纳金
	 * 
	 * @return 滞纳金
	 */
	public double getForfeit() {
		return forfeit;
	}

	/**
	 * 设置滞纳金
	 * 
	 * @param forfeit 要设置的滞纳金
	 */
	public void setForfeit(double forfeit) {
		this.forfeit = forfeit;
	}

	/**
	 * 获取崔收费
	 * 
	 * @return 崔收费
	 */
	public double getReminderFee() {
		return reminderFee;
	}

	/**
	 * 设置崔收费
	 * 
	 * @param reminderFee 要设置的崔收费
	 */
	public void setReminderFee(double reminderFee) {
		this.reminderFee = reminderFee;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取ip
	 * 
	 * @return ip
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置ip
	 * 
	 * @param addIp 要设置的ip
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	
    /**
     * 获取还款类型
     * 
     * @return 还款类型
     */	
    public int getType() {
        return type;
    }

    /**
     * 设置还款类型
     * 
     * @param type 要还款类型
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取实际还款者
     * 
     * @return 实际还款者
     */
    public User getRealRepayer() {
        return realRepayer;
    }

    /**
     * 设置实际还款者
     * 
     * @param realRepayer 实际还款者
     */
    public void setRealRepayer(User realRepayer) {
        this.realRepayer = realRepayer;
    }

    public String getMerBillNo() {
        return merBillNo;
    }

    public void setMerBillNo(String merBillNo) {
        this.merBillNo = merBillNo;
    }	
}
