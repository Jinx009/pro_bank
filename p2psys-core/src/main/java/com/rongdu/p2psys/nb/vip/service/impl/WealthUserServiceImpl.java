package com.rongdu.p2psys.nb.vip.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.vip.dao.WealthUserDao;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;
import com.rongdu.p2psys.nb.vip.domain.WealthManagerUser;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;
import com.rongdu.p2psys.nb.vip.model.WealthManagerModel;
import com.rongdu.p2psys.nb.vip.model.WealthManagerUserModel;
import com.rongdu.p2psys.nb.vip.model.WealthUserModel;
import com.rongdu.p2psys.nb.vip.service.WealthManagerUserService;
import com.rongdu.p2psys.nb.vip.service.WealthUserService;
@Service("wealthUserService")
public class WealthUserServiceImpl implements WealthUserService
{

	@Resource
	private WealthUserDao wealthUserDao;
	
	@Resource
	private WealthManagerUserService wealthManagerUserService;
	
	@Override
	public PageDataList<WealthUserModel> getPage(WealthUser model,int pageNumber, int pageSize)
	{			
		
		
	
		PageDataList<WealthUserModel> list = wealthUserDao.dataList(model, pageNumber, pageSize);
		
		for(int i = 0;i<list.getList().size();i++)
		{							
			WealthUserModel singlemodel = list.getList().get(i);
			
			List<WealthManagerUser> this_list = 	wealthManagerUserService.getByWealthUserId(singlemodel.getId());
			
			List<String> listStr = new ArrayList<String>();
			
			for(int j = 0;j<this_list.size();j++)
			{
				listStr.add(this_list.get(j).getId()+"|"+this_list.get(j).getWealthManager().getName());
			}
			
			singlemodel.setList(listStr);
		}
		return list;
	}

	@Override
	public WealthUser saveObject(WealthUser wealthUser) {
		return wealthUserDao.save(wealthUser);
		
	}

	@Override
	public void delWealthUser(Integer id) {
		wealthUserDao.delete(id);
		
	}

	@Override
	public void update(WealthUser wealthUser) {
		wealthUserDao.update(wealthUser);
		
	}

	@Override
	public WealthUser findById(Integer id) {
		
		return wealthUserDao.find(id);
	}


	@Override
	public List<WealthUser> findByWealthUserId(Integer userId) {
		String sql = "select * from ehb_wealth_user where user_id="+userId;
		return wealthUserDao.findByWealthUserId(sql);
	}

	@Override
	public WealthUser getByWealthUserId(Integer userId) {
		
		String sql = " FROM WealthUser where user.userId = "+userId+" ";
		WealthUser wealthUser = wealthUserDao.getByWealthUserId(sql);
		return wealthUser;
		
	}

	@Override
	public WealthUser getWealthUser(Integer wealthManagerId, Integer userId) {
		String sql = "SELECT * FROM ehb_wealth_user WHERE id IN(SELECT wealth_user_id FROM ehb_zc_wealth_manager_user WHERE wealth_manager_id="+wealthManagerId+") AND user_id="+userId;
		WealthUser wealthUser = wealthUserDao.getWealthUser(sql);
		return wealthUser;
	}

}
