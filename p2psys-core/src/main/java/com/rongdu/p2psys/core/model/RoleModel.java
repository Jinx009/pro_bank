package com.rongdu.p2psys.core.model;

import com.rongdu.p2psys.core.domain.Role;

public class RoleModel extends Role {

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
