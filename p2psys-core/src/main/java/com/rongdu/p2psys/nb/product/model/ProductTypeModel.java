package com.rongdu.p2psys.nb.product.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.product.domain.ProductType;

public class ProductTypeModel extends ProductType {

	private static final long serialVersionUID = -8839001823887749519L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	/**
	 * 分类所属产品集合
	 * 
	 * @since PC2.0
	 */
	private List<ProductBasicModel> productBasicModelList;

	public static ProductTypeModel instance(ProductType productType) {
		ProductTypeModel model = new ProductTypeModel();
		BeanUtils.copyProperties(productType, model);
		return model;
	}

	public static ProductType transProductType(ProductTypeModel model) {
		ProductType productType = new ProductType();
		BeanUtils.copyProperties(model, productType);
		return productType;
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

	public List<ProductBasicModel> getProductBasicModelList() {
		return productBasicModelList;
	}

	public void setProductBasicModelList(
			List<ProductBasicModel> productBasicModelList) {
		this.productBasicModelList = productBasicModelList;
	}

}
