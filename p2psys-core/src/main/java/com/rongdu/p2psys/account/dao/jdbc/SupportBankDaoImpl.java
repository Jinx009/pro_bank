package com.rongdu.p2psys.account.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.dao.SupportBankDao;
import com.rongdu.p2psys.account.domain.SupportBank;

@Repository("supportBankDao")
public class SupportBankDaoImpl extends BaseDaoImpl<SupportBank> implements SupportBankDao {

}
