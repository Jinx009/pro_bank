package com.rongdu.p2psys.wechat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.user.service.CouponService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.model.CouponModel;

public class WechatCouponAction extends BaseAction<CouponModel> implements
		ModelDriven<CouponModel> {
	private Map<String, Object> map;

	@Resource
	private CouponService couponService;

	/**
	 * 加息券页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/nb/wechat/account/coupons")
	public String coupons() throws Exception {
		Long userId = 0L;
		if (hasSessionUser()) {
			userId = getNBSessionUser().getUserId();
		}
		request.setAttribute("userId", userId);

		return "coupons";
	}

	/**
	 * 获取加息券信息
	 * 
	 * @throws Exception
	 */
	@Action("/nb/wechat/account/couponsList")
	public void couponsList() throws Exception {
		if (hasSessionUser()) {
			Long userId = getNBSessionUser().getUserId();

			// 更新过期信息
			couponService.updateOverdueInfo();

			// 未使用
			List<CouponModel> unusedList = couponService
					.getUnusedCouponByUserId(userId);
			// 已使用
			List<CouponModel> usedList = couponService
					.getUsedCouponByUserId(userId);
			// 已过期
			List<CouponModel> overdueList = couponService
					.getOverdueCouponByUserId(userId);
			// 转赠给他人
			List<CouponModel> donateList = couponService
					.getDonateCouponByUserId(userId);

			map = new HashMap<String, Object>();
			map.put("unusedList", unusedList);
			map.put("usedList", usedList);
			map.put("overdueList", overdueList);
			map.put("donateList", donateList);
		} else {
			map = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 转赠他人
	 * 
	 * @throws Exception
	 */
	@Action("/nb/wechat/account/donateCoupons")
	public void donateCoupons() throws Exception {
		map = new HashMap<String, Object>();

		if (ValidateUtil.checkValidCode(paramString("donation_code"))
				&& couponService.donateCoupon(getNBSessionUser().getUserId(),
						paramLong("donation_id"), paramString("donation_tel"))) {
			map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		} else {
			map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
		}
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 使用加息券
	 * 
	 * @throws Exception
	 */
	@Action("/nb/wechat/account/useCoupons")
	public void useCoupons() throws Exception {
		map = new HashMap<String, Object>();
		if (couponService.useCoupon(getNBSessionUser().getUserId(),
				paramLong("couponId"), paramLong("borrowTenderId"),
				paramLong("ppfundInId"))) {
			map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		} else {
			map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
		}
		printWebJson(getStringOfJpaObj(map));
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
}
