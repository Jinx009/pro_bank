package com.rongdu.p2psys.bond.domain;

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

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.user.domain.User;

/**
 * 债权实体
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Entity
@Table(name = "rd_bond")
public class Bond implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 债权状态:发布0 */
	public static final byte STATUS_RELEASE = 0;
	
	/** 债权状态:初审通过1 */
	public static final byte STATUS_VERIFY_SUCC = 1;
	
	/** 债权状态:初审不通过2 */
	public static final byte STATUS_VERIFY_FAIL = 2;
	
	/** 债权状态:已成功转让3 */
	public static final byte STATUS_SUCC_FULL = 3;
	
	/** 债权状态:自动撤回4 */
	public static final byte STATUS_STOP_AUTO = 4;
	
	/** 债权状态:后台撤回5 */
	public static final byte STATUS_STOP_MANAGER = 5;
	
	/** 债权状态:用户撤回6 */
	public static final byte STATUS_STOP_USER = 6;
	
	
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    /**
     * 债权转让编号
     */
    private long dayId;
    /**
     * 投资外键ID
     */
    private long kfId;
    /**
     * 债权人
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
     * 投标ID
     */
    private long borrowTenderId;
    /**
     * 转让类型:0投资转让，1债权再次转让
     */
    private byte type;
    /**
     * 债权金额
     */
    private double bondMoney;
    /**
     * 转出折扣率
     */
    private double bondApr;
	/**
	 * 开始期数
	 */
	private int startPeriod;
    /**
     * 已售债权本金
     */
    private double soldCapital;
    /**
     * 已售债权利息
     */
    private double soldInterest;
    /**
     * 已售债权奖励
     */
    private double soldAward;
    /**
     * 债权有效天数
     */
    private int limitDay;
    /**
     * 债权截止日期
     */
    private Date bondEndTime;
    /**
     * 债权状态:发布0，初审通过1，初审不通过2，转让完成3，自动撤回4，后台撤回5，用户撤回6
     */
    private byte status;
    /**
     * 债权等级：0普通、1优质
     */
    private byte level;
    /**
     * 债权定向密码
     */
    private String pwd;
    /**
     * 初审时间
     */
    private Date verifyTime;
    /**
     * 初审人
     */
    private long verifyUserId;
    /**
     * 初审备注
     */
    private String verifyRemark;
    /**
     * 复审时间
     */
    private Date fullVerifyTime;
    /**
     * 复审人
     */
    private long fullVerifyUserId;
    /**
     * 复审备注
     */
    private String fullVerifyRemark;
    /**
     * 添加时间
     */
    private Date addTime;
    
    /**
     * 获取主键
     * 
     * @return 主键
     */
    public long getId(){
        return id;
    }
    /**
     * 设置主键
     * 
     * @param id 要设置的主键
     */
    public void setId(long id){
        this.id = id;
    }
    
    public long getDayId() {
		return dayId;
	}

	public void setDayId(long dayId) {
		this.dayId = dayId;
	}

	/**
     * 获取投资外键ID
     * 
     * @return 投资外键ID
     */
    public long getKfId(){
        return kfId;
    }

    /**
     * 设置投资外键ID
     * 
     * @param kfId 要设置的投资外键ID
     */
    public void setKfId(long kfId){
        this.kfId = kfId;
    }
    /**
     * 获取债权人
     * 
     * @return 债权人
     */
    public User getUser(){
        return user;
    }

    /**
     * 设置债权人
     * 
     * @param userId 要设置的债权人
     */
    public void setUser(User user){
        this.user = user;
    }
    /**
     * 获取标ID
     * 
     * @return 标ID
     */
    public Borrow getBorrow(){
        return borrow;
    }

    /**
     * 设置标ID
     * 
     * @param borrowId 要设置的标ID
     */
    public void setBorrow(Borrow borrow){
        this.borrow = borrow;
    }
    /**
     * 获取投标ID
     * 
     * @return 投标ID
     */
    public long getBorrowTenderId(){
        return borrowTenderId;
    }

    /**
     * 设置投标ID
     * 
     * @param borrowTenderId 要设置的投标ID
     */
    public void setBorrowTenderId(long borrowTenderId){
        this.borrowTenderId = borrowTenderId;
    }
    /**
     * 获取转让类型:0投资转让，1债权再次转让
     * 
     * @return 转让类型:0投资转让，1债权再次转让
     */
    public byte getType(){
        return type;
    }

    /**
     * 设置转让类型:0投资转让，1债权再次转让
     * 
     * @param type 要设置的转让类型:0投资转让，1债权再次转让
     */
    public void setType(byte type){
        this.type = type;
    }
    /**
     * 获取债权金额
     * 
     * @return 债权金额
     */
    public double getBondMoney(){
        return bondMoney;
    }

    /**
     * 设置债权金额
     * 
     * @param bondMoney 要设置的债权金额
     */
    public void setBondMoney(double bondMoney){
        this.bondMoney = bondMoney;
    }
    /**
     * 获取转出折扣率
     * 
     * @return 转出折扣率
     */
    public double getBondApr(){
        return bondApr;
    }
    /**
     * 设置转出折扣率
     * 
     * @param bondApr 要设置的转出折扣率
     */
    public void setBondApr(double bondApr){
        this.bondApr = bondApr;
    }
    public int getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(int startPeriod) {
		this.startPeriod = startPeriod;
	}
	/**
     * 获取已售债权本金
     * 
     * @return 已售债权本金
     */
    public double getSoldCapital(){
        return soldCapital;
    }

    /**
     * 设置已售债权本金
     * 
     * @param soldCapital 要设置的已售债权本金
     */
    public void setSoldCapital(double soldCapital){
        this.soldCapital = soldCapital;
    }
    /**
     * 获取已售债权利息
     * 
     * @return 已售债权利息
     */
    public double getSoldInterest(){
        return soldInterest;
    }

    /**
     * 设置已售债权利息
     * 
     * @param soldInterest 要设置的已售债权利息
     */
    public void setSoldInterest(double soldInterest){
        this.soldInterest = soldInterest;
    }
    /**
     * 获取已售债权奖励
     * 
     * @return 已售债权奖励
     */
    public double getSoldAward(){
        return soldAward;
    }

    /**
     * 设置已售债权奖励
     * 
     * @param soldAward 要设置的已售债权奖励
     */
    public void setSoldAward(double soldAward){
        this.soldAward = soldAward;
    }
    /**
     * 获取债权有效天数
     * 
     * @return 债权有效天数
     */
    public int getLimitDay(){
        return limitDay;
    }

    /**
     * 设置债权有效天数
     * 
     * @param limitDay 要设置的债权有效天数
     */
    public void setLimitDay(int limitDay){
        this.limitDay = limitDay;
    }
    /**
     * 获取债权截止日期
     * 
     * @return 债权截止日期
     */
    public Date getBondEndTime(){
        return bondEndTime;
    }

    /**
     * 设置债权截止日期
     * 
     * @param bondEndTime 要设置的债权截止日期
     */
    public void setBondEndTime(Date bondEndTime){
        this.bondEndTime = bondEndTime;
    }
    /**
     * 获取债权状态
     * 
     * @return 债权状态
     */
    public byte getStatus(){
        return status;
    }

    /**
     * 设置债权状态
     * 
     * @param status 要设置的债权状态
     */
    public void setStatus(byte status){
        this.status = status;
    }
    /**
     * 获取债权等级：0普通、1优质
     * 
     * @return 债权等级：0普通、1优质
     */
    public byte getLevel(){
        return level;
    }

    /**
     * 设置债权等级：0普通、1优质
     * 
     * @param level 要设置的债权等级：0普通、1优质
     */
    public void setLevel(byte level){
        this.level = level;
    }
    /**
     * 获取债权定向密码
     * 
     * @return 债权定向密码
     */
    public String getPwd(){
        return pwd;
    }

    /**
     * 设置债权定向密码
     * 
     * @param pwd 要设置的债权定向密码
     */
    public void setPwd(String pwd){
        this.pwd = pwd;
    }
    /**
     * 获取初审时间
     * 
     * @return 初审时间
     */
    public Date getVerifyTime(){
        return verifyTime;
    }

    /**
     * 设置初审时间
     * 
     * @param verifyTime 要设置的初审时间
     */
    public void setVerifyTime(Date verifyTime){
        this.verifyTime = verifyTime;
    }
    /**
     * 获取初审人
     * 
     * @return 初审人
     */
    public long getVerifyUserId(){
        return verifyUserId;
    }

    /**
     * 设置初审人
     * 
     * @param verifyUserId 要设置的初审人
     */
    public void setVerifyUserId(long verifyUserId){
        this.verifyUserId = verifyUserId;
    }
    /**
     * 获取初审备注
     * 
     * @return 初审备注
     */
    public String getVerifyRemark(){
        return verifyRemark;
    }

    /**
     * 设置初审备注
     * 
     * @param verifyRemark 要设置的初审备注
     */
    public void setVerifyRemark(String verifyRemark){
        this.verifyRemark = verifyRemark;
    }
    /**
     * 获取复审时间
     * 
     * @return 复审时间
     */
    public Date getFullVerifyTime(){
        return fullVerifyTime;
    }

    /**
     * 设置复审时间
     * 
     * @param fullVerifyTime 要设置的复审时间
     */
    public void setFullVerifyTime(Date fullVerifyTime){
        this.fullVerifyTime = fullVerifyTime;
    }
    /**
     * 获取复审人
     * 
     * @return 复审人
     */
    public long getFullVerifyUserId(){
        return fullVerifyUserId;
    }

    /**
     * 设置复审人
     * 
     * @param fullVerifyUserId 要设置的复审人
     */
    public void setFullVerifyUserId(long fullVerifyUserId){
        this.fullVerifyUserId = fullVerifyUserId;
    }
    /**
     * 获取复审备注
     * 
     * @return 复审备注
     */
    public String getFullVerifyRemark(){
        return fullVerifyRemark;
    }

    /**
     * 设置复审备注
     * 
     * @param fullVerifyRemark 要设置的复审备注
     */
    public void setFullVerifyRemark(String fullVerifyRemark){
        this.fullVerifyRemark = fullVerifyRemark;
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
}


