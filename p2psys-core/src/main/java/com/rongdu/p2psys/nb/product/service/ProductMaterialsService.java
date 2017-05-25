package com.rongdu.p2psys.nb.product.service;

import java.util.List;

import com.rongdu.p2psys.nb.product.domain.ProductMaterials;

public interface ProductMaterialsService {
	/**
	 * 根据产品ID获取所有素材
	 * 
	 * @param productId
	 * @return List
	 */
	List<ProductMaterials> getMaterialsByProductId(Long id);

	/**
	 * 新增素材
	 * 
	 * @param list
	 */
	void addMaterials(List<ProductMaterials> list);

	/**
	 * 删除素材
	 * 
	 * @param materialsList
	 */
	void deleteMaterials(List<ProductMaterials> materialsList);
}
