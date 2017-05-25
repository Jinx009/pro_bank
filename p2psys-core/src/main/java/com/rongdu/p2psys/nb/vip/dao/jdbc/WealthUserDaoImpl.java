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
import com.rongdu.p2psys.nb.vip.dao.WealthUserDao;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;
import com.rongdu.p2psys.nb.vip.model.WealthUserModel;

@Repository("wealthUserDao")
public class WealthUserDaoImpl extends BaseDaoImpl<WealthUser> implements WealthUserDao
{

	@Override
	public PageDataList<WealthUserModel> dataList(WealthUser model,int pageNumber, int pageSize)
	{		
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,pageSize);
		
		if(!StringUtil.isBlank(model.getUser().getRealName())){
			param.addParam("user.realName", Operators.LIKE, model.getUser().getRealName());			
		}
		if(!StringUtil.isBlank(model.getUser().getUserName())){
			param.addParam("user.userName", Operators.LIKE, model.getUser().getUserName());			
		}
		
		PageDataList<WealthUser> pageDataList = super.findPageList(param);
		PageDataList<WealthUserModel> pageDataList_ = new PageDataList<WealthUserModel>();
		List<WealthUserModel> list = new ArrayList<WealthUserModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0)
		{
			for (int i = 0; i < pageDataList.getList().size(); i++)
			{
				WealthUser WealthUser = (WealthUser) pageDataList.getList().get(i);
				WealthUserModel wealthUserModel = WealthUserModel.instance(WealthUser);
				list.add(wealthUserModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}


	@Override
	public List<WealthUser> findByWealthUserId(String sql) {
		Query query = em.createNativeQuery(sql, WealthUser.class);
		
		List<WealthUser>  list = query.getResultList();
		
		return list;
	}

	@Override
	public WealthUser getByWealthUserId(String sql) {
		Query query = em.createQuery(sql, WealthUser.class);
		WealthUser wealthUser = (WealthUser) query.getSingleResult();
		return wealthUser;
	}

	@Override
	public WealthUser getWealthUser(String sql) {
		Query query = em.createNativeQuery(sql, WealthUser.class);
		List<WealthUser> wealthUsers = query.getResultList();
		if(wealthUsers.size()>0){
			return wealthUsers.get(0);
		}
		return null;
	}

	

	

}
