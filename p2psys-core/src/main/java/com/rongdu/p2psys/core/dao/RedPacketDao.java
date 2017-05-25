package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.RedPacket;

/**
 * 红包
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月10日
 */
public interface RedPacketDao extends BaseDao<RedPacket> {
	List<RedPacket> findActivities();
	
	List<RedPacket> findFixedActiveRedPacket();
}
