package com.rongdu.p2psys.user.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.user.dao.UserInviteDao;
import com.rongdu.p2psys.user.domain.UserInvite;

@Repository("userInviteDao")
public class UserInviteDaoImpl extends BaseDaoImpl<UserInvite> implements UserInviteDao {

}
