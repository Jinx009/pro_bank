package com.rongdu.p2psys.nb.product.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.model.ProductTypeModel;

public interface ProductTypeService {

	/**
	 * 所有类别列表(分页)
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return PageDataList<ProductTypeModel>
	 */
	PageDataList<ProductTypeModel> getModelPageList(int pageNumber, int pageSize);

	/**
	 * 获取系统URL前缀
	 * 
	 * @return URL前缀
	 */
	String getAdminUrl();

	/**
	 * 保存产品类别
	 * 
	 * @param typeModel
	 * @return ProductType
	 */
	ProductType saveProductType(ProductTypeModel typeModel);

	/**
	 * 修改产品标签
	 * 
	 * @param pojo
	 * @return ProductTypeFlag
	 */
	ProductType modifyProductType(ProductTypeModel typeModel);

	/**
	 * 更新产品标签
	 * 
	 * @param typeModel
	 * @return ProductType
	 */
	ProductType updateProductType(ProductType pojo);

	/**
	 * 删除产品类别
	 * 
	 * @param id
	 */
	void deleteProductType(Long id);

	/**
	 * 根据ID获取产品类别
	 * 
	 * @param id
	 * @return ProductType
	 */
	ProductType findById(Long id);

	/**
	 * 根据属性和状态获取类别列表
	 * 
	 * @return List<ProductType>
	 */
	List<ProductType> findProductTypeListByCategory(String category, int status);

	/**
	 * 获取某标签下所有有效的产品类型
	 * 
	 * @param flagId
	 * @return List<ProductType>
	 */
	List<ProductType> getProductTypeListByFlag(Long flagId);

}
