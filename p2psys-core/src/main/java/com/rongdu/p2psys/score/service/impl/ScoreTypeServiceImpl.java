package com.rongdu.p2psys.score.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.score.dao.ScoreTypeDao;
import com.rongdu.p2psys.score.domain.ScoreType;
import com.rongdu.p2psys.score.service.ScoreTypeService;

@Service("scoreTypeService")
public class ScoreTypeServiceImpl implements ScoreTypeService {

	@Resource
	private ScoreTypeDao scoreTypeDao;
	
	public List<ScoreType> getScoreTypeAll() {
		// TODO Auto-generated method stub
		return scoreTypeDao.findAll();
	}

	public ScoreType getScoreTypeByNid(String nid) {
		// TODO Auto-generated method stub
		return scoreTypeDao.getScoreTypeByNid(nid);
	}

}
