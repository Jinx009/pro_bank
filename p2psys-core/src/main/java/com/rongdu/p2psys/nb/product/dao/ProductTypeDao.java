package com.rongdu.p2psys.nb.product.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.product.domain.ProductType;

/**
 * 产品类型DAO
 */
public interface ProductTypeDao extends BaseDao<ProductType> {

	/**
	 * 获取某标签下所有有效的产品类型
	 * 
	 * @param flagId
	 * @return List<ProductType>
	 */
	List<ProductType> getProductTypeListByFlag(Long flagId);

}
