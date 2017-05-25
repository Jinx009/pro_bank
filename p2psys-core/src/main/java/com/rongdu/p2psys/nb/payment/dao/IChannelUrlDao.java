package com.rongdu.p2psys.nb.payment.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.domain.ChannelUrl;
import com.rongdu.p2psys.nb.payment.model.ChannelUrlModel;

public interface IChannelUrlDao extends BaseDao<ChannelUrl> {
	PageDataList<ChannelUrlModel> findChannelUrlByItem(int pageNumber,
			int pageSize, String searchName);
	
	ChannelUrl loadChannelUrlById(long id);
	
	void deleteChannelUrlById(long id);
	
	void updateChannelUrl(ChannelUrl channelUrl);
}
