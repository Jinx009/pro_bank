package com.rongdu.manage.action.crowdfunding;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.user.domain.User;

/**
 * 產品訂單
 * @author Jinx
 *
 */
public class InvestOrderAction extends BaseAction<OrderModel> implements ModelDriven<OrderModel>{

	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private OrderService cfOrderService;
	@Resource
	private UserService theUserService;
	
	private Map<String,Object> data;
	
	
	@Action(value="/modules/crowdfunding/order/user")
	public String user(){
		Long id = paramLong("id");
		request.setAttribute("id",id);
		return "user";
	}
	
	@Action(value = "/order/user")
	public void userOrder() throws IOException{
		Long id = paramLong("id");
		User user = theUserService.getByUserId(id);
		List<OrderModel> list = cfOrderService.getBuyList(user);
		data = new HashMap<String, Object>();
		data.put("data",list);
		printWebJson(getStringOfJpaObj(list));
	}
	
	/**
	 * 訂單頁面
	 * @return
	 */
	@Action("/modules/crowdfunding/order/order")
	public String order(){
		Long projectId = paramLong("id");
		request.setAttribute("id",projectId);
		return "order";
	}
	
	/**
	 * 獲取產品訂單信息
	 * @throws IOException
	 */
	@Action("/modules/crowdfunding/order/data")
	public void orderData() throws IOException{
		Long projectId = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		model.setProjectBaseinfo(projectBaseinfo);
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		
		PageDataList<OrderModel> list = cfOrderService.getOrderList(model, pageNumber, pageSize);
		
		data = new HashMap<String, Object>();
		data.put("total", list.getPage().getTotal());
		data.put("rows", list.getList());
		printJson(getStringOfJpaObj(data));
	}

	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
