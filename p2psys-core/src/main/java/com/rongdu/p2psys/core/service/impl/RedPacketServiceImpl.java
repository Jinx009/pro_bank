package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.RedPacketDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.model.RedPacketModel;
import com.rongdu.p2psys.core.service.RedPacketService;

@Service("redPacketService")
public class RedPacketServiceImpl implements RedPacketService {

	@Resource
	private RedPacketDao redPacketDao;

	@Override
	public PageDataList<RedPacket> list(RedPacketModel model) {
		QueryParam param = QueryParam.getInstance()
				.addPage(model.getPage(), model.getRows())
				.addParam("isDelete", 0);
		return redPacketDao.findPageList(param);
	}

	@Override
	public RedPacket addRedPacket(RedPacket redPacket) {
		return redPacketDao.save(redPacket);
	}

	@Override
	public void updateRedPacket(RedPacketModel model) {
		RedPacket redPacket = model.prototype();
		redPacketDao.update(redPacket);
	}

	@Override
	public RedPacket find(long id) {
		return redPacketDao.find(id);
	}

	@Override
	public List<RedPacket> findAll() {
		return redPacketDao.findAll();
	}

	@Override
	public List<RedPacket> findActivities() {
		return redPacketDao.findActivities();
	}

	@Override
	public List<RedPacket> findFixedActiveRedPacket() {
		return redPacketDao.findFixedActiveRedPacket();
	}

}
