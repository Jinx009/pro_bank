package com.rongdu.manage.action.finance;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.domain.ApplyConsultants;
import com.rongdu.p2psys.core.model.ApplyConsultantsModel;
import com.rongdu.p2psys.core.service.ApplyConsultantsService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 顾问申请管理
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月14日
 */
public class ManageApplyConsultantAction extends BaseAction<ApplyConsultantsModel> 
	implements ModelDriven<ApplyConsultantsModel> {
	@Resource
	private ApplyConsultantsService applyConsultantsService;
	
	private ApplyConsultantsModel model = new ApplyConsultantsModel();
	
	public ApplyConsultantsModel getModel() {
		
		return model;
	}
	
	private Map<String, Object> data;
	
	/**
	 * 顾问预约列表 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/applyConsultant/applyConsultantManage")
	public String applyConsultantManage() throws Exception {
		
		return "applyConsultantManage";
	}
	
	/**
	 * 顾问预约列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/applyConsultant/applyConsultantList")
	public void applyConsultantList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		String status = paramString("status");
		if (StringUtil.isBlank(status)) {
			model.setStatus(99);
		}
		PageDataList<ApplyConsultantsModel> dataList = applyConsultantsService.getAllList(pageNumber, pageSize, model);
		data.put("total", dataList.getPage().getTotal()); // 总行数
		data.put("rows", dataList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 受理
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/applyConsultant/applyConsultantDo")
	public void applyConsultantDo() throws Exception {
		data = new HashMap<String, Object>();
		ApplyConsultants consultants = applyConsultantsService.findById(model.getId());
		consultants.setStatus(1);
		applyConsultantsService.update(consultants);
		printResult(MessageUtil.getMessage("I10009"), true);
	}
}
