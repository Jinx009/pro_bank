package com.rongdu.p2psys.core.protocol;

import com.rongdu.p2psys.nb.protocol.domain.Protocol;

/**
 * 处理协议
 * 
 * @author qj
 * @version 2.0
 * @since 2014-05-22
 */
public interface ProtocolBean {

	/**
	 * 业务核心处理方法
	 * @param borrowId 借款ID
	 * @param protocol 协议实体
	 * @param userId 用户ID
	 */
	void executer(long borrowId, Protocol protocol, long userId);

	/**
	 * 业务核心处理方法
	 * @param borrowId 借款ID
	 * @param tenderId 投资ID
	 * @param protocol 协议实体
	 * @param userId 用户ID
	 */
	void executer(long borrowId, long tenderId, Protocol protocol, long userId);

	/**
	 * 逻辑执行之前的准备工作
	 */
	void validDownload();

	/**
	 * 逻辑执行之前的准备工作
	 */
	void prepare();

	/**
	 * 初始化协议需要参数
	 */
	void initData();

	/**
	 * 创建协议pdf
	 */
	void createPdf();

}
