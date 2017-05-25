package com.rongdu.p2psys.nb.wechat.util;

import java.util.Date;

public class WechatMessage
{
	//跳转地址
	private String url;
	//金额
	private double money;
	//产品名称
	private String productName;
	//前置标题
	private String firstData;
	//openId
	private String openId;
	//模板类型
	private String type;
	//收益
	private double profit;
	//产品期限
	private String productDate;
	//产品收益率
	private String productProfit;
	//产品特点
	private String productRemark;
	//交易类型  充值 / 提现
	private String businessType;
	//充值金额
	private double buyMoney;
	//取出金额
	private double sellAmount;
	//账户余额
	private double leftMoney;
	//功能名称
	private String FunctionName;
	//公众号Id
	private String appId;
	//公众号appSecret
	private String appSecret;
	//所用时间信息
	private Date dateInfo;
	//还款方式
	private String repaymentMethod;
	//备注
	private String remark;
	
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public double getMoney()
	{
		return money;
	}
	public void setMoney(double money)
	{
		this.money = money;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	public String getFirstData()
	{
		return firstData;
	}
	public void setFirstData(String firstData)
	{
		this.firstData = firstData;
	}
	public String getOpenId()
	{
		return openId;
	}
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public double getProfit()
	{
		return profit;
	}
	public void setProfit(double profit)
	{
		this.profit = profit;
	}
	public String getProductDate()
	{
		return productDate;
	}
	public void setProductDate(String productDate)
	{
		this.productDate = productDate;
	}
	public String getProductProfit()
	{
		return productProfit;
	}
	public void setProductProfit(String productProfit)
	{
		this.productProfit = productProfit;
	}
	public String getProductRemark()
	{
		return productRemark;
	}
	public void setProductRemark(String productRemark)
	{
		this.productRemark = productRemark;
	}
	public String getBusinessType()
	{
		return businessType;
	}
	public void setBusinessType(String businessType)
	{
		this.businessType = businessType;
	}
	public double getBuyMoney()
	{
		return buyMoney;
	}
	public void setBuyMoney(double buyMoney)
	{
		this.buyMoney = buyMoney;
	}
	public double getSellAmount()
	{
		return sellAmount;
	}
	public void setSellAmount(double sellAmount)
	{
		this.sellAmount = sellAmount;
	}
	public String getFunctionName()
	{
		return FunctionName;
	}
	public void setFunctionName(String functionName)
	{
		FunctionName = functionName;
	}
	public String getAppId()
	{
		return appId;
	}
	public void setAppId(String appId)
	{
		this.appId = appId;
	}
	public String getAppSecret()
	{
		return appSecret;
	}
	public void setAppSecret(String appSecret)
	{
		this.appSecret = appSecret;
	}
	public Date getDateInfo()
	{
		return dateInfo;
	}
	public void setDateInfo(Date dateInfo)
	{
		this.dateInfo = dateInfo;
	}
	public String getRepaymentMethod()
	{
		return repaymentMethod;
	}
	public void setRepaymentMethod(String repaymentMethod)
	{
		this.repaymentMethod = repaymentMethod;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public double getLeftMoney()
	{
		return leftMoney;
	}
	public void setLeftMoney(double leftMoney)
	{
		this.leftMoney = leftMoney;
	}
	
	
	
}
