package com.rongdu.p2psys.account.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

/**
 * 用户资金账户表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = (Global.DB_PREFIX + "account"))
public class Account implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 用户ID
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 账户总额
	 */
	private double total;
	/**
	 * 可用金额
	 */
	private double useMoney;
	/**
	 * 冻结总额
	 */
	private double noUseMoney;
	/**
	 * 待收总额
	 */
	private double collection;

	public Account() {
		super();
	}

	public Account(User user) {
		super();
		this.user = user;
	}

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
	 * 获取用户ID
	 * 
	 * @return 用户ID
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置用户
	 * 
	 * @param user 要设置的用户
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取账户总额
	 * 
	 * @return 账户总额
	 */
	public double getTotal() {
		return total;
	}
	
    /**
     * 获取账户总额显示值
     * 
     * @return 账户总额显示值
     */
    public String getTotalDisp() {
        return String.valueOf(total).concat("元");
    }	

	/**
	 * 设置账户总额
	 * 
	 * @param total 要设置的账户总额
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * 获取可用金额
	 * 
	 * @return 可用金额
	 */
	public double getUseMoney() {
		return useMoney;
	}
	
    /**
     * 获取可用金额显示值
     * 
     * @return 可用金额显示值
     */
    public String getUseMoneyDisp() {
        return String.valueOf(useMoney).concat("元");
    }	

	/**
	 * 设置可用金额
	 * 
	 * @param useMoney 要设置的可用金额
	 */
	public void setUseMoney(double useMoney) {
		this.useMoney = useMoney;
	}

	/**
	 * 获取冻结总额
	 * 
	 * @return 冻结总额
	 */
	public double getNoUseMoney() {
		return noUseMoney;
	}
	
    /**
     * 获取冻结总额显示值
     * 
     * @return 冻结总额显示值
     */
    public String getNoUseMoneyDisp() {
        return String.valueOf(noUseMoney).concat("元");
    }

	/**
	 * 设置冻结总额
	 * 
	 * @param noUseMoney 要设置的冻结总额
	 */
	public void setNoUseMoney(double noUseMoney) {
		this.noUseMoney = noUseMoney;
	}

	/**
	 * 获取待收总额
	 * 
	 * @return 待收总额
	 */
	public double getCollection() {
		return collection;
	}
	
    /**
     * 获取待收总额显示值
     * 
     * @return 待收总额显示值
     */
    public String getCollectionDisp() {
        return String.valueOf(collection).concat("元");
    }	

	/**
	 * 设置待收总额
	 * 
	 * @param collection 要设置的待收总额
	 */
	public void setCollection(double collection) {
		this.collection = collection;
	}

}
