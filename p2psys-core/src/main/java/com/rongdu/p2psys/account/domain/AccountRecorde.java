package com.rongdu.p2psys.account.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;

/**
 * 平台金额汇总记录
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月4日
 */
@Entity
@Table(name = (Global.DB_PREFIX + "account_recorde"))
public class AccountRecorde implements Serializable {
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

	/**
	 * 统计时间
	 */
	private Date addTime;

	public AccountRecorde() {
		super();
	}

	public AccountRecorde(double total, double useMoney, double noUseMoney,
			double collection) {
		super();
		this.total = total;
		this.useMoney = useMoney;
		this.noUseMoney = noUseMoney;
		this.collection = collection;
		this.addTime = new Date();
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
	 * @param id
	 *            要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @param total
	 *            要设置的账户总额
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
	 * @param useMoney
	 *            要设置的可用金额
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
	 * @param noUseMoney
	 *            要设置的冻结总额
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
	 * @param collection
	 *            要设置的待收总额
	 */
	public void setCollection(double collection) {
		this.collection = collection;
	}

	/**
	 * 获取统计时间
	 * 
	 * @return
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * 设置统计时间
	 * 
	 * @param addTime
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
