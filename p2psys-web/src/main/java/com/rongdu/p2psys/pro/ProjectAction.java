package com.rongdu.p2psys.pro;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.domain.ProjectLog;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;
import com.rongdu.p2psys.crowdfunding.model.ProjectLogModel;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.crowdfunding.service.ProjectLogService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 后台项目编辑相关
 * @author Jinx
 *
 */
public class ProjectAction extends BaseAction<ProjectBaseinfoModel> implements ModelDriven<ProjectBaseinfoModel>{

	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private ProjectLogService projectLogService;
	
	private Map<String,Object> data;

	/**
	 * 创建新项目--页面
	 * @return
	 */
	@Action(value = "/manage/code/create",results = {@Result(name = "create", type = "ftl", location = "/nb/pro/create.html")})
	public String create(){
		return "create";
	}
	
	/**
	 * 需要审核的项目
	 * @return
	 */
	@Action(value="/manage/code/wait",results={@Result(name="wait",type="ftl",location="/nb/pro/wait.html")})
	public String waitExamine(){
		return "wait";
	}
	
	/**
	 * 提交审核中的项目
	 * @return
	 */
	@Action(value="/manage/code/auditing",results={@Result(name="auditing",type="ftl",location="/nb/pro/auditing.html")})
	public String auditing(){
		return "auditing";
	}
	
	/**
	 * 通知中心页面
	 * @return
	 */
	@Action(value = "/manage/code/notice",results={@Result(name="notice",type="ftl",location="/nb/pro/notice.html")})
	public String notice(){
		return "notice";
	}
	
	/**
	 * 我的梦想
	 * @return
	 */
	@Action(value = "/manage/code/myDream",results={@Result(name="my-dream",type="ftl",location="/nb/pro/my-dream.html")})
	public String myDream(){
		return "my-dream";
	}
	
	/**
	 * 项目详情信息资料
	 * @throws IOException
	 */
	@Action(value="/manage/code/projectDetail")
	public void projectModel() throws IOException{
		Long projectId = paramLong("id");
		ProjectBaseinfoModel projectBaseinfoModel = projectBaseinfoService.find(projectId);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.MSG,"项目详情");
		data.put(ConstantUtil.DATA,projectBaseinfoModel);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 项目基本资料添加
	 * @throws IOException 
	 */
	@Action(value="/manage/code/addPro")
	public void addProject() throws IOException{
		ProjectBaseinfo projectBaseinfo= model.prototype();
		projectBaseinfo.setAddTime(new Date());
		projectBaseinfo.setAccount(0.00);
		projectBaseinfo.setIsExceedAccept(1);
		projectBaseinfo.setIsExceedAcceptInvestor(1);
		projectBaseinfo.setAddIp(Global.getIP());
		projectBaseinfo.setAttentionNum(0);
		projectBaseinfo.setIsRecommend(0);
		projectBaseinfo.setIsShowPc(0);
		projectBaseinfo.setIsShowWechat(0);
		projectBaseinfo.setLikeNum(0);
		projectBaseinfo.setManageFee(0.00);
		projectBaseinfo.setMaxInvestor(0);
		projectBaseinfo.setPcIndexStatus(0);
		projectBaseinfo.setRightDueTime(new Date());
		projectBaseinfo.setStatus(-1);
		projectBaseinfo.setWechatIndexStatus(0);
		projectBaseinfo.setMinInvestor(0);
		projectBaseinfo.setAcceptanceCode(0l);
		Long id = projectBaseinfoService.saveProjectBaseinfo(projectBaseinfo);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.MSG,"保存项目");
		data.put(ConstantUtil.DATA,id);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 项目基本信息分步修改
	 */
	@Action(value="/manage/code/proEdit")
	public void editPro(){
		Long projectId = paramLong("projectId");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		ProjectBaseinfo projectBaseinfo2 = model.prototype();
		projectBaseinfo2.setAddTime(projectBaseinfo.getAddTime());
		projectBaseinfo2.setIsShowPc(projectBaseinfo.getIsShowPc());
		
	}
	
	/**
	 * 待提交审核或者审核驳回
	 * @throws IOException
	 */
	@Action(value="/manage/code/waitList")
	public void waitData() throws IOException{
		User user = getNBSessionUser();
		List<ProjectBaseinfoModel> list = projectBaseinfoService.getWaitList(user);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,list);
		data.put(ConstantUtil.MSG,"需要审核的项目！");
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 提交审核中的数据
	 * @throws IOException
	 */
	@Action(value="/manage/code/auditingList")
	public void auditingData() throws IOException{
		User user = getNBSessionUser();
		List<ProjectBaseinfoModel> list = projectBaseinfoService.getAuditing(user);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 项目提交审核
	 * @throws IOException
	 */
	@Action(value="/manage/code/subProject")
	public void submit() throws IOException{
		Long id = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(id);
		projectBaseinfo.setStatus(0);
		projectBaseinfoService.update(projectBaseinfo);
		ProjectLog projectLog = new ProjectLog();
		projectLog.setAddTime(new Date());
		projectLog.setContent("项目：【"+projectBaseinfo.getProjectName()+"】提交审核中");
		projectLog.setProjectId(id);
		projectLog.setType(0);
		projectLogService.save(projectLog);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,"提交审核成功！");
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 通知中心
	 * @throws IOException
	 */
	@Action(value="/manage/code/noticeData")
	public void noticeData() throws IOException{
		User user = getNBSessionUser();
		List<ProjectLogModel> list = projectLogService.getNotices(user);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 正在募集或者预热中
	 * @throws IOException
	 */
	@Action(value = "/manage/code/myDreamData")
	public void myDreamData() throws IOException{
		User user = getNBSessionUser();
		List<ProjectBaseinfo> list = projectBaseinfoService.getMyDream(user);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 撤销项目
	 * @throws IOException
	 */
	@Action(value = "/manage/code/cacleProject")
	public void cacle() throws IOException{
		data = new HashMap<String, Object>();
		Long id = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(id);
		projectBaseinfoService.cancelProject(id);
		ProjectLog projectLog = new ProjectLog();
		projectLog.setAddTime(new Date());
		projectLog.setContent("项目：【"+projectBaseinfo.getProjectName()+"】撤销募集。");
		projectLog.setProjectId(id);
		projectLog.setType(6);
		projectLogService.save(projectLog);
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,"撤销成功！");
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 募集成功设置
	 * @throws IOException
	 */
	@Action(value = "/manage/code/passProject")
	public void pass() throws IOException{
		data = new HashMap<String, Object>();
		Long id = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(id);
		projectBaseinfo.setStatus(3);
		projectBaseinfoService.update(projectBaseinfo);
		ProjectLog projectLog = new ProjectLog();
		projectLog.setAddTime(new Date());
		projectLog.setContent("项目：【"+projectBaseinfo.getProjectName()+"】募集成功！");
		projectLog.setProjectId(id);
		projectLog.setType(7);
		projectLogService.save(projectLog);
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,"募集成功！");
		printWebJson(getStringOfJpaObj(data));
	}
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	} 
}
