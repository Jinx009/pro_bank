package com.rongdu.manage.action.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Consultant;
import com.rongdu.p2psys.core.service.ConsultantService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 后台顾问管理
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
public class ManageConsultantAction extends BaseAction<Consultant> implements
		ModelDriven<Consultant> {
	@Resource
	private ConsultantService consultantService;

	private Consultant model = new Consultant();

	public Consultant getModel() {
		return model;
	}
	
	private Map<String, Object> data;

	/**
	 * 顾问列表 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/consultant/consultantManagePage")
	public String consultantManagePage() throws Exception {

		return "consultantManagePage";
	}

	/**
	 * 顾问列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/consultant/consultantList")
	public void consultantList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		PageDataList<Consultant> dataList = consultantService.getAllList(pageNumber,pageSize,model);
		data.put("total", dataList.getPage().getTotal()); // 总行数
		data.put("rows", dataList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 顾问添加 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/consultant/addConsultantPage")
	public String addConsultantPage() throws Exception {

		return "addConsultantPage";
	}

	/**
	 * 顾问添加
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/consultant/addConsultant")
	public void addConsultant() throws Exception {
		//获得专家头像图片
		String avatarImage = imgUpload();
		if(avatarImage != null){
			model.setAvatar(avatarImage);
		}
		model.setAddTime(new Date());
		model.setAddIp(Global.getIP());
		consultantService.save(model);
		printResult(MessageUtil.getMessage("I10001"), true);
	}

	/**
	 * 顾问推荐
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/consultant/doRecommend")
	public void doRecommend() throws Exception {
		
	}
	
	/**
	 * 顾问修改 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/consultant/editConsultantPage")
	public String editConsultantPage() throws Exception {
		Consultant consultant = consultantService.getByid(model.getId());
		request.setAttribute("consultant", consultant);
		return "editConsultantPage";
	}
	
	/**
	 * 顾问修改
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/consultant/editConsultant")
	public void editConsultant() throws Exception {
		consultantService.update(model);
		printResult(MessageUtil.getMessage("I10002"), true);
	}
	
	/**
	 * 顾问删除
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/finance/consultant/deleteConsultant")
	public void deleteConsultant() throws Exception {
		Consultant consultant = consultantService.getByid(model.getId());
		consultant.setIsDelete(1);
		consultantService.update(consultant);
		printResult(MessageUtil.getMessage("I10003"), true);
	}
}
