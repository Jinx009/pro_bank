package com.rongdu.p2psys.core.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.ParentOperatorDao;
import com.rongdu.p2psys.core.domain.ParentOperator;

@Repository("parentOperatorDao")
public class ParentOperatorDaoImpl extends BaseDaoImpl<ParentOperator> implements ParentOperatorDao{

}
