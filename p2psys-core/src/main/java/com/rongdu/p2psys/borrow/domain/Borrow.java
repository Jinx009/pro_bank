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

import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.nb.voucher.domain.InterestRate;
import com.rongdu.p2psys.user.domain.CouponCategory;
import com.rongdu.p2psys.user.domain.User;

/**
 * 借款标
 */
@Entity
@Table(name = "rd_borrow")
public class Borrow implements Serializable {

	private static final long serialVersionUID = 3989430963989502118L;

	/********** 种类 **********/

	/**
	 * 101:秒标
	 */
	public static final int TYPE_SECOND = 101;

	/**
	 * 102:信用标
	 */
	public static final int TYPE_CREDIT = 102;

	/**
	 * 103:固定收益
	 */
	public static final int TYPE_MORTGAGE = 103;

	/**
	 * 104:净值标
	 */
	public static final int TYPE_PROPERTY = 104;
	
	/**
	 * vip标
	 */
	public static final int TYPE_VIP = 199;

	/**
	 * 105:随投随取
	 */
	public static final int TYPE_CASH = 105;

	/**
	 * 106:优选基金
	 */
	public static final int TYPE_FUNDS = 106;

	/**
	 * 107:股权众筹
	 */
	public static final int TYPE_STOCK_CROWDFUNDING = 107;

	/**
	 * 110:流转标
	 */
	public static final int TYPE_FLOW = 110;

	/**
	 * 112:担保标
	 */
	public static final int TYPE_OFFVOUCH = 112;

	/**
	 * 119:海外投资
	 */
	public static final int TYPE_ESTATE = 119;

	/**
	 * 122:浮动收益类
	 */
	public static final int TYPE_ENTRUST = 122;

	/********** 状态 **********/

	/**
	 * -2:管理员撤回处理中
	 */
	public static final int STATUS_MANAGER_CANCEL_DOING = -2;

	/**
	 * -1:用户自己取消
	 */
	public static final int STATUS_USER_CANCEL = -1;

	/**
	 * 0:发标未初审
	 */
	public static final int STATUS_PUBLISHING = 0;

	/**
	 * 1:初审通过
	 */
	public static final int STATUS_TRIAL_PASSED = 1;

	/**
	 * 19:初审通过，disruptor处理
	 */
	public static final int STATUS_AUTO_TENDER_DONE = 19;

	/**
	 * 2:初审不通过
	 */
	public static final int STATUS_TRIAL_PASSLESS = 2;

	/**
	 * 3:满标复审通过
	 */
	public static final int STATUS_RECHECK_PASS = 3;

	/**
	 * 4:满标复审不通过
	 */
	public static final int STATUS_RECHECK_PASSLESS = 4;

	/**
	 * 49:满标复审不通过，disruptor处理
	 */
	public static final int STATUS_RECHECK_PASSLESS2 = 49;

	/**
	 * 5:后台管理员撤回
	 */
	public static final int STATUS_MANAGER_CANCEL = 5;

	/**
	 * 59:后台管理员撤回，disruptor处理
	 */
	public static final int STATUS_MANAGER_CANCEL2 = 59;

	/**
	 * 6:还款中
	 */
	public static final int STATUS_REPAYMENT_START = 6;

	/**
	 * 7:部分还款中
	 */
	public static final int STATUS_REPAYMENT_DOING = 7;

	/**
	 * 8:还款成功
	 */
	public static final int STATUS_REPAYMENT_DONE = 8;

	/**
	 * 9:已登记待确认
	 */
	public static final int STATUS_REGISTRATE_DOING = 9;

	/**
	 * 11:已确认待初审
	 */
	public static final int STATUS_TRIAL_WAITING = 11;

	/**
	 * 99:无关状态
	 */
	public static final int STATUS_UNRELATED = 99;

	/********** 还款方式 **********/

	/**
	 * 1:等额本息（按月分期还款）
	 */
	public static final int STYLE_INSTALLMENT_REPAYMENT = 1;

	/**
	 * 2:利随本清（一次性还款）
	 */
	public static final int STYLE_ONETIME_REPAYMENT = 2;

	/**
	 * 3:每月还息到期还本
	 */
	public static final int STYLE_MONTHLY_INTEREST = 3;

	/**
	 * 4:按照设定日期还息，到期还本
	 */
	public static final int STYLE_MIDDLEDAY_INTEREST = 4;

	/********** 标类型（天标，月标） **********/

	/**
	 * 0:月标
	 */
	public static final int TIME_TYPE_MONTH = 0;

	/**
	 * 1:天标
	 */
	public static final int TIME_TYPE_DAY = 1;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 借款人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 借款标题
	 */
	private String name;

	/**
	 * 借款标信息详情
	 */
	private String content;

	/**
	 * 标种
	 */
	private int type;

	/**
	 * 标状态
	 */
	private int status;

	/**
	 * 是否新手标
	 * <p>
	 * 1:为新手标
	 * </p>
	 * <p>
	 * 0:不是新手标
	 * </p>
	 */
	private int isNovice;

	/**
	 * 期数
	 */
	private int period;

	/**
	 * 借款总额
	 */
	private double account;

	/**
	 * 实还总金额(已借)
	 */
	private double accountYes;

	/**
	 * 每份价格
	 */
	private Double unitAmount;

	/**
	 * 完成比例
	 */
	private double scales;

	/**
	 * 年利率
	 */
	private double apr;

	/**
	 * 还款方式
	 */
	private int style;

	/**
	 * 借款用途
	 */
	private String borrowUse;

	/**
	 * 标类
	 * <p>
	 * 0:月标
	 * </p>
	 * <p>
	 * 1:天标
	 * </p>
	 */
	private int borrowTimeType;

	/**
	 * 借款期限(月标)
	 */
	private int timeLimit;

	/**
	 * 定向密码
	 */
	private String pwd;

	/**
	 * 定时时间
	 */
	private Date fixedTime;

	/**
	 * 流转标金额
	 */
	private double flowMoney;

	/**
	 * 流转标的总份数
	 */
	private int flowCount;

	/**
	 * 流转标已经购买的份数
	 */
	private int flowYesCount;

	/**
	 * 最低投标金额
	 */
	private double lowestAccount;

	/**
	 * 最多投标金额
	 */
	private double mostAccount;

	/**
	 * 单笔最低限额
	 */
	private double lowestSingleLimit;

	/**
	 * 单笔最高限额
	 */
	private double mostSingleLimit;

	/**
	 * 有效时间(天数)
	 */
	private int validTime;

	/**
	 * 应还本金
	 */
	private double repaymentAccount;

	/**
	 * 实还本金
	 */
	private double repaymentYesAccount;

	/**
	 * 实还利息
	 */
	private double repaymentYesInterest;

	/**
	 * 投标完成笔数
	 */
	private int tenderTimes;

	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * 复审时间
	 */
	private Date reviewTime;

	/**
	 * 有效期过期时间
	 */
	private Date expirationTime;

	/**
	 * IP
	 */
	private String addIp;

	/**
	 * 截标前account值
	 */
	private double oldAccount;

	/**
	 * 投标奖励
	 * <p>
	 * 0:没有奖励
	 * </p>
	 * <p>
	 * 1:比例奖励
	 * </p>
	 * <p>
	 * 2:分摊奖励
	 * </p>
	 */
	private int award;

	/**
	 * 按投标金额比例(0.1-6)
	 */
	private double partAccount;

	/**
	 * 分摊奖励金额
	 */
	private double funds;

	/**
	 * 产品编码
	 */
	private String bidNo;

	/**
	 * 登记时间
	 */
	private Date registerTime;

	/**
	 * 资产包性质
	 */
	private String portfolioProp;

	/**
	 * 合同号
	 */
	private String dealNo;

	/**
	 * 担保公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vouch_id")
	private User vouchFirm;

	/**
	 * 借款手续费率
	 */
	private double borrowManageRate;

	/**
	 * 担保费率
	 */
	private double guaranteeRate;

	/**
	 * 登记担保方时候的订单号
	 */
	private String guaranteeNo;

	/**
	 * 风险备用金比率
	 */
	private double riskReserveRate;

	/**
	 * 预期收益下线
	 */
	private double expectedLow;

	/**
	 * 预期收益上线
	 */
	private double expectedUp;

	/**
	 * 借款公司名称
	 */
	private String companyName;

	/**
	 * 中期还款天数
	 */
	private int middleDay;

	/**
	 * 合同类型
	 */
	private int protocolType;

	/**
	 * 红包
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "red_packet_id")
	private RedPacket redPacket;

	/**
	 * 加息券
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "interest_rate_id")
	private InterestRate interestRate;
	
	/*
	 * 
	 **/
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "coupon_category_id")
	private CouponCategory couponCategory;

	public CouponCategory getCouponCategory() {
		return couponCategory;
	}

	public void setCouponCategory(CouponCategory couponCategory) {
		this.couponCategory = couponCategory;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public int getIsNovice() {
		return isNovice;
	}

	public void setIsNovice(int isNovice) {
		this.isNovice = isNovice;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public String getBorrowUse() {
		return borrowUse;
	}

	public void setBorrowUse(String borrowUse) {
		this.borrowUse = borrowUse;
	}

	public int getBorrowTimeType() {
		return borrowTimeType;
	}

	public void setBorrowTimeType(int borrowTimeType) {
		this.borrowTimeType = borrowTimeType;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Date getFixedTime() {
		return fixedTime;
	}

	public void setFixedTime(Date fixedTime) {
		this.fixedTime = fixedTime;
	}

	public double getFlowMoney() {
		return flowMoney;
	}

	public void setFlowMoney(double flowMoney) {
		this.flowMoney = flowMoney;
	}

	public int getFlowCount() {
		return flowCount;
	}

	public void setFlowCount(int flowCount) {
		this.flowCount = flowCount;
	}

	public int getFlowYesCount() {
		return flowYesCount;
	}

	public void setFlowYesCount(int flowYesCount) {
		this.flowYesCount = flowYesCount;
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

	public double getLowestSingleLimit() {
		return lowestSingleLimit;
	}

	public void setLowestSingleLimit(double lowestSingleLimit) {
		this.lowestSingleLimit = lowestSingleLimit;
	}

	public double getMostSingleLimit() {
		return mostSingleLimit;
	}

	public void setMostSingleLimit(double mostSingleLimit) {
		this.mostSingleLimit = mostSingleLimit;
	}

	public int getValidTime() {
		return validTime;
	}

	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public double getAccountYes() {
		return accountYes;
	}

	public void setAccountYes(double accountYes) {
		this.accountYes = accountYes;
	}

	public double getRepaymentAccount() {
		return repaymentAccount;
	}

	public void setRepaymentAccount(double repaymentAccount) {
		this.repaymentAccount = repaymentAccount;
	}

	public double getRepaymentYesAccount() {
		return repaymentYesAccount;
	}

	public void setRepaymentYesAccount(double repaymentYesAccount) {
		this.repaymentYesAccount = repaymentYesAccount;
	}

	public double getRepaymentYesInterest() {
		return repaymentYesInterest;
	}

	public void setRepaymentYesInterest(double repaymentYesInterest) {
		this.repaymentYesInterest = repaymentYesInterest;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	public double getScales() {
		return scales;
	}

	public void setScales(double scales) {
		this.scales = scales;
	}

	public Double getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(Double unitAmount) {
		this.unitAmount = unitAmount;
	}

	public int getTenderTimes() {
		return tenderTimes;
	}

	public void setTenderTimes(int tenderTimes) {
		this.tenderTimes = tenderTimes;
	}

	public double getOldAccount() {
		return oldAccount;
	}

	public void setOldAccount(double oldAccount) {
		this.oldAccount = oldAccount;
	}

	public int getAward() {
		return award;
	}

	public void setAward(int award) {
		this.award = award;
	}

	public double getPartAccount() {
		return partAccount;
	}

	public void setPartAccount(double partAccount) {
		this.partAccount = partAccount;
	}

	public double getFunds() {
		return funds;
	}

	public void setFunds(double funds) {
		this.funds = funds;
	}

	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}

	public String getPortfolioProp() {
		return portfolioProp;
	}

	public void setPortfolioProp(String portfolioProp) {
		this.portfolioProp = portfolioProp;
	}

	public User getVouchFirm() {
		return vouchFirm;
	}

	public void setVouchFirm(User vouchFirm) {
		this.vouchFirm = vouchFirm;
	}

	public String getDealNo() {
		return dealNo;
	}

	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public double getBorrowManageRate() {
		return borrowManageRate;
	}

	public void setBorrowManageRate(double borrowManageRate) {
		this.borrowManageRate = borrowManageRate;
	}

	public double getGuaranteeRate() {
		return guaranteeRate;
	}

	public void setGuaranteeRate(double guaranteeRate) {
		this.guaranteeRate = guaranteeRate;
	}

	public String getGuaranteeNo() {
		return guaranteeNo;
	}

	public void setGuaranteeNo(String guaranteeNo) {
		this.guaranteeNo = guaranteeNo;
	}

	public double getRiskReserveRate() {
		return riskReserveRate;
	}

	public void setRiskReserveRate(double riskReserveRate) {
		this.riskReserveRate = riskReserveRate;
	}

	public double getExpectedLow() {
		return expectedLow;
	}

	public void setExpectedLow(double expectedLow) {
		this.expectedLow = expectedLow;
	}

	public double getExpectedUp() {
		return expectedUp;
	}

	public void setExpectedUp(double expectedUp) {
		this.expectedUp = expectedUp;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getMiddleDay() {
		return middleDay;
	}

	public void setMiddleDay(int middleDay) {
		this.middleDay = middleDay;
	}

	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	public RedPacket getRedPacket() {
		return redPacket;
	}

	public void setRedPacket(RedPacket redPacket) {
		this.redPacket = redPacket;
	}

	public InterestRate getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(InterestRate interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 * 计算担保收益金额
	 * 
	 * @return
	 */
	public double getGuaranteeFee() {
		double fee = 0;
		if (this.borrowTimeType == 0) {
			// 如果是月标，则算月收益再乘以月数
			fee = this.guaranteeRate * this.timeLimit * this.account / 12 / 100;
		} else {
			// 如果是天标，则算天收益再乘以天数
			// 一年以365天计算
			fee = this.guaranteeRate * this.timeLimit * this.account / 365
					/ 100;
		}
		return fee;
	}

	/**
	 * 获取平台可得的管理费率
	 * 
	 * @return
	 */
	public double getBorrowManageFee() {
		// 费率计算规则：（登记的费率/100*借款金额-分给担保方的金额）*借款金额
		double manageFee = 0;
		double guaranteeFee = this.getGuaranteeFee();
		manageFee = (this.account * this.borrowManageRate / 100 - guaranteeFee)
				/ this.account;
		return manageFee;
	}

	public double getGuarantorMoney() {
		// 环迅最高支持年利50%，担保金额考虑逾期，则担保金额最高为本金+本金*年利50%*借款期限/12/100
		double guarantorMoney = 0;
		if (this.getBorrowTimeType() == 0) {
			// 如果是月标，则算月收益再乘以月数
			guarantorMoney = this.getAccount() + this.getAccount() * 0.5
					* this.getTimeLimit() / 12;
		} else {
			// 如果是天标，则算天收益再乘以天数
			// 一年以365天计算
			guarantorMoney = this.getAccount() + this.getAccount() * 0.5
					* this.getTimeLimit() / 365;
		}
		return guarantorMoney;
	}

}
