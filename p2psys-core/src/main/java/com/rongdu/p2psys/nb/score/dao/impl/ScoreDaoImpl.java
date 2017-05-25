package com.rongdu.p2psys.nb.score.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.score.dao.ScoreDao;
import com.rongdu.p2psys.score.domain.Score;

@Repository("theScoreDao")
public class ScoreDaoImpl extends BaseDaoImpl<Score> implements ScoreDao
{

}
