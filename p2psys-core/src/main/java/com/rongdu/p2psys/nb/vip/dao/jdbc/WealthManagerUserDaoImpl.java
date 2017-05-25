package com.rongdu.p2psys.nb.vip.dao.jdbc;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.nb.vip.dao.WealthManagerUserDao;
import com.rongdu.p2psys.nb.vip.domain.WealthManagerUser;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;
import com.rongdu.p2psys.nb.vip.model.WealthManagerUserModel;
import com.rongdu.p2psys.user.domain.User;
@Repository("WealthManagerUserDao")
public class WealthManagerUserDaoImpl extends BaseDaoImpl<WealthManagerUser> implements WealthManagerUserDao{

	@Override
	public PageDataList<WealthManagerUserModel> dataList(
			WealthManagerUser model, int pageNumber, int pageSize) {
		
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,pageSize);

		PageDataList<WealthManagerUser> pageDataList = super.findPageList(param);
		PageDataList<WealthManagerUserModel> pageDataList_ = new PageDataList<WealthManagerUserModel>();
		List<WealthManagerUserModel> list = new ArrayList<WealthManagerUserModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0)
		{
			for (int i = 0; i < pageDataList.getList().size(); i++)
			{
				WealthManagerUser WealthManagerUser = (WealthManagerUser) pageDataList.getList().get(i);
				WealthManagerUserModel wealthManagerUserModel = WealthManagerUserModel.instance(WealthManagerUser);
				list.add(wealthManagerUserModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
		
		
	}

	@Override
	public List<WealthManagerUser> getByProjectId(String hql) {
		
		Query query = (Query) em.createQuery(hql);
		 @SuppressWarnings("unchecked")
		List<WealthManagerUser>  list = query.getResultList();
		 
		 if(list!=null&&!list.isEmpty())
		 {
			 return list;
		 }
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserBySql(String sql)
	{
		Query query = em.createNativeQuery(sql);
		
		List<User> list = query.getResultList();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WealthManagerUser> getByWealthUserId(String sql)
	{
		Query query = em.createQuery(sql, WealthManagerUser.class);
		
		List<WealthManagerUser> list = query.getResultList();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WealthManagerUser> findByUserId(String sql) {
		
		Query query = em.createNativeQuery(sql, WealthManagerUser.class);
		
		List<WealthManagerUser>  list = query.getResultList();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WealthManagerUser> findByWealthManagerUserId(String sql) {
		
		Query query = em.createNativeQuery(sql, WealthManagerUser.class);
		
		List<WealthManagerUser>  list = query.getResultList();
		
		return list;
	}

	@Override
	public List<WealthManagerUser> findByWealthManagerId(String sql) {
		Query query = em.createNativeQuery(sql, WealthManagerUser.class);
		
		List<WealthManagerUser>  list = query.getResultList();
		
		return list;
	}

	@Override
	public User getUser(String sql) {
		Query query = em.createNativeQuery(sql,User.class);
		
		List<User> list = query.getResultList();
		if(list.size()>0){
			
			return list.get(0);	
		}
		return null;
	}

	@Override
	public List<User> getUserList(String sql) {
		Query query = em.createNativeQuery(sql,User.class);
		
		List<User> list = query.getResultList();

		return list;	
	
	}
	



}