package com.rongdu.p2psys.account.model.payment.llPay.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.WithdrawBean;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;

/**
* 提现测试
* @author cgw
* @date:2015-5-22 下午17:10:15
* @version :1.0
*
*/
public class Withdraw extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private static Logger logger = Logger.getLogger(SignHelper.class);
    
    /**
     * 提现处理
     * @param req
     * @param order
     * @throws ParseException 
     */
    public static WithdrawBean withdraw(HttpServletRequest req,LianlPay llpay, OrderInfo order) throws ParseException{
    	WithdrawBean withdrawInfo = new WithdrawBean();
    	SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	PartnerConfig pc = new PartnerConfig();
        // 构造支付请求对象
        withdrawInfo.setOid_partner(llpay.getOidPartner());
        withdrawInfo.setSign_type(llpay.getSignType());
        withdrawInfo.setNo_order(order.getNo_order());
        withdrawInfo.setDt_order(dataFormat.format(dataFormat.parse(order.getDt_order())));
        withdrawInfo.setInfo_order(order.getInfo_order());
        withdrawInfo.setMoney_order(order.getMoney_order());
        withdrawInfo.setNotify_url(llpay.getNotifyCUrl());
        withdrawInfo.setAcct_name(llpay.getAccName());
        withdrawInfo.setFlag_card("0");   	//对公对私标志，0-对私 1 –对公
        withdrawInfo.setCard_no(llpay.getCardNo());
        withdrawInfo.setBank_code(llpay.getBankCode());
        withdrawInfo.setProvince_code(llpay.getProvince());//开户行所在省编码
        withdrawInfo.setCity_code(llpay.getCity());//开户行所在市编码
        withdrawInfo.setBrabank_name(llpay.getBrabank()); //开户支行名称

        // 加签名
        String sign = LLPayUtil.addSign(JSON.parseObject(JSON
                .toJSONString(withdrawInfo)), pc.getTraderpriKey(),llpay.getSignType());
        withdrawInfo.setSign(sign);

        if(req!=null){        	
        	req.setAttribute("oid_partner", withdrawInfo.getOid_partner());
        	req.setAttribute("sign_type", withdrawInfo.getSign_type());
        	req.setAttribute("no_order", withdrawInfo.getNo_order());
        	req.setAttribute("dt_order", withdrawInfo.getDt_order());
        	req.setAttribute("info_order", withdrawInfo.getInfo_order());
        	req.setAttribute("money_order", withdrawInfo.getMoney_order());
        	req.setAttribute("notify_url", withdrawInfo.getNotify_url());
        	req.setAttribute("sign", withdrawInfo.getSign());
//        	req.setAttribute("req_url", ServerURLConfig.PAY_URL);
        }
        return withdrawInfo;
    }

}
