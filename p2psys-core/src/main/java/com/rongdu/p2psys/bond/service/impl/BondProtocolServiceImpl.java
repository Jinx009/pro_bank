package com.rongdu.p2psys.bond.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.bond.service.BondProtocolService;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.core.constant.ProtocolConstant;
import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;
import com.rongdu.p2psys.core.protocol.ProtocolHelper;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolDao;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;

@Service("bondProtocolService")
public class BondProtocolServiceImpl implements BondProtocolService {

	@Resource
	private ProtocolDao protocolDao;
	@Resource
	private BorrowTenderDao borrowTenderDao;

	/**
	 * 债权出让人协议下载
	 * 
	 * @param bondId
	 * @param userId
	 * @return
	 */
	@Override
	public AbstractProtocolBean buildBondSellProtocol(long borrowId, long userId) {
		Protocol protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_BOND_SELL).get(0);
		AbstractProtocolBean protocolBean = ProtocolHelper.doProtocol(ProtocolConstant.BASE_FOR_BOND_SELL);
		protocolBean.executer(borrowId, protocol, userId);
		return protocolBean;
	}

	/**
	 * 债权受让人协议下载
	 * 
	 * @param bondTenderId
	 * @return
	 */
	@Override
	public AbstractProtocolBean buildBondBuyProtocol(long bondTenderId, long userId) {
		Protocol protocol = protocolDao.findByProperty("nid", ProtocolConstant.BASE_FOR_BOND_BUY).get(0);
		AbstractProtocolBean protocolBean = ProtocolHelper.doProtocol(ProtocolConstant.BASE_FOR_BOND_BUY);
		protocolBean.executer(bondTenderId, protocol, userId);
		return protocolBean;
	}

}
