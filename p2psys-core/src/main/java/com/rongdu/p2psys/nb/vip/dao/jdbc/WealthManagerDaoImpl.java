package com.rongdu.p2psys.nb.vip.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.vip.dao.WealthManagerDao;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;
import com.rongdu.p2psys.nb.vip.model.WealthManagerModel;
import com.rongdu.p2psys.user.domain.User;
@Repository("wealthManagerDao")
public class WealthManagerDaoImpl extends BaseDaoImpl<WealthManager> implements WealthManagerDao{

	@Override
	public PageDataList<WealthManagerModel> dataList(WealthManager model,
			int pageNumber, int pageSize) {

		QueryParam param = QueryParam.getInstance().addPage(pageNumber,pageSize);
		if(!StringUtil.isBlank(model.getName())){//模糊查询条件
			param.addParam("name", Operators.EQ, model.getName());			
		}
		PageDataList<WealthManager> pageDataList = super.findPageList(param);
		PageDataList<WealthManagerModel> pageDataList_ = new PageDataList<WealthManagerModel>();
		List<WealthManagerModel> list = new ArrayList<WealthManagerModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0)
		{
			for (int i = 0; i < pageDataList.getList().size(); i++)
			{
				WealthManager wealthManager = (WealthManager) pageDataList.getList().get(i);
				WealthManagerModel wealthManagerModel = WealthManagerModel.instance(wealthManager);
				list.add(wealthManagerModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
		
		
		
	}

	@Override
	public void updateWealthManager(WealthManager wealthManager)
	{
		em.merge(wealthManager);
		
	}
	

	@Override
	public WealthManager find(String name) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("name", name);
		return findByCriteriaForUnique(param);
	}

	@Override
	public List<User> getAllUserByUserid(String sql) {
		Query query = em.createNativeQuery(sql, User.class);
		List<User> users = query.getResultList();
		return users;
	}

	@Override
	public List<WealthManager> findNotIn(String sql) {
		Query query = em.createNativeQuery(sql,WealthManager.class);
		List<WealthManager> wealthManager =query.getResultList();
		return wealthManager;
	}

	
	



}
