package com.rongdu.p2psys.tpp.yjf.model;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.rongdu.common.util.MD5Util;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.ReflectUtils;
import com.rongdu.p2psys.tpp.yjf.tool.HttpHelper;


public class PayModel {
	
	private static final Logger logger=Logger.getLogger(PayModel.class);
	
	protected String  service;
	private String  partnerId;
	private String  signType = "MD5";
	private String  sign;
	protected String orderNo;
	private String key;
	private String url;
	protected String tradeMemo;
	private String[] paramNames=new String[]{};
	public static boolean DEBUG_MODE=true;
	public PayModel() {
		super();
		init();
	}
	
	/**
	 * 是否开通线上环境配置。
	 * @return
	 */
	public static boolean isOnlineConfig(){
		return "1".equals(Global.getValue("config_online"));
	}
	
	/**
	 * 是否开通第三方接口
	 * @return
	 */
	public static boolean isOpenApi(){
		return "1".equals(Global.getValue("is_open_deposit"));
	}
	

	public void init(){
		setPartnerId(Global.getValue("yjf_partnerId"));
		setKey(Global.getValue("yjf_key"));
		if (isOnlineConfig()) {
			setUrl(Global.getValue("yjf_service_url")); //正式的地址
		} else {
			setUrl(Global.getValue("yjf_service_test_url")); //测试地址
		}
	}
	/**
	 * 产生签名
	 * @return String
	 */
	public String Createsign() {
		String[][] params = getParam();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			if (!"sign".equals(params[i][0])) {
				sb.append(params[i][0]);
				sb.append("=");
				sb.append(params[i][1]);
				if (i < params.length - 1) {
					sb.append("&");
				}
			}
		}
		sb.append(key);
		sign = MD5Util.MD5Encode(sb.toString(), "utf-8");
		setSign(sign);
		return sign;
	}
	
	public String submit() throws Exception{
		Createsign();
		HttpHelper http = new HttpHelper(getUrl(), getParam(), "utf-8");
		logger.debug(getUrl());
		String resp = "";
		resp = http.execute();
		if (resp != null) {
			logger.info("请求返回结果集:" + resp);
		}
		return resp;
	}

	private String[][] getParam(){
		String[] paramNames=getParamNames();
		//排序
		paramNames = stringSort(paramNames);
		String[][] namePair=new String[paramNames.length][2];
		for(int i=0;i<paramNames.length;i++){
			String name=paramNames[i];
			Object result=ReflectUtils.invokeGetMethod(getClass(), this, name);
			String value=(result==null?"":result.toString());
			namePair[i][0]=name;
			namePair[i][1]=value;
		}
		return namePair;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTradeMemo() {
		return tradeMemo;
	}

	public void setTradeMemo(String tradeMemo) {
		this.tradeMemo = tradeMemo;
	}
	public static String[] stringSort(String[] str) {
		MyString mySs[] = new MyString[str.length];// 创建自定义排序的数组
		for (int i = 0; i < str.length; i++) {
			mySs[i] = new MyString(str[i]);
		}
		Arrays.sort(mySs);// 排序
		String[] str2 = new String[mySs.length];
		for (int i = 0; i < mySs.length; i++) {
			str2[i] = mySs[i].s;
		}
		return str2;
	}
}
class MyString implements Comparable<MyString> {
	public String s;// 包装String

	public MyString(String s) {
		this.s = s;
	}

	@Override
	public int compareTo(MyString o) {
		if (o == null || o.s == null)
			return 1;
		return s.compareTo(o.s);
	}
	
}
