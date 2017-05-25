package com.rongdu.manage.action.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.FinanceSite;
import com.rongdu.p2psys.core.model.FinanceSiteModel;
import com.rongdu.p2psys.core.service.FinanceSiteService;
import com.rongdu.p2psys.core.web.BaseAction;

public class ManageFinanceSiteAction extends BaseAction<FinanceSiteModel> implements ModelDriven<FinanceSiteModel> {

	@Resource
	private FinanceSiteService financeSiteService;
	
	private Map<String, Object> data;
	
	/**
	 * 理财商学院栏目列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/site/financeSiteManager")
	public String financeSiteManager() throws Exception {
		return "financeSiteManager";
	}
	
	/**
	 * 理财商学院栏目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/site/financeSiteList")
	public void financeSiteList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		PageDataList<FinanceSiteModel> financeSiteList = financeSiteService.financeSiteList(pageNumber, pageSize, model);
		data.put("total", financeSiteList.getPage().getTotal()); //总行数
		data.put("rows", financeSiteList.getList()); //集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 添加理财商学院栏目页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/site/financeSiteAddPage")
	public String financeSiteAddPage() throws Exception {
		saveToken("financeSiteAddToken");
		return "financeSiteAddPage";
	}
	
	/**
	 * 添加理财商学院栏目
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/site/financeSiteAdd")
	public void financeSiteAdd() throws Exception {
		data = new HashMap<String, Object>();
		checkToken("financeSiteAddToken");
		FinanceSite financeSite = model.prototype();
		String picPath = imgUpload();
		if (picPath != null) {
			financeSite.setPicPath(picPath);
		}
		financeSite.setAddTime(new Date());
		financeSite.setAddIp(Global.getIP());
		financeSiteService.financeSiteAdd(financeSite);
		data.put("result", true);
		data.put("msg", "添加商学院栏目成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 修改理财商学院栏目页面
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/site/financeSiteEditPage")
	public String financeSiteEditPage() throws Exception {
		long id = paramLong("id");
		FinanceSite financeSite = financeSiteService.find(id);
		request.setAttribute("financeSite", financeSite);
		return "financeSiteEditPage";
	}

	/**
	 * 修改理财商学院栏目
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/site/financeSiteEdit")
	public void financeSiteEdit() throws Exception {
		data = new HashMap<String, Object>();
		FinanceSite financeSite = model.prototype();
		String picPath = imgUpload();
		if (picPath != null) {
			financeSite.setPicPath(picPath);
		}
		financeSiteService.financeSiteEdit(financeSite);
		data.put("result", true);
		data.put("msg", "修改理财商学院栏目成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除理财商学院栏目
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/site/financeSiteDelete")
	public void financeSiteDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		FinanceSite financeSite = financeSiteService.find(id);
		financeSite.setStatus(2);
		financeSiteService.financeSiteEdit(financeSite);
		data.put("result", true);
		data.put("msg", "删除理财商学院栏目成功！");
		printJson(getStringOfJpaObj(data));
	}
	
}
