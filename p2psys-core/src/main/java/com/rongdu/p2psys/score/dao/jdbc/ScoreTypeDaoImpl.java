package com.rongdu.p2psys.score.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.score.dao.ScoreTypeDao;
import com.rongdu.p2psys.score.domain.ScoreType;

@Repository("scoreTypeDao")
public class ScoreTypeDaoImpl extends BaseDaoImpl<ScoreType> implements ScoreTypeDao {

	@Override
	public ScoreType getScoreTypeByNid(String nid) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 1);
		param.addParam("nid", nid);
		return super.findByCriteriaForUnique(param);
	}
	
}
