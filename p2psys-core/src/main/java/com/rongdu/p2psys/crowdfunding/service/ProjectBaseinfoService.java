package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;
import com.rongdu.p2psys.user.domain.User;

public interface ProjectBaseinfoService {
	/**
	 * 获取新增未审核和审核失败的众筹记录
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return PageDataList
	 */
	public PageDataList<ProjectBaseinfoModel> getListOfNewAdded(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	/**
	 * 获取待审核的众筹记录列表
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return PageDataList
	 */
	public PageDataList<ProjectBaseinfoModel> getListOfWaitingForApprove(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	/**
	 * 根据ID获取众筹记录
	 * 
	 * @param id
	 * @return ProjectBaseinfo
	 */
	public ProjectBaseinfo getProjectBaseinfoById(Long id);

	/**
	 * 根据ID获取众筹模型
	 * 
	 * @param id
	 * @return ProjectBaseinfoModel
	 */
	public ProjectBaseinfoModel find(Long id);

	/**
	 * 保存众筹标
	 * 
	 * @param projectBaseinfo
	 * @return long
	 */
	public long saveProjectBaseinfo(ProjectBaseinfo baseinfo);

	/**
	 * 更新众筹标
	 * 
	 * @param projectBaseinfo
	 */
	public void update(ProjectBaseinfo projectBaseinfo);

	/**
	 * 审核通过
	 * 
	 * @param project
	 * @param operator
	 */
	public void verifyProject(ProjectBaseinfo project, Operator operator);


	/**
	 * 获取众筹对应的投资规则集合与收益规则集合
	 * 
	 * @param projectId
	 * @return
	 */

	public PageDataList<ProjectBaseinfoModel> list(ProjectBaseinfoModel model);

	public PageDataList<ProjectBaseinfoModel> dataList(ProjectBaseinfoModel model,
			int pageNumber, int pageSize, int type);

	public List<ProjectBaseinfoModel> getByTypePc(int type);

	public ProjectBaseinfoModel getById(Long projectId);

	public PageDataList<ProjectBaseinfoModel> getFunctionList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	/**
	 * 复审通过
	 * 
	 * @param id
	 */
	public void confirmProject(Long id);

	/**
	 * 撤回
	 * 
	 * @param id
	 */
	public void cancelProject(Long id);

	public List<ProjectBaseinfoModel> getPopularPc();

	public void changeType();

	public PageDataList<ProjectBaseinfoModel> successList(ProjectBaseinfoModel model,
			int pageNumber, int pageSize);

	public PageDataList<ProjectBaseinfoModel> cacleList(ProjectBaseinfoModel model,
			int pageNumber, int pageSize);
	
	public PageDataList<ProjectBaseinfoModel> failList(ProjectBaseinfoModel model,
			int pageNumber, int pageSize);
	
	/**
	 * 微信热销榜
	 * @return
	 */
	public List<ProjectBaseinfoModel> getPopularWechat();

	public List<ProjectBaseinfoModel> getWechatByType(Integer id);

	public List<ProjectBaseinfoModel> getWaitList(User user);

	public List<ProjectBaseinfoModel> getAuditing(User user);

	public List<ProjectBaseinfo> getMyDream(User user);
}
