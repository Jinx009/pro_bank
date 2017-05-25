package com.rongdu.p2psys.nb.payment.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.model.ChannelConfigModel;

public interface IChannelConfigDao extends BaseDao<ChannelConfig> {
	PageDataList<ChannelConfigModel> findChannelConfigByItem(int pageNumber,
			int pageSize, String searchName);
	
	ChannelConfig loadChannelConfigById(long id);
	
	void deleteChannelConfigById(long id);
	
	void updateChannelConfig(ChannelConfig channelConfig);
	
//	String loadGatewayUrlByKey(String gatewayKey);
	
	ChannelConfig getChannelConfigByBid(long bId);
	
	List<ChannelConfigModel> loadChannelInfo();
}
