package com.rongdu.p2psys.account.domain;

import java.io.Serializable;

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
 * 用户资金合计表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_account_sum")
public class AccountSum implements Serializable {
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 充值总和
	 */
	private double recharge;
	/**
	 * 提现总和
	 */
	private double cash;
	/**
	 * 提现手续费总和
	 */
	private double cashFee;
	/**
	 * 利息总和
	 */
	private double interest;
	/**
	 * 扣除的利息手续费
	 */
	private double interestFee;
	/**
	 * 奖励总和
	 */
	private double award;
	/**
	 * 扣款总和
	 */
	private double deduct;
	/**
	 * 已使用充值
	 */
	private double usedRecharge;
	/**
	 * 已使用利息
	 */
	private double usedInterest;
	/**
	 * 已使用奖励
	 */
	private double usedAward;
	/**
	 * 回款统计
	 */
	private double huikuan;
	/**
	 * 使用的回款
	 */
	private double usedHuikuan;
	/**
	 * 借款入账金额
	 */
	private double borrowCash;
	/**
	 * 已使用借款入账金额
	 */
	private double usedBorrowCash;
	/**
	 * 已还款合计
	 */
	private double repayCash;
	/**
	 * 使用的回款利息
	 */
	private double usedHuikuanInterest;

	/**
	 * 回款利息
	 */
	private double huikuanInterest;

	public AccountSum() {
		super();
	}

	public AccountSum(User user) {
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
	 * 设置用户ID
	 * 
	 * @param userId 要设置的用户ID
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取充值总和
	 * 
	 * @return 充值总和
	 */
	public double getRecharge() {
		return recharge;
	}

	/**
	 * 设置充值总和
	 * 
	 * @param recharge 要设置的充值总和
	 */
	public void setRecharge(double recharge) {
		this.recharge = recharge;
	}

	/**
	 * 获取提现总和
	 * 
	 * @return 提现总和
	 */
	public double getCash() {
		return cash;
	}

	/**
	 * 设置提现总和
	 * 
	 * @param cash 要设置的提现总和
	 */
	public void setCash(double cash) {
		this.cash = cash;
	}

	/**
	 * 获取提现手续费总和
	 * 
	 * @return 提现手续费总和
	 */
	public double getCashFee() {
		return cashFee;
	}

	/**
	 * 设置提现手续费总和
	 * 
	 * @param cashFee 要设置的提现手续费总和
	 */
	public void setCashFee(double cashFee) {
		this.cashFee = cashFee;
	}

	/**
	 * 获取利息总和
	 * 
	 * @return 利息总和
	 */
	public double getInterest() {
		return interest;
	}

	/**
	 * 设置利息总和
	 * 
	 * @param interest 要设置的利息总和
	 */
	public void setInterest(double interest) {
		this.interest = interest;
	}

	/**
	 * 获取扣除的利息手续费
	 * 
	 * @return 扣除的利息手续费
	 */
	public double getInterestFee() {
		return interestFee;
	}

	/**
	 * 设置扣除的利息手续费
	 * 
	 * @param interestFee 要设置的扣除的利息手续费
	 */
	public void setInterestFee(double interestFee) {
		this.interestFee = interestFee;
	}

	/**
	 * 获取奖励总和
	 * 
	 * @return 奖励总和
	 */
	public double getAward() {
		return award;
	}

	/**
	 * 设置奖励总和
	 * 
	 * @param award 要设置的奖励总和
	 */
	public void setAward(double award) {
		this.award = award;
	}

	/**
	 * 获取扣款总和
	 * 
	 * @return 扣款总和
	 */
	public double getDeduct() {
		return deduct;
	}

	/**
	 * 设置扣款总和
	 * 
	 * @param deduct 要设置的扣款总和
	 */
	public void setDeduct(double deduct) {
		this.deduct = deduct;
	}

	/**
	 * 获取已使用充值
	 * 
	 * @return 已使用充值
	 */
	public double getUsedRecharge() {
		return usedRecharge;
	}

	/**
	 * 设置已使用充值
	 * 
	 * @param usedRecharge 要设置的已使用充值
	 */
	public void setUsedRecharge(double usedRecharge) {
		this.usedRecharge = usedRecharge;
	}

	/**
	 * 获取已使用利息
	 * 
	 * @return 已使用利息
	 */
	public double getUsedInterest() {
		return usedInterest;
	}

	/**
	 * 设置已使用利息
	 * 
	 * @param usedInterest 要设置的已使用利息
	 */
	public void setUsedInterest(double usedInterest) {
		this.usedInterest = usedInterest;
	}

	/**
	 * 获取已使用奖励
	 * 
	 * @return 已使用奖励
	 */
	public double getUsedAward() {
		return usedAward;
	}

	/**
	 * 设置已使用奖励
	 * 
	 * @param usedAward 要设置的已使用奖励
	 */
	public void setUsedAward(double usedAward) {
		this.usedAward = usedAward;
	}

	/**
	 * 获取回款统计
	 * 
	 * @return 回款统计
	 */
	public double getHuikuan() {
		return huikuan;
	}

	/**
	 * 设置回款统计
	 * 
	 * @param huikuan 要设置的回款统计
	 */
	public void setHuikuan(double huikuan) {
		this.huikuan = huikuan;
	}

	/**
	 * 获取使用的回款
	 * 
	 * @return 使用的回款
	 */
	public double getUsedHuikuan() {
		return usedHuikuan;
	}

	/**
	 * 设置使用的回款
	 * 
	 * @param usedHuikuan 要设置的使用的回款
	 */
	public void setUsedHuikuan(double usedHuikuan) {
		this.usedHuikuan = usedHuikuan;
	}

	/**
	 * 获取借款入账金额
	 * 
	 * @return 借款入账金额
	 */
	public double getBorrowCash() {
		return borrowCash;
	}

	/**
	 * 设置借款入账金额
	 * 
	 * @param borrowCash 要设置的借款入账金额
	 */
	public void setBorrowCash(double borrowCash) {
		this.borrowCash = borrowCash;
	}

	/**
	 * 获取已使用借款入账金额
	 * 
	 * @return 已使用借款入账金额
	 */
	public double getUsedBorrowCash() {
		return usedBorrowCash;
	}

	/**
	 * 设置已使用借款入账金额
	 * 
	 * @param usedBorrowCash 要设置的已使用借款入账金额
	 */
	public void setUsedBorrowCash(double usedBorrowCash) {
		this.usedBorrowCash = usedBorrowCash;
	}

	/**
	 * 获取已还款合计
	 * 
	 * @return 已还款合计
	 */
	public double getRepayCash() {
		return repayCash;
	}

	/**
	 * 设置已还款合计
	 * 
	 * @param repayCash 要设置的已还款合计
	 */
	public void setRepayCash(double repayCash) {
		this.repayCash = repayCash;
	}

	/**
	 * 获取使用的回款利息
	 * 
	 * @return 使用的回款利息
	 */
	public double getUsedHuikuanInterest() {
		return usedHuikuanInterest;
	}

	/**
	 * 设置使用的回款利息
	 * 
	 * @param usedHuikuanInterest 要设置的使用的回款利息
	 */
	public void setUsedHuikuanInterest(double usedHuikuanInterest) {
		this.usedHuikuanInterest = usedHuikuanInterest;
	}

	public double getHuikuanInterest() {
		return huikuanInterest;
	}

	public void setHuikuanInterest(double huikuanInterest) {
		this.huikuanInterest = huikuanInterest;
	}
}
