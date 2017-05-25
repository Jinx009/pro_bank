package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;

public interface ProjectBaseinfoDao extends BaseDao<ProjectBaseinfo>
{

	/**
	 * 获取新增未审核和审核失败的众筹记录
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return PageDataList
	 */
	PageDataList<ProjectBaseinfoModel> getListOfNewAdded(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	/**
	 * 获取待审核的众筹记录
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return PageDataList
	 */
	PageDataList<ProjectBaseinfoModel> getListOfWaitingForApprove(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	/**
	 * 保存ProjectBaseinfo
	 * 
	 * @param baseinfo
	 * @return int
	 */
	long saveProjectBaseinfo(ProjectBaseinfo baseinfo);

	// TODO

	public PageDataList<ProjectBaseinfoModel> dataList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize, int type);

	public PageDataList<ProjectBaseinfoModel> projectFailedList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	public PageDataList<ProjectBaseinfoModel> projectFullList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	public void updataBySql(String sql);

	/**
	 * 获取所有的到期项目列表
	 */
	public List<ProjectBaseinfo> findDueProject();
	
	public List<ProjectBaseinfo> getByHql(String hql);

	public PageDataList<ProjectBaseinfoModel> getCfFunction(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	PageDataList<ProjectBaseinfoModel> getSuccessList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize);

	PageDataList<ProjectBaseinfoModel> getFailList(ProjectBaseinfoModel model,
			int pageNumber, int pageSize);

	PageDataList<ProjectBaseinfoModel> getCacleList(ProjectBaseinfoModel model,
			int pageNumber, int pageSize);
}
