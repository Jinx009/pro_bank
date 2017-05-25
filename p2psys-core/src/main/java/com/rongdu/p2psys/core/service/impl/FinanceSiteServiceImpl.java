package com.rongdu.p2psys.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.p2psys.core.dao.FinanceArticleDao;
import com.rongdu.p2psys.core.dao.FinanceSiteDao;
import com.rongdu.p2psys.core.domain.FinanceArticle;
import com.rongdu.p2psys.core.domain.FinanceSite;
import com.rongdu.p2psys.core.model.FinanceSiteModel;
import com.rongdu.p2psys.core.service.FinanceSiteService;

@Service("financeSiteService")
public class FinanceSiteServiceImpl implements FinanceSiteService {

	@Resource
	private FinanceSiteDao financeSiteDao;
	@Resource
	private FinanceArticleDao financeArticleDao;

	@Override
	public PageDataList<FinanceSiteModel> financeSiteList(int pageNumber, int pageSize, FinanceSiteModel model) {
		return financeSiteDao.financeSiteList(pageNumber, pageSize, model);
	}

	@Override
	public void financeSiteAdd(FinanceSite financeSite) {
		financeSiteDao.save(financeSite);
	}

	@Override
	public FinanceSite find(long id) {
		return financeSiteDao.find(id);
	}

	@Override
	public void financeSiteEdit(FinanceSite financeSite) {
		financeSiteDao.update(financeSite);
	}

	@Override
	public List<FinanceSite> getFinanceSiteByStatus() {
		return financeSiteDao.getFinanceSiteByStatus();
	}

	@Override
	public List<FinanceSiteModel> showFinanceSiteList() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 1);
		param.addOrder(OrderType.DESC, "addTime");
		List<FinanceSite> financeSiteList = financeSiteDao.findByCriteria(param, 0, 3);
		List<FinanceSiteModel> financeSiteModelList = new ArrayList<FinanceSiteModel>();
		if (financeSiteList.size() > 0) {
			for (int i = 0; i < financeSiteList.size(); i++) {
				FinanceSite financeSite = (FinanceSite)financeSiteList.get(i);
				FinanceSiteModel financeSiteModel = FinanceSiteModel.instance(financeSite);
				List<FinanceArticle> list = financeArticleDao.getFinanceArticleList(financeSite.getId(), 4);
				financeSiteModel.setList(list);
				financeSiteModelList.add(financeSiteModel);
			}
		}
		return financeSiteModelList;
	}

	@Override
	public FinanceSiteModel getNewFinanceSite() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 1);
		param.addOrder(OrderType.DESC, "addTime");
		List<FinanceSite> financeSiteList = financeSiteDao.findByCriteria(param, 0, 1);
		List<FinanceSiteModel> financeSiteModelList = new ArrayList<FinanceSiteModel>();
		if (financeSiteList.size() > 0) {
			for (int i = 0; i < financeSiteList.size(); i++) {
				FinanceSite financeSite = (FinanceSite)financeSiteList.get(i);
				FinanceSiteModel financeSiteModel = FinanceSiteModel.instance(financeSite);
				List<FinanceArticle> list = financeArticleDao.getFinanceArticleList(financeSite.getId(), 4);
				financeSiteModel.setList(list);
				financeSiteModelList.add(financeSiteModel);
			}
		}
		if(financeSiteModelList != null && financeSiteModelList.size() > 0){
			return financeSiteModelList.get(0);
		}
		return null;
	}
	
	
	
}
