package com.rongdu.p2psys.user.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.user.dao.UserUploadDao;
import com.rongdu.p2psys.user.domain.UserUpload;

@Repository("userUploadDao")
public class UserUploadDaoImpl extends BaseDaoImpl<UserUpload> implements UserUploadDao {


}
