package com.rongdu.p2psys.account.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;

/**
 * 提现Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月17日
 */
public class AccountCashModel extends AccountCash {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;
	/** 每页总数 **/
	private int rows;
	/** 用户名 **/
	private String userName;
	/** 真实姓名 **/
	private String realName;
	/** 银行 **/
	private String bankName;
	/** 银行卡编号 **/
	private long bankId;
	/** 确认银行账号 */
	private String comfirmAccount;
	/** 支付密码 */
	private String payPwd;
	/** 开始时间 **/
	private String startTime;
	/** 结束时间 **/
	private String endTime;
	/** 审核备注 **/
	private String remark;
	/** 审核人 **/
	private String verifyUserName;
	/** 提现状态名 **/
    private String statusStr;
    /**
     * 提现时间值
     */
    private String addTimeStr;
    /**日期范围：0：全部，1：最近七天 2：最近一个月  3：最近两个月，4 最近三个月**/
    private int time;
    /** 条件查询 **/
    private String searchName;
    
    private String channelName;
    
    private String cashType;
	
	public String getChannelName() {
		String s = StringUtil.isNull(getChannelKey());
		
		if(s.equals("llpay_channel_key")){
			channelName = "连连支付";
//		}else if (s.equals("unionpay_channel_key")) {
		}else{
			channelName = "银联支付";
		}
		return channelName;
	}

	public String getCashType() {
		if(getType()>0){
			cashType = "线下提现";
		}else{
			cashType = "线上提现";
		}
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
    
    /**
     * 时间段查询
     */
    private String timeVal;
	/**
	 * 
	 * @param accountCash
	 * @return
	 */
	public static AccountCashModel instance(AccountCash accountCash) {
		AccountCashModel accountCashModel = new AccountCashModel();
		BeanUtils.copyProperties(accountCash, accountCashModel);
		return accountCashModel;
	}

	/**
	 * 
	 * @param bank
	 * @return
	 */
	public AccountCash prototype(AccountBank bank) {
		AccountCash accountCash = new AccountCash(bank.getUser(), getMoney());
		accountCash.setBankNo(bank.getBankNo());
		accountCash.setBank(bank.getBank());
		accountCash.setBranch(bank.getBranch());
		return accountCash;
	}

	/**
	 * 添加银行卡校验
	 * @param countTotal 银行卡总数
	 * @param defaultTotal 系统设置的可添加银行卡最大数
	 * @param bank 个人银行账户实体类
	 */
	public void validAddBank(int countTotal, int defaultTotal, AccountBank bank) {
		if (countTotal >= defaultTotal) {
			throw new AccountException("添加银行不能超过设置的最大数" + defaultTotal + "个！", 1);
		}
		if (StringUtil.isBlank(bank.getBranch())) {
			throw new AccountException("开户行名称不能为空！", 1);
		}
		if (StringUtil.isBlank(bank.getBankNo())) {
			throw new AccountException("银行账号不能为空！", 1);
		}
		if (!bank.getBankNo().equals(getComfirmAccount())) {
			throw new AccountException("银行账号和确认银行账号不一致！", 1);
		}
		if (StringUtil.isBlank(bank.getBank())) {
			throw new AccountException("银行名称不能为空！", 1);
		}
		
	}

	/**
	 * 提现校验	
	 * @param user 用户
	 * @param userAttestation 用户认证状态
	 * @param bankList 银行账户
	 */
	public void validCash(User user, UserIdentify userAttestation, List<ChannelBank> bankList) {

		if (userAttestation.getRealNameStatus() != 1) {
			throw new AccountException("您还未通过实名认证，请实名认证!", 1);
		} else if (userAttestation.getMobilePhoneStatus() != 1) {
			throw new AccountException("您还未通过手机认证，请手机认证！", 1);
		} else if (bankList.size() == 0) {
			throw new AccountException("您还没有添加银行账号，请添加银行卡！", 1);
		} else if ( (!BaseTPPWay.isOpenApi() && StringUtil.isBlank(getBankNo())) || 
				(BaseTPPWay.isOpenApi() && StringUtil.isBlank(getBankNo()) && TPPWay.API_CODE == TPPWay.API_CODE_IPS)) {
			throw new AccountException("银行卡不能为空！", 1);
		} else if (getMoney() <= 0) {
			throw new AccountException("您的提现金额不能为空！", 1);
		} 
//		else if (null != payPwd && !payPwd.equals(user.getPayPwd())) {
//			throw new AccountException("支付密码不正确!！", 1);
//		}
	}
	
	/**
	 * 银联提现校验	
	 * @param user 用户
	 * @param userAttestation 用户认证状态
	 * @param bankList 银行账户
	 */
	public void validCash_yl(User user, UserIdentify userAttestation, List<AccountBank> bankList) {

		if (userAttestation.getRealNameStatus() != 1) {
			throw new AccountException("您还未通过实名认证，请实名认证!", 1);
		} else if (userAttestation.getMobilePhoneStatus() != 1) {
			throw new AccountException("您还未通过手机认证，请手机认证！", 1);
		} else if (bankList.size() == 0) {
			throw new AccountException("您还没有添加银行账号，请添加银行卡！", 1);
		} else if ( (!BaseTPPWay.isOpenApi() && StringUtil.isBlank(getBankNo())) || 
				(BaseTPPWay.isOpenApi() && StringUtil.isBlank(getBankNo()) && TPPWay.API_CODE == TPPWay.API_CODE_IPS)) {
			throw new AccountException("银行卡不能为空！", 1);
		} else if (getMoney() <= 0) {
			throw new AccountException("您的提现金额不能为空！", 1);
		} 
//		else if (null != payPwd && !payPwd.equals(user.getPayPwd())) {
//			throw new AccountException("支付密码不正确!！", 1);
//		}
	}
	
	/**
	 * 提现校验	
	 * @param user 用户
	 * @param userAttestation 用户认证状态
	 * @param bankList 银行账户
	 */
	public void validCash_old(User user, UserIdentify userAttestation, List<AccountBank> bankList, String payPwd) {

		if (userAttestation.getRealNameStatus() != 1) {
			throw new AccountException("您还未通过实名认证，请实名认证!", 1);
		} else if (userAttestation.getMobilePhoneStatus() != 1) {
			throw new AccountException("您还未通过手机认证，请手机认证！", 1);
		} else if (bankList.size() == 0) {
			throw new AccountException("您还没有添加银行账号，请添加银行卡！", 1);
		} else if ( (!BaseTPPWay.isOpenApi() && StringUtil.isBlank(getBankNo())) || 
				(BaseTPPWay.isOpenApi() && StringUtil.isBlank(getBankNo()) && TPPWay.API_CODE == TPPWay.API_CODE_IPS)) {
			throw new AccountException("银行卡不能为空！", 1);
		} else if (getMoney() <= 0) {
			throw new AccountException("您的提现金额不能为空！", 1);
		} 
		else if (null != payPwd && !payPwd.equals(user.getPayPwd())) {
			throw new AccountException("支付密码不正确!！", 1);
		}
	}
	
	
	/**
	 * 提现校验	
	 * @param user 用户
	 * @param userAttestation 用户认证状态
	 * @param bankList 银行账户
	 */
	public Map<String, Object> validCash_new(User user, UserIdentify userAttestation, List<AccountBank> bankList) {
		Map<String, Object> map = new  HashMap<String, Object>();
		if (userAttestation.getRealNameStatus() != 1) {
			map.put("title","实名认证失败");
			map.put("txt", "您还未通过实名认证，请实名认证!");
		} else if (userAttestation.getMobilePhoneStatus() != 1) {
			map.put("title","实名认证失败");
			map.put("txt","您还未通过手机认证，请手机认证！");
		} else if (bankList.size() == 0) {
			map.put("title","您还没有添加银行账号，请添加银行卡！");
			map.put("txt","");
		} else if ( (!BaseTPPWay.isOpenApi() && StringUtil.isBlank(getBankNo())) || 
				(BaseTPPWay.isOpenApi() && StringUtil.isBlank(getBankNo()) && TPPWay.API_CODE == TPPWay.API_CODE_IPS)) {
			map.put("title","银行卡不能为空！");
			map.put("txt","");
		} else if (getMoney() <= 0) {
			map.put("title","您的提现金额不能为空！");
			map.put("txt","");
		} 
		
		return map;
	}

	/**
	 * 客服提现审核
	 * @param accountCash 资金提现记录实体类
	 */
	public void validKFVerify(AccountCash accountCash) {
		if (accountCash == null || (accountCash.getStatus() != 6 && accountCash.getStatus() != 7)) {
			throw new AccountException("提现尚未初审！", 1);
		}
	}
	
	/**
	 * 财务提现审核
	 * @param accountCash 资金提现记录实体类
	 */
	public void validCWVerify(AccountCash accountCash) {
		if (accountCash == null || accountCash.getStatus() != 5) {
			throw new AccountException("提现审核失败！", 1);
		}
	}
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public long getBankId() {
		return bankId;
	}

	public void setBankId(long bankId) {
		this.bankId = bankId;
	}

	public String getComfirmAccount() {
		return comfirmAccount;
	}

	public void setComfirmAccount(String comfirmAccount) {
		this.comfirmAccount = comfirmAccount;
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

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVerifyUserName() {
		return verifyUserName;
	}

	public void setVerifyUserName(String verifyUserName) {
		this.verifyUserName = verifyUserName;
	}
	/**
     * 设置状态 状态 0：申请提现  1：提现成功，2：提现失败 4：用户取消提现 ，
     * 
     * @param status 要设置的状态 
     */
	public String getStatusStr() {
        String statusStr="";
        switch (getStatus()) {
            case 0:
                statusStr="申请提现";
                break;
            case 1:
                statusStr="提现成功";
                break;
            case 2:
                statusStr="提现失败";
                break;
            case 4:
                statusStr="用户取消提现";
                break;
            case 5:
            	statusStr="提现处理中";
            	break;
            case 6:
                statusStr="初审通过";
                break;
            case 7:
                statusStr="初审不通过";
                break;
            case 9:
                statusStr="复审不通过";
                break;
            default:
                statusStr="提现异常";
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getTimeVal() {
		return timeVal;
	}

	public void setTimeVal(String timeVal) {
		this.timeVal = timeVal;
	}
    
}
