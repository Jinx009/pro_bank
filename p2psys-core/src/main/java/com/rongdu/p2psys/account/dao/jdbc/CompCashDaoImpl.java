package com.rongdu.p2psys.account.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.dao.CompCashDao;
import com.rongdu.p2psys.account.domain.CompCash;

@Repository("compCashDao")
public class CompCashDaoImpl extends BaseDaoImpl<CompCash> implements CompCashDao {

}
