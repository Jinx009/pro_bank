package com.rongdu.p2psys.account.service;

import java.util.List;

import com.rongdu.p2psys.account.model.PayOnlinebankModel;

/**
 * 支付方式-网银直联(线上银行)Service
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月17日
 */
public interface PayOnlinebankService {

	/**
	 * 获取线上银行列表
	 * 
	 * @param paymentInterfaceId
	 * @return
	 */
	List<PayOnlinebankModel> list(long pay_id);

}
