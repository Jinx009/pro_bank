package com.rongdu.manage.action.column;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.SiteModel;
import com.rongdu.p2psys.core.service.SiteService;
import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class ManageSiteAction extends BaseAction implements ModelDriven<SiteModel> {

	@Resource
	private SiteService siteService;

	private SiteModel model = new SiteModel();

	private Map<String, Object> data;

	@Override
	public SiteModel getModel() {
		return model;
	}

	/**
	 * 栏目列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/site/siteManager")
	public String siteManager() throws Exception {
		return "siteManager";
	}

	/**
	 * 栏目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/site/siteList")
	public void siteList() throws Exception {
		data = new HashMap<String, Object>();
		List<Site> siteList = siteService.siteList(model);
		data.put("rows", siteList); //集合对象
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 添加栏目页面
	 * 
	 * @return 返回页面
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/column/site/siteAddPage")
	public String siteAddPage() throws Exception {
		long pid = paramLong("id"); // 获取id，设置所添加菜单的下级pid
		int l = 0;
		if (pid != -1) {
			l = siteService.find(pid).getLevel();
		} else {
			pid = 0;
		}
		request.setAttribute("fpid", pid);
		request.setAttribute("l", l + 1);
		saveToken("siteAddToken");
		return "siteAddPage";
	}

	/**
	 * 添加栏目
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/column/site/siteAdd")
	public void siteAdd() throws Exception {
		data = new HashMap<String, Object>();
		checkToken("siteAddToken");
		Site siteNew = model.prototype();
		siteNew.setAddTime(new Date());
		siteNew.setAddIp(Global.getIP());

		siteService.siteAdd(siteNew);
		data.put("result", true);
		data.put("msg", "添加栏目成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改栏目页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/site/siteEditPage")
	public String siteEditPage() throws Exception {
		long id = paramLong("id");
		Site siteNew = siteService.find(id);
		request.setAttribute("siteNew", siteNew);
		return "siteEditPage";
	}

	/**
	 * 修改栏目
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/column/site/siteEdit")
	public void siteEdit() throws Exception {
		data = new HashMap<String, Object>();
		Site siteNew = model.prototype();

		siteService.siteEdit(siteNew);
		data.put("result", true);
		data.put("msg", "修改栏目成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 删除栏目
	 * @throws Exception
	 */
	@Action(value = "/modules/column/site/siteDelete")
	public void siteDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		Site site = siteService.find(id);
		site.setIsDelete((byte) 1);
		siteService.siteEdit(site);
		data.put("result", true);
		data.put("msg", "删除栏目成功！");
		printJson(getStringOfJpaObj(data));
	}
}
