package com.rongdu.p2psys.pc.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.user.service.CouponLootLogService;
import com.rongdu.p2psys.nb.user.service.CouponService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.model.CouponModel;

public class CouponAction extends BaseAction<CouponModel> implements
		ModelDriven<CouponModel> {

	private Map<String, Object> map;

	@Resource
	private CouponService couponService;
	@Resource
	private CouponLootLogService couponLootLogService;

	Logger logger = Logger.getLogger(CouponAction.class);

	/**
	 * 加息券页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/nb/pc/member/coupons")
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
	@Action("/nb/pc/member/couponsList")
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
	@Action("/nb/pc/member/donateCoupons")
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
	@Action("/nb/pc/member/useCoupons")
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

	/**
	 * 用户抢加息券
	 * 
	 * @throws Exception
	 */
	@Action("/nb/pc/member/lootCoupons")
	public void lootCoupons() throws Exception {
		map = new HashMap<String, Object>();

		String lootCode = paramString("loot_code");
		String lootTel = paramString("loot_tel");
		Long categoryId = paramLong("category_id");

		try {
			if (ValidateUtil.checkValidCode(lootCode)
					&& StringUtil.isNotBlank(lootTel)) {
				couponService.lootCoupon(map, categoryId, lootTel);
			} else {
				map.put(ConstantUtil.CODE, ConstantUtil.CODE_WRONG_VALID);
				map.put(ConstantUtil.MESSAGE, "验证码错误");
			}
		} catch (Exception e) {
			map.put(ConstantUtil.CODE, ConstantUtil.CODE_FAILURE);
			map.put(ConstantUtil.MESSAGE, "失败");
			logger.debug(e.getMessage());
			throw new BussinessException("用户抢加息券失败\n" + e.getMessage(), 1);
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
