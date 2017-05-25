package com.rongdu.p2psys.nb.product.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;

public class ProductTypeFlagModel extends ProductTypeFlag {

	private static final long serialVersionUID = -9055892284967065526L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	public static ProductTypeFlagModel instance(ProductTypeFlag productTypeFlag) {
		ProductTypeFlagModel model = new ProductTypeFlagModel();
		BeanUtils.copyProperties(productTypeFlag, model);
		return model;
	}

	public static ProductTypeFlag transProductTypeFlag(
			ProductTypeFlagModel model) {
		ProductTypeFlag productTypeFlag = new ProductTypeFlag();
		BeanUtils.copyProperties(model, productTypeFlag);
		return productTypeFlag;
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
