package com.rongdu.p2psys.account.model.accountlog;

import java.util.Date;
import java.util.Map;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.dao.AccountSumLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;

/**
 * 抽象资金日志类
 * 
 * @version 1.0
 * @since 2013-8-26
 */
public class BaseAccountLog extends AccountLog implements AccountLogEvent {

	private static final long serialVersionUID = 1L;

	protected AccountSumDao accountSumDao;

	protected AccountSumLogDao accountSumLogDao;

	protected AccountDao accountDao;

	protected AccountLogDao accountLogDao;

	protected String sumLogRemarkTemplate;

	protected String usedRechargeRemark = "已使用充值：扣除充值${user_cash}元!";

	protected String usedInterestRemark = "已使用利息：扣除获得利息${user_cash}元!";

	protected String usedAwardRemark = "已使用奖励：扣除获得奖励${user_cash}元!";

	protected String usedHuikuanRemark = "已使用回款本金：扣除回款${user_cash}元!";

	protected String usedHuikuanInterestRemark = "已使用回款利息：扣除回款${user_cash}元!";

	protected String usedBorrowCashRemark = "已使用借款额：扣除借款额${user_cash}元!";

	protected String cashfeeRemark = "提现收费合计：提现${cash.total}元,手续费${cash.fee}元!";

	protected String cashRemark = "提现金额合计：提现${cash.total}元!";

	protected String rechargeRemark = "充值合计：增加充值${recharge.money}元!";

	protected String usedRemark = "扣款合计：扣除${money}元!";

	protected String huikuanRemark = "回款合计：增加${money}元!";
	protected String interestFeeRemark = "利息管理费合计：增加${money}元!";

	protected UserDao userDao;
	protected UserCacheDao userCacheDao;
	protected NoticeService noticeService;

	public static boolean DEBUG = false;

	public BaseAccountLog() {
		super();
		accountSumDao = (AccountSumDao) BeanUtil.getBean("accountSumDao");
		accountSumLogDao = (AccountSumLogDao) BeanUtil.getBean("accountSumLogDao");
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		userDao = (UserDao) BeanUtil.getBean("userDao");
		userCacheDao = (UserCacheDao) BeanUtil.getBean("userCacheDao");
		noticeService = (NoticeService) BeanUtil.getBean("noticeService");
	}

	public BaseAccountLog(double money, User user) {
		this();
		this.setMoney(money);
		this.setUser(user);
	}

	public BaseAccountLog(double money, Account act) {
		this();
		this.setMoney(money);
		if (act != null) {
			this.setTotal(act.getTotal());
			this.setUseMoney(act.getUseMoney());
			this.setNoUseMoney(act.getNoUseMoney());
			this.setCollection(act.getCollection());
			this.setUser(act.getUser());
			this.setAddTime(new Date());
			User toUser = new User();
			toUser.setUserId(1L);
			this.setToUser(toUser);
		}
	}

	public AccountSumLog baseAccountSumLog() {
		AccountSumLog sumLog = new AccountSumLog();
		sumLog.setAddIp(this.getAddIp());
		sumLog.setAddTime(this.getAddTime());
		sumLog.setMoney(this.getMoney());
		sumLog.setRemark(this.getAccountSumLogRemark());
		sumLog.setUser(this.getUser());
		sumLog.setToUser(this.getToUser());
		sumLog.setType(this.getType());
		return sumLog;
	}

	public BaseAccountLog(double money, User user, User toUser) {
		this(money, user);
		this.setToUser(toUser);
	}

	public BaseAccountLog(double money, Account act, User toUser) {
		this(money, act);
		this.setToUser(toUser);
	}

	/**
	 * 默认的事件执行
	 */
	@Override
	public void doEvent() {
		// 调试时手动传参，服务器上通过Spring容器获取
		if (DEBUG) {
			transfer();
		}
		updateAccount();
		addLog();
		accountSumProperty();
		// 资金记录
		Global.setTransfer("weburl", Global.getString("weburl"));
		this.setRemark(this.getLogRemark());
		this.setType(this.getType());
		accountLogDao.save(this);
		// 消息
		sendNotice();
		// 操作日志
		addOperateLog();

	}

	/**
	 * 实现默认的接口方法
	 */
	@Override
	public void accountSumProperty() {

	}

	public String getLogRemark() {
		try {
			return FreemarkerUtil.renderTemplate(this.getLogRemarkTemplate(), transfer());
		} catch (Exception e) {
		}
		return "";
	}

	public String getAccountSumLogRemark() {
		try {
			return FreemarkerUtil.renderTemplate(getAccountSumLogRemarkTemplate(), transfer());
		} catch (Exception e) {
		}
		return "";
	}

	@Override
	public Map<String, Object> transfer() {
		Map<String, Object> data = Global.getTransfer();
		if (accountSumDao == null) {
			accountSumDao = (AccountSumDao) data.get("accountSumDao");
		}
		if (accountLogDao == null) {
			accountLogDao = (AccountLogDao) data.get("accountLogDao");
		}
		if (accountSumLogDao == null) {
			accountSumLogDao = (AccountSumLogDao) data.get("accountSumLogDao");
		}

		return data;
	}

	public String getLogRemarkTemplate() {
		return Global.getLogTempValue((byte)2, this.getTemplateNid());
	}

	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}

	public void setAccountSumLogRemarkTemplate(String sumLogRemarkTemplate) {
		this.sumLogRemarkTemplate = sumLogRemarkTemplate;
	}

	public String getType() {
		return getAccountLogType(this.getTemplateNid());
	}

	@Override
	public void initCode(String todo) {

	}

	@Override
	public void sendNotice() {
		// 能取到noticeTypeNid就可以发送
		if (!"".equals(getNoticeTypeNid())) {
			Map<String, Object> transferMap = transfer();

			long userid = this.getUser().getUserId();
			User user = userDao.findObjByProperty("userId", userid);

			transferMap.put("host", Global.getValue("weburl"));
			transferMap.put("webname", Global.getValue("webname"));
			transferMap.put("noticeTime", DateUtil.dateStr(new Date(), "MM月dd日 HH时mm分ss秒"));
			transferMap.put("user", user);
			noticeService.sendNotice(getNoticeTypeNid(), transferMap);
		}
	}

	/**
	 * sum和sum日志更新
	 * 
	 * @param money 操作金额
	 * @param sumMoney 当前已有的金额
	 * @param logType 日志类型
	 * @param sumType sum修改字段类型
	 * @return 扣费的顺序是充值、奖励、利息、借款，如果不同，则在子类中写
	 */
	public void sumUpdate(double money, double sumMoney, String logType, String sumType) {
		AccountSumLog sumLog = new AccountSumLog();
		double after_money = money + sumMoney;
		sumLog.setUser(this.getUser());
		sumLog.setToUser(this.getToUser());
		sumLog.setBeforeMoney(sumMoney);
		sumLog.setMoney(money);
		sumLog.setAfterMoney(after_money);
		sumLog.setAddIp(this.getAddIp());
		sumLog.setAddTime(new Date());
		sumLog.setType(logType);
		sumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.save(sumLog);
		accountSumDao.update(sumType, money, this.getUser().getUserId());
	}

	/**
	 * sum数据处理
	 * 
	 * @return
	 */
	public void sumManage() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.findByUserId(this.getUser().getUserId());
		// 取出可能扣款的值
		double recharge = accountSum.getRecharge();
		double usedRecharge = accountSum.getUsedRecharge();
		double useRecharge = recharge - usedRecharge;

		double award = accountSum.getAward();
		double usedAward = accountSum.getUsedAward();
		double useAward = award - usedAward;

		double interest = accountSum.getInterest();
		double usedInterest = accountSum.getUsedInterest();
		double useInterest = interest - usedInterest;

		double usedBorrowCash = accountSum.getUsedBorrowCash();

		// 增加account_sum_log日志
		// 扣费的顺序是充值、奖励、利息、借款
		double money = this.getMoney();
		double currRecharge = 0;
		double currAward = 0;
		double currInterest = 0;
		double currBorrowCash = 0;

		if (useRecharge >= money) {
			currRecharge = money;
		} else {
			if ((useRecharge + useAward) >= money) {
				currRecharge = useRecharge;
				currAward = money - useRecharge;
			} else {
				if ((useRecharge + useAward + useInterest) >= money) {
					currRecharge = useRecharge;
					currAward = useAward;
					currInterest = money - (useRecharge + useAward);
				} else {
					currRecharge = useRecharge;
					currAward = useAward;
					currInterest = useInterest;
					currBorrowCash = money - (useRecharge + useAward + useInterest);
				}
			}
		}

		if (currRecharge > 0) {
			// 增加recharge sum log 日志
			Global.setTransfer("user_cash", currRecharge);
			this.setAccountSumLogRemarkTemplate(usedRechargeRemark);
			String logType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			String sumType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			this.sumUpdate(currRecharge, usedRecharge, logType, sumType);
		}
		if (currAward > 0) {
			// 增加award sum log 使用日志
			Global.setTransfer("user_cash", currAward);
			this.setAccountSumLogRemarkTemplate(usedAwardRemark);
			String logType = EnumAccountSumProperty.USED_AWARD.getValue();
			String sumType = EnumAccountSumProperty.USED_AWARD.getValue();
			this.sumUpdate(currAward, usedAward, logType, sumType);
		}
		if (currInterest > 0) {
			// 增加interest sum log使用日志
			Global.setTransfer("user_cash", currInterest);
			this.setAccountSumLogRemarkTemplate(usedInterestRemark);
			String logType = EnumAccountSumProperty.USED_INTEREST.getValue();
			String sumType = EnumAccountSumProperty.USED_INTEREST.getValue();
			this.sumUpdate(currInterest, usedInterest, logType, sumType);
		}
		if (currBorrowCash > 0) {
			// 增加interest sum log使用日志
			Global.setTransfer("user_cash", currBorrowCash);
			this.setAccountSumLogRemarkTemplate(usedBorrowCashRemark);
			String logType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			String sumType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			this.sumUpdate(currBorrowCash, usedBorrowCash, logType, sumType);
		}
		// 扣除回款的account_sum_log
		this.huikuanManage(null);
	}

	/**
	 * 回款处理
	 * 
	 * @param money 操作金额，如果为空，默认值就是account log中的money值
	 */
	public void huikuanManage(Double money) {
		AccountSum accountSum = accountSumDao.findByUserId(this.getUser().getUserId());
		double huikuan = accountSum.getHuikuan();
		double usedHuikuan = accountSum.getUsedHuikuan();
		double useHuikuan = huikuan - usedHuikuan;

		double huikuanInterest = accountSum.getUsedHuikuanInterest();
		double usedHuikuanInterest = accountSum.getUsedHuikuanInterest();
		double useHuikuanInterest = huikuanInterest - usedHuikuanInterest;

		if (money == null)
			money = this.getMoney();
		double currHuikuan = 0;
		double currHuikuanInterest = 0;
		double moenyHKuan = useHuikuanInterest - money;
		currHuikuanInterest = moenyHKuan >= 0 ? money : useHuikuanInterest;
		if (moenyHKuan < 0) {// 本次使用的回款最大不能超过可用回款，
			currHuikuan = money - useHuikuanInterest;
			currHuikuan = currHuikuan >= useHuikuan ? useHuikuan : currHuikuan;
		}
		if (currHuikuanInterest > 0) {
			this.huikuanInterestUpdate(currHuikuanInterest);
		}
		if (currHuikuan > 0) {
			this.huikuanUpdate(currHuikuan);
		}
	}

	// 回款更新
	public void huikuanUpdate(Double currHuikuan) {
		if (currHuikuan == null)
			currHuikuan = this.getMoney();
		AccountSum accountSum = accountSumDao.findByUserId(this.getUser().getUserId());
		double huikuan = accountSum.getHuikuan() - accountSum.getUsedHuikuan();
		currHuikuan = currHuikuan >= huikuan ? huikuan : currHuikuan;
		Global.setTransfer("user_cash", currHuikuan);
		this.setAccountSumLogRemarkTemplate(usedHuikuanRemark);
		String logType = EnumAccountSumProperty.USED_HUIKUAN.getValue();
		String sumType = EnumAccountSumProperty.USED_HUIKUAN.getValue();
		this.sumUpdate(currHuikuan, accountSum.getUsedHuikuan(), logType, sumType);
	}

	// 回款利息更新
	public void huikuanInterestUpdate(Double currHuikuanInterest) {
		if (currHuikuanInterest == null)
			currHuikuanInterest = this.getMoney();
		AccountSum accountSum = accountSumDao.findByUserId(this.getUser().getUserId());
		double huikuanInterest = accountSum.getUsedHuikuanInterest() - accountSum.getUsedHuikuanInterest();
		currHuikuanInterest = currHuikuanInterest >= huikuanInterest ? huikuanInterest : currHuikuanInterest;
		Global.setTransfer("user_cash", currHuikuanInterest);
		this.setAccountSumLogRemarkTemplate(usedHuikuanInterestRemark);
		String logType = EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue();
		String sumType = EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue();
		this.sumUpdate(currHuikuanInterest, accountSum.getUsedHuikuanInterest(), logType, sumType);
	}

	@Override
	public void addOperateLog() {

	}

	public void addLog() {

	}

	/**
	 * 更新账户表
	 */
	@Override
	public void updateAccount() {

	}

	public String getTemplateNid() {
		return "";
	}

	public String getAccountLogType(String nid) {
		return Global.getLogType((byte)2, nid);
	}

	public AccountSumDao getAccountSumDao() {
		return accountSumDao;
	}

	public void setAccountSumDao(AccountSumDao accountSumDao) {
		this.accountSumDao = accountSumDao;
	}

	public AccountLogDao getAccountLogDao() {
		return accountLogDao;
	}

	public void setAccountLogDao(AccountLogDao accountLogDao) {
		this.accountLogDao = accountLogDao;
	}

	public AccountSumLogDao getAccountSumLogDao() {
		return accountSumLogDao;
	}

	public void setAccountSumLogDao(AccountSumLogDao accountSumLogDao) {
		this.accountSumLogDao = accountSumLogDao;
	}

	public UserCacheDao getUserCacheDao() {
		return userCacheDao;
	}

	public void setUserCacheDao(UserCacheDao userCacheDao) {
		this.userCacheDao = userCacheDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public NoticeService getNoticeService() {
		return noticeService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public String getNoticeTypeNid() {
		return "";
	}

	@Override
	public void extend() {
		// 做些扩展动作
	}

	@Override
	public void pretreatmentBank() {

	}

}
