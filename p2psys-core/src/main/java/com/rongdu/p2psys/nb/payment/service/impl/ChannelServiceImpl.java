package com.rongdu.p2psys.nb.payment.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.dao.IChannelDao;
import com.rongdu.p2psys.nb.payment.dao.impl.ChannelDaoImpl;
import com.rongdu.p2psys.nb.payment.domain.Channel;
import com.rongdu.p2psys.nb.payment.model.ChannelModel;
import com.rongdu.p2psys.nb.payment.service.IChannelService;
import com.rongdu.p2psys.user.dao.UserDao;

/***
 * 支付通道业务处理
 * @author ChenGangwei
 * 2015-06-23
 */
@Service("channelService")
public class ChannelServiceImpl implements IChannelService {

	@Resource
	private UserDao userDao;
	
	@Resource                        
	private IChannelDao channelDao;
	
	@Override
	public PageDataList<ChannelModel> findChannelByItem(int pageNumber,
			int pageSize, String searchName) {
		return channelDao.findChannelByItem(pageNumber,pageSize, searchName);
	}

	@Override
	public ChannelModel loadChannelById(long id) {
		Channel channel = channelDao.loadChannelById(id);
		ChannelModel model = ChannelModel.instance(channel);
		return model;
	}

	@Override
	public void deleteChannelById(long id) {
		channelDao.deleteChannelById(id);
	}

	@Override
	public void updateChannel(ChannelModel model) {
		Channel channel = ChannelModel.instance(model);
		channelDao.updateChannel(channel);		
	}

	@Override
	public void saveChannel(ChannelModel model) {
		Channel channel = ChannelModel.instance(model);
		channelDao.save(channel);
	}

}
