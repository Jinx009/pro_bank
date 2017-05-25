package com.rongdu.p2psys.cf.project;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatUtil;

/**
 * 微信众筹订单
 * @author Chris
 *
 */
public class WechatOrderAction extends BaseAction<OrderModel> implements ModelDriven<OrderModel> {
	
	@Resource
	private OrderService cfOrderService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private UserCacheService theUserCacheService;
	
	
	private Map<String,Object> data;
	
	/**
	 * 我购买产品列表 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/buyList",results={@Result(name="buy-list",type="ftl",location="/nb/cf/wechat/user/my_invest_project.html")})
	public String buyList(){
		return "buy-list";
	}
	
	/**
	 * 购买数据 -- 数据
	 * @throws IOException 
	 */
	@Action(value = "/cf/wehcat/user/buyListData")
	public void buyData() throws IOException{
		User user = getNBSessionUser();
		int type = paramInt("type");
		data = new HashMap<String, Object>();
		if(type >=0){
			List<OrderModel> list = cfOrderService.getBuyListForWechat(user,type);
			if(null != list && !list.isEmpty()){
				data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
				data.put(ConstantUtil.MSG, ConstantUtil.SUCCESS);
				data.put(ConstantUtil.DATA,list);
			}else{
				data.put(ConstantUtil.CODE,ConstantUtil.DATA_IS_NULL);
				data.put(ConstantUtil.MSG, "没有数据对应");
			}
			
		}else{
			data.put(ConstantUtil.CODE,ConstantUtil.ILLEGAL_PARAMETER);
			data.put(ConstantUtil.MSG, "非合法参数");
		}
		
		printWebJson(getStringOfJpaObj(data));
	} 
	
	/**
	 * 购买产品列表
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Action(value = "/wechat/pro/buy",results={@Result(name="buy",type="ftl",location="/nb/cf/wechat/order/order_one.html")})
	public String buy() throws ClientProtocolException, IOException{
		String code = paramString("code");
		String openid = "";
		// 微信进入
		if (null != code && !"".equals(code)) {
			openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,WechatData.A_APP_SECRET);
		}
		request.setAttribute("openid",openid);
		request.setAttribute("projectId", paramLong("projectId"));
		request.setAttribute("ruleId", paramLong("ruleId"));
		User user = getNBSessionUser();
		if(null!=user.getPayPwd()&&!"".equals(user.getPayPwd())){
			request.setAttribute("status",1);
		}else{
			request.setAttribute("status",0);
		}
		return "buy";
	}
	
	/**
	 * 用户支付全款操作相关 -- 操作
	 * @throws IOException 
	 */
	@Action(value = "/cf/wechat/user/payAll")
	public void payAll() throws IOException{
		Integer id = paramInt("id");
		User user  = getNBSessionUser();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,300);
		data.put(ConstantUtil.MSG,"用户支付全款操作相关");
		if(null!=user){
			String result = cfOrderService.payAll(id);
			if(!ConstantUtil.SUCCESS.equals(result)){
				data.put(ConstantUtil.DATA,result);
			}else{
				cfOrderService.payAll(id);
				data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
				data.put(ConstantUtil.DATA,"支付成功！");
			}
			
		}else{
			data.put(ConstantUtil.DATA,"用户登录失效！");
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 取消预约 -- 操作
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/user/caclePay")
	public void caclePay() throws IOException{
		Integer id = paramInt("id");
		User user  = getNBSessionUser();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,300);
		data.put(ConstantUtil.MSG,"用户取消支付操作相关");
		if(null!=user){
			String result = cfOrderService.caclePay(id);
			if(!ConstantUtil.SUCCESS.equals(result)){
				data.put(ConstantUtil.DATA,result);
			}else{
				cfOrderService.payAll(id);
				data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
				data.put(ConstantUtil.DATA,"取消成功！");
			}
		}else{
			data.put(ConstantUtil.DATA,"用户登录失效！");
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 收货地址 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/address",results={@Result(name="order-address",type="ftl",location="/nb/cf/wechat/pro/address.html")})
	public String address(){
		Integer id = paramInt("id");
		InvestOrder investOrder = cfOrderService.find(id);
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(investOrder.getProjectBaseinfo().getId());
		request.setAttribute("projectBaseinfo",projectBaseinfo);
		request.setAttribute("orderId",id);
		return "order-address";
	}
	
	/**
	 * 订单详情 -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/user/orderDetail")
	public void orderDetail() throws IOException{
		Integer id = paramInt("id");
		InvestOrder order = cfOrderService.find(id);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.DATA,order);
		data.put(ConstantUtil.MSG,"订单详情相关");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 保存收货地址 -- 操作
	 * @throws IOException
	 */
	@Action(value="/cf/wechat/user/saveAddress")
	public void saveOrderInfo() throws IOException{
		String address = paramString("address");
		String mobilePhone = paramString("mobilePhone");
		Integer postNum = paramInt("postNum");
		String realName = paramString("realName");
		Integer id = paramInt("id");
		
		InvestOrder order = cfOrderService.find(id);
		order.setAddress(address);
		order.setMobilePhone(mobilePhone);
		order.setPostNum(postNum);
		order.setRealName(realName);
		
		cfOrderService.updateOrder(order);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.DATA,"保存成功！");
		data.put(ConstantUtil.MSG,"保存收货地址相关");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
}
