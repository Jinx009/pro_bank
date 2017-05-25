package com.rongdu.p2psys.nb.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.p2psys.nb.user.dao.CouponCategoryDao;
import com.rongdu.p2psys.nb.user.service.CouponCategoryService;
import com.rongdu.p2psys.user.domain.CouponCategory;
import com.rongdu.p2psys.user.model.CouponCategoryModel;

@Service("couponCategoryService")
public class CouponCategoryServiceImpl implements CouponCategoryService {

	@Resource
	private CouponCategoryDao couponCategoryDao;

	public CouponCategory findById(Long id) {
		return couponCategoryDao.find(id);
	}

	@Override
	public PageDataList<CouponCategoryModel> findAllPageList(
			CouponCategoryModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(),
				model.getSize());
		PageDataList<CouponCategory> pojoPageList = couponCategoryDao
				.findPageList(param);

		List<CouponCategoryModel> modellist = new ArrayList<CouponCategoryModel>();
		for (CouponCategory pojo : pojoPageList.getList()) {
			CouponCategoryModel tempModel = CouponCategoryModel.instance(pojo);
			modellist.add(tempModel);
		}

		PageDataList<CouponCategoryModel> pageList = new PageDataList<CouponCategoryModel>();
		pageList.setList(modellist);
		pageList.setPage(pojoPageList.getPage());
		return pageList;
	}

	@Override
	public List<CouponCategory> findAllValid() {
		QueryParam param = new QueryParam();
		param.addParam("validFrom", Operators.LTE, new Date());
		param.addParam("validTo", Operators.GTE, new Date());
		return couponCategoryDao.findByCriteria(param);
	}

	@Override
	public List<CouponCategory> findAllNonOverdue() {
		QueryParam param = new QueryParam();
		param.addParam("validFrom", Operators.LTE, new Date());
		return couponCategoryDao.findByCriteria(param);
	}

	@Override
	public void addUserCouponCategory(CouponCategoryModel model) {
		CouponCategory pojo = model.prototype();
		couponCategoryDao.save(pojo);
	}

	@Override
	public void updateUserCouponCategory(CouponCategoryModel model) {
		CouponCategory pojo = model.prototype();
		couponCategoryDao.update(pojo);
	}

}
