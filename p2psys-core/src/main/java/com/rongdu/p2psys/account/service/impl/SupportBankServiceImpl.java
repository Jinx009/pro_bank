package com.rongdu.p2psys.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.account.dao.SupportBankDao;
import com.rongdu.p2psys.account.domain.SupportBank;
import com.rongdu.p2psys.account.service.SupportBankService;

@Service("supportBankService")
public class SupportBankServiceImpl implements SupportBankService {
	@Resource
	private SupportBankDao supportBankDao;
	
	@Override
	public SupportBank findByName(String name) {
		
		return supportBankDao.findObjByProperty("name", name);
	}

}
