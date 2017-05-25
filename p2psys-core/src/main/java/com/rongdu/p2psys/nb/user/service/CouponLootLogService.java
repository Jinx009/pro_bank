package com.rongdu.p2psys.nb.user.service;

import java.util.Date;

public interface CouponLootLogService {

	void addLootRecord(Date lootTime, String mobile);

	Integer countLootRecord(Date lootTime, String mobile);

}
