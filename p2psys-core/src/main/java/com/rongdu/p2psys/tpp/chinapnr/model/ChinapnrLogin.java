package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.UnsupportedEncodingException;

public class ChinapnrLogin extends ChinapnrModel {
	/**
	 * 2.0中用户登录汇付控制台操作类
	 */
	private String address = "/muser/publicRequests";
	private String testAddress = "/muser/publicRequests";
	public ChinapnrLogin(String usrCustId){
		super();
		this.setCmdId("UserLogin");
		this.setUsrCustId(usrCustId);
	}
	
	private String[] paramNames=new String[] {
			"version","cmdId","merCustId","usrCustId"
	};
	

	@Override
	public StringBuffer getMerData() throws UnsupportedEncodingException {
		StringBuffer merData=super.getMerData();
		             merData.append(getUsrCustId());
		   return merData;
		
	}
	
	
	
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTestAddress() {
		return testAddress;
	}
	public void setTestAddress(String testAddress) {
		this.testAddress = testAddress;
	}
		
}
