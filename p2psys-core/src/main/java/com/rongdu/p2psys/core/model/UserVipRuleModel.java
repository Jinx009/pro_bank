package com.rongdu.p2psys.core.model;

import com.rongdu.p2psys.core.domain.UserVipRule;

/**
 * 用户VIP规则model
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月9日
 */
public class UserVipRuleModel extends UserVipRule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows;

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
}
