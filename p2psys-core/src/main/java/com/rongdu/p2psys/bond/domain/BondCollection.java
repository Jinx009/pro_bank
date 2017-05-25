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
 * 债权待收实体
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Entity
@Table(name = "rd_bond_collection")
public class BondCollection implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    /**
     * 债权
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bond_id")
    private Bond bond;
    /**
     * 债权投资ID
     */
    private long bondTenderId;
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
     * 还款ID
     */
    private long borrowRepaymentId;
    /**
     * 状态：0未还，1已还
     */
    private byte status;
    /**
     * 投资人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * 待收期数
     */
    private byte period;
    /**
     * 预计收款时间
     */
    private Date collectionTime;
    /**
     * 实际收款时间
     */
    private Date collectionYesTime;
    /**
     * 预计收款金额
     */
    private double collectionAccount;
    /**
     * 实际收款金额
     */
    private double collectionYesAccount;
    /**
     * 待收本金
     */
    private double capital;
    /**
     * 待收利息
     */
    private double interest;
    /**
     * 待收奖励
     */
    private double award;
    /**
     * 已成功转出本金
     */
    private double bondCapital;
    /**
     * 已成功转出利息
     */
    private double bondInterest;
    /**
     * 已成功转出奖励
     */
    private double bondAward;
    /**
     * 预计天数
     */
    private int lateDays;
    /**
     * 逾期利息
     */
    private double lateInterest;
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
    /**
     * 获取债权
     * 
     * @return 债权
     */
    public Bond getBond(){
        return bond;
    }

    /**
     * 设置债权
     * 
     * @param bond 要设置的债权
     */
    public void setBond(Bond bond){
        this.bond = bond;
    }
    /**
     * 获取债权投资ID
     * 
     * @return 债权投资ID
     */
    public long getBondTenderId(){
        return bondTenderId;
    }

    /**
     * 设置债权投资ID
     * 
     * @param bondTenderId 要设置的债权投资ID
     */
    public void setBondTenderId(long bondTenderId){
        this.bondTenderId = bondTenderId;
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
     * 获取还款ID
     * 
     * @return 还款ID
     */
    public long getBorrowRepaymentId(){
        return borrowRepaymentId;
    }

    /**
     * 设置还款ID
     * 
     * @param borrowRepaymentId 要设置的还款ID
     */
    public void setBorrowRepaymentId(long borrowRepaymentId){
        this.borrowRepaymentId = borrowRepaymentId;
    }
    /**
     * 获取状态：0未还，1已还
     * 
     * @return 状态：0未还，1已还
     */
    public byte getStatus(){
        return status;
    }

    /**
     * 设置状态：0未还，1已还
     * 
     * @param status 要设置的状态：0未还，1已还
     */
    public void setStatus(byte status){
        this.status = status;
    }
    /**
     * 获取投资人
     * 
     * @return 投资人
     */
    public User getUser(){
        return user;
    }

    /**
     * 设置投资人
     * 
     * @param user 要设置的投资人
     */
    public void setUser(User user){
        this.user = user;
    }
    /**
     * 获取待收期数
     * 
     * @return 待收期数
     */
    public byte getPeriod(){
        return period;
    }

    /**
     * 设置待收期数
     * 
     * @param period 要设置的待收期数
     */
    public void setPeriod(byte period){
        this.period = period;
    }
    /**
     * 获取预计收款时间
     * 
     * @return 预计收款时间
     */
    public Date getCollectionTime(){
        return collectionTime;
    }

    /**
     * 设置预计收款时间
     * 
     * @param collectionTime 要设置的预计收款时间
     */
    public void setCollectionTime(Date collectionTime){
        this.collectionTime = collectionTime;
    }
    /**
     * 获取实际收款时间
     * 
     * @return 实际收款时间
     */
    public Date getCollectionYesTime(){
        return collectionYesTime;
    }

    /**
     * 设置实际收款时间
     * 
     * @param collectionYesTime 要设置的实际收款时间
     */
    public void setCollectionYesTime(Date collectionYesTime){
        this.collectionYesTime = collectionYesTime;
    }
    /**
     * 获取预计收款金额
     * 
     * @return 预计收款金额
     */
    public double getCollectionAccount(){
        return collectionAccount;
    }

    /**
     * 设置预计收款金额
     * 
     * @param collectionAccount 要设置的预计收款金额
     */
    public void setCollectionAccount(double collectionAccount){
        this.collectionAccount = collectionAccount;
    }
    /**
     * 获取实际收款金额
     * 
     * @return 实际收款金额
     */
    public double getCollectionYesAccount(){
        return collectionYesAccount;
    }

    /**
     * 设置实际收款金额
     * 
     * @param collectionYesAccount 要设置的实际收款金额
     */
    public void setCollectionYesAccount(double collectionYesAccount){
        this.collectionYesAccount = collectionYesAccount;
    }
    /**
     * 获取待收本金
     * 
     * @return 待收本金
     */
    public double getCapital(){
        return capital;
    }

    /**
     * 设置待收本金
     * 
     * @param capital 要设置的待收本金
     */
    public void setCapital(double capital){
        this.capital = capital;
    }
    /**
     * 获取待收利息
     * 
     * @return 待收利息
     */
    public double getInterest(){
        return interest;
    }

    /**
     * 设置待收利息
     * 
     * @param interest 要设置的待收利息
     */
    public void setInterest(double interest){
        this.interest = interest;
    }
    /**
     * 获取待收奖励
     * 
     * @return 待收奖励
     */
    public double getAward(){
        return award;
    }

    /**
     * 设置待收奖励
     * 
     * @param award 要设置的待收奖励
     */
    public void setAward(double award){
        this.award = award;
    }
    /**
     * 获取已成功转出本金
     * 
     * @return 已成功转出本金
     */
    public double getBondCapital(){
        return bondCapital;
    }

    /**
     * 设置已成功转出本金
     * 
     * @param bondCapital 要设置的已成功转出本金
     */
    public void setBondCapital(double bondCapital){
        this.bondCapital = bondCapital;
    }
    /**
     * 获取已成功转出利息
     * 
     * @return 已成功转出利息
     */
    public double getBondInterest(){
        return bondInterest;
    }

    /**
     * 设置已成功转出利息
     * 
     * @param bondInterest 要设置的已成功转出利息
     */
    public void setBondInterest(double bondInterest){
        this.bondInterest = bondInterest;
    }
    /**
     * 获取已成功转出奖励
     * 
     * @return 已成功转出奖励
     */
    public double getBondAward(){
        return bondAward;
    }

    /**
     * 设置已成功转出奖励
     * 
     * @param bondAward 要设置的已成功转出奖励
     */
    public void setBondAward(double bondAward){
        this.bondAward = bondAward;
    }
    /**
     * 获取预计天数
     * 
     * @return 预计天数
     */
    public int getLateDays(){
        return lateDays;
    }

    /**
     * 设置预计天数
     * 
     * @param lateDays 要设置的预计天数
     */
    public void setLateDays(int lateDays){
        this.lateDays = lateDays;
    }
    /**
     * 获取逾期利息
     * 
     * @return 逾期利息
     */
    public double getLateInterest(){
        return lateInterest;
    }

    /**
     * 设置逾期利息
     * 
     * @param lateInterest 要设置的逾期利息
     */
    public void setLateInterest(double lateInterest){
        this.lateInterest = lateInterest;
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


