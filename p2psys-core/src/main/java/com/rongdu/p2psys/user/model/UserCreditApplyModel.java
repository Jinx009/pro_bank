package com.rongdu.p2psys.user.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.user.domain.UserCreditApply;

/**
 * 用户信用额度申请model类
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月18日15:14:10
 */
public class UserCreditApplyModel extends UserCreditApply {

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
	/** 用户类型 **/
	private int userType;
	/** 起始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/**
	 * 查询条件（用户名，真实姓名）
	 */
	private String searchName;

	public static UserCreditApplyModel instance(UserCreditApply apply) {
		UserCreditApplyModel model = new UserCreditApplyModel();
		BeanUtils.copyProperties(apply, model);
		return model;
	}

	/**
     * 交易信用额度申请时候的详细说明和其他说明
     * @param remark 其他说明
     * @param content 详细说明
     */
    public void validRemarkAndContentForAmountApply(String remark,String content) {
        if (!StringUtil.isBlank(remark) && remark.length() > 100) {
            throw new AccountException("您输入的其他说明信息过长", 1);
        }
        if (!StringUtil.isBlank(content) && content.length() > 100) {
            throw new AccountException("您输入的详细说明信息过长", 1);
        }
    }
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
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

}
