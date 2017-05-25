package com.rongdu.p2psys.nb.vip.dao;



import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.vip.domain.WealthManagerUser;
import com.rongdu.p2psys.nb.vip.model.WealthManagerUserModel;
import com.rongdu.p2psys.user.domain.User;

public interface WealthManagerUserDao extends BaseDao<WealthManagerUser>{
	
	public PageDataList<WealthManagerUserModel> dataList(WealthManagerUser model, int pageNumber, int pageSize);
	
	public List<WealthManagerUser> getByProjectId(String hql);

	public List<User> getUserBySql(String sql);
	public User getUser(String sql);
	public List<User> getUserList(String sql);

	public List<WealthManagerUser> getByWealthUserId(String sql);

	
	public List<WealthManagerUser> findByUserId(String sql);

	public List<WealthManagerUser> findByWealthManagerUserId(String sql);
	
	public List<WealthManagerUser> findByWealthManagerId(String sql);
}
