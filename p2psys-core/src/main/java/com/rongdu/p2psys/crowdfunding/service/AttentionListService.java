package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.p2psys.crowdfunding.domain.AttentionList;
import com.rongdu.p2psys.crowdfunding.model.AttentionListModel;

public interface AttentionListService {
	
	public AttentionList findById(Integer id);
	
	public List<AttentionListModel>  getByUserId(Long userId);
	
	public List<AttentionList> getByProjectId(Long id);
	
	public void delete(Integer id);
	
	public AttentionList save(AttentionList attentionList);
	
	public void update(AttentionList attentionList);

	public List<AttentionList> getByDoubleId(Long projectId, long userId);
}
