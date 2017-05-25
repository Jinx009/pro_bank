package com.rongdu.p2psys.account.model.payment.llPay.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.PaymentInfo;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;

/**
* 调用连连支付WEB支付服务接口
* @author cgw
* @date:2015-05-24
* @version :1.0
*
*/
public class ToPay{
	private static Logger logger = Logger.getLogger(ToPay.class);

    /**
     * 卡前置支付处理
     * @param type 支付方式0Web支付，1wap支付
     * @param req
     * @param llpay
     * @param order
     */
	public static void prepositPay(int type,HttpServletRequest req, LianlPay llpay, OrderInfo order){
		logger.info("llpay pay return json is :" + JSON.toJSONString(llpay));
		logger.info("llpay order return json is :" + JSON.toJSONString(order));
		PartnerConfig pc = new PartnerConfig();
        // 构造支付请求对象
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setVersion(llpay.getVersion());
        paymentInfo.setOid_partner(llpay.getOidPartner());
        paymentInfo.setUser_id(llpay.getUserId());
        paymentInfo.setSign_type(llpay.getSignType());
        paymentInfo.setBusi_partner(llpay.getBusiPartner());
        paymentInfo.setNo_order(order.getNo_order());
        paymentInfo.setDt_order(order.getDt_order());
        paymentInfo.setName_goods(order.getName_goods());
        paymentInfo.setInfo_order(order.getInfo_order());
        paymentInfo.setMoney_order(order.getMoney_order());
        paymentInfo.setNotify_url(llpay.getNotifyUrl());
        paymentInfo.setUserreq_ip(llpay.getUserreqIp());
        paymentInfo.setUrl_order(llpay.getUrlOrder());
        paymentInfo.setValid_order(llpay.getValidOrder());// 单位分钟，可以为空，默认7天
        paymentInfo.setRisk_item(createRiskItem(llpay.getAccName(),llpay.getUserId(),llpay.getAddTime(),llpay.getAccId()));
        paymentInfo.setTimestamp(LLPayUtil.getCurrentDateTimeStr());
        paymentInfo.setAcct_name(llpay.getAccName());
        paymentInfo.setId_no(llpay.getIdNo());
        if (!LLPayUtil.isnull(llpay.getNoAgree())){
            paymentInfo.setNo_agree(llpay.getNoAgree());
            paymentInfo.setBack_url(llpay.getBackUrl());
        } else {
            // 从系统中获取用户身份信息
            paymentInfo.setId_type(llpay.getIdType());//默认0身份证
            paymentInfo.setFlag_modify("0");
            paymentInfo.setCard_no(llpay.getCardNo());
            paymentInfo.setBack_url(llpay.getBackUrl());
        }
        if(type==1){//wap支付     
        	paymentInfo.setUrl_return(pc.getWapurlReturn());
        	paymentInfo.setApp_request("3");
        }else{
        	paymentInfo.setUrl_return(llpay.getUrlReturn());
        }
        // 加签名
        String sign = LLPayUtil.addSign(JSON.parseObject(JSON
                .toJSONString(paymentInfo)), pc.getTraderpriKey(),
                pc.getMd5Key());
        paymentInfo.setSign(sign);

        req.setAttribute("version", paymentInfo.getVersion());
        req.setAttribute("oid_partner", paymentInfo.getOid_partner());
        req.setAttribute("user_id", paymentInfo.getUser_id());
        req.setAttribute("sign_type", paymentInfo.getSign_type());
        req.setAttribute("busi_partner", paymentInfo.getBusi_partner());
        req.setAttribute("no_order", paymentInfo.getNo_order());
        req.setAttribute("dt_order", paymentInfo.getDt_order());
        req.setAttribute("name_goods", paymentInfo.getName_goods());
        req.setAttribute("info_order", paymentInfo.getInfo_order());
        req.setAttribute("money_order", paymentInfo.getMoney_order());
        req.setAttribute("notify_url", paymentInfo.getNotify_url());
        req.setAttribute("userreq_ip", paymentInfo.getUserreq_ip());
        req.setAttribute("url_order", paymentInfo.getUrl_order());
        req.setAttribute("valid_order", paymentInfo.getValid_order());
        req.setAttribute("timestamp", paymentInfo.getTimestamp());
        req.setAttribute("sign", paymentInfo.getSign());
        req.setAttribute("risk_item", paymentInfo.getRisk_item());
        req.setAttribute("no_agree", paymentInfo.getNo_agree());
        req.setAttribute("id_type", paymentInfo.getId_type());
        req.setAttribute("id_no", paymentInfo.getId_no());
        req.setAttribute("acct_name", paymentInfo.getAcct_name());
        req.setAttribute("flag_modify", paymentInfo.getFlag_modify());
        req.setAttribute("card_no", paymentInfo.getCard_no());
        req.setAttribute("back_url", paymentInfo.getBack_url());
        if(type==0){//web支付        	
        	req.setAttribute("url_return", paymentInfo.getUrl_return());
        	req.setAttribute("req_url", pc.getPayUrl());
        }else{//wap支付     
        	req.setAttribute("req_url", pc.getWappayUrl());
        	req.setAttribute("url_return", paymentInfo.getUrl_return());
        	String req_data = JSON.toJSONString(paymentInfo);
        	req.setAttribute("req_data", req_data);
        }
        logger.info("pay info is：" + JSON.toJSONString(paymentInfo));
    }

    /**
     * 根据连连支付风控部门要求的参数进行构造风控参数
     * @return
     */
    public static String createRiskItem(String accName,String userid,String addTime,String accid){
        JSONObject riskItemObj = new JSONObject();
        riskItemObj.put("user_info_full_name", accName);
        riskItemObj.put("frms_ware_category", "2009");
        riskItemObj.put("user_info_mercht_userno", userid);
        riskItemObj.put("user_info_dt_register", addTime);
        riskItemObj.put("user_info_id_no", accid);
        riskItemObj.put("user_info_identify_state", "0");
        logger.info("风控参数：" + riskItemObj.toString());
        return riskItemObj.toString();
    }

}
