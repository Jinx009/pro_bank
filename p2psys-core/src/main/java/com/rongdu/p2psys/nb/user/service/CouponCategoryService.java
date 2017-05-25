package com.rongdu.p2psys.nb.user.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.user.domain.CouponCategory;
import com.rongdu.p2psys.user.model.CouponCategoryModel;

public interface CouponCategoryService {

	/**
	 * 根据ID获取用户加息券类型信息
	 * 
	 * @param id
	 * @return CouponCategory
	 */
	CouponCategory findById(Long id);

	/**
	 * 分页取所有加息券信息
	 * 
	 * @param model
	 * @return PageDataList<CouponCategoryModel>
	 */
	PageDataList<CouponCategoryModel> findAllPageList(CouponCategoryModel model);

	/**
	 * 查询有效期内的加息券
	 */
	List<CouponCategory> findAllValid();

	/**
	 * 查询所有未过期的加息券
	 */
	List<CouponCategory> findAllNonOverdue();

	/**
	 * 新增用户加息券类型
	 * 
	 * @param model
	 */
	void addUserCouponCategory(CouponCategoryModel model);

	/**
	 * 修改用户加息券类型
	 * 
	 * @param model
	 */
	void updateUserCouponCategory(CouponCategoryModel model);

}
