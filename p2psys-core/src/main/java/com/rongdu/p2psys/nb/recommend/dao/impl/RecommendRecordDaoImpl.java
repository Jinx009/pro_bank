package com.rongdu.p2psys.nb.recommend.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.recommend.dao.RecommendRecordDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;

@Repository("recommendRecordDao")
public class RecommendRecordDaoImpl extends BaseDaoImpl<RecommendRecord> implements RecommendRecordDao
{

	@SuppressWarnings("unchecked")
	public RecommendRecord findRecord(User inviteUser, User user)
	{
		String hql = "  from RecommendRecord where inviteUser.userId = "+inviteUser.getUserId()+" and user.userId = "+user.getUserId()+" ";
		
		Query query = em.createQuery(hql);
		
		List<RecommendRecord> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list.get(0);
		}
		
		return null;
	}
	public void savePcRecommendRecord(User user, UserRedPacket userRedPacket,User inviteUser)
	{
		RecommendRecord recommendRecord = new RecommendRecord();
		recommendRecord.setUserRedPacket(userRedPacket);
		recommendRecord.setAccount(userRedPacket.getAmount());
		recommendRecord.setInviteUser(inviteUser);
		recommendRecord.setName(userRedPacket.getType().getServiceName());
		recommendRecord.setUser(user);
		
		save(recommendRecord);
	}

}
