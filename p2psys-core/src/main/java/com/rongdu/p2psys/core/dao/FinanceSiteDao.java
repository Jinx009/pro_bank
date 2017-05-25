package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.FinanceSite;
import com.rongdu.p2psys.core.model.FinanceSiteModel;

public interface FinanceSiteDao extends BaseDao<FinanceSite> {

	/**
	 * 理财商学院栏目列表
	 * @param model
	 * @return
	 */
	public PageDataList<FinanceSiteModel> financeSiteList(int pageNumber, int pageSize, FinanceSiteModel model);

	/**
	 * 获得可用的栏目
	 * @return
	 */
	public List<FinanceSite> getFinanceSiteByStatus();

	
	
}
