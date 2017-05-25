package com.rongdu.p2psys.nb.score.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.score.dao.ScoreDao;
import com.rongdu.p2psys.nb.score.service.ScoreService;
import com.rongdu.p2psys.score.domain.Score;

@Service("theScoreService")
public class ScoreServiceImpl implements ScoreService
{

	@Resource
	private ScoreDao theScoreDao;
	
	public Score saveScore(Score score)
	{
		return theScoreDao.save(score);
	}

}
