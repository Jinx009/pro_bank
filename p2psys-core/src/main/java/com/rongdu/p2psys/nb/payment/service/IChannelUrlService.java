package com.rongdu.p2psys.nb.payment.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.domain.Channel;
import com.rongdu.p2psys.nb.payment.domain.ChannelUrl;
import com.rongdu.p2psys.nb.payment.model.ChannelUrlModel;

public interface IChannelUrlService {
	PageDataList<ChannelUrlModel> findChannelUrlByItem(int pageNumber,
			int pageSize, String searchName);
	
	ChannelUrlModel loadChannelUrlById(long id);
	
	void deleteChannelUrlById(long id);
	
	void updateChannelUrl(ChannelUrlModel model);
	
	void saveChannelUrl(ChannelUrlModel model);
	
	List<Channel> loadChannelList();
	
	List<ChannelUrl> loadChannelUrlByType(int urlType);
}
