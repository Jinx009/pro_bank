package com.rongdu.p2psys.nb.card.service;

import com.rongdu.p2psys.nb.card.domain.ActivityRecord;

public interface ActivityRecordService
{
	public void updateActivityRecord(ActivityRecord activityRecord);

	public ActivityRecord getByUserId(Long userId);
	
	public ActivityRecord saveActivityRecord(ActivityRecord activityRecord);
	
	public ActivityRecord getById(Long id);
}
