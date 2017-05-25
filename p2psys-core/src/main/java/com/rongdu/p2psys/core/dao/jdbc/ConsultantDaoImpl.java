package com.rongdu.p2psys.core.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.ConsultantDao;
import com.rongdu.p2psys.core.domain.Consultant;

/**
 * 顾问专家
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
@Repository("consultantDao")
public class ConsultantDaoImpl extends BaseDaoImpl<Consultant> implements
		ConsultantDao {

}
