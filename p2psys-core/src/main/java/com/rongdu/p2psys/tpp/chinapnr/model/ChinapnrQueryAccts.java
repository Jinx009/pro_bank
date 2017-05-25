package com.rongdu.p2psys.tpp.chinapnr.model;

import com.rongdu.common.util.StringUtil;

/**
 * 商户子账户信息查询
 * @author yinliang
 * @version 2.0
 * @Date   2015年1月17日
 */
public class ChinapnrQueryAccts extends ChinapnrModel {
	
	public ChinapnrQueryAccts(){
		super();
		this.setCmdId("QueryAccts");
	}
	
	private String[] paramNames = new String[] { "version", "cmdId", "merCustId","chkValue"};
	
	public StringBuffer getMerData() {

		StringBuffer MerData = new StringBuffer();
		MerData.append(StringUtil.isNull(getVersion()))
				.append(StringUtil.isNull(getCmdId()))
				.append(StringUtil.isNull(getMerCustId()));
		return MerData;
	}
	
	
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

}
