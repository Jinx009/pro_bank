package com.rongdu.p2psys.nb.voucher.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.voucher.domain.InterestRate;

public class InterestRateModel extends InterestRate {

	private static final long serialVersionUID = 7292392179381043996L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	public static InterestRateModel instance(InterestRate pojo) {
		InterestRateModel model = new InterestRateModel();
		BeanUtils.copyProperties(pojo, model);
		return model;
	}

	public InterestRate prototype() {
		InterestRate pojo = new InterestRate();
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
