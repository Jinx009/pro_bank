package com.rongdu.p2psys.nb.payment.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.nb.payment.domain.Channel;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.nb.payment.model.ChannelBankModel;
import com.rongdu.p2psys.user.domain.User;

public interface IChannelBankService {
	PageDataList<ChannelBankModel> findChannelBankByItem(int pageNumber,
			int pageSize, String searchName);
	
	void updateChannelBank(ChannelBankModel model);
	
	List<Channel> loadChannelList();
	
	List<ChannelBank> list(long userId,String channelKey);
	
	void saveChannelBank(User user, AccountBank ab,String channelKey);
	
	ChannelBankModel loadChannelBankById(long id);
}
