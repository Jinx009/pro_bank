package com.rongdu.p2psys.nb.product.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;
import com.rongdu.p2psys.nb.product.model.ProductTypeFlagModel;

public interface ProductTypeFlagService {

	/**
	 * 所有产品标签列表(分页)
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param status
	 * @return 标签列表(分页)
	 */
	PageDataList<ProductTypeFlagModel> getModelPageList(
			ProductTypeFlagModel flagModel);

	/**
	 * 获取系统URL前缀
	 * 
	 * @return URL前缀
	 */
	String getAdminUrl();

	/**
	 * 保存产品标签
	 * 
	 * @param flagModel
	 * @return 保存后的产品标签实例
	 */
	ProductTypeFlag saveProductTypeFlag(ProductTypeFlagModel flagModel);

	/**
	 * 根据ID获取产品标签
	 * 
	 * @param id
	 * @return 产品标签实例
	 */
	ProductTypeFlag findById(Long id);

	/**
	 * 修改产品标签
	 * 
	 * @param flagModel
	 * @return ProductTypeFlag
	 */
	ProductTypeFlag modifyProductTypeFlag(ProductTypeFlagModel flagModel);

	/**
	 * 更新产品标签
	 * 
	 * @param pojo
	 * @return ProductTypeFlag
	 */
	ProductTypeFlag updateProductTypeFlag(ProductTypeFlag pojo);

	/**
	 * 删除产品标签
	 * 
	 * @param id
	 */
	void deleteProductTypeFlag(Long id);

	/**
	 * 所有产品标签列表
	 * 
	 * @param status
	 * @return
	 */
	List<ProductTypeFlag> getPojoList(Integer status);

	/**
	 * 获取有效的产品标签
	 * 
	 * @return
	 */
	List<ProductTypeFlag> findAllEnabledProductTypeFlag();

}
