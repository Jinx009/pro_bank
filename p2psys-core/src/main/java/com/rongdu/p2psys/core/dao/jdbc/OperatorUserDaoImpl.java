package com.rongdu.p2psys.core.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.OperatorUserDao;
import com.rongdu.p2psys.core.domain.OperatorUser;

@Repository("operatorUserDao")
public class OperatorUserDaoImpl extends BaseDaoImpl<OperatorUser> implements OperatorUserDao{

}
