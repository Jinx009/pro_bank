package com.rongdu.p2psys.nb.vip.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;
import com.rongdu.p2psys.nb.vip.model.WealthManagerModel;
import com.rongdu.p2psys.user.domain.User;

public interface WealthManagerDao extends BaseDao<WealthManager>{
	
	/**
	 * 查询所有财富管家信息
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageDataList<WealthManagerModel> dataList(WealthManager model, int pageNumber, int pageSize);

	public void updateWealthManager(WealthManager wealthManager);
	
	public WealthManager find(String name);
	
	public List<User> getAllUserByUserid(String sql);
	
	public List<WealthManager> findNotIn(String sql);
	
}
