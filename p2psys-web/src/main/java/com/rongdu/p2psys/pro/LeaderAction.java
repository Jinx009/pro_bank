package com.rongdu.p2psys.pro;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.LeaderModel;
import com.rongdu.p2psys.crowdfunding.service.LeaderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

public class LeaderAction extends BaseAction<LeaderModel> implements ModelDriven<LeaderModel>{
	
	private Map<String,Object> data;
	
	@Resource
	private LeaderService leaderService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;

	/**
	 * 产品领投人列表
	 * @return
	 */
	@Action(value = "/manage/code/leader",results = {@Result(name = "leader", type = "ftl", location = "/nb/pro/leader.html")})
	public String leader(){
		request.setAttribute("id",paramLong("id"));
		return "leader";
	}
	
	/**
	 * 项目领投人数据
	 * @throws IOException
	 */
	@Action(value = "/manage/code/leaderData")
	public void list() throws IOException{
		data = new HashMap<String, Object>();
		List<Leader> list = leaderService.getByProjectId(paramLong("id"));
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(paramLong("id"));
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,list);
		data.put("pro",projectBaseinfo);
		printWebJson(getStringOfJpaObj(data));
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
