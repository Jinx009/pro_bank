package com.rongdu.p2psys.nb.payment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.nb.payment.dao.IChannelDao;
import com.rongdu.p2psys.nb.payment.dao.IChannelUrlDao;
import com.rongdu.p2psys.nb.payment.domain.Channel;
import com.rongdu.p2psys.nb.payment.domain.ChannelUrl;
import com.rongdu.p2psys.nb.payment.model.ChannelUrlModel;
import com.rongdu.p2psys.nb.payment.service.IChannelUrlService;
import com.rongdu.p2psys.user.dao.UserDao;

/***
 * 支付通道URL业务处理
 * @author ChenGangwei
 * 2015-06-23
 */
@Service("channelUrlService")
public class ChannelUrlServiceImpl implements IChannelUrlService {

	@Resource
	private UserDao userDao;
	
	@Resource                        
	private IChannelUrlDao channelUrlDao;
	
	@Resource                        
	private IChannelDao channelDao;
	
	@Override
	public PageDataList<ChannelUrlModel> findChannelUrlByItem(int pageNumber,
			int pageSize, String searchName) {
		return channelUrlDao.findChannelUrlByItem(pageNumber,pageSize, searchName);
	}

	@Override
	public ChannelUrlModel loadChannelUrlById(long id) {
		ChannelUrl channelUrl = channelUrlDao.loadChannelUrlById(id);
		ChannelUrlModel model = ChannelUrlModel.instance(channelUrl);
		model.setCid(channelUrl.getChanel().getId());
		return model;
	}

	@Override
	public void deleteChannelUrlById(long id) {
		channelUrlDao.deleteChannelUrlById(id);
	}

	@Override
	public void updateChannelUrl(ChannelUrlModel model) {
		ChannelUrl channelUrl = ChannelUrlModel.instance(model);
		channelUrlDao.updateChannelUrl(channelUrl);		
	}

	@Override
	public void saveChannelUrl(ChannelUrlModel model) {
		ChannelUrl channelUrl = ChannelUrlModel.instance(model);
		channelUrlDao.save(channelUrl);
	}

	@Override
	public List<Channel> loadChannelList() {
		return channelDao.findAll();
	}

	@Override
	public List<ChannelUrl> loadChannelUrlByType(int urlType) {
		return channelUrlDao.findByProperty("urlType", urlType);
	}

}
