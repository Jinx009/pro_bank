package com.rongdu.p2psys.account.service;

import com.rongdu.p2psys.account.domain.SupportBank;

/**
 * 支持的银行卡
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月28日
 */
public interface SupportBankService {
	/**
	 * 查询银行卡
	 * 
	 * @param name
	 * @return
	 */
	SupportBank findByName(String name);
}
