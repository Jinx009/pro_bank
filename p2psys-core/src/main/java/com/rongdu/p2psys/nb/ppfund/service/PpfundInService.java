package com.rongdu.p2psys.nb.ppfund.service;

import java.util.Date;

import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.domain.User;

public interface PpfundInService
{
	public PpfundIn savePpfundIn(PpfundIn ppfundIn);
	
	public void updatePpfundIn(PpfundIn ppfundIn);

	public void saveNewPpfundIn(double total_money, String ip_address,Ppfund ppfund, User user,Date out_time);
	
	/**
	 * 获取该用户本日投资现金产品总额
	 * 
	 * @param userId
	 * @return Double
	 */
	Double getTotalDayInvestMoney(Long userId);
}
