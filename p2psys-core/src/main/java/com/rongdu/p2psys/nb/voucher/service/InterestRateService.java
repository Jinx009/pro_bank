package com.rongdu.p2psys.nb.voucher.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.voucher.domain.InterestRate;
import com.rongdu.p2psys.nb.voucher.model.InterestRateModel;

public interface InterestRateService {

	/**
	 * 根据ID查加息券信息
	 * 
	 * @param id
	 * @return InterestRate
	 */
	InterestRate findById(Long id);

	/**
	 * 发标时获取所有有效的加息券
	 * 
	 * @return List<InterestRate>
	 */
	List<InterestRate> findAllValid();

	/**
	 * 分页取所有加息券信息
	 * 
	 * @param model
	 * @return PageDataList<InterestRateModel>
	 */
	PageDataList<InterestRateModel> findAllPageList(InterestRateModel model);

	/**
	 * 新增加息券
	 * 
	 * @param name
	 * @param rate
	 */
	void addProductCoupon(InterestRateModel model);

	/**
	 * 更新加息券
	 * 
	 * @param id
	 * @param name
	 * @param rate
	 */
	void updateProductCoupon(InterestRateModel model);

	/**
	 * 更新加息券状态
	 * 
	 * @param id
	 */
	void updateStatus(Long id);
}
