package com.rongdu.p2psys.score.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.score.domain.ScoreType;

public interface ScoreTypeDao extends BaseDao<ScoreType> {
	
	/**
	 * 根据积分类型代码查询积分类型
	 * @param nid 积分类型代码
	 * @return 积分类型
	 */
	public ScoreType getScoreTypeByNid(String nid);

}
