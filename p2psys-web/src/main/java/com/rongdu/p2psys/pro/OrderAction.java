package com.rongdu.p2psys.pro;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

public class OrderAction extends BaseAction<OrderModel> implements ModelDriven<OrderModel>{

	private Map<String,Object> data;
	
	@Resource
	private OrderService orderService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;

	/**
	 * 项目订单
	 * @return
	 */
	@Action(value = "/manage/code/order",results = {@Result(name = "order", type = "ftl", location = "/nb/pro/order.html")})
	public String order(){
		return "order";
	}
	
	/**
	 * 项目订单数据
	 * @throws IOException
	 */
	public void orderList() throws IOException{
		List<OrderModel> list = orderService.getList(paramLong("id"));
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(paramLong("id"));
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.DATA,list);
		data.put("pro",projectBaseinfo);
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
