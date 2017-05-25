package com.rongdu.p2psys.user.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.user.domain.UserVipApply;

/**
 * 用户信用额度申请model类
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月18日15:14:10
 */
public class UserVipApplyModel extends UserVipApply {

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

	/** 起始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 
	 * 查询条件
	 */
	private String searchName;
	

	public static UserVipApplyModel instance(UserVipApply apply) {
		UserVipApplyModel model = new UserVipApplyModel();
		BeanUtils.copyProperties(apply, model);
		return model;
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

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	

}
