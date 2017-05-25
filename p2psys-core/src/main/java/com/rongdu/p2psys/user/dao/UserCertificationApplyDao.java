package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCertificationApply;

/**
 * 证明材料申请Dao
 * 
 * @author zf
 * @version 2.0
 * @since 2014年11月07日
 */
public interface UserCertificationApplyDao extends BaseDao<UserCertificationApply> {

	int getStatusByUserAndTypeId(User user, long typeId);

	

}
