package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.domain.Log;
import com.rongdu.p2psys.core.model.LogModel;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.service.LogService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 系统操作日志Action
 * 
 * @author wujing
 * @version 1.0
 * @since 2014-04-03
 */
public class LogAction extends BaseAction implements ModelDriven<LogModel> {

	private LogModel model = new LogModel();

	@Override
	public LogModel getModel() {
		return model;
	}

	private Map<String, Object> data = new HashMap<String, Object>();

	@Resource
	private LogService logService;
	@Resource
	private DictService dictService;

	/**
	 * 获取系统操作日志集合
	 * 
	 * @return 返回页面
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/sconfig/logManager")
	public String logManager() throws Exception {
		return "logManager";
	}

	/**
	 * 获取系统操作日志集合
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/sconfig/logList")
	public void logList() throws Exception {
		int pageSize = paramInt("rows");// 每页总数
		model.setSize(pageSize);
		PageDataList<LogModel> pageList = logService.list(model);
		List<Dict> dicList = dictService.list("log_type");
		request.setAttribute("dicList", dicList);
		data.put("total", pageList.getPage().getTotal());
		data.put("rows", pageList.getList());
		data.put("dicList", dicList);
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 获取系统日志
	 * 
	 * @throws Exception 异常
	 * @return 页面
	 */
	@Action(value = "/modules/system/sconfig/logPage")
	public String logPage() throws Exception {
		long id = this.paramLong("id");
		Log log = logService.getLogById(id);
		request.setAttribute("log", log);
		return "logPage";
	}

}
