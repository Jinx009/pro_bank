package com.rongdu.p2psys.nb.payment.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.model.ChannelModel;

public interface IChannelService {
	PageDataList<ChannelModel> findChannelByItem(int pageNumber,
			int pageSize, String searchName);
	
	ChannelModel loadChannelById(long id);
	
	void deleteChannelById(long id);
	
	void updateChannel(ChannelModel model);
	
	void saveChannel(ChannelModel model);
}
