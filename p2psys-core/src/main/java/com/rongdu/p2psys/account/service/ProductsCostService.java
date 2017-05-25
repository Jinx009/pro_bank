package com.rongdu.p2psys.account.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.ProductsCost;
import com.rongdu.p2psys.account.model.ProductsCostModel;

/**
 * 产品费用
 * 
 * @author yl
 *
 */
public interface ProductsCostService {
	
	/**
	 * 保存记录
	 * @param cost
	 * @return
	 */
	ProductsCost save(ProductsCost cost);
	
	/**
	 * 分页查询
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<ProductsCostModel> list(ProductsCostModel model);
}
