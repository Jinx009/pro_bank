package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.crowdfunding.dao.LeaderDao;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.LeaderModel;
import com.rongdu.p2psys.crowdfunding.service.LeaderService;
import com.rongdu.p2psys.user.domain.User;

@Service("leaderService")
public class LeaderServiceImpl implements LeaderService {

	@Resource
	private LeaderDao leaderDao;
	
	public List<Leader> getByProjectId(Long projectId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("  FROM Leader where project.id = ");
		buffer.append(projectId);
		return leaderDao.getByHql(buffer.toString());
	}

	public Leader saveLeader(Leader leader) {
		return leaderDao.save(leader);
	}

	public void updateLeader(Leader leader) {
		leaderDao.update(leader);
	}

	public List<Leader> getByProjectAndUserId(Long userId, Long projectId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM Leader where project.id =  ");
		buffer.append(projectId);
		buffer.append(" and user.id= ");
		buffer.append(userId);
		return leaderDao.getByHql(buffer.toString());
	}

	public PageDataList<LeaderModel> getLeaderList(LeaderModel model,int pageNumber, int pageSize) {
		return leaderDao.getList(model,pageNumber,pageSize);
	}

	public Leader getById(Integer id) {
		return leaderDao.find(id);
	}

	public void setLeader(Leader leader) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("  FROM Leader where project.id = ");
		buffer.append(leader.getProject().getId());
		List<Leader> list =  leaderDao.getByHql(buffer.toString());
		for(int i = 0;i<list.size();i++){
			Leader leader2 = list.get(i);
			leader2.setStatus(0);
			leaderDao.update(leader2);
		}
		leader.setStatus(1);
		leaderDao.update(leader);
	}

	public List<Leader> getByUserId(long userId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM Leader WHERE user.userId =");
		buffer.append(userId);
		buffer.append("  ORDER BY addTime DESC  ");
		return leaderDao.getByHql(buffer.toString());
	}

	public List<ProjectBaseinfo> getLeaderList(User user) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM Leader WHERE user.userId =");
		buffer.append(user.getUserId());
		buffer.append(" AND status =1  ORDER BY addTime DESC  ");
		List<Leader> list = leaderDao.getByHql(buffer.toString());
		List<ProjectBaseinfo> result = null;
		if(null!=list&&!list.isEmpty()){
			result = new ArrayList<ProjectBaseinfo>();
			for(int i = 0;i<list.size();i++){
				ProjectBaseinfo projectBaseinfo = list.get(i).getProject();
				result.add(projectBaseinfo);
			}
		}
		return result;
	}

	/**
	 * 获取项目现有领投人
	 */
	public Leader getRealLeader(long projectId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM Leader WHERE project.id =");
		buffer.append(projectId);
		buffer.append(" AND status  = 1 ");
		List<Leader> list = leaderDao.getByHql(buffer.toString());
		if(null!=list&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	
}
