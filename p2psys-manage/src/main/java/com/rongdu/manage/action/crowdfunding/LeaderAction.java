package com.rongdu.manage.action.crowdfunding;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.LeaderModel;
import com.rongdu.p2psys.crowdfunding.service.LeaderFactoryService;
import com.rongdu.p2psys.crowdfunding.service.LeaderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

public class LeaderAction extends BaseAction<LeaderModel> implements ModelDriven<LeaderModel>{

	@Resource
	private LeaderService leaderService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private LeaderFactoryService leaderFactoryService;
	
	private Map<String,Object> data;
	
	/**
	 * 领投人页面
	 * @return
	 */
	@Action("/modules/crowdfunding/leader/leader")
	public String leader(){
		System.out.println("---"+paramLong("id"));
		request.setAttribute("id",paramLong("id"));
		return "leader";
	}
	
	/**
	 * 项目领投人列表
	 * @throws IOException
	 */
	@Action("/modules/crowdfunding/leader/data")
	public void leaderData() throws IOException{
		Long projectId = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		model.setProject(projectBaseinfo);
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		
		PageDataList<LeaderModel> list = leaderService.getLeaderList(model, pageNumber, pageSize);
		
		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 不带分页产品数据
	 * @throws IOException
	 */
	@Action(value = "/cf/leader/data")
	public void projectLeaders() throws IOException{
		Long projectId = paramLong("id");
		
		List<Leader> list = leaderService.getByProjectId(projectId);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 项目设定领投人（申请中选择）
	 * @throws IOException
	 */
	@Action("/cf/leader/change")
	public void updateData() throws IOException{
		Long projectId = paramLong("projectId");
		Integer id = paramInt("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		Leader leader = leaderService.getById(id);
		projectBaseinfo.setLeader(leader);
		projectBaseinfoService.update(projectBaseinfo);
		leader.setStatus(1);
		leaderService.setLeader(leader);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG, "设定成功");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 设定领投人
	 * @throws IOException
	 */
	@Action("/cf/leader/update")
	public void updateLeader() throws IOException{
		Long projectId = paramLong("projectId");
		Integer factoryId = paramInt("factoryId");
		LeaderFactory leaderFactory = leaderFactoryService.find(factoryId);
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		
		Leader leader = new Leader();
		leader.setInfo(paramString("info"));
		leader.setLeaderFactory(leaderFactory);
		leader.setName(leaderFactory.getName());
		leader.setPicPath(leaderFactory.getPicPath());
		leader.setProject(projectBaseinfo);
		leader.setReason(paramString("reason"));
		leader.setStatus(1);
		leader.setUser(null);
		leader.setAddTime(new Date());
		
		leader = leaderService.saveLeader(leader);
		
		projectBaseinfo.setLeader(leader);
		projectBaseinfoService.update(projectBaseinfo);
		leader.setStatus(1);
		leaderService.setLeader(leader);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG, "设定成功");
		printJson(getStringOfJpaObj(data));
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
