package com.rongdu.p2psys.nb.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.user.dao.UserCreditDao;
import com.rongdu.p2psys.user.domain.UserCredit;

@Repository("theUserCreditDao")
public class UserCreditDaoImpl extends BaseDaoImpl<UserCredit> implements UserCreditDao
{

}
