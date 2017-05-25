package com.rongdu.p2psys.bond.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.bond.dao.BondCollectionDao;
import com.rongdu.p2psys.bond.dao.BondDao;
import com.rongdu.p2psys.bond.dao.BondTenderDao;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.bond.exception.BondException;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.rule.BondConfigRuleCheck;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;

/**
 * 债权model
 * 
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
public class BondModel extends Bond {

	private static final long serialVersionUID = 1L;

	/** 投标金额 **/
	private double money;

	/** 支付密码 **/
	private String payPwd;

	/** 用户名 **/
	private String userName;

	/** 标ID */
	private long borrowId;

	/** 投标ID */
	private long borrowTenderId;

	/** 标名称 */
	private String borrowName;

	/** 年利率 */
	private double apr;

	/** 标种 */
	private int borrowType;

	/** 投资金额 */
	private double tenderMoney;

	/** 最低投标金额 */
	private double lowestAccount;

	/** 最多投标金额 */
	private double mostAccount;

	/** 持有天数 */
	private long holdDays;

	/** 剩余天数 */
	private long remainDays;

	/** 计息起始日 */
	private Date reviewTime;

	/** 到期日 */
	private Date lastRepaymentTime;

	/** 本期回款日 */
	private Date nextRepaymentTime;

	/** 债权价值 */
	private double remainMoney;

	/** 开始时间 **/
	private String startTime;

	/** 结束时间 **/
	private String endTime;

	/** 当前页数 **/
	private int page = 1;

	/** 每页数据条数 */
	private int rows = Page.ROWS;

	/** 排序 asc/desc **/
	private String order;

	/** 排序字段 **/
	private String sort;

	/** 债权名称 **/
	private String name;

	/** 红包id **/
	private Long[] ids;

	/** 提前付息 **/
	private double payInterest;

	/** 转让手续费 **/
	private double manageFee;

	/** 转让期数 **/
	private String periodStartAll;

	/** 还款方式 1按月分期还款; 2一次性还款;3每月还息到期还本 */
	private int borrowStyle;

	/** 订单号 */
	private String orderId;
	/** 订单日期 */
	private String orderDate;

	/** 债权专区 - 年化率排序 **/
	private int aprSearch;

	/** 债权专区 - 期限排序 **/

	private int timeSearch;

	/** 债权专区 - 借款金额排序 **/
	private int moneySearch;

	public static BondModel instance(Bond item) {
		BondModel model = new BondModel();
		BeanUtils.copyProperties(item, model);
		return model;
	}

	public Bond prototype() {
		Bond item = new Bond();
		BeanUtils.copyProperties(this, item);
		return item;
	}

	/**
	 * 发布债权前校验
	 */
	public void checkBeforAddBond(BondModel bondModel) {
		if (getBondMoney() < Constant.BOND_LOWEST_TRANS_MONEY) {
			throw new BondException("债权最低转让金额为"
					+ Constant.BOND_LOWEST_TRANS_MONEY + "元!", 1);
		}
		BorrowCollectionDao borrowCollectionDao = (BorrowCollectionDao) BeanUtil
				.getBean("borrowCollectionDao");
		BondCollectionDao bondCollectionDao = (BondCollectionDao) BeanUtil
				.getBean("bondCollectionDao");
		BorrowTenderDao borrowTenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		BondTenderDao bondTenderDao = (BondTenderDao) BeanUtil
				.getBean("bondTenderDao");
		long borrowId = bondModel.getBorrowId();
		Date addTime = null;
		Date nowDate = new Date();
		double remainCaptil = 0;
		long tendId = bondModel.getKfId();

		// 投资转让
		if (bondModel.getType() == 0) {
			// 投标信息
			BorrowTender borrowTender = borrowTenderDao.find(tendId);
			// 投标时间
			addTime = borrowTender.getAddTime();
			// 剩余待还本金
			remainCaptil = borrowCollectionDao.getRemainderCapital(tendId);
			// 债权再转让
		} else {
			// 债权投资信息
			BondTender bondTender = bondTenderDao.getBondTenderById(tendId);
			// 债权投资时间
			addTime = bondTender.getAddTime();
			// 剩余待还本金
			remainCaptil = bondCollectionDao.getRemainderCapital(tendId);
		}

		// 债权规则
		BondConfigRuleCheck bondConfigRuleCheck = (BondConfigRuleCheck) Global
				.getRuleCheck("bondConfig");

		// 持有期需大于等于rule表配置的天数
		if (DateUtil.daysBetween(addTime, nowDate) < bondConfigRuleCheck.holdDays) {
			throw new BondException("持有期需大于等于" + bondConfigRuleCheck.holdDays
					+ "天!", 1);
		}

		// 下一期还款
		BorrowTender borrowTender = borrowTenderDao.find(bondModel
				.getBorrowTenderId()); // 获得最初发布人的userId
		BorrowCollection nextBorrowCollection = borrowCollectionDao
				.getNextCollectionByBorrowId(borrowId, borrowTender.getUser()
						.getUserId());
		// 距最后还款到期时间大于等于rule表配置的天数
		if (DateUtil.daysBetween(nowDate,
				nextBorrowCollection.getRepaymentTime()) < bondConfigRuleCheck.remainDays) {
			throw new BondException("距最近还款到期时间需大于等于"
					+ bondConfigRuleCheck.remainDays + "天!", 1);
		}

		// 未发生逾期
		if (borrowCollectionDao.isLatedByBorrowId(borrowId)
				|| bondCollectionDao.isLatedByBorrowId(borrowId)) {
			throw new BondException("逾期的标不能发布债权!", 1);
		}

		// 设置转让折扣率，平台可限定转让价格的合理范围
		if (getBondApr() < bondConfigRuleCheck.bondAprL
				|| getBondApr() > bondConfigRuleCheck.bondAprH) {
			throw new BondException("转让折扣率需在" + bondConfigRuleCheck.bondAprL
					+ "和" + bondConfigRuleCheck.bondAprH + "之间!", 1);
		}

		// 一条投资记录允许拆分转让（即部分转让），待收本金小于等于1000时，需一次性转让全部；待收本金大于1000时单笔转让需大于等于1000。
		BondDao bondDao = (BondDao) BeanUtil.getBean("bondDao");
		double bondSold = bondDao.getSoldBondMoneyByTenderId(tendId,
				bondModel.getType());
		double bondSelling = bondDao.getSellingBondMoneyByTenderId(tendId,
				bondModel.getType());
		double actulBond = BigDecimalUtil.add(remainCaptil, -bondSold,
				-bondSelling);

		// 实际投资金额不能大于可投金额
		if (getBondMoney() > actulBond) {
			throw new BondException("实际投资金额不能大于可投金额!", 1);
		}

		// 单笔最低转让金额
		double minBondMoney = bondConfigRuleCheck.minBondMoney;
		if (actulBond > minBondMoney) {
			if (getBondMoney() < minBondMoney) {
				throw new BondException("单笔转让需大于等于" + minBondMoney + "!", 1);
			}
		} else {
			if (getBondMoney() < actulBond) {
				throw new BondException("待收本金小于等于" + minBondMoney
						+ "时，需一次性转让全部!", 1);
			}
		}
	}

	/**
	 * 债权投资前校验
	 */
	public void checkBeforBondTender(Bond bond) {

		if (bond.getUser().getUserId() == getUser().getUserId()) {
			throw new BondException("自己不能投自己发布的债权！", 2);
		}
		// 托管版平台这边不需要密码
		if (!BaseTPPWay.isOpenApi()) {
			if (StringUtil.isBlank(payPwd)) {
				throw new BondException("支付密码不能为空!", 2);
			}
			if (!MD5.encode(payPwd).equals(getUser().getPayPwd())) {
				throw new BondException("支付密码不正确!", 2);
			}
		}

		if (bond.getSoldCapital() >= bond.getBondMoney()) {
			throw new BondException("此债权已满!", 2);
		}

		// 债权规则
		BondConfigRuleCheck bondConfigRuleCheck = (BondConfigRuleCheck) Global
				.getRuleCheck("bondConfig");
		double minTenderMoney = bondConfigRuleCheck.minTenderMoney;

		// 投资转让中的债权时，最小申购金额为100，剩余可申购金额小于100的债权，需一次性申购完。
		double reMainMoney = BigDecimalUtil.sub(bond.getBondMoney(),
				bond.getSoldCapital());
		if (reMainMoney < minTenderMoney) {
			if (reMainMoney > money) {
				throw new BondException("最小申购金额为" + minTenderMoney + "!", 2);
			}
		} else {
			if (money < minTenderMoney) {
				throw new BondException("最小申购金额为" + minTenderMoney + "!", 2);
			}
		}

		AccountDao accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		Account act = accountDao.getAccountByUserId(getUser().getUserId());
		UserRedPacketDao userRedPacketDao = (UserRedPacketDao) BeanUtil
				.getBean("userRedPacketDao");
		// 红包使用金额
		double redPacketMoney = userRedPacketDao.getTotalPacketMoneyByIds(ids);
		// 实际投资金额
		double account = BigDecimalUtil.sub(money, redPacketMoney);
		if (account > act.getUseMoney()) {
			throw new BorrowException("您的可用余额不足，投标失败!", 2);
		}

		// 等额本息的债权只允许1个人投资人受让
		if (bond.getBorrow().getStyle() == Borrow.STYLE_INSTALLMENT_REPAYMENT) {
			if (bond.getBondMoney() != money) {
				throw new BondException("等额本息的债权需全额受让!", 2);
			}
		}

	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(long borrowId) {
		this.borrowId = borrowId;
	}

	public long getBorrowTenderId() {
		return borrowTenderId;
	}

	public void setBorrowTenderId(long borrowTenderId) {
		this.borrowTenderId = borrowTenderId;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public double getApr() {
		return apr;
	}

	public int getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(int borrowType) {
		this.borrowType = borrowType;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public double getTenderMoney() {
		return tenderMoney;
	}

	public void setTenderMoney(double tenderMoney) {
		this.tenderMoney = tenderMoney;
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

	public long getHoldDays() {
		return holdDays;
	}

	public void setHoldDays(long holdDays) {
		this.holdDays = holdDays;
	}

	public long getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(long remainDays) {
		this.remainDays = remainDays;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Date getLastRepaymentTime() {
		return lastRepaymentTime;
	}

	public void setLastRepaymentTime(Date lastRepaymentTime) {
		this.lastRepaymentTime = lastRepaymentTime;
	}

	public Date getNextRepaymentTime() {
		return nextRepaymentTime;
	}

	public void setNextRepaymentTime(Date nextRepaymentTime) {
		this.nextRepaymentTime = nextRepaymentTime;
	}

	public double getRemainMoney() {
		return remainMoney;
	}

	public void setRemainMoney(double remainMoney) {
		this.remainMoney = remainMoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public double getPayInterest() {
		return payInterest;
	}

	public void setPayInterest(double payInterest) {
		this.payInterest = payInterest;
	}

	public double getManageFee() {
		return manageFee;
	}

	public void setManageFee(double manageFee) {
		this.manageFee = manageFee;
	}

	public int getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(int borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public String getPeriodStartAll() {
		return periodStartAll;
	}

	public void setPeriodStartAll(String periodStartAll) {
		this.periodStartAll = periodStartAll;
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

	public int getAprSearch() {
		return aprSearch;
	}

	public void setAprSearch(int aprSearch) {
		this.aprSearch = aprSearch;
	}

	public int getTimeSearch() {
		return timeSearch;
	}

	public void setTimeSearch(int timeSearch) {
		this.timeSearch = timeSearch;
	}

	public int getMoneySearch() {
		return moneySearch;
	}

	public void setMoneySearch(int moneySearch) {
		this.moneySearch = moneySearch;
	}

}
