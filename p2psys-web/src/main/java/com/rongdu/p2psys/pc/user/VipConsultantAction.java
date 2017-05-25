package com.rongdu.p2psys.pc.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Consultant;
import com.rongdu.p2psys.core.service.ConsultantService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.vip.domain.VipConsultant;
import com.rongdu.p2psys.nb.vip.model.VipConsultantModel;
import com.rongdu.p2psys.nb.vip.service.VipConsultantService;

public class VipConsultantAction extends BaseAction<VipConsultantModel> implements
		ModelDriven<VipConsultantModel> {

	@Resource
	private VipConsultantService vipConsultantService;
	
	@Resource
	private ConsultantService consultantService;
	

	private Map<String, Object> data;

	/**
	 * 
	 * @throws Exception
	 */
	@Action("/nb/pc/vip/appointConsultant")
	public void appointConsultant() throws Exception {
		VipConsultant vipConsultant = model.prototype();
		Consultant consultant = consultantService.getByid(paramLong("consultant_id"));
		vipConsultant.setConsultant(consultant);
		vipConsultant.setAddTime(new Date());
		vipConsultant.setAddIp(Global.getIP());
	    vipConsultantService.saveObject(vipConsultant);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"预约成功!");
		printWebJson(getStringOfJpaObj(data));
    }


}
