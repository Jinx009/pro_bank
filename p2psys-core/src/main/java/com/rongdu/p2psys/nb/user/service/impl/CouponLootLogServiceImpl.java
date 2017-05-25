package com.rongdu.p2psys.nb.user.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.nb.user.dao.CouponLootLogDao;
import com.rongdu.p2psys.nb.user.service.CouponLootLogService;
import com.rongdu.p2psys.user.domain.CouponLootLog;

@Service("couponLootLogService")
public class CouponLootLogServiceImpl implements CouponLootLogService {

	@Resource
	private CouponLootLogDao couponLootLogDao;

	@Override
	public void addLootRecord(Date lootTime, String mobile) {
		CouponLootLog pojo = new CouponLootLog();
		pojo.setLootMobile(mobile);
		pojo.setLootTime(lootTime);
		couponLootLogDao.save(pojo);
	}

	@Override
	public Integer countLootRecord(Date lootTime, String mobile) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("lootTime", lootTime);
		param.addParam("lootMobile", mobile);
		List<CouponLootLog> list = couponLootLogDao.findByCriteria(param);

		Integer count = 0;
		if (list != null) {
			count = list.size();
		}
		return count;
	}

}
