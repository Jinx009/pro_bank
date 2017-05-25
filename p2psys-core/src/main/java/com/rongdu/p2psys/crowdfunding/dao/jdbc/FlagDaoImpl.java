package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.FlagDao;
import com.rongdu.p2psys.crowdfunding.domain.Flag;

@Repository("flagDao")
public class FlagDaoImpl extends BaseDaoImpl<Flag> implements FlagDao{

}
