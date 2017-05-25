package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.FlagDao;
import com.rongdu.p2psys.crowdfunding.dao.LeaderFlagDao;
import com.rongdu.p2psys.crowdfunding.domain.Flag;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFlag;
import com.rongdu.p2psys.crowdfunding.service.LeaderFlagService;

@Service("leaderFlagService")
public class LeaderFlagServiceImpl implements LeaderFlagService{

	@Resource
	private FlagDao flagDao;
	@Resource
	private LeaderFlagDao leaderFlagDao;
	
	/**
	 * 领投人添加产品标签
	 */
	public void saveList(LeaderFactory leaderFactory, String[] str) {
		if(null!=str&&str.length>0){
			for(int  i =0;i<str.length;i++){
				Flag flag = flagDao.find(Integer.valueOf(str[i]));
				LeaderFlag leaderFlag = new LeaderFlag();
				leaderFlag.setFlag(flag);
				leaderFlag.setLeaderFactory(leaderFactory);
				leaderFlagDao.save(leaderFlag);
			}
		}
	}

	public List<LeaderFlag> getByFactory(int id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM LeaderFlag WHERE leaderFactory.id =");
		buffer.append(id);
		return leaderFlagDao.getByHql(buffer.toString());
	}

	public void doDelete(Integer id) {
		leaderFlagDao.delete(id);
	}

}
