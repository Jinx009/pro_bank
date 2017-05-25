package com.rongdu.p2psys.nb.user.service;

import java.util.List;
import java.util.Map;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.model.CouponModel;

public interface CouponService {

	/**
	 * 根据ID获取加息券信息
	 * 
	 * @param couponId
	 * @return Coupon
	 */
	Coupon findCouponInfoById(Long couponId);

	/**
	 * 根据非现金投资记录ID找到加息券信息
	 * 
	 * @param borrowTenderId
	 * @return Coupon
	 */
	Coupon findCouponInfoByBorrowTenderId(Long borrowTenderId);

	/**
	 * 根据现金投资记录ID找到加息券信息
	 * 
	 * @param ppfundInId
	 * @return Coupon
	 */
	Coupon findCouponInfoByPpfundInId(Long ppfundInId);

	/**
	 * 更新加息券信息
	 * 
	 * @param pojo
	 * @return Coupon
	 */
	Coupon updateCouponInfo(Coupon pojo);

	/**
	 * 更新过期加息券
	 */
	void updateOverdueInfo();

	/**
	 * 获取该用户未使用的加息券
	 * 
	 * @param userId
	 * @return List<CouponModel>
	 */
	List<CouponModel> getUnusedCouponByUserId(Long userId);

	/**
	 * 获取该用户使用过的加息券
	 * 
	 * @param userId
	 * @return List<CouponModel>
	 */
	List<CouponModel> getUsedCouponByUserId(Long userId);

	/**
	 * 获取该用户过期的加息券
	 * 
	 * @param userId
	 * @return List<CouponModel>
	 */
	List<CouponModel> getOverdueCouponByUserId(Long userId);

	/**
	 * 获取该用户转赠出去的加息券
	 * 
	 * @param userId
	 * @return List<CouponModel>
	 */
	List<CouponModel> getDonateCouponByUserId(Long userId);

	/**
	 * 使用加息券
	 * 
	 * @param userId
	 * @param couponId
	 * @return Boolean
	 */
	Boolean useCoupon(Long userId, Long couponId, Long borrowTenderId,
			Long ppfundInId);

	/**
	 * 转赠加息券
	 * 
	 * @param userId
	 * @param couponId
	 * @param tel
	 * @return Boolean
	 */
	Boolean donateCoupon(Long userId, Long couponId, String tel);

	/**
	 * 抢券
	 * 
	 * @param map
	 * @param categoryId
	 * @param lootTel
	 */
	void lootCoupon(Map<String, Object> map, Long categoryId, String lootTel);

	/**
	 * 生成新加息券
	 * 
	 * @return Boolean
	 */
	Boolean generateCoupon(String operation, Long categoryId,
			String... mobileNumbers);

	/**
	 * 分页获取所有加息券信息
	 * 
	 * @param model
	 * @return PageDataList<CouponModel>
	 */
	PageDataList<CouponModel> findAllPageList(CouponModel model);

	/**
	 * 根据手机号获取已抢到的加息券集合
	 * 
	 * @param categoryId
	 * @param mobile
	 * @return List<Coupon>
	 */
	List<Coupon> getCouponListByMobileNumber(Long categoryId, String mobile);

}
