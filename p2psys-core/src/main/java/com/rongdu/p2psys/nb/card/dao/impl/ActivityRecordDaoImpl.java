package com.rongdu.p2psys.nb.card.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.card.dao.ActivityRecordDao;
import com.rongdu.p2psys.nb.card.domain.ActivityRecord;

@Repository("activityRecordDao")
public class ActivityRecordDaoImpl extends BaseDaoImpl<ActivityRecord> implements ActivityRecordDao
{
	@SuppressWarnings("unchecked")
	public ActivityRecord getByUserId(Long userId)
	{
		String hql = "  from  ActivityRecord where user.userId = "+userId+" ";
		Query query = em.createQuery(hql);
		List<ActivityRecord> list = query.getResultList();
		if(null!=list&&!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}
}
