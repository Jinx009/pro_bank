package com.rongdu.p2psys.nb.voucher.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.voucher.dao.InterestRateDao;
import com.rongdu.p2psys.nb.voucher.domain.InterestRate;
import com.rongdu.p2psys.nb.voucher.model.InterestRateModel;
import com.rongdu.p2psys.nb.voucher.service.InterestRateService;

@Service("interestRateService")
public class InterestRateServiceImpl implements InterestRateService {

	@Resource
	private InterestRateDao interestRateDao;

	@Override
	public List<InterestRate> findAllValid() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 1);
		return interestRateDao.findByCriteria(param);
	}

	@Override
	public InterestRate findById(Long id) {
		return interestRateDao.find(id);
	}

	@Override
	public PageDataList<InterestRateModel> findAllPageList(
			InterestRateModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(),
				model.getSize());
		PageDataList<InterestRate> pojoPageList = interestRateDao
				.findPageList(param);

		List<InterestRateModel> modellist = new ArrayList<InterestRateModel>();
		for (InterestRate pojo : pojoPageList.getList()) {
			InterestRateModel tempModel = InterestRateModel.instance(pojo);
			modellist.add(tempModel);
		}

		PageDataList<InterestRateModel> pageList = new PageDataList<InterestRateModel>();
		pageList.setList(modellist);
		pageList.setPage(pojoPageList.getPage());
		return pageList;
	}

	@Override
	public void addProductCoupon(InterestRateModel model) {
		InterestRate pojo = model.prototype();
		pojo.setStatus(ConstantUtil.FLAG_FALSE);
		pojo.setAddTime(new Date());

		interestRateDao.save(pojo);
	}

	@Override
	public void updateProductCoupon(InterestRateModel model) {
		InterestRate pojo = findById(model.getId());
		pojo.setName(model.getName());
		pojo.setRate(model.getRate());

		interestRateDao.update(pojo);
	}

	@Override
	public void updateStatus(Long id) {
		InterestRate pojo = findById(id);
		if (pojo.getStatus() == ConstantUtil.FLAG_TRUE) {
			pojo.setStatus(ConstantUtil.FLAG_FALSE);
		} else {
			pojo.setStatus(ConstantUtil.FLAG_TRUE);
		}
		interestRateDao.update(pojo);
	}

}
