package com.rongdu.p2psys.nb.user.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.model.CouponModel;

public interface CouponDao extends BaseDao<Coupon> {

	List<Coupon> getOverdueCouponsList();

	List<CouponModel> getCouponModelByUserIdAndStatus(Long userId,
			Integer status);

	Coupon getInfoByBorrowTenderId(Long borrowTenderId);

	Coupon getInfoByPpfundInId(Long ppfundInId);

	Coupon getInfoByCouponCode(String code);

	List<Coupon> getLootedCouponsListByCategoryId(Long categoryId);

	Integer countLootedCouponByMobile(Long categoryId, String mobile);

}
