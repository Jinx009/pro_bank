package com.rongdu.p2psys.nb.ppfund.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.ppfund.dao.PpfundInDao;
import com.rongdu.p2psys.nb.ppfund.service.PpfundInService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.domain.User;

@Service("thePpfundInService")
public class PpfundInServiceImpl implements PpfundInService {

	@Resource
	private PpfundInDao thePpfundInDao;

	public PpfundIn savePpfundIn(PpfundIn ppfundIn) {
		return thePpfundInDao.save(ppfundIn);
	}

	public void updatePpfundIn(PpfundIn ppfundIn) {
		thePpfundInDao.update(ppfundIn);
	}

	public void saveNewPpfundIn(double total_money, String ip_address,
			Ppfund ppfund, User user, Date out_time) {
		PpfundIn ppfundIn = new PpfundIn();

		ppfundIn.setUser(user);
		ppfundIn.setAddIp(ip_address);
		ppfundIn.setAccount(total_money);
		ppfundIn.setAddTime(new Date());
		ppfundIn.setInterest(0.0);
		ppfundIn.setInterestAmount(0.0);
		ppfundIn.setIsOut(0);
		ppfundIn.setOutTime(new Date());
		ppfundIn.setMoney(total_money);
		ppfundIn.setPpfund(ppfund);
		ppfundIn.setOutTime(out_time);

		thePpfundInDao.save(ppfundIn);
	}

	@Override
	public Double getTotalDayInvestMoney(Long userId) {
		return thePpfundInDao.getTotalDayInvestMoney(userId);
	}
}
