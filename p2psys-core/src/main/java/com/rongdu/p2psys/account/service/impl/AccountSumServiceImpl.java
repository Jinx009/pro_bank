package com.rongdu.p2psys.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.SearchParam;
import com.rongdu.common.util.Page;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.service.AccountSumService;

/**
 * 用户资金合计信息处理
 */
@Service("accountSumService")
public class AccountSumServiceImpl implements AccountSumService {

	@Resource
	private AccountSumDao accountSumDao;

	@Override
	public void add(AccountSum accountSum) {
		accountSumDao.save(accountSum);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PageDataList getAccountSumPage(int page, SearchParam param) {
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(accountSumDao.count(param), page);
		pageDataList.setList(accountSumDao.findByUserId(pages.getStart(), pages.getPernum(), param));
		pageDataList.setPage(pages);
		return pageDataList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public PageDataList getAccountSumLogPage(int page, long userId, SearchParam param) {
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(accountSumDao.count(param), page);
		// pageDataList.setList(accountSumDao.getAccountSumLogPage(pages.getStart(),
		// pages.getPernum(), userId, param));
		pageDataList.setPage(pages);
		return pageDataList;
	}

	@Override
	public AccountSum findByUserId(long userId) {
		return accountSumDao.findByUserId(userId);
	}

}
