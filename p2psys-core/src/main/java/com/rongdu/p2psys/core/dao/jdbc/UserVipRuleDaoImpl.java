package com.rongdu.p2psys.core.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.UserVipRuleDao;
import com.rongdu.p2psys.core.domain.UserVipRule;

/**
 * 用户VIP规则
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月9日
 */
@Repository("UserVipRuleDao")
public class UserVipRuleDaoImpl extends BaseDaoImpl<UserVipRule> implements UserVipRuleDao {

}
