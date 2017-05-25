package com.rongdu.p2psys.borrow.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.borrow.dao.BorrowAutoDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.service.BorrowAutoService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

@Service("borrowAutoService")
public class BorrowAutoServiceImpl implements BorrowAutoService {

	@Resource
	private BorrowAutoDao borrowAutoDao;

	@Override
	public BorrowAuto init(User user) {
		BorrowAuto auto = borrowAutoDao.findObjByProperty("user.userId", user.getUserId());
		if (auto == null) {
			auto = new BorrowAuto(user);
			borrowAutoDao.save(auto);
		}
		return auto;
	}

	@Override
	public void update(BorrowAuto auto) throws Exception {
		auto.setAddTime(new Date());
		auto.setAddIp(Global.getIP());
		auto.setUpdateTime(new Date());
		borrowAutoDao.update(auto);
	}

	@Override
	public int count(Borrow borrow) {
		return borrowAutoDao.count();
	}

	@Override
	public void modifyTime(BorrowAuto auto) {
		auto = borrowAutoDao.find(auto.getId());
		auto.setUpdateTime(new Date());
		borrowAutoDao.update(auto);
	}

	@Override
	public void modifyStatus(BorrowAuto auto) {
		auto.setEnable(auto.getEnable());
		borrowAutoDao.update(auto);
	}

	@Override
	public void add(BorrowAuto auto) {
		borrowAutoDao.save(auto);
	}

	@Override
	public BorrowAuto findByUser(User user) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user", user);
		return borrowAutoDao.findByCriteriaForUnique(param);
	}

	@Override
	public BorrowAuto getBorrowAutoById(int autoTenderId) {
		return borrowAutoDao.findObjByProperty("id", autoTenderId);
	}

	@Override
	public void updateBorrowAuto(BorrowAuto ba) {
		borrowAutoDao.update(ba);
	}

}
