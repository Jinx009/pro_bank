package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.RedPacketDetail;

/**
 * 红包附属信息
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月10日
 */
public interface RedPacketDetailDao extends BaseDao<RedPacketDetail> {
	/**
	 * 根据红包获取红包附属信息
	 * @param redPacket
	 * @return
	 */
	List<RedPacketDetail> getList(RedPacket redPacket);
}
