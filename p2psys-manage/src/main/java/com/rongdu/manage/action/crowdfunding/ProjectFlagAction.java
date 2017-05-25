package com.rongdu.manage.action.crowdfunding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.Flag;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.domain.ProjectFlag;
import com.rongdu.p2psys.crowdfunding.service.FlagService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.crowdfunding.service.ProjectFlagService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
/**
 * 项目产品标签 与 产品标签管理
 * @author Jinx
 *
 */
public class ProjectFlagAction extends BaseAction<ProjectFlag> implements ModelDriven<ProjectFlag>{

	@Resource
	private ProjectFlagService projectFlagService;
	@Resource
	private FlagService flagService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	
	private Map<String,Object> data;

	@Action("/modules/crowdfunding/flag/projectFlag")
	public String projectFlag(){
		request.setAttribute("id",paramString("id"));
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(paramLong("id"));
		request.setAttribute("name",projectBaseinfo.getProjectName());
		return "projectFlag";
	}
	
	@Action("/modules/crowdfunding/flag/flag")
	public String flag(){
		return "flag";
	}
	/**
	 * 产品标签数据
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/flag/flagData")
	public void flagData() throws Exception {
		List<Flag> list = flagService.findAll();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 产品标签添加
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/flag/flagAdd")
	public void flagAdd() throws Exception {
		String name = paramString("name");
		
		Flag flag = new Flag();
		flag.setName(name);
		flagService.saveFlag(flag);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"添加成功");
		
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 产品标签删除
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/flag/flagDelete")
	public void flagDelete() throws Exception {
		int id = paramInt("id");
		flagService.delete(id);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"删除成功");
		
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 项目产品标签数据
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/flag/projectFlagData")
	public void projectFlagData() throws Exception {
		int projectId = paramInt("projectId");
		
		List<ProjectFlag> list = projectFlagService.findByProject(projectId);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 项目产品标签添加
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/flag/projectFlagAdd")
	public void projectFlagAdd() throws Exception {
		int flagId = paramInt("flagId");
		int projectId = paramInt("projectId");
		
		Flag flag = flagService.findById(flagId);
		ProjectFlag projectFlag = new ProjectFlag();
		projectFlag.setProjectId(projectId);
		projectFlag.setFlag(flag);
		
		projectFlagService.save(projectFlag);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"添加成功");
		
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 项目产品标签删除
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/flag/projectFlagDelete")
	public void projectFlagDelete() throws Exception {
		int id = paramInt("id");
		projectFlagService.delete(id);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"删除成功");
		
		printJson(getStringOfJpaObj(data));
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
