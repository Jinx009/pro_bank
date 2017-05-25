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
 * 债权投资实体
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Entity
@Table(name = "rd_bond_tender")
public class BondTender implements Serializable {
	
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
     * 状态
     */
    private byte status;
    /**
     * 投资人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * 投资金额
     */
    private double tenderMoney;
    /**
     * 提前付息
     */
    private double payInterest;
    /**
     * 已收金额
     */
    private double receivedAccount;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 订单日期
     */
    private String orderDate;    
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
     * @param bondId 要设置的债权
     */
    public void setBond(Bond bond){
        this.bond = bond;
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
     * 获取状态
     * 
     * @return 状态
     */
    public byte getStatus(){
        return status;
    }

    /**
     * 设置状态
     * 
     * @param status 要设置的状态
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
     * @param userId 要设置的投资人
     */
    public void setUser(User user){
        this.user = user;
    }
    /**
     * 获取投资金额
     * 
     * @return 投资金额
     */
    public double getTenderMoney(){
        return tenderMoney;
    }

    /**
     * 设置投资金额
     * 
     * @param tenderMoney 要设置的投资金额
     */
    public void setTenderMoney(double tenderMoney){
        this.tenderMoney = tenderMoney;
    }
    /**
     * 获取提前付息
     * 
     * @return 提前付息
     */
    public double getPayInterest(){
        return payInterest;
    }

    /**
     * 设置提前付息
     * 
     * @param payInterest 要设置的提前付息
     */
    public void setPayInterest(double payInterest){
        this.payInterest = payInterest;
    }
    /**
     * 获取已收金额
     * 
     * @return 已收金额
     */
    public double getReceivedAccount(){
        return receivedAccount;
    }

    /**
     * 设置已收金额
     * 
     * @param receivedAccount 要设置的已收金额
     */
    public void setReceivedAccount(double receivedAccount){
        this.receivedAccount = receivedAccount;
    }
    
    public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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


