package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.FinanceArticle;
import com.rongdu.p2psys.core.domain.FinanceArticleExpert;
import com.rongdu.p2psys.core.domain.FinanceSite;
import com.rongdu.p2psys.core.model.FinanceArticleModel;

public interface FinanceArticleService {

	/**
	 * 理财商学院文章列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	public PageDataList<FinanceArticleModel> financeArticleList(int pageNumber,
			int pageSize, FinanceArticleModel model);

	/**
	 * 添加理财商学院文章
	 * @param financeArticle
	 */
	public void financeArticleAdd(FinanceArticle financeArticle);

	/**
	 * 获得FinanceArticle
	 * @param id
	 * @return
	 */
	public FinanceArticle find(long id);

	/**
	 * 修改理财商学院文章
	 * @param financeArticle
	 */
	public void financeArticleEdit(FinanceArticle financeArticle);

	/**
	 * 进入栏目显示文章列表
	 * @param financeSiteId
	 * @return
	 */
	public PageDataList<FinanceArticleModel> enterFinanceSite(int pageNumber, int pageSize, long financeSiteId);

	/**
	 * 显示最新文章标题
	 * @return
	 */
	public List<FinanceArticle> getNewFinanceArticle(FinanceSite financeSite);

	public PageDataList<FinanceArticleExpert> financeArticleExpertList(
			int pageNumber, int pageSize, FinanceArticleModel model);

	/**
	 * 保存FinanceArticleExpert
	 * @param financeArticleExpert
	 */
	public void financeArticleExpertAdd(FinanceArticleExpert financeArticleExpert);

	/**
	 * 根据ID获得FinanceArticleExpert
	 * @param id
	 * @return
	 */
	public FinanceArticleExpert getFinanceArticleExpertById(long id);

	/**
	 * 更新FinanceArticleExpert
	 * @param financeArticleExpert
	 */
	public void financeArticleExpertEdit(
			FinanceArticleExpert financeArticleExpert);

	/**
	 * 根据status获得FinanceArticleExpert
	 * @return
	 */
	public List<FinanceArticleExpert> getFinanceArticleExpertByStatus();

	/**
	 * 专家介绍
	 * @param financeSiteId
	 * @return
	 */
	public List<FinanceArticleExpert> expertIntroduce(long financeSiteId);

	/**
	 * 获取专家
	 * @param expertId
	 * @return
	 */
	public FinanceArticleExpert findExpertById(long expertId);
	
}
