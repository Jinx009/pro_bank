package com.rongdu.p2psys.tpp.chinapnr.model;

/**
 * 充值对账
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年1月17日
 */
public class ChinapnrSaveReconciliation extends ChinapnrCashReconciliation {

	public ChinapnrSaveReconciliation(String beginDate, String endDate,
			String pageNum, String pageSize) {
		super(beginDate, endDate, pageNum, pageSize);
		this.setCmdId("SaveReconciliation");
	}

}
