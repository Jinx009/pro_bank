package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.model.RedPacketModel;

/**
 * 红包
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月10日
 */
public interface RedPacketService {
	/**
	 * 红包列表
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<RedPacket> list(RedPacketModel model);
	
	/**
	 * 添加红包
	 * @param model
	 */
	RedPacket addRedPacket(RedPacket redPacket);
	
	/**
	 * 修改红包
	 * @param model
	 */
	void updateRedPacket(RedPacketModel model);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	RedPacket find(long id);
	
	
	/**
	 * 获取所有红包列表
	 * @return
	 */
	List<RedPacket> findAll();
	
	/**
	 * 选择活动红包
	 * 
	 * @return
	 */
	List<RedPacket> findActivities();
	
	/**
	 * 定向发送红包
	 * @return
	 */
	List<RedPacket> findFixedActiveRedPacket();
}
