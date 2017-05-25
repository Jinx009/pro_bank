package com.rongdu.p2psys.crowdfunding.service;

import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;

public interface ProtocolService {

	public String createProtocol(InvestOrder investOrder,String protocolCode) throws Exception ;

	public String getProtocol(InvestOrder investOrder, String protocolCode) throws Exception;
	
	public String getEmptyProtocol(InvestOrder investOrder,String protocolCode) throws Exception ;
	
}
