package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.model.LeaderFactoryModel;

public interface LeaderFactoryService {

	public LeaderFactory save(LeaderFactory leaderFactory);
	
	public void delete(Integer id);
	
	public List<LeaderFactory> getList();
	
	public LeaderFactory find(Integer id);

	public List<LeaderFactoryModel> getModelList();

	public void doUpdate(LeaderFactory leaderFactory);

	public LeaderFactory findByUserId(long userId);

	public LeaderFactory getByNameAndTel(String realname, String tel);
	
}
