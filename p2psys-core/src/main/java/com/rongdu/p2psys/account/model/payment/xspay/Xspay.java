package com.rongdu.p2psys.account.model.payment.xspay;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.RechargeUtil;
import com.rongdu.p2psys.account.constant.RechargeConstant;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 新生支付
 * 
 * @author fxx
 * @version 2.0
 * @since 2014年3月24日
 */
public class Xspay extends BasePayment {

	private String version; // 网关版本号
	private String url = "http://www.hnapay.com/website/pay.htm"; // 提交的路径
	private String serialID; // 流水号
	private String submitTime; // 订单提交时间
	private String failureTime; // 订单失效时间
	private String customerIP; // 客户下单域名及IP customerIP
	private String orderDetails;// 订单明细信息
	private String totalAmount;// 订单总金额
	private String type;// 交易类型
	private String buyerMarked;// 付款方新生账户号
	private String payType; // 付款方式
							// ALL：全部（默认），ACCT_RMB：账户支付，BANK_B2C：网银B2C支付，BANK_B2B：网银B2B支付
	private String orgCode; // 目标资金机构代码
	private String currencyCode;// 交易币种1：人民币（默认）,2：预付卡（选择用预付费卡支付时，可选）,3：授信额度
	private String directFlag;// 是否直连 0：非直连 （默认）,1：直连
	private String borrowingMarked;// 资金来源借贷标识 0：无特殊要求（默认）,1：只借记,2：只贷记
	private String couponFlag; // 优惠券标识 couponFlag 1：可用 （默认）,0：不可用
	private String platformID; // 平台商ID
	private String returnUrl;// 商户回调地址 returnUrl
	private String noticeUrl; // 商户通知地址
	private String partnerID;// 商户ID
	private String remark;// 扩展字段
	private String charset;// 编码方式
	private String signType = "1";// 编码方式 1：RSA 方式（推荐）,2：MD5 方式
	private String signMsg;// 签名字符串
	private String orderID; // 订单id
	private String orderAmount;// 订单明细金额
	private String displayName;// 下单商户显示名
	private String goodsName;// 商品名称
	private String goodsCount;// 商品数量

	/** 下面是回调时需要接受的参数 **/
	private String resultCode;
	private String stateCode;
	private String payAmount;
	private String acquiringTime;
	private String completeTime;
	private String orderNo;

	@Override
	public void init(AccountRecharge recharge) {
		PayService payService = (PayService) BeanUtil.getBean("payService");
		this.pay = payService.findByNid(RechargeConstant.XSPAY);
		this.payname = "xspay";
		this.orderPrefix = "H";

		this.setOrderID(RechargeUtil.generateTradeNO(recharge.getUser().getUserId(), this.orderPrefix));
		recharge.setTradeNo(this.getOrderID());

		String money = String.valueOf((long) (recharge.getMoney() * 100));
		this.totalAmount = money;
		this.orderAmount = money;
		this.goodsCount = "1";
		this.customerIP = Global.getIP();
		this.remark = recharge.getRemark();

		this.version = "2.6";
		this.payType = "ALL";
		this.currencyCode = "1";
		this.borrowingMarked = "0";
		this.couponFlag = "0";
		this.type = "1000";
		this.currencyCode = "1";
		this.directFlag = "0";
		this.couponFlag = "1";
		this.charset = "1";
		this.platformID = "";
		this.buyerMarked = "";
		this.orgCode = "";

		this.serialID = DateUtil.getNowTimeStr();
		this.returnUrl = Global.getValue("weburl") + pay.getReturnUrl();
		this.noticeUrl = this.returnUrl;
		this.partnerID = pay.getMerchantId();
		this.signType = pay.getSignType();
		this.url = pay.getGatewayUrl();
		Calendar cal = Calendar.getInstance();
		this.submitTime = DateUtil.dateStr(cal.getTime(), "yyyyMMddhhmmss");
		this.failureTime = DateUtil.dateStr(cal.getTime(), "yyyyMMddhhmmss");
		String[] fieldArr = new String[] { this.tranNo, "", recharge.getMoney() + "", Global.getValue("webname"),
				Global.getValue("webname"), "1" };
		this.orderDetails = StringUtils.join(fieldArr, ',');
	}

	@Override
	public String encrypt(String sign) {
		try {
			if ("2".equals(this.signType)) {
				String pkey = "";
				if (pay != null) {
					pkey = pay.getGoodsKey();
				}
				sign = sign + "&pkey=" + pkey;
				this.encryptSign = Hnapay.genSignByMD5(sign, CharsetTypeEnum.UTF8);
			} else if ("1".equals(this.signType)) {
				this.encryptSign = Hnapay.genSignByRSA(sign, CharsetTypeEnum.UTF8);
			}
		} catch (Exception e) {
			new Exception("签名失败！");
		}
		return encryptSign;
	}

	@Override
	public void sign() {
		StringBuffer url = new StringBuffer();
		url.append("version=").append(this.getVersion()).append("&");// 网关版本号
		url.append("serialID=").append(this.getSerialID()).append("&");// 字符集 1
																		// GBK 2
																		// UTF-8
		url.append("submitTime=").append(this.getSubmitTime()).append("&");// 1
																			// 中文
																			// 2
																			// 英文
		url.append("failureTime=").append(this.getFailureTime()).append("&");// 报文加密方式
																				// 1
																				// MD5
																				// 2
																				// SHA
		url.append("customerIP=").append(this.getCustomerIP()).append("&");// 交易代码
																			// 本域指明了交易的类型，支付网关接口必须为8888
		url.append("orderDetails=").append(this.getOrderDetails()).append("&");// 商户代码
		url.append("totalAmount=").append(this.getTotalAmount()).append("&");// 订单号
		url.append("type=").append(this.getType()).append("&");// 交易金额
		url.append("buyerMarked=").append(this.getBuyerMarked()).append("&");//

		url.append("payType=").append(this.getPayType()).append("&");// 156，代表人民币
		url.append("orgCode=").append(this.getOrgCode()).append("&");// 商户前台通知地址
		url.append("currencyCode=").append(this.getCurrencyCode()).append("&");

		url.append("directFlag=").append(this.getDirectFlag()).append("&");// 本域为订单发起的交易时间
		url.append("borrowingMarked=").append(this.getBorrowingMarked()).append("&");// 本域指卖家在国付宝平台开设的国付宝账户号
		url.append("couponFlag=").append(this.getCouponFlag()).append("&");// 发起交易的客户IP地址
		url.append("platformID=").append(this.getPlatformID()).append("&");// 0不允许重复
																			// 1
																			// 允许重复

		url.append("returnUrl=").append(this.getReturnUrl()).append("&");
		url.append("noticeUrl=").append(this.getNoticeUrl()).append("&");
		url.append("partnerID=").append(this.getPartnerID()).append("&");
		url.append("remark=").append(this.getRemark()).append("&");
		url.append("charset=").append(this.getCharset()).append("&");
		url.append("signType=").append(this.getSignType());
		this.sign = url.toString();
		this.signMsg = url.toString();
	}

	@Override
	public String encryptSign() {
		this.encryptSign = encrypt(sign);
		return encryptSign;
	}

	@Override
	public String localReturnSign() {
		String signStr = "orderID=" + this.tranNo + "&resultCode=" + resultCode + "&stateCode=" + stateCode
				+ "&orderAmount=" + orderAmount + "&payAmount=" + payAmount + "&acquiringTime=" + acquiringTime
				+ "&completeTime=" + completeTime + "&orderNo=" + orderNo + "&partnerID=" + partnerID + "&remark="
				+ remark + "&charset=" + charset + "&signType=" + signType;
		this.localReturnSign = signStr;
		return localReturnSign;
	}

	@Override
	public String encryptLocalReturnSign() {
		this.encryptSign = encrypt(localReturnSign);
		return encryptSign;
	}

	@Override
	public String remoteReturnSin() {
		return signMsg;
	}

	@Override
	public Object protype() {
		return this;
	}

	@Override
	public boolean reponseSuccess() {
		boolean result = false;
		try {
			if ("2".equals(signType)) {
				// String pkey =
				// "30819f300d06092a864886f70d010101050003818d003081890281810096db29e2b96a2a421db86ad24db9f7770944ad21398d56fd72d29083c27ab40658b5f96e9f296a27f6c78798410d0fb9baab4d60a9a4229c84962c2cdcf59be5249f4a10b3f90ce6832d18b1ff68429d117a0234eef89df8ba6ef769f3d85cedbd7bf8e58a0c79cb4f6b44684f336b7f04bc4d247c0996ebbd1c8dc0b4025dc10203010001";
				String pkey = pay.getGoodsKey();
				localReturnSign = "&pkey=" + pkey;
				result = Hnapay.verifySignatureByMD5(localReturnSign, signMsg, CharsetTypeEnum.UTF8);
			} else if ("1".equals(signType)) {
				// RSA：网关公钥钥解签
				result = Hnapay.verifySignatureByRSA(localReturnSign, signMsg, CharsetTypeEnum.UTF8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = result & (stateCode != null && stateCode.equals("2"));
		return result;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSerialID() {
		return serialID;
	}

	public void setSerialID(String serialID) {
		this.serialID = serialID;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getFailureTime() {
		return failureTime;
	}

	public void setFailureTime(String failureTime) {
		this.failureTime = failureTime;
	}

	public String getCustomerIP() {
		return customerIP;
	}

	public void setCustomerIP(String customerIP) {
		this.customerIP = customerIP;
	}

	public String getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(String orderDetails) {
		this.orderDetails = orderDetails;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBuyerMarked() {
		return buyerMarked;
	}

	public void setBuyerMarked(String buyerMarked) {
		this.buyerMarked = buyerMarked;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(String directFlag) {
		this.directFlag = directFlag;
	}

	public String getBorrowingMarked() {
		return borrowingMarked;
	}

	public void setBorrowingMarked(String borrowingMarked) {
		this.borrowingMarked = borrowingMarked;
	}

	public String getCouponFlag() {
		return couponFlag;
	}

	public void setCouponFlag(String couponFlag) {
		this.couponFlag = couponFlag;
	}

	public String getPlatformID() {
		return platformID;
	}

	public void setPlatformID(String platformID) {
		this.platformID = platformID;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNoticeUrl() {
		return noticeUrl;
	}

	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	public String getPartnerID() {
		return partnerID;
	}

	public void setPartnerID(String partnerID) {
		this.partnerID = partnerID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getAcquiringTime() {
		return acquiringTime;
	}

	public void setAcquiringTime(String acquiringTime) {
		this.acquiringTime = acquiringTime;
	}

	public String getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
