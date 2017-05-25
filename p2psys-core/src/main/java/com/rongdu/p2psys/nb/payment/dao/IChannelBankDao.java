package com.rongdu.p2psys.nb.payment.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.nb.payment.model.ChannelBankModel;

public interface IChannelBankDao extends BaseDao<ChannelBank> {
	PageDataList<ChannelBankModel> findChannelBankByItem(int pageNumber,
			int pageSize, String searchName);
	
	ChannelBank loadChannelBankById(long id);
	
	void deleteChannelBankById(long id);
	
	void updateChannelBank(ChannelBank channelBank);
	
	/**
	 * 提现银行卡列表
	 * 
	 * @param userId 用户ID
	 * @param channelId 通道ID
	 * @return 提现银行卡列表
	 */
	List<ChannelBank> list(long userId,long channelId);
}
