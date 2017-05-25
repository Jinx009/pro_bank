package com.rongdu.p2psys.nb.vip.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.vip.dao.WealthManagerUserDao;
import com.rongdu.p2psys.nb.vip.domain.WealthManagerUser;
import com.rongdu.p2psys.nb.vip.model.WealthManagerUserModel;
import com.rongdu.p2psys.nb.vip.service.WealthManagerUserService;
import com.rongdu.p2psys.user.domain.User;

@Service("WealthManagerService")
public class WealthManagerUserServiceImpl implements WealthManagerUserService{

	@Resource
	private WealthManagerUserDao wealthManagerUserDao;
	@Override
	public PageDataList<WealthManagerUserModel> dataList(
			WealthManagerUser model, int pageNumber, int pageSize) {
		return wealthManagerUserDao.dataList(model, pageNumber, pageSize);
	}

	@Override
	public void saveObject(WealthManagerUser wealthManagerUser) {
		wealthManagerUserDao.save(wealthManagerUser);
		
	}

	@Override
	public void delWealthManagerUser(Integer id) {
		wealthManagerUserDao.delete(id);
		
	}

	@Override
	public void update(WealthManagerUser wealthManagerUser) {
		wealthManagerUserDao.update(wealthManagerUser);
		
	}

	@Override
	public WealthManagerUser findById(Integer id) {
		
		return wealthManagerUserDao.find(id);
	}

	@Override
	public List<User> getUser(int start_num, int end_num)
	{
		/*String sql = "SELECT * FROM rd_user where real_name!='' AND user_id  not in(select user_id FROM ehb_wealth_user) order by user_id desc LIMIT "+start_num+","+(end_num-start_num)+"";*/
		String sql = "SELECT * FROM rd_user where user_id  not in(select user_id FROM ehb_wealth_user) order by user_id  LIMIT "+(start_num-1)+","+(end_num-start_num)+"";
		
		List<User> list = wealthManagerUserDao.getUserBySql(sql);
		
		return list;
	}

	@Override
	public List<WealthManagerUser> getByWealthUserId(Integer id)
	{
		String sql = " FROM WealthManagerUser where wealthUserId = "+id+" ";
		
		List<WealthManagerUser> list = wealthManagerUserDao.getByWealthUserId(sql);
		
		return list;
	}


	@Override
	public List<WealthManagerUser> findByUserId(Integer userId) {
		String sql = "SELECT * FROM ehb_zc_wealth_manager_user WHERE wealth_user_id="+userId;
		List<WealthManagerUser> list = wealthManagerUserDao.findByUserId(sql);
		return list;
	}

	@Override
	public List<Integer> findByWealthManagerId(Integer id) {
		String sql = "SELECT * FROM ehb_zc_wealth_manager_user WHERE wealth_manager_id="+id;
		List<WealthManagerUser> list = wealthManagerUserDao.findByWealthManagerUserId(sql);
		
		List<Integer> wealthUserIds = new ArrayList();
		for(int i = 0;i<list.size();i++){
			wealthUserIds.add(list.get(i).getWealthUserId());
		}
		return wealthUserIds;
	}

	@Override
	public List<Integer> getByWealthManagerId(Integer id) {
		String sql = "SELECT * FROM ehb_zc_wealth_manager_user WHERE wealth_manager_id="+id;
		List<WealthManagerUser> list = wealthManagerUserDao.findByWealthManagerUserId(sql);
		
		List<Integer> wealthUserIds = new ArrayList();
		for(int i = 0;i<list.size();i++){
			wealthUserIds.add(list.get(i).getId());
		}
		return wealthUserIds;
	}

	@Override
	public Integer getByWuId(Integer id) {
		String sql = "SELECT * FROM ehb_zc_wealth_manager_user WHERE wealth_user_id="+id;
		List<WealthManagerUser> list = wealthManagerUserDao.findByWealthManagerUserId(sql);
		return list.get(0).getId();
	}

	@Override
	public List<User> getUser(String userName, String mobilePhone) {
		
		
		String sql = "SELECT * FROM rd_user WHERE user_id  NOT IN(SELECT user_id FROM ehb_wealth_user) AND (user_name='"+mobilePhone+"'  OR real_name='"+userName+"')";
		
		List<User> user = wealthManagerUserDao.getUserList(sql);

		return user;
	}



}
