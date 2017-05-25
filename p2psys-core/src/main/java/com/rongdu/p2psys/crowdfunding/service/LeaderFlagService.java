package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFlag;

public interface LeaderFlagService {

	public void saveList(LeaderFactory leaderFactory, String[] str);

	public List<LeaderFlag> getByFactory(int id);

	public void doDelete(Integer id);

}
