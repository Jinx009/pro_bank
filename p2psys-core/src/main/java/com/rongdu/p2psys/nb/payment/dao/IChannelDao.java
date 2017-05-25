package com.rongdu.p2psys.nb.payment.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.domain.Channel;
import com.rongdu.p2psys.nb.payment.model.ChannelModel;

public interface IChannelDao extends BaseDao<Channel> {
	PageDataList<ChannelModel> findChannelByItem(int pageNumber,
			int pageSize, String searchName);
	
	Channel loadChannelById(long id);
	
	void deleteChannelById(long id);
	
	void updateChannel(Channel channel);
}
