package com.rongdu.p2psys.nb.account.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.nb.account.dao.AccountSumDao;

@Repository("theAccountSumDao")
public class AccountSumDaoImpl extends BaseDaoImpl<AccountSum> implements AccountSumDao
{

}
