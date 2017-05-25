package com.rongdu.p2psys.account.model.payment.unionPay;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.tpp.chinapnr.tool.HttpClientUtils;

/**
 * 银联在线接口调用
 * 
 * @author yinliang
 * @version 2.0
 * @Date   2015年3月7日
 */
public class SignHelper {
	private static Logger logger = Logger.getLogger(SignHelper.class);

	private static Map<String, String> params;

	/**
	 * 地址前缀，具体地址为postUrl+接口名称+.do
	 */
	private static String postUrl = "http://cps.jiezhifu.cn/bind/";
//	private static String postUrl = "http://115.236.91.116:8081/bind/";
	/**
	 * 绑定银行卡（卡号+姓名+身份证+预留手机号码+验证码验证）
	 * 
	 * @param pay UnionPay
	 * @return
	 */
	public static UnionPayRet cardBindByMC(UnionPay pay) {
		try {
			params = new HashMap<String, String>();
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("绑卡请求参数[cardBindByMC]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "cardBindByMC.do", params, "UTF-8", 100);
			logger.info("绑卡响应参数[cardBindByMC]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 绑定银行卡（卡号+姓名+身份证验证）
	 * 
	 * @param pay
	 * @return
	 */
	public static UnionPayRet realNameBind(UnionPay pay) {
		try {
			params = new HashMap<String, String>();
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("绑卡请求参数[realNameBind]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "realNameBind.do", params, "UTF-8", 100);
			logger.info("绑卡响应参数[realNameBind]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生产验证码
	 * 
	 * @param mobile 手机号码
	 */
	public static UnionPayRet sendCode(String mobile) {
		try {
			params = new HashMap<String, String>();
			UnionPay pay = new UnionPay();
			pay.setMobile(mobile);
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("生产验证码请求参数[sendCode]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "sendCode.do", params, "UTF-8", 100);
			logger.info("生产验证码响应参数[sendCode]：" + resp);

			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询绑定信息
	 * @param bindId 绑定号
	 * @return
	 */
	public static UnionPayRet queryBind(String bindId) {
		try{
			params = new HashMap<String, String>();
			UnionPay pay = new UnionPay();
			pay.setBindId(bindId);
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("查询绑定信息请求参数[queryBind]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "queryBind.do", params, "UTF-8", 100);
			logger.info("查询绑定信息响应参数[queryBind]：" + resp);
			
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			if(!"0000".equals(ret.getRetCode())){
				params = new HashMap<String, String>();
				UnionPay pay2 = new UnionPay();
				pay2.setBindId(bindId);
				params = pay2.getParams(2);
				params.put("sign", pay2.getSign2());
				logger.info("查询绑定信息请求参数[queryBind]：" + params.toString());
				String resp2 = HttpClientUtils.doHttpsPost(postUrl + "queryBind.do", params, "UTF-8", 100);
				logger.info("查询绑定信息响应参数[queryBind]：" + resp2);
				
				UnionPayRet ret2 = JSONObject.parseObject(resp2, UnionPayRet.class);
				return ret2;
			}
			return ret;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解除银行卡
	 * 
	 * @param bindId 绑定号
	 * @return
	 */
	public static UnionPayRet unbind(String bindId) {
		try{
			params = new HashMap<String, String>();
			logger.info("解除银行卡BindId：" + bindId);
			UnionPay pay = new UnionPay();
			pay.setBindId(bindId);
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("解除银行卡绑定请求参数[unbind]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "unbind.do", params, "UTF-8", 100);
			logger.info("解除银行卡绑定响应参数[unbind]：" + resp);
			
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			if(!"0000".equals(ret.getRetCode())){
				params = new HashMap<String, String>();
				logger.info("解除银行卡BindId：" + bindId);
				UnionPay pay2 = new UnionPay();
				pay2.setBindId(bindId);
				params = pay2.getParams(2);
				params.put("sign", pay2.getSign2());
				logger.info("解除银行卡绑定请求参数[unbind]：" + params.toString());
				String resp2 = HttpClientUtils.doHttpsPost(postUrl + "unbind.do", params, "UTF-8", 100);
				logger.info("解除银行卡绑定响应参数[unbind]：" + resp2);
				
				UnionPayRet ret2 = JSONObject.parseObject(resp2, UnionPayRet.class);
				return ret2;
			}
			return ret;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 单笔支付（充值）
	 * 
	 * @param pay UnionPay
	 * @return
	 */
	public static UnionPayRet singlePay(UnionPay pay) {
		try {
			params = new HashMap<String, String>();
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("充值请求参数[singlePay]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "singlePay.do", params, "UTF-8", 100);
			logger.info("充值响应参数[singlePay]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 扣款
	 * 
	 * @param pay UnionPay
	 * @return
	 */
	public static UnionPayRet singlePay2(UnionPay pay) {
		try {
			params = new HashMap<String, String>();
			params = pay.getParams(2);
			params.put("sign", pay.getSign2());
			logger.info("扣款请求参数[singlePay]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "singlePay.do", params, "UTF-8", 100);
			logger.info("扣款响应参数[singlePay]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 付款（提现，需绑定号）
	 * 
	 * @param pay UnionPay
	 * @return
	 */
	public static UnionPayRet payment(UnionPay pay) {
		try {
			pay.setMerId(Global.getValue("pay_account"));
			pay.setKey(Global.getValue("pay_key"));
			params = new HashMap<String, String>();
			params = pay.getParams(2);
			params.put("sign", pay.getSign2());
			logger.info("提现请求参数[payment]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "payment.do", params, "UTF-8", 100);
			logger.info("提现响应参数[payment]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 付款 （提现，无绑定号)
	 * 
	 * @param pay
	 * @return
	 */
	public static UnionPayRet paymentNoBind(UnionPay pay) {
		try {
			pay.setMerId(Global.getValue("pay_account"));
			pay.setKey(Global.getValue("pay_key"));
			params = new HashMap<String, String>();
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("提现请求参数[paymentNoBind]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "paymentNoBind.do", params, "UTF-8", 100);
			logger.info("提现响应参数[paymentNoBind]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			String retDesc = ret.getRetDesc();
			logger.info("提现请求-响应的结果信息：" + retDesc);
			logger.info("提现请求-响应的结果信息比对equals：" + retDesc.equals("身份认证失败"));
			logger.info("提现请求-响应的结果信息比对==：" + (retDesc=="身份认证失败"));
			if(retDesc.equals("身份认证失败") || retDesc=="身份认证失败"){
				Thread.sleep(60*1000);//间隔60秒钟再次请求
				logger.info("提现请求无身份证和手机号参数[paymentNoBind]：" + "===无身份证和手机号参数验证开始===");
				UnionPay pay2 = new UnionPay();
				pay2.setCardNo(pay.getCardNo());
				pay2.setAccName(pay.getAccName());
//				pay2.setMobile(pay.getMobile());
//				pay2.setAccId(pay.getAccId());
				pay2.setAmount(pay.getAmount());
				pay2.setPayType(pay.getPayType());
				pay2.setRemarks(pay.getRemarks());
				pay2.setMerId(Global.getValue("pay_account"));
				pay2.setKey(Global.getValue("pay_key"));
				params = new HashMap<String, String>();
				params = pay2.getParams(1);
				params.put("sign", pay2.getSign());
				logger.info("提现请求无身份证和手机号参数[paymentNoBind]：" + params.toString());
				String resp2 = HttpClientUtils.doHttpsPost(postUrl + "paymentNoBind.do", params, "UTF-8", 100);
				logger.info("提现无身份证和手机号响应参数[paymentNoBind]：" + resp2);
				UnionPayRet ret2 = JSONObject.parseObject(resp2, UnionPayRet.class);
				logger.info("提现请求无身份证和手机号参数[paymentNoBind]：" + "===无身份证和手机号参数验证结束===");
				return ret2;
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 付款（对公）
	 * @param pay
	 * @return
	 */
	public static UnionPayRet paymentForComp(UnionPay pay) {
		try {
			params = new HashMap<String, String>();
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("提现请求参数[paymentForComp]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "paymentForComp.do", params, "UTF-8", 100);
			logger.info("提现响应参数[paymentForComp]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 订单查询
	 * 
	 * @param orderNo 订单号
	 * @return
	 */
	public static UnionPayRet queryOrder(String orderNo) {
		try{
			params = new HashMap<String, String>();
			UnionPay pay = new UnionPay();
			pay.setOrderNo(orderNo);
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("订单查询请求参数[queryOrder]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "queryOrder.do", params, "UTF-8", 100);
			logger.info("订单查询响应参数[queryOrder]：" + resp);
			
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			
			//解析订单查询OrderDetail参数
			UnionPayRetOrderDetail detail = JSONObject.parseObject(ret.getOrderDetail(), UnionPayRetOrderDetail.class);
			ret.setOrderStatus(detail.getOrderStatus());
			return ret;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public static void main(String[] args) {
//		UnionPayRet ret = queryOrder("1505071514096811");
//		UnionPayRetOrderDetail detail = JSONObject.parseObject(ret.getOrderDetail(), UnionPayRetOrderDetail.class);
//		if(detail.getOrderStatus() == 2) {
//			System.out.println(1);
//		} else {
//			System.out.println(2);
//		}
//	}
	
	/**
	 * 对账查询
	 * 
	 * @param pay UnionPay
	 * @return
	 */
	public static UnionPayRet checkOrders(UnionPay pay, HttpServletRequest request, HttpServletResponse response,
			String startTime) {
		try {
			params = new HashMap<String, String>();
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("对账查询请求参数[checkOrders]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "checkOrders.do", params, "UTF-8", 100);
			logger.info("对账查询响应参数[checkOrders]：" + resp);
			
			response.setContentType("text/plain");
			String fileName = startTime;
			response.setHeader("Content-Disposition","attachment; filename=" + fileName + ".txt");
			ServletOutputStream outSTr = response.getOutputStream();
			BufferedOutputStream buff = new BufferedOutputStream(outSTr);
			buff.write(resp.getBytes("UTF-8"));
			buff.flush();
			buff.close();
			outSTr.close();    
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 备付金查询
	 * 
	 * @param merType 商户类型
	 * @return
	 */
	public static UnionPayRet queryProv(int merType) {
		try{
			params = new HashMap<String, String>();
			UnionPay pay = new UnionPay();
			pay.setMerType(merType);
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("备付金查询请求参数[queryProv]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "queryProv.do", params, "UTF-8", 100);
			logger.info("备付金查询响应参数[queryProv]：" + resp);
			
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 投资人提现备付金查询
	 * 
	 * @param merType 商户类型
	 * @return
	 */
	public static UnionPayRet queryProvForCash(int merType) {
		try{
			params = new HashMap<String, String>();
			UnionPay pay = new UnionPay();
			pay.setMerId(Global.getValue("pay_account"));
			pay.setKey(Global.getValue("pay_key"));
			pay.setMerType(merType);
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("备付金查询请求参数[queryProvForCash]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "queryProv.do", params, "UTF-8", 100);
			logger.info("备付金查询响应参数[queryProvForCash]：" + resp);
			
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取银行卡信息
	 * 
	 * @param cardNo 银行卡号
	 * @return
	 */
	public static UnionPayRet cardInfo(String cardNo) {
		try {
			params = new HashMap<String, String>();
			UnionPay pay = new UnionPay();
			pay.setCardNo(cardNo);
			params = pay.getParams(1);
			params.put("sign", pay.getSign());
			logger.info("获取银行卡信息请求参数[cardInfo]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "cardInfo.do",
					params, "UTF-8", 100);
			logger.info("获取银行卡信息相应参数[cardInfo]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 绑定银行卡（随机付款验证）
	 * 
	 * @param pay UnionPay
	 * @return
	 */
	public static UnionPayRet paymentBind(UnionPay pay) {
		try {
			params = new HashMap<String, String>();
			params = pay.getParams(2);
			params.put("sign", pay.getSign2());
			logger.info("绑卡请求参数[paymentBind]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "paymentBind.do", params, "UTF-8", 100);
			logger.info("绑卡响应参数[paymentBind]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 随机付款
	 * 
	 * @param pay UnionPay
	 * @return
	 */
	public static UnionPayRet randomPay(UnionPay pay) {
		try {
			params = new HashMap<String, String>();
			params = pay.getParams(2);
			params.put("sign", pay.getSign2());
			logger.info("随机付款请求参数[randomPay]：" + params.toString());
			String resp = HttpClientUtils.doHttpsPost(postUrl + "randomPay.do", params, "UTF-8", 100);
			logger.info("随机付款响应参数[randomPay]：" + resp);
			UnionPayRet ret = JSONObject.parseObject(resp, UnionPayRet.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
