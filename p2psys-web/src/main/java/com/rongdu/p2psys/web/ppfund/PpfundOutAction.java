package com.rongdu.p2psys.web.ppfund;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.ppfund.model.PpfundOutModel;
import com.rongdu.p2psys.ppfund.service.PpfundOutService;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品转出
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月19日
 */
public class PpfundOutAction extends BaseAction<PpfundOutModel> implements
		ModelDriven<PpfundOutModel> {
	@Resource
	private PpfundOutService ppfundOutService;
	
	private Map<String, Object> data;

	/**
	 * 我的转出记录
	 * 
	 * @throws Exception
	 */
	@Action(value="/ppfund/myOutList",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void myOutList() throws Exception {
		data = new HashMap<String, Object>();
		User user = getSessionUser();
		model.setUser(user);
		PageDataList<PpfundOutModel> pageDataList = ppfundOutService.pageDataList(model);
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}
}
