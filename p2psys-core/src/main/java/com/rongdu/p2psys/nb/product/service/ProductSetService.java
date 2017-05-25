package com.rongdu.p2psys.nb.product.service;

import java.util.List;

import com.rongdu.p2psys.nb.product.domain.ProductSet;

public interface ProductSetService
{
	/**
	 * 获取组合配置信息
	 * 
	 * @param productId
	 * @return List
	 */
	List<ProductSet> getProdSetList(Long productId);

	/**
	 * 保存组合配置信息
	 * 
	 * @param productBasic
	 */
	void saveProductSet(List<ProductSet> list);

	/**
	 * 删除组合配置信息
	 * 
	 * @param productBasic
	 */
	void deleteProductSet(List<ProductSet> list);
}
