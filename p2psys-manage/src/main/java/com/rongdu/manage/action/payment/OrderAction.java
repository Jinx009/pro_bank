package com.rongdu.manage.action.payment;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.payment.service.IOrderService;

public class OrderAction extends BaseAction<OrderInfo> implements ModelDriven<OrderInfo> {

	@Resource
	private IOrderService orderService;
	
	private Map<String, Object> data;

	@Action(value="/modules/payment/orderManager")
	public String orderManager(){
		return "orderManager";
	}
	
	/**
	 * 分页展示
	 * @throws IOException
	 */
	@Action(value="/modules/payment/orderList")
	public void getOrderList() throws IOException{
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		String searchName = paramString("searchName");

		PageDataList<OrderInfo> recordList = orderService.findOrderByItem(pageNumber, pageSize, searchName);
		data.put("total",recordList.getPage().getTotal()); // 总行数
		data.put("rows", recordList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
}
