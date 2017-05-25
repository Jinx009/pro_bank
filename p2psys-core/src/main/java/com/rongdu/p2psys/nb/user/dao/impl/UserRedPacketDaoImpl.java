package com.rongdu.p2psys.nb.user.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.nb.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;

@Repository("theUserRedPacketDao")
public class UserRedPacketDaoImpl extends BaseDaoImpl<UserRedPacket> implements UserRedPacketDao
{

	public void updateRedPachet(UserRedPacket userRedPacket)
	{
		update(userRedPacket);
	}

	public UserRedPacket saveUserRedPacket(UserRedPacket userRedPacket)
	{
		return save(userRedPacket);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findBySql(String sql)
	{
		Query query = em.createNativeQuery(sql);
		
		List<Object[]> list = query.getResultList();
		
		return list;
	}
	
	public UserRedPacket savePcUserRedPacket(RedPacket redPacket, User inviteUser,Date date)
	{
		UserRedPacket userRedPacket = new UserRedPacket();
		userRedPacket.setAddTime(new Date());
		userRedPacket.setAmount(redPacket.getMoney());
		userRedPacket.setExpiredTime(date);
		userRedPacket.setRedPacketType(2);
		userRedPacket.setType(redPacket);
		userRedPacket.setUsed(false);
		userRedPacket.setUser(inviteUser);
		
		return save(userRedPacket);
	}
}
