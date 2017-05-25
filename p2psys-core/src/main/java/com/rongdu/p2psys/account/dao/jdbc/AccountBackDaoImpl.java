package com.rongdu.p2psys.account.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.dao.AccountBackDao;
import com.rongdu.p2psys.account.domain.AccountDeduct;

@Repository("accountBackDao")
public class AccountBackDaoImpl extends BaseDaoImpl<AccountDeduct> implements AccountBackDao {

}
