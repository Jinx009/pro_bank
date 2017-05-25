package com.rongdu.manage.action.loan;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 借贷管理-投标信息管理
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月17日
 */
public class ManageTenderAction extends BaseAction implements ModelDriven<BorrowTenderModel> {

	BorrowTenderModel model = new BorrowTenderModel();

	public BorrowTenderModel getModel() {
		return model;
	}

	@Resource
	private BorrowTenderService borrowTenderService;

	private Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * 投标信息展示
	 * 
	 * @return
	 */
	@Action("/modules/loan/tender/tenderManager")
	public String borrowManager() throws Exception {

		return "tenderManager";
	}

	/**
	 * 投标信息列表
	 * 
	 * @return
	 */
	@Action("/modules/loan/tender/tenderList")
	public void borrowList() throws Exception {
		model.setSize(paramInt("rows"));
		PageDataList<BorrowTenderModel> list = borrowTenderService.list(model);
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}
}
