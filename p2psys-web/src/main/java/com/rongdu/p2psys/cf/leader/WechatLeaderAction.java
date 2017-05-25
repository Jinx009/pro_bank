package com.rongdu.p2psys.cf.leader;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.LeaderModel;
import com.rongdu.p2psys.crowdfunding.service.AttentionListService;
import com.rongdu.p2psys.crowdfunding.service.LeaderFactoryService;
import com.rongdu.p2psys.crowdfunding.service.LeaderService;
import com.rongdu.p2psys.crowdfunding.service.ProfitRuleService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 微信端项目领投人
 * @author Jinx
 *
 */
public class WechatLeaderAction extends BaseAction<LeaderModel> implements ModelDriven<LeaderModel>{
	
	private Map<String,Object> data;
	
	@Resource
	private LeaderService leaderService;
	@Resource
	private UserService theUserService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private LeaderFactoryService leaderFactoryService;
	@Resource
	private UserCacheService theUserCacheService;
	@Resource
	private ProfitRuleService profitRuleService;
	@Resource
	private AttentionListService attentionListService;
	
	
	
	/**
	 * 申请成为某项产品的领投人 -- 页面
	 * @return
	 */
	@Action(value="/cf/wechat/user/beLeader",results={@Result(name="be-leader",type="ftl",location="/nb/cf/wechat/leader/be-leader.html")})
	public String beLeader(){
		Long projectId = paramLong("projectId");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		request.setAttribute("projectBaseinfo",projectBaseinfo);
		return "be-leader";
	}
	
	/**
	 * 申请成为单个项目的领投人  -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/beLeaderData")
	public void beLeaderData() throws IOException{
		data = new HashMap<String, Object>();
		User user = getNBSessionUser();
		Long projectId = paramLong("id");
		List<Leader> list = leaderService.getByProjectAndUserId(user.getUserId(),projectId);
		Leader leader = null;
		data.put("size",0);
		if(null!=list&&!list.isEmpty()){
			leader = list.get(0);
			data.put("size",1);
			if(1==leader.getStatus()){
				data.put("size",-1);
			}
		}
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"申请成为项目领投人相关数据");
		data.put(ConstantUtil.DATA,leader);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 申请成功某个项目数据提交 -- 操作
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/user/saveLeaderData")
	public void saveLeaderData() throws IOException{
		Long projectId = paramLong("projectId");
		Integer leaderId = paramInt("leaderId");
		String name = paramString("name");
		String info = paramString("info");
		String reason = paramString("reason");
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"申请成为领投人数据相关");
		if(0!=leaderId){
			Leader leader = leaderService.getById(leaderId);
			leader.setName(name);
			leader.setInfo(info);
			leader.setReason(reason);
			leaderService.updateLeader(leader);
			data.put(ConstantUtil.DATA,"申请成功！");
		}else{
			ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
			User user = getNBSessionUser();
			LeaderFactory leaderFactory = leaderFactoryService.findByUserId(user.getUserId());
			Leader leader = new Leader();
			leader.setAddTime(new Date());
			leader.setInfo(info);
			leader.setName(name);
			leader.setPicPath(null);
			leader.setProject(projectBaseinfo);
			leader.setReason(reason);
			leader.setStatus(0);
			leader.setLeaderFactory(leaderFactory);
			leader.setUser(user);
			leaderService.saveLeader(leader);
			data.put(ConstantUtil.DATA,"保存成功！");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
