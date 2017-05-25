package com.rongdu.p2psys.ppfund.service;

import com.rongdu.p2psys.core.protocol.AbstractProtocolForPpfundBean;

/**
 * PPfund下载协议
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月15日
 */
public interface PpfundProtocolService {

	/**
	 * 投资人协议下载
	 * 
	 * @param tenderId
	 * @return
	 */
	AbstractProtocolForPpfundBean buildPpfundInProtocol(long inId);
	
	/**
	 * 微信投资人协议
	 * 
	 * @param inId
	 * @return
	 */
	String buildPpfundProtocol(String type,long inId);
}
