package com.rongdu.p2psys.nb.user.dao;

import java.util.Date;
import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;

public interface UserRedPacketDao extends BaseDao<UserRedPacket>
{
	/**
	 * 更新红包信息
	 * 
	 * @param userRedPacket
	 */
	public void updateRedPachet(UserRedPacket userRedPacket);
	
	/**
	 * 保存新红包
	 * 
	 * @param userRedPacket
	 * @return
	 */
	public UserRedPacket saveUserRedPacket(UserRedPacket userRedPacket);

	public List<Object[]> findBySql(String sql);
	
	public UserRedPacket savePcUserRedPacket(RedPacket redPacket, User inviteUser, Date date);
}
