package com.rongdu.p2psys.account.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.RechargeUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.constant.RechargeConstant;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;

/**
 * 充值
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月17日10:49:14
 */
public class AccountRechargeModel extends AccountRecharge {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;
	/** 每页总数 **/
	private int rows;
	/** 排序 asc/desc **/
	private String order;
	/** 排序字段 **/
	private String sort;
	/** 用户名 **/
	private String userName;
	/** 真实姓名 **/
	private String realName;
	/** 锁定会话 **/
	private String rechargeToken;
	/** 支付方式-网银直联(线上银行) */
	private String payOnlinebank;
	/** 支付方式 */
	private String pay;
	/** 支付方式-线下支付(线下银行/收款账户) */
	private long payOfflinebankId;
	/** 线下银行信息 */
	private String payOfflinebankInfo;
	/** 支付方式名称 */
	private String payName;
	/** 验证码 **/
	private String valicode;
	/** 成功充值 **/
	private double rechargeTotal;
	/** 线上成功充值 **/
	private double onlineRechargeTotal;
	/** 线下成功充值 **/
	private double offlineRechargeTotal;
	/** 审核会话 **/
	public String verifyRechargeToken;
	/** 审核人 **/
	private String verifyUserName;
	/** 审核备注 **/
	private String verifyRemark;
	/** 充值状态名 **/
	private String statusStr;
	/** 充值方式名 1网上充值 2 网上支付 3线下充值 21银行充值 22线下充值奖励 23回款续投奖励 24调单充值 25活动奖励 26其他充值**/
	private String typeStr;
	/** 充值时间 **/
	private String addTimeStr;
	/**
	 * 银行编号
	 */
	private String bankCode;
	/**
	 * 充值渠道：1：网银 2代 扣充值
	 */
	private String channelType; 
	
	
	/** 开始时间 **/
	private String startTime;
	/** 结束时间 **/
	private String endTime;
	/**日期范围：0：全部，1：最近七天 2：最近一个月  3：最近两个月，4 最近三个月**/
	private int time;
	/** 条件查询 */
	private String searchName;
	
	/**
	 * 到账金额总额
	 */
	private double sumAmountIn;
	
	/**
	 * 手续费金额总额
	 */
	private double sumAmountFee;
	
	private String channelName;
	/**
	 * 连连钱包支付单号
	 */
	private String oid_paybill;
	
	public String getChannelName() {
		String s = StringUtil.isNull(getChannelKey());
		if (s.equals("unionpay_channel_key")) {
			channelName = "银联支付";

		}else if(s.equals("llpay_channel_key")){
			channelName = "连连支付";
		}
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getTypeStr() {
		switch (getType()) {
		case 3:
			typeStr = "线下充值";
			break;

		default:
			typeStr = "线上充值";
			break;
		}
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public static AccountRechargeModel instance(AccountRecharge accountRecharge) {
		AccountRechargeModel accountRechargeModel = new AccountRechargeModel();
		BeanUtils.copyProperties(accountRecharge, accountRechargeModel);
		return accountRechargeModel;
	}

	public AccountRecharge prototype(User user, double fee) {
		AccountRecharge accountRecharge = new AccountRecharge();
		BeanUtils.copyProperties(this, accountRecharge);
		accountRecharge.setUser(user);
		accountRecharge.setFee(fee);
		accountRecharge.setAddTime(new Date());
		accountRecharge.setAddIp(Global.getIP());
		switch (getType()) {
			case 1:
				accountRecharge.setRemark("网银直连充值" + getMoney() + "元");
				break;
			case 2:
				accountRecharge.setRemark("网上支付充值" + getMoney() + "元");
				break;
			case 3:
				accountRecharge.setTradeNo(RechargeUtil.generateTradeNO(user.getUserId(), "E"));
				accountRecharge.setRemark("线下支付充值" + getMoney() + "元");
				break;
			default:
				throw new AccountException("暂不支持该充值方式！", 1);
		}

		return accountRecharge;
	}

	/**
	 * 充值前的各种认证效验
	 */
	public void validIdentifyForRecharge(UserIdentify userAttestation) {
		if (userAttestation.getRealNameStatus() != 1) {
			throw new AccountException("请先通过实名认证！", 2);
		}
//		if (userAttestation.getMobilePhoneStatus() != 1) {
//			throw new AccountException("请先通过手机认证！", 2);
//		}
//		if (userAttestation.getEmailStatus() != 1) {
//			throw new AccountException("请先通过邮箱认证！", 2);
//		}
	}

	/**
	 * 充值校验
	 */
	public void validNewRecharge() {
		if (!BaseTPPWay.isOpenApi()) { //标准版入口增加校验
			if (getType() == 1 && StringUtil.isBlank(getPayOnlinebank())) {
				throw new AccountException("请选择" + RechargeConstant.PAY_ONLINE + "充值方式！", 2);
			}
			if (getType() == 2 && StringUtil.isBlank(getPay())) {
				throw new AccountException("请选择" + RechargeConstant.PAY + "充值方式！", 2);
			}
			if (!ValidateUtil.checkValidCode(getValicode())) {
				throw new AccountException("验证码错误！", 2);
			}
		}
		if (getMoney() <= 0) {
			throw new AccountException("充值的金额不能小于0！", 2);
		}
		if (getType() == 3) {
//			if (getPayOfflinebankId() == 0) {
//				throw new AccountException("请选择" + RechargeConstant.PAY_OFFLINE + "收账账户！", 2);
//			} 
			if (StringUtil.isBlank(getRealName())) {
				throw new AccountException("请输入您银行卡的真实姓名！", 2);
			}else if (StringUtil.isBlank(getBankNo())) {
				throw new AccountException("请输入您的银行卡号！", 2);
			}
		}
		
		if( BaseTPPWay.isOpenApi() && (TPPWay.API_CODE == TPPWay.API_CODE_IPS) && (StringUtil.isBlank(getBankCode()))){
			throw new AccountException("请选择充值银行！", 2);
		}
	}

	/**
	 * 添加线下充值校验
	 */
	public void validAccountRecharge() {
		if (StringUtil.isBlank(this.getRechargeToken())) {
			throw new AccountException("会话为空！", 1);
		}
		if (StringUtil.isBlank(this.getUserName())) {
			throw new AccountException("用户名不能为空！", 1);
		}
		if (this.getMoney() <= 0) {
			throw new AccountException("充值金额过小,请重新输入金额！", 1);
		}
		if (this.getMoney() >= 100000000) {
			throw new AccountException("你充值的金额过大,目前系统仅支持千万级别的充值！", 1);
		}
		if (StringUtil.isBlank(this.getRemark())) {
			throw new AccountException("备注不能为空！", 1);
		}
	}

	/**
	 * 线下充值审核
	 */
	public void validVerifyAccountRecharge() {
		if (StringUtil.isBlank(this.getVerifyRechargeToken())) {
			throw new AccountException("会话为空！", 1);
		}
		if (this.getMoney() <= 0) {
			throw new AccountException("当前充值金额为" + this.getMoney() + ",充值金额不能为负数！", 1);
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getPayOnlinebank() {
		return payOnlinebank;
	}

	public void setPayOnlinebank(String payOnlinebank) {
		this.payOnlinebank = payOnlinebank;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public long getPayOfflinebankId() {
		return payOfflinebankId;
	}

	public void setPayOfflinebankId(long payOfflinebankId) {
		this.payOfflinebankId = payOfflinebankId;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getValicode() {
		return valicode;
	}

	public void setValicode(String valicode) {
		this.valicode = valicode;
	}

	public double getRechargeTotal() {
		return rechargeTotal;
	}

	public void setRechargeTotal(double rechargeTotal) {
		this.rechargeTotal = rechargeTotal;
	}

	public double getOnlineRechargeTotal() {
		return onlineRechargeTotal;
	}

	public void setOnlineRechargeTotal(double onlineRechargeTotal) {
		this.onlineRechargeTotal = onlineRechargeTotal;
	}

	public double getOfflineRechargeTotal() {
		return offlineRechargeTotal;
	}

	public void setOfflineRechargeTotal(double offlineRechargeTotal) {
		this.offlineRechargeTotal = offlineRechargeTotal;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public String getRechargeToken() {
		return rechargeToken;
	}

	public void setRechargeToken(String rechargeToken) {
		this.rechargeToken = rechargeToken;
	}

	public String getVerifyRechargeToken() {
		return verifyRechargeToken;
	}

	public void setVerifyRechargeToken(String verifyRechargeToken) {
		this.verifyRechargeToken = verifyRechargeToken;
	}

	public String getVerifyUserName() {
		return verifyUserName;
	}

	public void setVerifyUserName(String verifyUserName) {
		this.verifyUserName = verifyUserName;
	}

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	public String getStatusStr() {
		switch (getStatus()) {
			case 0:
				statusStr="充值提交中";
				break;
			case 1:
				statusStr="充值成功";
				break;
			case 2:
				statusStr="充值失败";
				break;
			case 6:
				statusStr="充值初审通过";
				break;
			case 7:
				statusStr="充值初审不通过";
				break;
			default:
				statusStr="充值状态异常";
				break;
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

	public String getPayOfflinebankInfo() {
		return payOfflinebankInfo;
	}

	public void setPayOfflinebankInfo(String payOfflinebankInfo) {
		this.payOfflinebankInfo = payOfflinebankInfo;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public double getSumAmountIn() {
		return sumAmountIn;
	}

	public void setSumAmountIn(double sumAmountIn) {
		this.sumAmountIn = sumAmountIn;
	}

	public double getSumAmountFee() {
		return sumAmountFee;
	}

	public void setSumAmountFee(double sumAmountFee) {
		this.sumAmountFee = sumAmountFee;
	}

	public String getOid_paybill() {
		return oid_paybill;
	}

	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}
	
}
