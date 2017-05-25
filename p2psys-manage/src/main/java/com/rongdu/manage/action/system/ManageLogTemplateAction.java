package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.domain.LogTemplate;
import com.rongdu.p2psys.core.service.LogTemplateService;
import com.rongdu.p2psys.core.web.BaseAction;

public class ManageLogTemplateAction extends BaseAction implements ModelDriven<LogTemplate> {

	@Resource
	private LogTemplateService logTemplateService;

	private LogTemplate logTemplate = new LogTemplate();

	private Map<String, Object> data;

	public LogTemplate getModel() {
		return logTemplate;
	}

	/**
	 * 获得日志模板列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "/modules/system/logTemplate/logTemplateManager")
	public String logTemplateManager() throws Exception {
		QueryParam param = new QueryParam();
		param.addParam("type",5);
		List<LogTemplate> logTemplateList = logTemplateService.logTemplateList(param);
		request.setAttribute("logTemplateList", logTemplateList);
		return "logTemplateManager";
	}

	/**
	 * 获得日志模板列表数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/logTemplate/logTemplateList")
	public void logTemplateList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		PageDataList<LogTemplate> pagaDataList = logTemplateService.logTemplateList(pageNumber, pageSize, logTemplate);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 添加日志模板页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/logTemplate/logTemplateAddPage")
	public String logTemplateAddPage() throws Exception {
		return "logTemplateAddPage";
	}

	/**
	 * 添加日志模板
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/logTemplate/logTemplateAdd")
	public void logTemplateAdd() throws Exception {
		data = new HashMap<String, Object>();
		logTemplateService.logTemplateAdd(logTemplate);
		data.put("result", true);
		data.put("msg", "添加日志模板成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 编辑日志模板页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/logTemplate/logTemplateEditPage")
	public String logTemplateEditPage() throws Exception {
		long id = paramLong("id");
		LogTemplate logTemplate = logTemplateService.logTemplateEditPage(id);
		request.setAttribute("logTemplate", logTemplate);
		return "logTemplateEditPage";
	}

	/**
	 * 修改日志模板
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/logTemplate/logTemplateEdit")
	public void logTemplateEdit() throws Exception {
		data = new HashMap<String, Object>();
		logTemplateService.logTemplateEdit(logTemplate);
		data.put("result", true);
		data.put("msg", "修改日志模板成功！");
		printJson(getStringOfJpaObj(data));
	}
}
