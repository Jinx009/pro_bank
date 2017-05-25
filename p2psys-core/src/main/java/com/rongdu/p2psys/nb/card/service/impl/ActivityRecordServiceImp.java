package com.rongdu.p2psys.nb.card.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.card.dao.ActivityRecordDao;
import com.rongdu.p2psys.nb.card.domain.ActivityRecord;
import com.rongdu.p2psys.nb.card.service.ActivityRecordService;

@Service("activityRecordService")
public class ActivityRecordServiceImp implements ActivityRecordService
{

	@Resource
	private ActivityRecordDao activityRecordDao;
	
	public void updateActivityRecord(ActivityRecord activityRecord)
	{
		activityRecordDao.update(activityRecord);
	}

	public ActivityRecord getByUserId(Long userId)
	{
		return activityRecordDao.getByUserId(userId);
	}

	public ActivityRecord saveActivityRecord(ActivityRecord activityRecord)
	{
		return activityRecordDao.save(activityRecord);
	}

	public ActivityRecord getById(Long id)
	{
		return activityRecordDao.find(id);
	}

}
