package com.rongdu.p2psys.bond.service;

import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;

/**
 * 协议servcei
 * 
 * @author qj
 */
public interface BondProtocolService {

	/**
	 * 债权出让人协议下载
	 * 
	 * @param bondId
	 * @param userId
	 * @return
	 */
	AbstractProtocolBean buildBondSellProtocol(long borrowId, long userId);

	/**
	 * 债权受让人协议下载
	 * 
	 * @param bondTenderId
	 * @return
	 */
	AbstractProtocolBean buildBondBuyProtocol(long bondTenderId, long userId);

}
