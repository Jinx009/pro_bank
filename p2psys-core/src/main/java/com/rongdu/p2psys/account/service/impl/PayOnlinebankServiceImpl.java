package com.rongdu.p2psys.account.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.account.dao.PayOnlinebankDao;
import com.rongdu.p2psys.account.model.PayOnlinebankModel;
import com.rongdu.p2psys.account.service.PayOnlinebankService;

@Service("payOnlinebankService")
public class PayOnlinebankServiceImpl implements PayOnlinebankService {

	@Resource
	private PayOnlinebankDao payOnlinebankDao;

	@Override
	public List<PayOnlinebankModel> list(long pay_id) {
		return payOnlinebankDao.list(pay_id);
	}

}
