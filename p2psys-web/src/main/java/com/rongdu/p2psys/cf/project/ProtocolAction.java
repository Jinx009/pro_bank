package com.rongdu.p2psys.cf.project;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProtocolService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;

public class ProtocolAction extends BaseAction<InvestOrder> implements ModelDriven<InvestOrder>{

	@Resource
	private OrderService cfOrderService;
	@Resource
	private ProtocolService cfProtocolService;
	
	private Map<String,Object> data;
	
	@Action(value="/create/protocol")
	public String createProtocol() throws Exception{
		Integer id = paramInt("id");
		String protocolCode = paramString("protocolCode");
		InvestOrder investOrder = cfOrderService.find(id);
		User user = investOrder.getUser();
		ProjectBaseinfo projectBaseinfo = investOrder.getProjectBaseinfo();
		String result = cfProtocolService.createProtocol(investOrder, protocolCode);
		if("success".equals(result)){
			String path = Global.getString("protocol_host")+projectBaseinfo.getId()+"/";
			generateDownloadFile(path+user.getUserId()+".pdf",projectBaseinfo.getProjectName()+"用户"+StringUtil.isNull(user.getRealName())+"协议.pdf");
			
		}
		return null;
	}
	
	@Action(value="/empty/protocol")
	public String emptyProtocol() throws Exception{
		Integer id = paramInt("id");
		String protocolCode = paramString("protocolCode");
		InvestOrder investOrder = cfOrderService.find(id);
		User user = investOrder.getUser();
		ProjectBaseinfo projectBaseinfo = investOrder.getProjectBaseinfo();
		String result = cfProtocolService.getEmptyProtocol(investOrder, protocolCode);
		if("success".equals(result)){
			String path = Global.getString("protocol_host")+projectBaseinfo.getId()+"/";
			generateDownloadFile(path+user.getUserId()+"empty.pdf",projectBaseinfo.getProjectName()+"用户"+StringUtil.isNull(user.getRealName())+"协议.pdf");
			
		}
		return null;
	}
	
	@Action(value="/get/protocol")
	public void getProtocol() throws Exception{
		Integer id = paramInt("id");
		String protocolCode = paramString("protocolCode");
		InvestOrder investOrder = cfOrderService.find(id);
		String result = cfProtocolService.getProtocol(investOrder, protocolCode);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,result);
		printWebJson(getStringOfJpaObj(data));
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
