package com.rongdu.p2psys.score.service;

import java.util.List;

import com.rongdu.p2psys.score.domain.ScoreType;


public interface ScoreTypeService {

	/**
	 * 查询所有的积分类型
	 * @param user_id
	 * @return
	 */
	public List<ScoreType> getScoreTypeAll();
	
	/**
	 * 根据nid查询积分类型信息
	 * @param nid积分类型表nid
	 * @return
	 */
	public ScoreType getScoreTypeByNid(String nid);
}
