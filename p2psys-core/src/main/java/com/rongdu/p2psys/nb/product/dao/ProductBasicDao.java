package com.rongdu.p2psys.nb.product.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;

/**
 * 产品DAO
 */
public interface ProductBasicDao extends BaseDao<ProductBasic> {

	/**
	 * 获取推荐产品集合
	 * 
	 * @param platform
	 * @param isExperienceProduct
	 * @return List<ProductBasic>
	 */
	List<ProductBasic> getRecommendProduct(String platform,
			Boolean isExperienceProduct);

	/**
	 * 根据标签和类型获取产品集合
	 * 
	 * @param flagId
	 * @param typeId
	 * @param orderField
	 * @return List<ProductBasic>
	 */
	List<ProductBasic> getProductByFlag(Long flagId, String orderField);

	/**
	 * 更改微信端首页推荐
	 * 
	 * @param id
	 * @param reason
	 */
	void changeRecommend(Long id, String reason);

}
