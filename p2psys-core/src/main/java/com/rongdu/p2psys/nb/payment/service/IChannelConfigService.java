package com.rongdu.p2psys.nb.payment.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.model.ChannelConfigModel;

public interface IChannelConfigService {
	PageDataList<ChannelConfigModel> findChannelConfigByItem(int pageNumber,
			int pageSize, String searchName);
	
	ChannelConfigModel loadChannelConfigById(long id);
	
	void deleteChannelConfigById(long id);
	
	void updateChannelConfig(ChannelConfigModel model);
	
	void saveChannelConfig(ChannelConfigModel model);
	
	List<ChannelConfigModel> loadChannelInfo();
	
//	String loadGatewayUrlByKey(String gatewayKey);
	
	ChannelConfig getChannelConfigByBid(long bId);
	
	ChannelConfig getChannelConfigByCode(String bankCode);
}
