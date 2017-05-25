package com.rongdu.p2psys.account.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 支付接口
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_pay")
public class Pay implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 标识
	 */
	private String nid;
	/**
	 * 是否开启,0:关闭,1:开启
	 */
	private int enable;
	/**
	 * 是否开启直连,0:关闭,1:开启
	 */
	private int enableDirect;
	/**
	 * 商品编号
	 */
	private String merchantId;
	/**
	 * 商品key
	 */
	private String goodsKey;
	/**
	 * 充值费率
	 */
	private double rechargeFee;
	/**
	 * 请求地址
	 */
	private String requestUrl;
	/**
	 * 回调地址
	 */
	private String returnUrl;
	/**
	 * 装入账户
	 */
	private String intoAccount;
	/**
	 * 编码格式
	 */
	private String chartset;
	/**
	 * 加密方式
	 */
	private String signType;
	/**
	 * 支付方式
	 */
	private String payStyle;
	/**
	 * 卖家email
	 */
	private String sellerEmail;
	/**
	 * 处理模式
	 */
	private String transport;
	/**
	 * 商品描述
	 */
	private String orderDescription;
	/**
	 * 网关url
	 */
	private String gatewayUrl;
	/**
	 * 订单查询url
	 */
	private String orderInquireUrl;
	/**
	 * 证书路径
	 */
	private String certPosition;
	/**
	 * 终端号
	 */
	private String terminalId;
	/**
	 * 排序
	 */
	private int sort;

	/**
	 * 图片路径
	 */
	private String imageUrl;

	/**
	 * 获取主键ID
	 * 
	 * @return 主键ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键ID
	 * 
	 * @param id 要设置的主键ID
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name 要设置的名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取标识
	 * 
	 * @return 标识
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置标识
	 * 
	 * @param nid 要设置的标识
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}

	/**
	 * 获取是否开启,0:关闭,1:开启
	 * 
	 * @return 是否开启,0:关闭,1:开启
	 */
	public int getEnable() {
		return enable;
	}

	/**
	 * 设置是否开启,0:关闭,1:开启
	 * 
	 * @param enable 要设置的是否开启,0:关闭,1:开启
	 */
	public void setEnable(int enable) {
		this.enable = enable;
	}

	/**
	 * 获取是否开启直连,0:关闭,1:开启
	 * 
	 * @return 是否开启直连,0:关闭,1:开启
	 */
	public int getEnableDirect() {
		return enableDirect;
	}

	/**
	 * 设置是否开启直连,0:关闭,1:开启
	 * 
	 * @param enableDirect 要设置的是否开启直连,0:关闭,1:开启
	 */
	public void setEnableDirect(int enableDirect) {
		this.enableDirect = enableDirect;
	}

	/**
	 * 获取商品编号
	 * 
	 * @return 商品编号
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * 设置商品编号
	 * 
	 * @param merchantId 要设置的商品编号
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * 获取商品key
	 * 
	 * @return 商品key
	 */
	public String getGoodsKey() {
		return goodsKey;
	}

	/**
	 * 设置商品key
	 * 
	 * @param goodsKey 要设置的商品key
	 */
	public void setGoodsKey(String goodsKey) {
		this.goodsKey = goodsKey;
	}

	/**
	 * 获取充值费率
	 * 
	 * @return 充值费率
	 */
	public double getRechargeFee() {
		return rechargeFee;
	}

	/**
	 * 设置充值费率
	 * 
	 * @param rechargeFee 要设置的充值费率
	 */
	public void setRechargeFee(double rechargeFee) {
		this.rechargeFee = rechargeFee;
	}

	/**
	 * 获取请求地址
	 * 
	 * @return 请求地址
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

	/**
	 * 设置请求地址
	 * 
	 * @param requestUrl 要设置的请求地址
	 */
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	/**
	 * 获取回调地址
	 * 
	 * @return 回调地址
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

	/**
	 * 设置回调地址
	 * 
	 * @param returnUrl 要设置的回调地址
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	/**
	 * 获取装入账户
	 * 
	 * @return 装入账户
	 */
	public String getIntoAccount() {
		return intoAccount;
	}

	/**
	 * 设置装入账户
	 * 
	 * @param intoAccount 要设置的装入账户
	 */
	public void setIntoAccount(String intoAccount) {
		this.intoAccount = intoAccount;
	}

	/**
	 * 获取编码格式
	 * 
	 * @return 编码格式
	 */
	public String getChartset() {
		return chartset;
	}

	/**
	 * 设置编码格式
	 * 
	 * @param chartset 要设置的编码格式
	 */
	public void setChartset(String chartset) {
		this.chartset = chartset;
	}

	/**
	 * 获取加密方式
	 * 
	 * @return 加密方式
	 */
	public String getSignType() {
		return signType;
	}

	/**
	 * 设置加密方式
	 * 
	 * @param signType 要设置的加密方式
	 */
	public void setSignType(String signType) {
		this.signType = signType;
	}

	/**
	 * 获取支付方式
	 * 
	 * @return 支付方式
	 */
	public String getPayStyle() {
		return payStyle;
	}

	/**
	 * 设置支付方式
	 * 
	 * @param payStyle 要设置的支付方式
	 */
	public void setPayStyle(String payStyle) {
		this.payStyle = payStyle;
	}

	/**
	 * 获取卖家email
	 * 
	 * @return 卖家email
	 */
	public String getSellerEmail() {
		return sellerEmail;
	}

	/**
	 * 设置卖家email
	 * 
	 * @param sellerEmail 要设置的卖家email
	 */
	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	/**
	 * 获取处理模式
	 * 
	 * @return 处理模式
	 */
	public String getTransport() {
		return transport;
	}

	/**
	 * 设置处理模式
	 * 
	 * @param transport 要设置的处理模式
	 */
	public void setTransport(String transport) {
		this.transport = transport;
	}

	/**
	 * 获取商品描述
	 * 
	 * @return 商品描述
	 */
	public String getOrderDescription() {
		return orderDescription;
	}

	/**
	 * 设置商品描述
	 * 
	 * @param orderDescription 要设置的商品描述
	 */
	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	/**
	 * 获取网关url
	 * 
	 * @return 网关url
	 */
	public String getGatewayUrl() {
		return gatewayUrl;
	}

	/**
	 * 设置网关url
	 * 
	 * @param gatewayUrl 要设置的网关url
	 */
	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	/**
	 * 获取订单查询url
	 * 
	 * @return 订单查询url
	 */
	public String getOrderInquireUrl() {
		return orderInquireUrl;
	}

	/**
	 * 设置订单查询url
	 * 
	 * @param orderInquireUrl 要设置的订单查询url
	 */
	public void setOrderInquireUrl(String orderInquireUrl) {
		this.orderInquireUrl = orderInquireUrl;
	}

	/**
	 * 获取证书路径
	 * 
	 * @return 证书路径
	 */
	public String getCertPosition() {
		return certPosition;
	}

	/**
	 * 设置证书路径
	 * 
	 * @param certPosition 要设置的证书路径
	 */
	public void setCertPosition(String certPosition) {
		this.certPosition = certPosition;
	}

	/**
	 * 获取终端号
	 * 
	 * @return 终端号
	 */
	public String getTerminalId() {
		return terminalId;
	}

	/**
	 * 设置终端号
	 * 
	 * @param terminalId 要设置的终端号
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * 设置排序
	 * 
	 * @param sort 要设置的排序
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}

	/**
	 * 获取图片路径
	 * 
	 * @return
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * 设置图片路径
	 * 
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
