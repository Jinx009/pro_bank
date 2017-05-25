package com.rongdu.p2psys.nb.ppfund.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundModel;

public interface PpfundService {

	/**
	 * 根据ID获取实体类
	 * 
	 * @param id
	 * @return Ppfund
	 */
	Ppfund getById(Long id);

	/**
	 * 获取投资总额
	 * 
	 * @param ppfund
	 * @return Double
	 */
	Double getTotalInvestMoney(Ppfund ppfund);

	/**
	 * 获取投资总收益
	 * 
	 * @param ppfund
	 * @return Double
	 */
	Double getTotalProfitMoney(Ppfund ppfund);

	/**
	 * 后台分页查询
	 * 
	 * @param model
	 * @return PageDataList<PpfundModel>
	 */
	PageDataList<PpfundModel> manageList(PpfundModel model);

	/**
	 * 后台添加现金产品
	 * 
	 * @param ppfund
	 * @return Ppfund
	 */
	Ppfund addPpfund(PpfundModel ppfundModel);

	/**
	 * 后台修改现金产品
	 * 
	 * @param ppfund
	 */
	void updatePpfund(PpfundModel ppfundModel);

	/**
	 * 更新现金产品
	 * 
	 * @param ppfund
	 * @return
	 */
	void update(Ppfund ppfund);

	/**
	 * 后台审核现金产品
	 * 
	 * @param model
	 * @param operator
	 */
	void verifyPpfund(PpfundModel model, Operator operator);

}
