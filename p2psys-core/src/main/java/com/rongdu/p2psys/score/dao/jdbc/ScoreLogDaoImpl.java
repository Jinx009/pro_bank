package com.rongdu.p2psys.score.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.score.dao.ScoreLogDao;
import com.rongdu.p2psys.score.domain.ScoreLog;

@Repository("scoreLogDao")
public class ScoreLogDaoImpl extends BaseDaoImpl<ScoreLog> implements ScoreLogDao {

}
