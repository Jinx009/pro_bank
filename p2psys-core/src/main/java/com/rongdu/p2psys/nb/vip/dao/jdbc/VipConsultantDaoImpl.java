package com.rongdu.p2psys.nb.vip.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.vip.dao.VipConsultantDao;
import com.rongdu.p2psys.nb.vip.domain.VipConsultant;

@Repository("vipConsultantDao")
public class VipConsultantDaoImpl extends BaseDaoImpl<VipConsultant> implements VipConsultantDao
{
}
