package com.rongdu.p2psys.nb.card.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.card.domain.ActivityRecord;

public interface ActivityRecordDao extends BaseDao<ActivityRecord>
{
	public ActivityRecord getByUserId(Long userId);
}
