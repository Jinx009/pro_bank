package com.rongdu.p2psys.user.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.user.domain.CouponCategory;

public class CouponCategoryModel extends CouponCategory {

	private static final long serialVersionUID = -5444832179191084335L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	public static CouponCategoryModel instance(CouponCategory pojo) {
		CouponCategoryModel model = new CouponCategoryModel();
		BeanUtils.copyProperties(pojo, model);
		return model;
	}

	public CouponCategory prototype() {
		CouponCategory pojo = new CouponCategory();
		BeanUtils.copyProperties(this, pojo);
		return pojo;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
