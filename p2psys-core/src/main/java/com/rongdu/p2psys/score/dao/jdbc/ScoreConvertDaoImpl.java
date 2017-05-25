package com.rongdu.p2psys.score.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.score.dao.ScoreConvertDao;
import com.rongdu.p2psys.score.domain.ScoreConvert;

@Repository("scoreConvertDao")
public class ScoreConvertDaoImpl extends BaseDaoImpl<ScoreConvert> implements ScoreConvertDao {

}
