package com.rongdu.p2psys.user.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.user.dao.UserCreditLogDao;
import com.rongdu.p2psys.user.domain.UserCreditLog;

@Repository("userCreditLogDao")
public class UserCreditLogDaoImpl extends BaseDaoImpl<UserCreditLog> implements UserCreditLogDao {

}
