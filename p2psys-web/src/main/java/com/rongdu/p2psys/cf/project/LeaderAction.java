package com.rongdu.p2psys.cf.project;

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
import com.rongdu.p2psys.crowdfunding.service.LeaderFactoryService;
import com.rongdu.p2psys.crowdfunding.service.LeaderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;

/**
 * 领投人申请
 * @author Jinx
 *
 */
public class LeaderAction extends BaseAction<Leader> implements ModelDriven<Leader>{

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
	
	/**
	 * 购买数据
	 * @throws IOException 
	 */
	@Action(value = "/cf/user/myLeaderData")
	public void leaderListData() throws IOException{
		User user = getNBSessionUser();
		List<ProjectBaseinfo> list = leaderService.getLeaderList(user);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 我的领投
	 * @return
	 */
	@Action(value = "/cf/user/myLeader",results={@Result(name="myLeader",type="ftl",location="/nb/cf/leaderFactory/my-leader.html")})
	public String myLeader(){
		return "myLeader";
	}
	
	/**
	 * 保存申请的领投人
	 * @throws IOException
	 */
	@Action(value = "/cf/user/saveLeader")
	public void saveLeader() throws IOException{
		User user = getNBSessionUser();
//		UserCache userCache = theUserCacheService.getById(user.getUserId());
		UserCache userCache = theUserCacheService.getByUserId(user.getUserId());
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		Long projectId = paramLong("id");
		String reason = paramString("reason");
		String info = paramString("info");
		String name = paramString("name");
		
		List<Leader> list = leaderService.getByProjectAndUserId(user.getUserId(),projectId);
		if(null!=list){
			data.put(ConstantUtil.ERRORMSG,"您已申请过此项目的领投人!");
		}else{
			ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
			LeaderFactory leaderFactory = leaderFactoryService.findByUserId(user.getUserId());
			Leader leader = new Leader();
			leader.setInfo(info);
			leader.setName(name);
			leader.setReason(reason);
			leader.setStatus(0);
			leader.setUser(user);
			leader.setAddTime(new Date());
			leader.setPicPath(userCache.getCardPositive());
			if(null!=leaderFactory){
				leader.setLeaderFactory(leaderFactory);
			}
			String fileName = imgUpload();
			if(StringUtil.isNotBlank(fileName)){
				leader.setPicPath(fileName);
			}
			leader.setProject(projectBaseinfo);
			leader.setPicPath(userCache.getCardPositive());
			leaderService.saveLeader(leader);
			
			data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			data.put(ConstantUtil.ERRORMSG,"提交成功!");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 申请成为某项产品的领投人
	 * @return
	 */
	@Action(value="/cf/user/beLeader",results={@Result(name="be",type="ftl",location="/nb/cf/pro/be.html")})
	public String beLeader(){
		Long projectId = paramLong("projectId");
		User user = getNBSessionUser();
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		List<Leader> list = leaderService.getByProjectAndUserId(user.getUserId(),projectId);
		if(null!=list){
			request.setAttribute("alreadyStatus",list.get(0).getId());
		}else{
			request.setAttribute("alreadyStatus",0);
		}
		request.setAttribute("projectBaseinfo",projectBaseinfo);
		return "be";
	}
	
	/**
	 * 领投产品数据
	 * @throws IOException
	 */
	@Action(value="/cf/leader/singleData")
	public void leaderData() throws IOException{
		User user = getNBSessionUser();
		List<Leader> list = leaderService.getByUserId(user.getUserId());
		data =new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 领投指定产品数据
	 * @throws IOException
	 */
	@Action(value="/cf/leader/singleProjectData")
	public void leaderProjectData() throws IOException{
		Long projectId = paramLong("projectId");
		User user = getNBSessionUser();
		List<Leader> list = leaderService.getByProjectAndUserId(user.getUserId(), projectId);
		data =new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
