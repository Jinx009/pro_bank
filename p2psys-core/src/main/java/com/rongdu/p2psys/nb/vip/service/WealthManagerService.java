package com.rongdu.p2psys.nb.vip.service;



import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;
import com.rongdu.p2psys.nb.vip.model.WealthManagerModel;
import com.rongdu.p2psys.user.domain.User;

public interface WealthManagerService {
	/**
	 * 查询所有财富管家信息
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageDataList<WealthManagerModel> dataList(WealthManager model, int pageNumber, int pageSize);
	
	/**
	 * 根据财富id删除财富管家信息
	 * @param id
	 */
	public void delWealthManager(Integer id);
	
	/**
	 * 修改财富管家信息
	 */
	public void update(WealthManager wealthManager);
	
	/**
	 * 添加财富管家信息
	 */
	public void saveObject(WealthManager wealthManager);
	
	public WealthManager findById(Integer id);
	
	public WealthManager find(String name);
	
	public List<WealthManager> getList();
	
	public List<User> getAllUserList(Integer id);
	
	public List<WealthManager> findNotIn(Integer id);
}
