package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountDeduct;

/**
 * 线下扣款model
 */
public class AccountBackModel extends AccountDeduct {

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
	/** 审核人 **/
	private String verifyUserName;
	/** 类型名称 **/
	private String typeName;
	/** 状态名称 **/
	private String statusName;
	/** 字符串形式的money **/
	private String moneyString;
	/** 条件查询 */
	private String searchName;

	/**
	 * 
	 * @param accountBack
	 * @return
	 */
	public static AccountBackModel instance(AccountDeduct accountBack) {
		AccountBackModel model = new AccountBackModel();
		BeanUtils.copyProperties(accountBack, model);
		return model;
	}

	/**
	 * 添加线下扣款校验
	 * @param account 资金账户实体类
	 * @return 校验消息
	 */
	public String validAccountBack(Account account) {
		String rtnMsg = "";
		if (this.getMoney() < 0.01) {
			rtnMsg = "扣款金额过小,请重新输入金额！";
		}
		if (this.getMoney() >= 100000000) {
			rtnMsg = "你充值的金额过大,目前系统仅支持千万级别的充值！";
		}
		return rtnMsg;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getVerifyUserName() {
		return verifyUserName;
	}

	public void setVerifyUserName(String verifyUserName) {
		this.verifyUserName = verifyUserName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getMoneyString() {
		return moneyString;
	}

	public void setMoneyString(String moneyString) {
		this.moneyString = moneyString;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
}
