package com.rongdu.p2psys.account.model.payment.llPay.handler;

import com.alibaba.fastjson.JSON;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.model.PayDataBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.RetBean;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;

/**
* 支付结果异步通知接收服务接口
* @author cgw
* @date:2015-05-24
* @version :1.0
*
*/
public class ReceiveNotify{
//	private static Logger logger = Logger.getLogger(ReceiveNotify.class);

    
    public void notify(String reqStr){
//        resp.setCharacterEncoding("UTF-8");
        System.out.println("进入支付异步通知数据接收处理");
        RetBean retBean = new RetBean();
        PartnerConfig pc = new PartnerConfig();
//        String reqStr = LLPayUtil.readReqStr(req);
        if (LLPayUtil.isnull(reqStr)){
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
//            resp.getWriter().write(JSON.toJSONString(retBean));
//            resp.getWriter().flush();
            return;
        }
        System.out.println("接收支付异步通知数据：【" + reqStr + "】");
        try
        {
            if (!LLPayUtil.checkSign(reqStr, pc.getYtpubKey(),
                    pc.getMd5Key()))
            {
                retBean.setRet_code("9999");
                retBean.setRet_msg("交易失败");
//                resp.getWriter().write(JSON.toJSONString(retBean));
//                resp.getWriter().flush();
                System.out.println("支付异步通知验签失败");
                return;
            }
        } catch (Exception e){
            System.out.println("异步通知报文解析异常：" + e);
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
//            resp.getWriter().write(JSON.toJSONString(retBean));
//            resp.getWriter().flush();
            return;
        }
        retBean.setRet_code("0000");
        retBean.setRet_msg("交易成功");
//        resp.getWriter().write(JSON.toJSONString(retBean));
//        resp.getWriter().flush();
        System.out.println("支付异步通知数据接收处理成功");
        // 解析异步通知对象
        PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
        // TODO:更新订单，发货等后续处理
    }

}
