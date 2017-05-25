package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.TppGlodLog;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinaPnrType;

/**
 * 平台账户操作记录model
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年2月4日
 */
public class TppGlodLogModel extends TppGlodLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7645730957090389340L;
	/**
	 * 页数
	 */
	private int page;
	/**
	 * 行数
	 */
	private int rows;
	
	/**
	 * 条件查询
	 */
	private String searchName;
	
	/** 开始日期 **/
	private String startTime;
	
	/** 结束日期 **/
	private String endTime;
	
	/** 锁定会话 **/
	private String rechargeToken;
	/**
	 * 操作员用户名
	 */
	private String userName;
	
	/**
	 * 状态文字描述
	 */
	private String statusStr;
	
	/** 操作类型文字描述 **/
	private String typeStr;

	public TppGlodLogModel() {
		super();
	}

	public static TppGlodLogModel instance(TppGlodLog glodLog) {
		TppGlodLogModel tppGlodLogModel = new TppGlodLogModel();
		BeanUtils.copyProperties(glodLog, tppGlodLogModel);
		return tppGlodLogModel;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRechargeToken() {
		return rechargeToken;
	}

	public void setRechargeToken(String rechargeToken) {
		this.rechargeToken = rechargeToken;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
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

	public String getStatusStr() {
		switch (getStatus()) {
		case 0:
			statusStr = "待处理";
			break;
		case 1:
			statusStr = "成功";
			break;
		case 2:
			statusStr = "失败";
			break;
		default:
			statusStr = "状态异常";
			break;
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
	public String getTypeStr() {
		String str = getType();
		if(str.equals(ChinaPnrType.CASH)){
			typeStr = "账户取现";
		}
		if(str.equals(ChinaPnrType.TRANSFER)){
			typeStr = "账户互转";
		}
		if(str.equals(ChinaPnrType.RECHARGE)){
			typeStr = "账户充值";
		}
		if(str.equals(ChinaPnrType.WEBRECHARGE)){
			typeStr = "账户转账";
		}
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	/**
	 * 平台充值校验
	 */
	public void validGlodRecharge() {
		if (StringUtil.isBlank(this.getRechargeToken())) {
			throw new AccountException("会话为空！", 1);
		}
		if (this.getMoney() <= 0) {
			throw new AccountException("充值金额过小,请重新输入金额！", 1);
		}
		if (this.getMoney() >= 100000000) {
			throw new AccountException("你充值的金额过大,目前系统仅支持千万级别的充值！", 1);
		}
	}
}
