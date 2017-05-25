package com.rongdu.p2psys.web.finance;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.ApplyConsultants;
import com.rongdu.p2psys.core.domain.Consultant;
import com.rongdu.p2psys.core.model.ApplyConsultantsModel;
import com.rongdu.p2psys.core.service.ApplyConsultantsService;
import com.rongdu.p2psys.core.service.ConsultantService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 私人顾问
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
public class PersonalConsultantsAction extends BaseAction<ApplyConsultantsModel>
		implements ModelDriven<ApplyConsultantsModel> {
	private Logger logger = Logger.getLogger(PersonalConsultantsAction.class);
	
	@Resource
	private ApplyConsultantsService applyConsultantsService;
	@Resource
	private ConsultantService consultantService;
	
	private ApplyConsultantsModel model = new ApplyConsultantsModel();
	
	public ApplyConsultantsModel getModel() {
		return model;
	}
	
	private Map<String, Object> data;
	
	/**
	 * 私人顾问页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/lcschool/personalConsultantsPage")
	public String personalConsultantsPage() throws Exception {
		
		return "personalConsultantsPage";
	}
	
	/**
	 * 私人顾问列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/lcschool/personalConsultants")
	public void personalConsultants() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");
		PageDataList<Consultant> dataList = consultantService.getAllList(pageNumber, 10, null);
		data.put("data", dataList);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 申请私人顾问 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/lcschool/applyAc", interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public String applyAc() throws Exception {
		request.setAttribute("consultant_id", paramLong("consultant_id"));
		return "applyAc";
	}
	
	/**
	 * 申请私人顾问
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/lcschool/addAc", interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public String addAc() throws Exception {
		try{
			User user = getSessionUser();
			logger.info("私人顾问id:" + paramLong("consultant_id"));
			Consultant consultant = consultantService.getByid(paramLong("consultant_id"));
			model.setConsultant(consultant);
			model.checkModel();
			ApplyConsultants consultants = new ApplyConsultants(user, consultant, model.getTimeFirst(), model.getTimeSecond());
			applyConsultantsService.save(consultants);
			printWebResult("预约成功", true);
		} catch(UserException ue) {
			printWebResult(ue.getMessage(), false);
		}
		return null;
	}
}
