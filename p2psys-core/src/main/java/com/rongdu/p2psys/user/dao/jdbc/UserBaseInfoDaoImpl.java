package com.rongdu.p2psys.user.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.user.dao.UserBaseInfoDao;
import com.rongdu.p2psys.user.domain.UserBaseInfo;

/**
 * 用户基本信息
 * 
 * @author wzh
 * @version 2.0
 * @since 2014年11月4日
 */
@Repository("userBaseInfoDao")
public class UserBaseInfoDaoImpl extends BaseDaoImpl<UserBaseInfo> implements UserBaseInfoDao {

}
