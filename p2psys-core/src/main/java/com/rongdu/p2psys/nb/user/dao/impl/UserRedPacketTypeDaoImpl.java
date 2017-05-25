package com.rongdu.p2psys.nb.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.user.dao.UserRedPacketTypeDao;
import com.rongdu.p2psys.user.domain.UserRedPacketType;

@Repository("theUserRedPacketTypeDao")
public class UserRedPacketTypeDaoImpl extends BaseDaoImpl<UserRedPacketType> implements UserRedPacketTypeDao
{

}
