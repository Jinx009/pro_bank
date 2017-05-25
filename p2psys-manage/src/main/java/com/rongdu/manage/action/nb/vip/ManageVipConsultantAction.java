package com.rongdu.manage.action.nb.vip;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.vip.domain.VipConsultant;
import com.rongdu.p2psys.nb.vip.model.VipConsultantModel;
import com.rongdu.p2psys.nb.vip.service.VipConsultantService;

/**
 * 顾问申请管理
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月14日
 */
public class ManageVipConsultantAction extends BaseAction<VipConsultantModel> 
	implements ModelDriven<VipConsultantModel> {
	@Resource
	private VipConsultantService vipConsultantService;
	
	private Map<String, Object> data;
	
	/**
	 * 顾问预约列表 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/vipConsultantManage")
	public String vipConsultantManage() throws Exception {
		
		return "vipConsultantManage";
	}
	
	/**
	 * 顾问预约列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/vipConsultantList")
	public void vipConsultantList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		String status = paramString("status");
		if (StringUtil.isBlank(status)) {
			model.setStatus(99);
		}
		PageDataList<VipConsultantModel> dataList = vipConsultantService.getAllList(pageNumber, pageSize, model);
		data.put("total", dataList.getPage().getTotal()); // 总行数
		data.put("rows", dataList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 受理
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/vipConsultantDo")
	public void vipConsultantDo() throws Exception {
		data = new HashMap<String, Object>();
		VipConsultant vipConsultant = vipConsultantService.findById(model.getId());
		vipConsultant.setStatus(1);
		vipConsultantService.update(vipConsultant);
		printResult(MessageUtil.getMessage("I10009"), true);
	}
}
