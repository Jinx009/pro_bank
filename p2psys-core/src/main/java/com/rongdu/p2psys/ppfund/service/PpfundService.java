package com.rongdu.p2psys.ppfund.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;
import com.rongdu.p2psys.ppfund.model.PpfundModel;

/**
 * 现金管理产品（PPfund）
 */
public interface PpfundService {
	/**
	 * 分页查询
	 * 
	 * @param model
	 * @return PageDataList
	 */
	PageDataList<PpfundModel> list(PpfundModel model);

	/**
	 * 后台分页查询
	 * 
	 * @param model
	 * @return PageDataList
	 */
	PageDataList<PpfundModel> manageList(PpfundModel model);

	/**
	 * 添加
	 * 
	 * @param ppfund
	 * @param list
	 */
	void addPpfund(PpfundModel ppfundModel, List<PpfundUpload> list);

	/**
	 * 根据ID获取
	 * 
	 * @param id
	 * @return Ppfund
	 */
	Ppfund getPpfundById(long id);

	/**
	 * 审核
	 * 
	 * @param model
	 * @param operator
	 */
	void verifyPpfund(PpfundModel model, Operator operator);

	/**
	 * 修改
	 * 
	 * @param model
	 * @param list
	 * @param delIds
	 */
	void updatePpfund(PpfundModel model, List<PpfundUpload> list,
			String[] delIds);

	/**
	 * 首页PPfund数据
	 * 
	 * @return PpfundModel
	 */
	PpfundModel getLastPpfund();

	/**
	 * 修改
	 * 
	 * @param ppfund
	 * @return Ppfund
	 */
	Ppfund update(Ppfund ppfund);

	/**
	 * 已成交Ppfund
	 * 
	 * @return Object[]
	 */
	Object[] countByFinish();
}
