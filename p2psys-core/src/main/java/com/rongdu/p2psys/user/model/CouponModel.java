package com.rongdu.p2psys.user.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.user.domain.Coupon;

public class CouponModel extends Coupon {

	private static final long serialVersionUID = 7323652413472192140L;

	public static final String OPERATION_REGIST = "coupon_regist";
	public static final String OPERATION_BONUS = "coupon_bonus";
	public static final String OPERATION_LOOT = "coupon_loot";
	public static final String OPERATION_UPLOAD = "coupon_upload";

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	/**
	 * 用来查询的手机号
	 */
	private String searchName;

	private String couponCategoryName;

	private Date couponValidFrom;

	private Date couponValidTo;

	public static CouponModel instance(Coupon pojo) {
		CouponModel model = new CouponModel();
		BeanUtils.copyProperties(pojo, model);
		return model;
	}

	public Coupon prototype() {
		Coupon pojo = new Coupon();
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

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getCouponCategoryName() {
		couponCategoryName = getCategory().getName();
		return couponCategoryName;
	}

	public void setCouponCategoryName(String couponCategoryName) {
		this.couponCategoryName = couponCategoryName;
	}

	public Date getCouponValidFrom() {
		couponValidFrom = getCategory().getValidFrom();
		return couponValidFrom;
	}

	public void setCouponValidFrom(Date couponValidFrom) {
		this.couponValidFrom = couponValidFrom;
	}

	public Date getCouponValidTo() {
		couponValidTo = getCategory().getValidTo();
		return couponValidTo;
	}

	public void setCouponValidTo(Date couponValidTo) {
		this.couponValidTo = couponValidTo;
	}

}
