package com.rongdu.p2psys.nb.vip.service;


import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;
import com.rongdu.p2psys.nb.vip.model.WealthUserModel;

public interface WealthUserService
{
	public PageDataList<WealthUserModel> getPage(WealthUser model,int pageNumber, int pageSize);
	
	/**
	 * 添加
	 * @param wealthManagerUser
	 */
	public WealthUser saveObject(WealthUser wealthUser);

	/**
	 * 根据财富id删除财富管家信息
	 * @param id
	 */
	public void delWealthUser(Integer id);
	

	
	/**
	 * 修改财富管家信息
	 */
	public void update(WealthUser wealthUser);
	
	/**
	 * 通过id查找财富管家对应关系
	 * @param id
	 * @return
	 */
	public WealthUser findById(Integer id);
	
	public List<WealthUser> findByWealthUserId(Integer userId);
	
	public WealthUser getByWealthUserId(Integer userId);
	
	public WealthUser getWealthUser(Integer wealthManagerId, Integer userId);
	

}
