package com.rongdu.p2psys.account.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.dao.PayDao;
import com.rongdu.p2psys.account.domain.Pay;

@Repository("PayDao")
public class PayDaoImpl extends BaseDaoImpl<Pay> implements PayDao {

}
