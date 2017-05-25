package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.RedPacketDetail;

/**
 * 红包附属信息
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月10日
 */
public interface RedPacketDetailService {
	/**
	 * 批量添加红包附属信息
	 * @param list
	 */
	void addRedPacketDetail(List<RedPacketDetail> list);
	
	/**
	 * 根据红包获取红包附属信息
	 * @param redPacket
	 * @return
	 */
	List<RedPacketDetail> getList(RedPacket redPacket);
	
	/**
	 * 伪删除红包附属信息
	 * @param redPacket
	 */
	void deleteRedPacketDetail(RedPacket redPacket);
}
