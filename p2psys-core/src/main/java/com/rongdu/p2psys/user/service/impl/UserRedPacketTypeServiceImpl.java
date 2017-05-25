package com.rongdu.p2psys.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.dao.UserRedPacketTypeDao;
import com.rongdu.p2psys.user.domain.UserRedPacketType;
import com.rongdu.p2psys.user.service.UserRedPacketTypeService;

@Service
public class UserRedPacketTypeServiceImpl implements UserRedPacketTypeService {

	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private UserRedPacketTypeDao userRedPacketTypeDao;
	@Override
	public List<UserRedPacketType> findAll() {
		QueryParam param = QueryParam.getInstance().addParam("isOpen", true);
		return userRedPacketTypeDao.findByCriteria(param);
	}
	
}
