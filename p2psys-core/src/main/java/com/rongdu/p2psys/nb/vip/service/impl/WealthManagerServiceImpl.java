package com.rongdu.p2psys.nb.vip.service.impl;



import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.vip.dao.WealthManagerDao;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;
import com.rongdu.p2psys.nb.vip.model.WealthManagerModel;
import com.rongdu.p2psys.nb.vip.service.WealthManagerService;
import com.rongdu.p2psys.user.domain.User;

@Service("wealthManagerService")
public class WealthManagerServiceImpl implements WealthManagerService{
	
	@Resource
	private WealthManagerDao wealthManagerDao;
	
	@Override
	public PageDataList<WealthManagerModel> dataList(WealthManager model,
			int pageNumber, int pageSize) {
		return wealthManagerDao.dataList(model, pageNumber, pageSize);
	}

	@Override
	public void delWealthManager(Integer id) {
		wealthManagerDao.delete(id);
		
	}

	@Override
	public void update(WealthManager wealthManager) {
		wealthManagerDao.updateWealthManager(wealthManager);
		
	}

	@Override
	public void saveObject(WealthManager wealthManager) {
		wealthManagerDao.save(wealthManager);
		
	}

	@Override
	public WealthManager findById(Integer wealth_manager_id) {
		return wealthManagerDao.find(wealth_manager_id);
	}


	@Override
	public WealthManager find(String name) {
		return wealthManagerDao.find(name);
	}

	@Override
	public List<WealthManager> getList()
	{
		return wealthManagerDao.findAll();
	}

	@Override
	public List<User> getAllUserList(Integer id) {
		String sql = "SELECT * FROM rd_user WHERE real_name!='' AND user_id IN(SELECT user_id FROM ehb_wealth_user WHERE id IN(SELECT wealth_user_id FROM ehb_zc_wealth_manager_user WHERE wealth_manager_id=(SELECT id FROM ehb_zc_wealth_manager WHERE id="+id+")))";
		List<User> users = wealthManagerDao.getAllUserByUserid(sql);
		return users;
	}

	@Override
	public List<WealthManager> findNotIn(Integer id) {
		String sql = "SELECT * FROM ehb_zc_wealth_manager WHERE id !="+id;
		return wealthManagerDao.findNotIn(sql);
	}

	
	
}
