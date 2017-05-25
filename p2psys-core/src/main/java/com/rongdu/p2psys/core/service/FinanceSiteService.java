package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.FinanceSite;
import com.rongdu.p2psys.core.model.FinanceSiteModel;

public interface FinanceSiteService {

	/**
	 * 理财商学院栏目列表
	 * @param model
	 * @return
	 */
	public PageDataList<FinanceSiteModel> financeSiteList(int pageNumber, int pageSize, FinanceSiteModel model);

	/**
	 * 添加理财商学院栏目
	 * @param financeSite
	 */
	public void financeSiteAdd(FinanceSite financeSite);

	/**
	 * 通过ID获得FinanceSite
	 * @param id
	 * @return
	 */
	public FinanceSite find(long id);

	/**
	 * 修改理财商学院栏目
	 * @param financeSite
	 */
	public void financeSiteEdit(FinanceSite financeSite);

	/**
	 * 获得可用的栏目
	 * @return
	 */
	public List<FinanceSite> getFinanceSiteByStatus();

	/**
	 * 获得栏目列表
	 * @return
	 */
	public List<FinanceSiteModel> showFinanceSiteList();

	/**
	 * 获得最新的栏目
	 * @return
	 */
	public FinanceSiteModel getNewFinanceSite();

}
