package com.rongdu.p2psys.borrow.service;

import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;

/**
 * 协议servcei
 * 
 * @author qj
 */
public interface BorrowProtocolService {

	/**
	 * 借款人协议下载
	 * 
	 * @param borrowId
	 * @param userId
	 * @return
	 */
	AbstractProtocolBean buildBorrowerProtocol(long borrowId, long userId);

	/**
	 * 投资人协议下载
	 * 
	 * @param tenderId
	 * @return
	 */
	AbstractProtocolBean buildTenderProtocol(long tenderId);

	/**
	 * 平台协议下载
	 * 
	 * @param borrowId
	 * @param userId
	 * @param operatorId
	 * @return
	 */
	AbstractProtocolBean buildWebProtocol(long borrowId, long userId, long operatorId);

}
