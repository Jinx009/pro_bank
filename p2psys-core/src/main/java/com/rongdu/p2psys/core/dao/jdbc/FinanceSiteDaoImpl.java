package com.rongdu.p2psys.core.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.FinanceSiteDao;
import com.rongdu.p2psys.core.domain.FinanceSite;
import com.rongdu.p2psys.core.model.FinanceSiteModel;

@Repository("financeSiteDao")
public class FinanceSiteDaoImpl extends BaseDaoImpl<FinanceSite> implements FinanceSiteDao {

	@Override
	public PageDataList<FinanceSiteModel> financeSiteList(int pageNumber, int pageSize, FinanceSiteModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
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
		PageDataList<FinanceSite> pageDataList = super.findPageList(param);
		PageDataList<FinanceSiteModel> pageDataList_ = new PageDataList<FinanceSiteModel>();
		List<FinanceSiteModel> list = new ArrayList<FinanceSiteModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				FinanceSite financeSite = (FinanceSite)pageDataList.getList().get(i);
				FinanceSiteModel financeSiteModel = FinanceSiteModel.instance(financeSite);
				list.add(financeSiteModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinanceSite> getFinanceSiteByStatus() {
		String sql = "select * from rd_finance_site where status = 1";
		Query query = em.createNativeQuery(sql, FinanceSite.class);
		return query.getResultList();
	}

	
	
}
