package com.rongdu.p2psys.tpp.chinapnr.service;

import java.util.Map;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;

public interface TppPnrPayService {
	/**
	 * 专门处理关于汇付放款和还款接口有时候
	 * 汇付处理失败，时时返回信息为失败，但是
	 * 汇付会不停处理，不停回调，所以要更改
	 * 调度任务表中此条信息的时时状态
	 * @param ordNo
	 * @param respcode
	 * @param respdesc
	 */
	public void dealChinapnrBack(String ordNo,String respcode,String respdesc);
	
	/**
     * 查询第三方支付支付信息列表
     * @param map 参数
     * @return List第三方支付记录
     */
	public PageDataList<TppPnrPay> getTppChinapnrPay(Map<String, Object> map);
	
	/**
     * 根据ID查询第三方支付支付信息列表
     * @param id 参数
     * @return 第三方支付记录
     */
	TppPnrPay getTppPayById(int id);
}
