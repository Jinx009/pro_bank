package com.rongdu.p2psys.score.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.score.dao.ScoreLogDao;
import com.rongdu.p2psys.score.domain.ScoreLog;
import com.rongdu.p2psys.score.model.ScoreLogModel;
import com.rongdu.p2psys.score.service.ScoreLogService;

@Service("scoreLogService")
public class ScoreLogServiceImpl implements ScoreLogService {

	@Resource
	private ScoreLogDao scoreLogDao;
	
	public PageDataList<ScoreLogModel> getScoreLogPage(ScoreLogModel model) {
		QueryParam param = QueryParam.getInstance();
		if(!StringUtil.isBlank(model.getSearchName())){//模糊查询
			param.addParam("user.userName",Operators.LIKE , model.getSearchName());
		}else{////精确查询
			if(model.getUser() != null && model.getUser().getUserId() > 0){
				param.addParam("user.userId", model.getUser().getUserId());
			}
			if(model.getUserName() != null && model.getUserName().length() > 0){
				param.addParam("user.userName",Operators.EQ , model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			if (StringUtil.isNotBlank(model.getScoreTypeNid()) && !("").equals(model.getScoreTypeNid())) {
				param.addParam("scoreTypeNid", model.getScoreTypeNid());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		if(model.getRows() == 0){
			param.addPage(model.getPage());
		}else {
			param.addPage(model.getPage(), model.getRows());
		}
		
		PageDataList<ScoreLog> itemPage = scoreLogDao.findPageList(param);
		List<ScoreLogModel> modelList = new ArrayList<ScoreLogModel>();
		PageDataList<ScoreLogModel> modelPage = new PageDataList<ScoreLogModel>();
		if(itemPage != null && itemPage.getList() != null && itemPage.getList().size() > 0){
			modelPage.setPage(itemPage.getPage());
			for (int i = 0 ; i < itemPage.getList().size(); i++) {
				ScoreLog item = itemPage.getList().get(i);
				ScoreLogModel model_ = ScoreLogModel.instance(item);
				model_.setUserName(item.getUser().getUserName());
				modelList.add(model_);
			}
		}
		modelPage.setList(modelList);
		return modelPage;
	}

	public List<ScoreLog> getScoreLogList(long userId, String typeNid) {
		// TODO Auto-generated method stub
		return null;
	}

}
