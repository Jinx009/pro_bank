package com.rongdu.p2psys.account.model.payment.llPay.handler;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.conn.HttpRequestSimple;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderResultsBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderResultsRetBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.SupportBank;
import com.rongdu.p2psys.account.model.payment.llPay.model.UnllBank;
import com.rongdu.p2psys.account.model.payment.llPay.model.UnllBankRet;
import com.rongdu.p2psys.account.model.payment.llPay.model.UserBankCard;
import com.rongdu.p2psys.account.model.payment.llPay.model.UserBankCardRet;
import com.rongdu.p2psys.account.model.payment.llPay.model.WithdrawBean;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;
import com.rongdu.p2psys.core.util.OrderNoUtils;

/**
 * 连连在线接口调用
 * 
 * @author cgw
 * @version 1.0
 * @Date   2015年5月26日
 */
public class LianlPayHelper {
	private static Logger logger = Logger.getLogger(LianlPayHelper.class);
	
	/**
	 * 地址前缀，具体地址为postUrl+接口名称+.htm
	 */
	private static String postUrl = "https://yintong.com.cn/traderapi/";
	
	/**
	 * 解除银行卡
	 * bankcardunbind.htm
	 * @param noAgree 银行卡签约的唯一编号
	 * @param userId 商户用户唯一编号
	 * @return
	 */
	public static UnllBankRet unbind(String noAgree,String userId) {
		try{
			PartnerConfig pc = new PartnerConfig();
			UnllBank bank = new UnllBank();
			bank.setNo_agree(noAgree);
			bank.setOid_partner(pc.getOidPartner());
			bank.setUser_id(userId);
			bank.setSign_type(pc.getSignType());
			bank.setPay_type("D");
			// 加签名
	        String sign = LLPayUtil.addSign(JSON.parseObject(JSON
	                .toJSONString(bank)), pc.getTraderpriKey(),
	                pc.getMd5Key());
	        bank.setSign(sign);
			String reqJson = JSON.toJSONString(bank);
			logger.info("解除银行卡请求参数：" + reqJson);
			HttpRequestSimple httpclent = new HttpRequestSimple();
	        String resJson = httpclent.postSendHttp(postUrl + "bankcardunbind.htm",
	                reqJson);
	        logger.info("解除银行卡响应参数：" + resJson);
			UnllBankRet ret = JSONObject.parseObject(resJson, UnllBankRet.class);
			return ret;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 订单结果查询
	 * orderquery.htm
	 * @param orderNo 订单号
	 * @return
	 */
	public static OrderResultsRetBean queryOrder(String orderNo) {
		try{
			PartnerConfig pc = new PartnerConfig();
			OrderResultsBean orderResult = new OrderResultsBean();
			orderResult.setOid_partner(pc.getOidPartner());
			orderResult.setQuery_version(pc.getVersion());
			orderResult.setSign_type(pc.getSignType());
			orderResult.setNo_order(orderNo);
			// 加签名
	        String sign = LLPayUtil.addSign(JSON.parseObject(JSON
	                .toJSONString(orderResult)), pc.getTraderpriKey(),
	                pc.getMd5Key());
	        orderResult.setSign(sign);
			String reqJson = JSON.toJSONString(orderResult);
			logger.info("订单查询请求参数：" + reqJson);
			HttpRequestSimple httpclent = new HttpRequestSimple();
	        String resJson = httpclent.postSendHttp(postUrl + "orderquery.htm",
	                reqJson);
	        logger.info("订单查询响应参数：" + resJson);
	        OrderResultsRetBean ret = JSONObject.parseObject(resJson, OrderResultsRetBean.class);
			return ret;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
     * 银行卡卡bin信息查询
     * bankcardquery.htm
     * @param cardno
     * @return
     */
	public static String queryCardBin(String cardno){
        try {
        	PartnerConfig pc = new PartnerConfig();
			JSONObject reqObj = new JSONObject();
			reqObj.put("oid_partner", pc.getOidPartner());
			reqObj.put("card_no", cardno);
			reqObj.put("sign_type", pc.getSignType());
			reqObj.put("pay_type", "D");//2：快捷支付 （默认） D：认证支付
			reqObj.put("flag_amt_limit", "1");//是否返回限额标识0：不返回（默认）1：返回
			String sign = LLPayUtil.addSign(reqObj, pc.getTraderpriKey(),
			        pc.getMd5Key());
			reqObj.put("sign", sign);
			String reqJSON = reqObj.toString();
			logger.info("银行卡卡bin信息查询请求报文[" + reqJSON + "]");
			String resJSON = HttpRequestSimple.getInstance().postSendHttp(
					postUrl + "bankcardquery.htm", reqJSON);
			logger.info("银行卡卡bin信息查询响应报文[" + resJSON + "]");
			return resJSON;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
	 /**
     * 用户已绑定银行列表查询
     * userbankcard.htm
     * @param userId 用户id
     * @return
     */
    public static String queryBankcardList(long userId){
        try {
        	PartnerConfig pc = new PartnerConfig();
			JSONObject reqObj = new JSONObject();
			reqObj.put("oid_partner", pc.getOidPartner());
			reqObj.put("user_id", userId);
			reqObj.put("offset", "0");
			reqObj.put("pay_type", "D");//2：快捷支付 （默认） D：认证支付
			reqObj.put("sign_type", pc.getSignType());
			String sign = LLPayUtil.addSign(reqObj, pc.getTraderpriKey(),
			        pc.getMd5Key());
			reqObj.put("sign", sign);
			String reqJSON = reqObj.toString();
			logger.info("用户已绑定银行列表查询请求报文[" + reqJSON + "]");
			String resJSON = HttpRequestSimple.getInstance().postSendHttp(
					postUrl + "userbankcard.htm", reqJSON);
			logger.info("用户已绑定银行列表查询响应报文[" + resJSON + "]");
			return resJSON;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 提现申请
	 * cardandpay.htm
	 * @param withdrawInfo 提现参数
	 * @return
	 */
	public static String llcash(WithdrawBean withdrawInfo) {
		try{
			String reqJson = JSON.toJSONString(withdrawInfo);
			logger.info("提现请求参数：" + reqJson);
			HttpRequestSimple httpclent = new HttpRequestSimple();
	        String resJson = httpclent.postSendHttp(postUrl + "cardandpay.htm",
	                reqJson);
	        logger.info("提现响应参数：" + resJson);
//	        WithdrawRetBean ret = JSONObject.parseObject(resJson, WithdrawRetBean.class);
			return resJson;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 用户签约信息查询API接口
	 * userbankcard.htm
	 * @param bank 解绑参数
	 * @return
	 */
	public static UserBankCardRet userbankcard(UserBankCard ubc) {
		try{
			String reqJson = JSON.toJSONString(ubc);
			logger.info("用户签约信息查询请求参数：" + reqJson);
			HttpRequestSimple httpclent = new HttpRequestSimple();
	        String resJson = httpclent.postSendHttp(postUrl + "userbankcard.htm",
	                reqJson);
	        logger.info("用户签约信息查询响应参数：" + resJson);
			UserBankCardRet ret = JSONObject.parseObject(resJson, UserBankCardRet.class);
			return ret;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 /**
	  * 模拟商户创建订单
	  * @param type 订单类型，0支付，1提现
	  * @param money 订单金额
	  * @param order 订单描述
	  * @return
	  */
    public static OrderInfo createOrder(int type,String goods,String money){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setNo_order("11"+OrderNoUtils.getSerialNumber());
        orderInfo.setDt_order(LLPayUtil.getCurrentDateTimeStr());
        orderInfo.setMoney_order(money);
        orderInfo.setName_goods(goods);
        orderInfo.setStatus(0);
        if(type==0){        	
        	orderInfo.setInfo_order("用户购买" + goods);
        }else{
        	orderInfo.setInfo_order("用户提现" + goods);
        }
        return orderInfo;
    }
    
    /**
     * 银行支持额度信息查询
     * supportbankquery.htm
     * @param cardno
     * @return
     */
	public static String querySupportBank(SupportBank sb){
        try {
        	PartnerConfig pc = new PartnerConfig();
			JSONObject reqObj = new JSONObject();
			reqObj.put("oid_partner", pc.getOidPartner());
			reqObj.put("api_version", pc.getVersion());
			reqObj.put("sign_type", pc.getSignType());
			if(sb!=null&&sb.getBank_code()!=null&&!sb.getBank_code().equals("")){				
				reqObj.put("bank_code", sb.getBank_code());
				reqObj.put("card_type", "2");//银行卡类型,2借记卡，3信用卡
			}
			reqObj.put("product_type", "1");// 1：认证支付
			reqObj.put("pay_chnl", sb.getPay_chnl());//支付渠道类型，13PCweb端，16wap端
			String sign = LLPayUtil.addSign(reqObj, pc.getTraderpriKey(),
			        pc.getMd5Key());
			reqObj.put("sign", sign);
			String reqJSON = reqObj.toString();
			logger.info("银行支持额度信息查询请求报文[" + reqJSON + "]");
			String resJSON = HttpRequestSimple.getInstance().postSendHttp(
					"https://yintong.com.cn/queryapi/supportbankquery.htm", reqJSON);
			logger.info("银行支持额度信息查询响应报文[" + resJSON + "]");
			return resJSON;
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	
}
