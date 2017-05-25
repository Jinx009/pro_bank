package com.rongdu.p2psys.nb.vip.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.vip.domain.WealthManagerUser;
import com.rongdu.p2psys.nb.vip.model.WealthManagerUserModel;
import com.rongdu.p2psys.user.domain.User;

public interface WealthManagerUserService {
	
	/**
	 * 查询所有财富管家信息
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageDataList<WealthManagerUserModel> dataList(WealthManagerUser model, int pageNumber, int pageSize);
	
	/**
	 * 添加
	 * @param wealthManagerUser
	 */
	public void saveObject(WealthManagerUser wealthManagerUser);
	
	/**
	 * 根据财富id删除财富管家信息
	 * @param id
	 */
	public void delWealthManagerUser(Integer id);
	
	/**
	 * 修改财富管家信息
	 */
	public void update(WealthManagerUser wealthManagerUser);
	
	/**
	 * 通过id查找财富管家对应关系
	 * @param id
	 * @return
	 */
	public WealthManagerUser findById(Integer id);
	/**
	 * 自定义查询没有财富管家用户列表
	 */
	public List<User> getUser(int start_num,int end_num);

	
	/**
	 * 根据用户姓名和用户手机号查询用户
	 * @param userName
	 * @param mobilePhone
	 * @return
	 */
	public List<User> getUser(String userName,String mobilePhone);
	
	
	public List<WealthManagerUser> getByWealthUserId(Integer id);
	
	
	public List<WealthManagerUser> findByUserId(Integer userId);
	
	public List<Integer> findByWealthManagerId(Integer id);
	
	public List<Integer> getByWealthManagerId(Integer id);
	
	public Integer getByWuId(Integer id);
	
}
