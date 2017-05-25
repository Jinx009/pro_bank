package com.rongdu.p2psys.nb.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.user.dao.UserRedPacketTypeDao;
import com.rongdu.p2psys.nb.user.service.UserRedPacketTypeService;
import com.rongdu.p2psys.user.domain.UserRedPacketType;

@Service("theUserRedPacketTypeService")
public class UserRedPacketTypeServiceImpl implements UserRedPacketTypeService
{

	@Resource
	private UserRedPacketTypeDao theUserRedPacketTypeDao;
	
	public UserRedPacketType getById(long id)
	{
		return theUserRedPacketTypeDao.find(id);
	}

}
