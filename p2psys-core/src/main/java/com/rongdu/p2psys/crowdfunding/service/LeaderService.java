package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.LeaderModel;
import com.rongdu.p2psys.user.domain.User;

public interface LeaderService {

	public List<Leader> getByProjectId(Long projectId);

	public Leader saveLeader(Leader leader);

	public void updateLeader(Leader leader);
	
	public List<Leader> getByProjectAndUserId(Long userId,Long projectId);
	
	public PageDataList<LeaderModel> getLeaderList(LeaderModel model, int pageNumber, int pageSize);
	
	public Leader getById(Integer id);

	public void setLeader(Leader leader);

	public List<Leader> getByUserId(long userId);

	public List<ProjectBaseinfo> getLeaderList(User user);
	
	public Leader getRealLeader(long projectId);
	
}
