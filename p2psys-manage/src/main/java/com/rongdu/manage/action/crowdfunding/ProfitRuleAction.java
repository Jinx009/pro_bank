package com.rongdu.manage.action.crowdfunding;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.ProfitRuleModel;
import com.rongdu.p2psys.crowdfunding.service.MaterialsService;
import com.rongdu.p2psys.crowdfunding.service.ProfitRuleService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;

/**
 * 权益规则
 * @author Jinx
 *
 */
public class ProfitRuleAction extends BaseAction<ProfitRuleModel> implements  ModelDriven<ProfitRuleModel>{

	private Map<String,Object> data;
	
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private ProfitRuleService profitRuleService;
	@Resource
	private MaterialsService materialsService;
	
	/**
	 * 添加权益规则
	 * @throws IOException 
	 */
	@Action("/cf/addProfitRule")
	public void doAddProfitRule() throws IOException{
		Long projectId = paramLong("projectId");
		ProfitRule profitRule = model.prototype();
		String path = imgUpload();
		if(StringUtil.isNotBlank(path)){
			profitRule.setPicPath(path);
		}else{
			profitRule.setPicPath("");
		}
		profitRule.setContent(paramString("content"));
		profitRule.setMoney(paramDouble("money"));
		profitRule.setProjectId(projectId);
		profitRule.setName(paramString("name"));
		
		profitRuleService.saveObject(profitRule);
	
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"添加成功!");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 编辑权益规则
	 * @throws IOException 
	 */
	@Action("/cf/editProfitRule")
	public void doEditProfitRule() throws IOException{
		ProfitRule profitRule = profitRuleService.find(paramInt("realId"));
		String realPic = paramString("realPic");
		String path = imgUpload();
		profitRule.setPicPath(realPic);
		if(StringUtil.isNotBlank(path)){
			profitRule.setPicPath(path);
		}else{
			profitRule.setPicPath("");
		}
		profitRule.setContent(paramString("realRontent"));
		profitRule.setMoney(paramDouble("realMoney"));
		profitRule.setName(paramString("realName"));
		
		profitRuleService.saveObject(profitRule);
	
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"修改成功!");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 项目权益 页面
	 * @return
	 */
	@Action(value = "/modules/crowdfunding/profitRule/profitRule")
	public String profitRule(){
		Long projectId = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		
		request.setAttribute("projectName",projectBaseinfo.getProjectName());
		request.setAttribute("projectId",projectId);
		request.setAttribute("crowdAccount", projectBaseinfo.getWannaAccount());
		return "profitRule";
	}
	
	/**
	 * 删除一条权益规则
	 * @throws IOException
	 */
	@Action("/cf/deleteProfitRule")
	public void doDeleteProfitRule() throws IOException{
		Integer profitRuleId = paramInt("id");
		profitRuleService.delete(profitRuleId);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"删除成功!");
		
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 项目权益列表
	 * @throws IOException
	 */
	@Action(value = "/cf/profitRule/data")
	public void profitRuleData() throws IOException{
		Long projectId = paramLong("projectId");
		List<ProfitRule> list = profitRuleService.findByProjectId(projectId);
		
		data = new HashMap<String, Object>();
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
