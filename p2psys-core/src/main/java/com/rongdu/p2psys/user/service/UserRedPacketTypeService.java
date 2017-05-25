package com.rongdu.p2psys.user.service;

import java.util.List;

import com.rongdu.p2psys.user.domain.UserRedPacketType;

public interface UserRedPacketTypeService {

	/**
	 * 查找所有的开启的红包类型
	 */
	List<UserRedPacketType> findAll();

	
}
