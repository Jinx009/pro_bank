package com.rongdu.p2psys.nb.borrow.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.nb.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.user.domain.User;

@Service("theBorrowCollectionService")
public class BorrowCollectionServiceImpl implements BorrowCollectionService
{

	@Resource
	private BorrowCollectionDao theBorrowCollectionDao;
	
	public double netProfit(User user)
	{
		return theBorrowCollectionDao.netProfit(user);
	}

	@Override
	public double getInterestByUser(User user) {
		return theBorrowCollectionDao.getInterestByUser(user);
	}

	@Override
	public double inInvestAmount(User user, int status) {
		return theBorrowCollectionDao.inInvestAmount(user, status);
	}

}
