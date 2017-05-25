package com.rongdu.p2psys.nb.wechat.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class WechatMessageUtil
{
	public static String getMessageByType(WechatMessage wechatMessage)
	{
		
		if(WechatMessageData.Project_Line.equals(wechatMessage.getType()))
		{
			return getProjectLine(wechatMessage);
		}
		if(WechatMessageData.Acoount_Change.equals(wechatMessage.getType()))
		{
			return getAccountChange(wechatMessage);
		}
		if(WechatMessageData.Has_Rate.equals(wechatMessage.getType()))
		{
			return getHasRate(wechatMessage);
		}
		if(WechatMessageData.Start_Rate.equals(wechatMessage.getType()))
		{
			return getStartRate(wechatMessage);
		}
		
		return null;
	}
	
	/**
	 * 收益起始通知 
	 * @param wechatMessage
	 * @return
	 */
	private static String getStartRate(WechatMessage wechatMessage)
	{
		String msg = " { "+
			      "\"touser\":\""+wechatMessage.getOpenId()+"\","+
			      "\"template_id\":\""+WechatMessageData.Start_Rate_Id+"\", "+
			      "\"url\":\""+wechatMessage.getUrl()+"\","+
			      "\"topcolor\":\"#FF0000\","+
			      "\"data\":{ "+
			            "\"first\": {"+
			                 "\"value\":\""+wechatMessage.getFirstData()+"\","+
			                 "\"color\":\"#173177\" "+
			                 "},"+
				             "\"publicproductname\": {"+
				             "\"value\":\""+wechatMessage.getProductName()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
					          "\"expectedyield\": {"+
				             "\"value\":\""+wechatMessage.getProductProfit()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
				             "\"term\": {"+
				             "\"value\":\""+wechatMessage.getProductDate()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
					         "\"remark\":{"+
					         "\"value\":\""+wechatMessage.getRemark()+"\","+
					         "\"color\":\"#000000\" "+
					         "}"+
					       "}"+
			         "}";
			return msg; 
	}

	/**
	 * 项目起息
	 * @param wechatMessage
	 * @return
	 */
	public static String getHasRate(WechatMessage wechatMessage)
	{
		String msg = " { "+
			      "\"touser\":\""+wechatMessage.getOpenId()+"\","+
			      "\"template_id\":\""+WechatMessageData.Has_Rate_Id+"\", "+
			      "\"url\":\""+wechatMessage.getUrl()+"\","+
			      "\"topcolor\":\"#FF0000\","+
			      "\"data\":{ "+
			            "\"first\": {"+
			                 "\"value\":\""+wechatMessage.getFirstData()+"\","+
			                 "\"color\":\"#173177\" "+
			                 "},"+
				             "\"keyword1\": {"+
				             "\"value\":\""+wechatMessage.getProductName()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
					          "\"keyword2\": {"+
				             "\"value\":\""+wechatMessage.getProductProfit()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
				             "\"keyword3\": {"+
				             "\"value\":\""+wechatMessage.getProductDate()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
				             "\"keyword4\": {"+
				             "\"value\":\""+getMoney(wechatMessage.getBuyMoney())+"元\","+
				             "\"color\":\"#173177\" "+
				             "},"+
				             "\"keyword5\": {"+
				             "\"value\":\""+getMoney(wechatMessage.getMoney())+"元\","+
				             "\"color\":\"#173177\" "+
				             "},"+
					         "\"remark\":{"+
					         "\"value\":\""+wechatMessage.getRemark()+"\","+
					         "\"color\":\"#000000\" "+
					         "}"+
					       "}"+
			         "}";
			return msg; 
	}


	/**
	 * 项目上线
	 * @param wechatMessage
	 * @return
	 */
	public static String getProjectLine(WechatMessage wechatMessage)
	{
		String msg = " { "+
			      "\"touser\":\""+wechatMessage.getOpenId()+"\","+
			      "\"template_id\":\""+WechatMessageData.Project_Line_Id+"\", "+
			      "\"url\":\""+wechatMessage.getUrl()+"\","+
			      "\"topcolor\":\"#FF0000\","+
			      "\"data\":{ "+
			            "\"first\": {"+
			                 "\"value\":\""+wechatMessage.getFirstData()+"\","+
			                 "\"color\":\"#173177\" "+
			                 "},"+
				             "\"keyword1\": {"+
				             "\"value\":\""+wechatMessage.getProductName()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
					          "\"keyword2\": {"+
				             "\"value\":\""+wechatMessage.getProductProfit()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
				             "\"keyword3\": {"+
				             "\"value\":\""+wechatMessage.getProductDate()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
				             "\"keyword4\": {"+
				             "\"value\":\""+wechatMessage.getRepaymentMethod()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
					         "\"remark\":{"+
					         "\"value\":\""+wechatMessage.getRemark()+"\","+
					         "\"color\":\"#000000\" "+
					         "}"+
					       "}"+
			         "}";
			return msg; 
	}

	/**
	 * 资金变动通知
	 * @param wechatMessage
	 * @return
	 */
	public static String getAccountChange(WechatMessage wechatMessage)
	{
		String msg = " { "+
			      "\"touser\":\""+wechatMessage.getOpenId()+"\","+
			      "\"template_id\":\""+WechatMessageData.Acoount_Change_Id+"\", "+
			      "\"url\":\""+wechatMessage.getUrl()+"\","+
			      "\"topcolor\":\"#FF0000\","+
			      "\"data\":{ "+
			            "\"first\": {"+
			                 "\"value\":\""+wechatMessage.getFirstData()+"\","+
			                 "\"color\":\"#173177\" "+
			                 "},"+
				             "\"date\": {"+
				             "\"value\":\""+wechatMessage.getProductDate()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
					          "\"tradetype\": {"+
				             "\"value\":\""+wechatMessage.getBusinessType()+"\","+
				             "\"color\":\"#173177\" "+
				             "},"+
				             "\"tradenum\": {"+
				             "\"value\":\""+getMoney(wechatMessage.getBuyMoney())+"元\","+
				             "\"color\":\"#173177\" "+
				             "},"+
				             "\"traderemain\": {"+
				             "\"value\":\""+getMoney(wechatMessage.getLeftMoney())+"元\","+
				             "\"color\":\"#173177\" "+
				             "},"+
					         "\"remark\":{"+
					         "\"value\":\""+wechatMessage.getRemark()+"\","+
					         "\"color\":\"#000000\" "+
					         "}"+
					       "}"+
			         "}";
			return msg; 
	}
	
	public static String getMoney(double money)
	{
		NumberFormat nf = new DecimalFormat(",###.##");
		
		return nf.format(money);
	}
}
