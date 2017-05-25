package com.rongdu.manage.action.crowdfunding;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;

/**
 * 众筹发标、审核
 */
public class ProjectBaseinfoAction extends
		BaseAction<ProjectBaseinfoModel> implements
		ModelDriven<ProjectBaseinfoModel> {

	@Resource
	private ProjectBaseinfoService projectBaseinfoService;

	private Map<String, Object> data;

	/**
	 * 众筹发标 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoCommon")
	public String projectInfoCommon() throws Exception {
		return "projectInfoCommon";
	}

	/**
	 * 众筹发标 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoList")
	public void projectInfoList() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProjectBaseinfoModel> list = projectBaseinfoService
				.getListOfNewAdded(model, pageNumber, pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 产品详情
	 * 
	 * @throws Exception
	 */
	@Action("/crowdfunding/getProjectInfo")
	public void getProject() throws Exception {
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.find(paramLong("id"));

		data = new HashMap<String, Object>();
		data.put("data", projectBaseinfo);
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 众筹发标 - 新增标 - 弹出页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoCommonAddPage")
	public String projectInfoCommonAddPage() throws Exception {
		return "projectInfoCommonAddPage";
	}

	/**
	 * 众筹发标 - 新增标 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoAdd")
	public void projectInfoAdd() throws Exception {
		ProjectBaseinfo baseinfo = model.prototype();
		baseinfo.setAddTime(new Date());
		baseinfo.setAddIp(InetAddress.getLocalHost().toString());
		baseinfo.setAccount(0.0);
		baseinfo.setLikeNum(0);
		baseinfo.setAttentionNum(0);
		projectBaseinfoService.saveProjectBaseinfo(baseinfo);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "保存成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 众筹发标 - 编辑标 - 弹出页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoCommonEditPage")
	public String projectInfoCommonEditPage() throws Exception {
		ProjectBaseinfo baseinfo = projectBaseinfoService.find(paramLong("id"));

		request.setAttribute("projectBaseinfo", baseinfo);
		return "projectInfoCommonEditPage";
	}

	/**
	 * 众筹发标 - 编辑标 - 确认
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoUpdate")
	public void projectInfoUpdate() throws Exception {
		ProjectBaseinfo baseinfo = model.prototype();
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(paramLong("id"));
		baseinfo.setId(paramLong("id"));
		baseinfo.setAddIp(InetAddress.getLocalHost().toString());
		baseinfo.setLikeNum(projectBaseinfo.getLikeNum());
		baseinfo.setAttentionNum(projectBaseinfo.getAttentionNum());
		baseinfo.setFinancing(projectBaseinfo.getFinancing());
		baseinfo.setWechatIndexStatus(projectBaseinfo.getWechatIndexStatus());
		baseinfo.setPcIndexStatus(projectBaseinfo.getPcIndexStatus());
		baseinfo.setLeader(projectBaseinfo.getLeader());
		baseinfo.setAddTime(projectBaseinfo.getAddTime());
		projectBaseinfoService.update(baseinfo);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 众筹发标 - 提交审核
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoAddSubmit")
	public void projectInfoAddSubmit() throws Exception {
		ProjectBaseinfo baseinfo = projectBaseinfoService
				.getProjectBaseinfoById(paramLong("id"));
		baseinfo.setStatus(ProjectBaseinfoModel.STATUS_WAITING_FOR_APPROVE);
		projectBaseinfoService.update(baseinfo);

		data = new HashMap<String, Object>();
		data.put("result", "success");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 众筹待审 - 页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfo")
	public String projectManager() throws Exception {
		return "projectInfo";
	}
	

	/**
	 * 众筹成功 - 众筹成功页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/successProject")
	public String projectSuccess() throws Exception {
		return "successProject";
	}
	

	/**
	 * 众筹撤回 - 撤回产品页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/cacleProject")
	public String projectCacle() throws Exception {
		return "cacleProject";
	}
	
	/**
	 * 众筹撤回- 已经撤回列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/cacleProjectList")
	public void projectInfoCacle() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProjectBaseinfoModel> list = projectBaseinfoService
				.cacleList(model, pageNumber, pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 众筹过期- 过期产品页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/failProject")
	public String projectFail() throws Exception {
		return "failProject";
	}
	
	/**
	 * 众筹过期- 过期产品列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/failProjectList")
	public void projectInfoFail() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProjectBaseinfoModel> list = projectBaseinfoService
				.failList(model, pageNumber, pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	
	/**
	 * 众筹成功 - 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/successProjectList")
	public void projectInfoSuccess() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProjectBaseinfoModel> list = projectBaseinfoService
				.successList(model, pageNumber, pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 众筹待审 - 需要初审列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoApprovingList")
	public void projectInfoApprovingList() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProjectBaseinfoModel> list = projectBaseinfoService
				.getListOfWaitingForApprove(model, pageNumber, pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 众筹待审 - 执行初审通过
	 *
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectInfoApprove")
	public void projectInfoApprove() throws Exception {
		ProjectBaseinfo baseinfo = projectBaseinfoService
				.getProjectBaseinfoById(paramLong("id"));
		baseinfo.setStatus(ProjectBaseinfoModel.STATUS_APPROVED);
		projectBaseinfoService.verifyProject(baseinfo, getOperator());

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "审核通过成功!");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 众筹待审 - 执行初审驳回
	 *
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectNoFunction")
	public void projectInfoNoFunction() throws Exception {
		ProjectBaseinfo baseinfo = projectBaseinfoService
				.getProjectBaseinfoById(paramLong("id"));
		baseinfo.setStatus(ProjectBaseinfoModel.STATUS_SAVED);
		projectBaseinfoService.verifyProject(baseinfo, getOperator());

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "审核驳回成功!");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 产品管理- 初审通过页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/cfFunction")
	public String projectInfoFunction() throws Exception {
		return "cfFunction";
	}

	/**
	 * 产品管理 - 列表（初审通过在线上可以显示的产品）
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/cfFunctionList")
	public void projectInfoFunctionList() throws Exception {
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<ProjectBaseinfoModel> list = projectBaseinfoService.getFunctionList(model, pageNumber, pageSize);

		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 产品管理 - 复审通过（完成产品）
	 *
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectConfirm")
	public void projectConfirm() throws Exception {
		projectBaseinfoService.confirmProject(paramLong("id"));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "复审通过成功!");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 产品管理 - 撤回
	 *
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/projectCancel")
	public void projectCancel() throws Exception {
		projectBaseinfoService.cancelProject(paramLong("id"));

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "撤回成功!");
		printJson(getStringOfJpaObj(data));
	}

}
