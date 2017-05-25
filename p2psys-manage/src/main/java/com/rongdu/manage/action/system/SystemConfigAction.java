package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.model.SystemConfigModel;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.chinapnr.tool.HttpClientUtils;

/**
 * 系统参数管理
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月21日
 */
@SuppressWarnings("rawtypes")
public class SystemConfigAction extends BaseAction implements ModelDriven<SystemConfigModel> {

	private SystemConfigModel model = new SystemConfigModel();

	public SystemConfigModel getModel() {
		return model;
	}

	@Resource
	private SystemConfigService systemConfigService;

	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/sconfig/sconfigManager")
	public String sconfigManager() throws Exception {
		return "sconfigManager";
	}

	/**
	 * 列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/sconfig/sconfigList")
	public void sconfigList() throws Exception {
		if ("".equals(paramString("status"))) {
			model.setStatus(99);
		}
		if ("".equals(paramString("type"))) {
			model.setType(99);
		}
		PageDataList<SystemConfigModel> pageList = systemConfigService.list(model);
		int totalPage = pageList.getPage().getTotal(); // 总页数
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/sconfig/sconfigAddPage")
	public String sconfigAddPage() throws Exception {
		saveToken("configAddToken");
		return "sconfigAddPage";
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/sconfig/sconfigAdd")
	public void sconfigAdd() throws Exception {
		checkToken("configAddToken");
		systemConfigService.add(model.prototype());
		printResult(MessageUtil.getMessage("I10001"), true);
	}

	/**
	 * 修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/sconfig/sconfigEditPage")
	public String sconfigEditPage() throws Exception {
		SystemConfig sconfig = systemConfigService.find(model.getId());
		request.setAttribute("sconfig", sconfig);
		return "sconfigEditPage";
	}

	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/sconfig/sconfigEdit")
	public void sconfigEdit() throws Exception {
		systemConfigService.update(model.prototype());
		printResult(MessageUtil.getMessage("I10002"), true);
	}

	@Action("/modules/system/sconfig/clean")
	public void clean() throws Exception{
		//后台缓存清理
		systemConfigService.clean();
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "test");//params不能为空，随便设置参数
		//前台路径
		String weburl = Global.getValue("weburl");
		String url = weburl+"/system/clean.html";
		//前台缓存清理
		String result = HttpClientUtils.doHttpsPost(url, params, "utf-8", 100);
		Map<String, Object> data = new HashMap<String, Object>();
		if(result.contains("ok")){
			data.put("success", true);
			printWebJson(getStringOfJpaObj(data));
		}else{
			data.put("success", false);
			printWebJson(getStringOfJpaObj(data));
		}
	}
}
