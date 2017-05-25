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
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.ProfitRuleModel;
import com.rongdu.p2psys.crowdfunding.service.ProfitRuleService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

/**
 * 后台收益规则
 * @author Jinx
 *
 */
public class ProfitRuleAction extends BaseAction<ProfitRuleModel> implements ModelDriven<ProfitRuleModel>{

	@Resource
	private ProfitRuleService profitRuleService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	
	private Map<String,Object> data;

	/**
	 * 根据项目id获取项目收益规则
	 * @throws IOException
	 */
	@Action(value = "/manage/code/profitRules")
	public void profitRule() throws IOException{
		Long projectId = paramLong("projectId");
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.MSG,"获取项目收益规则");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		List<ProfitRuleModel> list = profitRuleService.findByProjectIdforWechat(projectId);
		if(null==projectBaseinfo){
			data.put(ConstantUtil.CODE,400);
			data.put(ConstantUtil.DATA,"该项目不存在！");
		}else{
			data.put(ConstantUtil.CODE,200);
			data.put(ConstantUtil.DATA,list);
			data.put("projectName",projectBaseinfo.getProjectName());
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 新建权益
	 * @throws IOException 
	 */
	@Action(value = "/manage/code/saveProfit")
	public void save() throws IOException{
		Long projectId = paramLong("id");
		Integer maxInvestor = paramInt("maxInvestor");
		Double money = paramDouble("money");
		String content = paramString("content");
		String name = paramString("name");
		Integer isAccept = paramInt("isAccept");
		data = new HashMap<String, Object>();
		boolean status = true;
		String errorMsg = "";
		List<ProfitRule> list = profitRuleService.findByProjectId(projectId);
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		if(null!=list&&!list.isEmpty()){
			Double double1 = new Double(0.00);
			for(ProfitRule profitRule:list){
				if(double1.equals(profitRule.getMoney())){
					status = false;
					errorMsg = "已经存在支持金额为0的条目不能添加更多权益！";
				}
				if(money.equals(profitRule.getMoney())){
					status = false;
					errorMsg = "已经存在相同支持金额的分段！";
				}
			}
		}
		if(status){
			if(2<=projectBaseinfo.getStatus()){
				data.put(ConstantUtil.CODE,400);
				data.put(ConstantUtil.DATA,"该项目已经上线过，不支持修改！");
			}else{
				ProfitRule profitRule = new ProfitRule();
				profitRule.setContent(content);
				profitRule.setIsAccept(isAccept);
				profitRule.setMaxInvestor(maxInvestor);
				profitRule.setMoney(money);
				profitRule.setProjectId(projectId);
				profitRule.setName(name);
				profitRuleService.saveObject(profitRule);
				data.put(ConstantUtil.CODE,200);
				data.put(ConstantUtil.DATA,"保存成功！");
			}
		}else{
			data.put(ConstantUtil.CODE,400);
			data.put(ConstantUtil.DATA,errorMsg);
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 后台身份验证首页
	 * @return
	 */
	@Action(value = "/manage/code/profit",results = {@Result(name = "index", type = "ftl", location = "/nb/pro/profit.html")})
	public String manageIndex(){
		request.setAttribute("id",paramLong("id"));
		return "index";
	}
	
	/**
	 * 修改权益
	 * @throws IOException
	 */
	@Action(value="/manage/code/editProfit")
	public void edit() throws IOException{
		Integer id = paramInt("id");
		Integer maxInvestor = paramInt("maxInvestor");
		Double money = paramDouble("money");
		String content = paramString("content");
		String name = paramString("name");
		Integer isAccept = paramInt("isAccept");
		ProfitRule profitRule = profitRuleService.find(id);
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(profitRule.getProjectId());
		data = new HashMap<String, Object>();
		if(projectBaseinfo.getStatus()>=3){
			data.put(ConstantUtil.CODE,400);
			data.put(ConstantUtil.DATA,"已上线过的项目无法修改！");
		}else{
			profitRule.setIsAccept(isAccept);
			profitRule.setMoney(money);
			profitRule.setMaxInvestor(maxInvestor);
			profitRule.setName(name);
			profitRule.setContent(content);
			profitRuleService.update(profitRule);
			data.put(ConstantUtil.CODE,200);
			data.put(ConstantUtil.MSG,"修改权益");
			data.put(ConstantUtil.DATA,"修改成功！");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除权益
	 * @throws IOException
	 */
	@Action(value = "/manage/code/delProfit")
	public void delete() throws IOException{
		data = new HashMap<String, Object>();
		Integer id = paramInt("id");
		ProfitRule profitRule = profitRuleService.find(id);
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(profitRule.getProjectId());
		if(projectBaseinfo.getStatus()>=2){
			data.put(ConstantUtil.CODE,400);
			data.put(ConstantUtil.DATA,"已上线过的项目无法修改！");
		}else{
			profitRuleService.delete(id);
			data.put(ConstantUtil.CODE,200);
			data.put(ConstantUtil.DATA,"删除成功！");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
