package com.rongdu.p2psys.core.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.ApplyConsultantsDao;
import com.rongdu.p2psys.core.domain.ApplyConsultants;

/**
 * 私人顾问
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */

@Repository("applyConsultantsDao")
public class ApplyConsultantsDaoImpl extends
		BaseDaoImpl<ApplyConsultants> implements ApplyConsultantsDao {

}
