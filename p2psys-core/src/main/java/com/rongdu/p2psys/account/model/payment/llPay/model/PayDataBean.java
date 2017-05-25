package com.rongdu.p2psys.account.model.payment.llPay.model;

import java.io.Serializable;

import com.rongdu.common.util.StringUtil;

/**
* 
* @author guoyx
* @date:May 27, 2013 8:12:30 PM
* @version :1.0
*
*/
public class PayDataBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String sign;                 // 加密签名
    private String sign_type;            // RSA 或者 MD5
    private String oid_partner;          // 商户编号
    private String dt_order;             // 商户订单时间
    private String no_order;             // 商户唯一订单号
    private String oid_paybill;          // 连连钱包支付单号
    private String money_order;          // 交易金额
    private String result_pay;           // 支付结果
    private String settle_date;          // 清算日期
    private String info_order;           // 订单描述
    private String pay_type;			 //支付方式
    private String bank_code;			 //银行编号
    private String acct_name;			 //用户姓名
    private String id_no;			 	 //身份证号
    private String id_type;			     //身份类型
    private String no_agree;			 //绑定协议号
    private String card_no;			     //银行卡号

//  {"acct_name":"陈岗伟","bank_code":"01050000","dt_order":"20150722101829","id_no":"610424199011233975","id_type":"0","info_order":"用户购买连连充值","money_order":"0.01","no_agree":"2015072105339372","no_order":"20150722101829","oid_partner":"201505221000337504","oid_paybill":"2015072200558604","pay_type":"D","result_pay":"SUCCESS","settle_date":"20150722","sign":"adc886fa35347d71069f101053d18f88","sign_type":"MD5"}
//  {"acct_name":"陈岗伟","bank_code":"01050000","card_no":"622700*********3414","dt_order":"20150722105539","id_no":"610424199011233975","id_type":"0","info_order":"用户购买连连充值","money_order":"0.01","no_agree":"2015072105339372","no_order":"20150722105539","oid_partner":"201505221000337504","oid_paybill":"2015072200582437","pay_type":"D","result_pay":"SUCCESS","settle_date":"20150722","sign":"ced5856971b396e89fc4fab6444da890","sign_type":"MD5"｝

	public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getSign_type()
    {
        return sign_type;
    }

    public void setSign_type(String sign_type)
    {
        this.sign_type = sign_type;
    }

    public String getOid_partner()
    {
        return oid_partner;
    }

    public void setOid_partner(String oid_partner)
    {
        this.oid_partner = oid_partner;
    }

    public String getDt_order()
    {
        return dt_order;
    }

    public void setDt_order(String dt_order)
    {
        this.dt_order = dt_order;
    }

    public String getNo_order()
    {
        return no_order;
    }

    public void setNo_order(String no_order)
    {
        this.no_order = no_order;
    }

    public String getOid_paybill()
    {
        return oid_paybill;
    }

    public void setOid_paybill(String oid_paybill)
    {
        this.oid_paybill = oid_paybill;
    }

    public String getMoney_order()
    {
        return money_order;
    }

    public void setMoney_order(String money_order)
    {
        this.money_order = money_order;
    }

    public String getResult_pay()
    {
        return result_pay;
    }

    public void setResult_pay(String result_pay)
    {
        this.result_pay = result_pay;
    }

    public String getSettle_date()
    {
        return settle_date;
    }

    public void setSettle_date(String settle_date)
    {
        this.settle_date = settle_date;
    }

    public String getInfo_order()
    {
        return info_order;
    }

    public void setInfo_order(String info_order)
    {
        this.info_order = info_order;
    }
	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getAcct_name() {
		return acct_name;
	}

	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}

	public String getCard_no() {
		//622700*********3414
		if (StringUtil.isNotBlank(card_no)) {
			return card_no.substring(card_no.length() - 4, card_no.length());
		}
		return "";
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	
}
