package com.rongdu.p2psys.score.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.score.domain.ScoreLog;
import com.rongdu.p2psys.score.model.ScoreLogModel;

public interface ScoreLogService {

	/**
	 * 会员操作记录查询 
	 * @param page
	 * @param param 查询条件分装类
	 * @return page
	 */
	public PageDataList<ScoreLogModel> getScoreLogPage(ScoreLogModel model);
	
	/**
	 * 查询积分记录信息
	 * @param user_id
	 * @param typeNid积分类型表主键ID
	 * @return
	 */
	public List<ScoreLog> getScoreLogList(long userId , String typeNid);
	
}
