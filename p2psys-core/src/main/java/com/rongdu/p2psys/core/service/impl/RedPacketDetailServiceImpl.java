package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.core.dao.RedPacketDetailDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.RedPacketDetail;
import com.rongdu.p2psys.core.service.RedPacketDetailService;

@Service("redPacketDetailService")
public class RedPacketDetailServiceImpl implements RedPacketDetailService {
	@Resource
	RedPacketDetailDao redPacketDetailDao;

	@Override
	public void addRedPacketDetail(List<RedPacketDetail> list) {
		for (RedPacketDetail redPacketDetail : list) {
			redPacketDetailDao.save(redPacketDetail);
		}

	}

	@Override
	public List<RedPacketDetail> getList(RedPacket redPacket) {
		return redPacketDetailDao.getList(redPacket);
	}

	@Override
	public void deleteRedPacketDetail(RedPacket redPacket) {
		List<RedPacketDetail> list = redPacketDetailDao.getList(redPacket);
		for (RedPacketDetail redPacketDetail : list) {
			redPacketDetail.setIsDelete(1);
			redPacketDetailDao.update(redPacketDetail);
		}
	}

}
