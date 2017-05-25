package com.rongdu.p2psys.ppfund.domain;

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

import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.voucher.domain.InterestRate;

/**
 * PPfund（资金管理产品）
 */
@Entity
@Table(name = "rd_ppfund")
public class Ppfund implements Serializable {

	private static final long serialVersionUID = -6505190265853304436L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 状态
	 * 
	 * 0:待审 、1：开启、2：审核失败、3：关闭
	 */
	private int status;

	/**
	 * 是否固定期限产品
	 */
	private int isFixedTerm;

	/**
	 * 借款期限
	 * 
	 * 0代表无限期
	 */
	private int timeLimit;

	/**
	 * 年利率
	 */
	private double apr;

	/**
	 * 产品金额
	 */
	private double account;

	/**
	 * 实际募资金额
	 */
	private double accountYes;

	/**
	 * 截标前金额值
	 */
	private double oldAccount;

	/**
	 * 进度
	 */
	private double scales;

	/**
	 * 单笔最低投资金额
	 */
	private double lowestAccount;

	/**
	 * 单笔最高投资金额
	 */
	private double mostAccount;

	/**
	 * 最多投资总额
	 */
	private double mostAccountTotal;

	/**
	 * 周期天数
	 */
	private int cycle;

	/**
	 * 计息方式
	 * 
	 * T+?,?代表interestWay的值
	 */
	private int interestWay;

	/**
	 * 风险备用费率
	 */
	private double riskReserveRate;

	/**
	 * 居间费率
	 */
	private double manageRate;

	/**
	 * 每日购买开始时间，时分秒格式(hh:mm)
	 */
	private String startTime;

	/**
	 * 每日购买截止时间，时分秒格式(hh:mm)
	 */
	private String endTime;

	/**
	 * 详情介绍
	 */
	private String content;

	/**
	 * 产品编号
	 */
	private String pidNo;

	/**
	 * 红包
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "red_packet_id")
	private RedPacket redPacket;

	/**
	 * 发布时间
	 */
	private Date addTime;

	/**
	 * 发布IP
	 */
	private String addIp;

	/**
	 * 标类型
	 *
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	private ProductType productType;

	/**
	 * 加息券
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_rate_id")
	private InterestRate interestRate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsFixedTerm() {
		return isFixedTerm;
	}

	public void setIsFixedTerm(int isFixedTerm) {
		this.isFixedTerm = isFixedTerm;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public double getAccountYes() {
		return accountYes;
	}

	public void setAccountYes(double accountYes) {
		this.accountYes = accountYes;
	}

	public double getOldAccount() {
		return oldAccount;
	}

	public void setOldAccount(double oldAccount) {
		this.oldAccount = oldAccount;
	}

	public double getScales() {
		return scales;
	}

	public void setScales(double scales) {
		this.scales = scales;
	}

	public double getLowestAccount() {
		return lowestAccount;
	}

	public void setLowestAccount(double lowestAccount) {
		this.lowestAccount = lowestAccount;
	}

	public double getMostAccount() {
		return mostAccount;
	}

	public void setMostAccount(double mostAccount) {
		this.mostAccount = mostAccount;
	}

	public double getMostAccountTotal() {
		return mostAccountTotal;
	}

	public void setMostAccountTotal(double mostAccountTotal) {
		this.mostAccountTotal = mostAccountTotal;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public int getInterestWay() {
		return interestWay;
	}

	public void setInterestWay(int interestWay) {
		this.interestWay = interestWay;
	}

	public double getRiskReserveRate() {
		return riskReserveRate;
	}

	public void setRiskReserveRate(double riskReserveRate) {
		this.riskReserveRate = riskReserveRate;
	}

	public double getManageRate() {
		return manageRate;
	}

	public void setManageRate(double manageRate) {
		this.manageRate = manageRate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPidNo() {
		return pidNo;
	}

	public void setPidNo(String pidNo) {
		this.pidNo = pidNo;
	}

	public RedPacket getRedPacket() {
		return redPacket;
	}

	public void setRedPacket(RedPacket redPacket) {
		this.redPacket = redPacket;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public InterestRate getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(InterestRate interestRate) {
		this.interestRate = interestRate;
	}

}
