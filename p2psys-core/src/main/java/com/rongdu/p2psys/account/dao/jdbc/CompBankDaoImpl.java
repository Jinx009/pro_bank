package com.rongdu.p2psys.account.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.dao.CompBankDao;
import com.rongdu.p2psys.account.domain.CompBank;

@Repository("compBankDao")
public class CompBankDaoImpl extends BaseDaoImpl<CompBank> implements CompBankDao {

}
