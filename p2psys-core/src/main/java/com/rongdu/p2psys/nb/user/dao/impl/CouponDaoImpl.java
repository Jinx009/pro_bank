package com.rongdu.p2psys.nb.user.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.p2psys.core.constant.CouponStatusConstant;
import com.rongdu.p2psys.nb.user.dao.CouponDao;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.model.CouponModel;

@Repository("couponDao")
public class CouponDaoImpl extends BaseDaoImpl<Coupon> implements CouponDao {

	@Override
	public List<Coupon> getOverdueCouponsList() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", CouponStatusConstant.STATUS_UNUSED);
		param.addParam("borrowTenderId", null);
		param.addParam("ppfundInId", null);
		param.addParam("category.validTo", Operators.LT, new Date());
		return findByCriteria(param);
	}

	@Override
	public List<CouponModel> getCouponModelByUserIdAndStatus(Long userId,
			Integer status) {
		QueryParam param = QueryParam.getInstance();
		if (CouponStatusConstant.STATUS_DONATED.equals(status)) {
			param.addParam("userFrom.userId", userId);
		} else {
			param.addParam("userTo.userId", userId);
			param.addParam("status", status);
		}
		if (CouponStatusConstant.STATUS_UNUSED.equals(status)) {
			param.addParam("category.validTo", Operators.GT, new Date());
		}
		if (CouponStatusConstant.STATUS_OVERDUE.equals(status)) {
			param.addParam("category.validTo", Operators.LT, new Date());
		}

		param.addOrder(OrderType.DESC, "addTime");
		List<Coupon> pojoList = this.findByCriteria(param);

		List<CouponModel> modelList = new ArrayList<CouponModel>();
		for (Coupon pojo : pojoList) {
			CouponModel model = CouponModel.instance(pojo);
			modelList.add(model);
		}
		return modelList;
	}

	@Override
	public Coupon getInfoByBorrowTenderId(Long borrowTenderId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrowTenderId", borrowTenderId);

		return findByCriteriaForUnique(param);
	}

	@Override
	public Coupon getInfoByPpfundInId(Long ppfundInId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("ppfundInId", ppfundInId);

		return findByCriteriaForUnique(param);
	}

	@Override
	public Coupon getInfoByCouponCode(String code) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("code", code);

		return findByCriteriaForUnique(param);
	}

	@Override
	public List<Coupon> getLootedCouponsListByCategoryId(Long categoryId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", Operators.NOTEQ,
				CouponStatusConstant.STATUS_DONATED);
		param.addParam("category.id", categoryId);
		return findByCriteria(param);
	}

	@Override
	public Integer countLootedCouponByMobile(Long categoryId, String mobile) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("toMobile", mobile);
		param.addParam("category.id", categoryId);
		List<Coupon> list = findByCriteria(param);

		Integer count = 0;
		if (list != null) {
			count = list.size();
		}
		return count;
	}

}
