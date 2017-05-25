package com.rongdu.p2psys.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.FinanceArticleDao;
import com.rongdu.p2psys.core.dao.FinanceArticleExpertDao;
import com.rongdu.p2psys.core.dao.FinanceSiteDao;
import com.rongdu.p2psys.core.domain.FinanceArticle;
import com.rongdu.p2psys.core.domain.FinanceArticleExpert;
import com.rongdu.p2psys.core.domain.FinanceSite;
import com.rongdu.p2psys.core.model.FinanceArticleModel;
import com.rongdu.p2psys.core.service.FinanceArticleService;

@Service("financeArticleService")
public class FinanceArticleServiceImpl implements FinanceArticleService {

	@Resource
	private FinanceArticleDao financeArticleDao;
	@Resource
	private FinanceSiteDao financeSiteDao;
	@Resource
	private FinanceArticleExpertDao financeArticleExpertDao;

	@Override
	public PageDataList<FinanceArticleModel> financeArticleList(int pageNumber,
			int pageSize, FinanceArticleModel model) {
		
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize).addParam("isDelete", 0);
		if(!StringUtil.isBlank(model.getSearchName())){//模糊查询条件
			param.addParam("title", Operators.LIKE, model.getSearchName());
		}else{ //精确查询条件
			if (model.getStatus() != 0) {
				param.addParam("status", model.getStatus());
			}
			if (!StringUtil.isBlank(model.getTitle())) {
				param.addParam("title", Operators.EQ, model.getTitle());
			}
		}
		PageDataList<FinanceArticle> pageDataList = financeArticleDao.findPageList(param);
		PageDataList<FinanceArticleModel> pageDataList_ = new PageDataList<FinanceArticleModel>();
		List<FinanceArticleModel> list = new ArrayList<FinanceArticleModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				FinanceArticle financeArticle = (FinanceArticle)pageDataList.getList().get(i);
				FinanceArticleModel financeArticleModel = FinanceArticleModel.instance(financeArticle);
				if(financeArticle.getFinanceSite() != null){
					FinanceSite financeSite = financeSiteDao.find(financeArticle.getFinanceSite().getId());
					financeArticleModel.setFinanceSiteTitle(financeSite.getTitle());
				}
				FinanceArticleExpert financeArticleExpert = financeArticleExpertDao.find(financeArticle.getFinanceArticleExpert().getId());
				financeArticleModel.setAutorName(financeArticleExpert.getAutorName());
				
				list.add(financeArticleModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public void financeArticleAdd(FinanceArticle financeArticle) {
		financeArticleDao.save(financeArticle);
	}

	@Override
	public FinanceArticle find(long id) {
		return financeArticleDao.find(id);
	}

	@Override
	public void financeArticleEdit(FinanceArticle financeArticle) {
		financeArticleDao.update(financeArticle);
	}

	@Override
	public PageDataList<FinanceArticleModel> enterFinanceSite(int pageNumber, int pageSize, long financeSiteId) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		param.addParam("status", 1);
		param.addParam("financeSite.id", financeSiteId);
		param.addParam("isDelete", 0);
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<FinanceArticle> pageDataList = financeArticleDao.findPageList(param);
		PageDataList<FinanceArticleModel> pageDataList_ = new PageDataList<FinanceArticleModel>();
		List<FinanceArticleModel> list = new ArrayList<FinanceArticleModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				FinanceArticle financeArticle = (FinanceArticle)pageDataList.getList().get(i);
				FinanceArticleModel financeArticleModel = FinanceArticleModel.instance(financeArticle);
				
				FinanceArticleExpert financeArticleExpert = financeArticleExpertDao.find(financeArticle.getFinanceArticleExpert().getId());
				financeArticleModel.setAutorName(financeArticleExpert.getAutorName());
				
				list.add(financeArticleModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public List<FinanceArticle> getNewFinanceArticle(FinanceSite financeSite) {
		return financeArticleDao.getFinanceArticleList(financeSite.getId(), 5);
	}

	@Override
	public PageDataList<FinanceArticleExpert> financeArticleExpertList(
			int pageNumber, int pageSize, FinanceArticleModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize).addParam("isDelete", 0);
		if(!StringUtil.isBlank(model.getSearchName())){//模糊查询条件
			param.addParam("autorName", Operators.LIKE, model.getSearchName());
		}else{ //精确查询条件
			if (model.getStatus() != 0) {
				param.addParam("status", model.getStatus());
			}
			if (!StringUtil.isBlank(model.getTitle())) {
				param.addParam("autorName", Operators.EQ, model.getTitle());
			}
		}
		PageDataList<FinanceArticleExpert> pageDataList = financeArticleExpertDao.findPageList(param);
		return pageDataList;
	}

	@Override
	public void financeArticleExpertAdd(FinanceArticleExpert financeArticleExpert) {
		financeArticleExpertDao.save(financeArticleExpert);
	}

	@Override
	public FinanceArticleExpert getFinanceArticleExpertById(long id) {
		return financeArticleExpertDao.find(id);
	}

	@Override
	public void financeArticleExpertEdit(
			FinanceArticleExpert financeArticleExpert) {
		financeArticleExpertDao.update(financeArticleExpert);
	}

	@Override
	public List<FinanceArticleExpert> getFinanceArticleExpertByStatus() {
		return financeArticleExpertDao.getFinanceArticleExpertByStatus();
	}

	@Override
	public List<FinanceArticleExpert> expertIntroduce(long financeSiteId) {
		return financeArticleExpertDao.expertIntroduce(financeSiteId);
	}

	@Override
	public FinanceArticleExpert findExpertById(long expertId) {
		return financeArticleExpertDao.find(expertId);
	}
	
	
	
}
