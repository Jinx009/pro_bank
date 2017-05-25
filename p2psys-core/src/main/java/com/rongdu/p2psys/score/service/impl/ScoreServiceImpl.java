package com.rongdu.p2psys.score.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.score.dao.ScoreDao;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.model.ScoreModel;
import com.rongdu.p2psys.score.service.ScoreService;

@Service("scoreService")
public class ScoreServiceImpl implements ScoreService {

	@Resource
	private ScoreDao scoreDao;
	
	public PageDataList<ScoreModel> getScorePage(ScoreModel model) {
		QueryParam param = QueryParam.getInstance();
		if(!StringUtil.isBlank(model.getSearchName())){
			param.addParam("user.userName",Operators.LIKE , model.getSearchName());
		}else{
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
		}
		param.addOrder(OrderType.DESC, "id");
		param.addPage(model.getPage(), model.getRows());
		PageDataList<Score> itemPage = scoreDao.findPageList(param);
		List<ScoreModel> modelList = new ArrayList<ScoreModel>();
		PageDataList<ScoreModel> modelPage = new PageDataList<ScoreModel>();
		if(itemPage != null && itemPage.getList() != null && itemPage.getList().size() > 0){
			modelPage.setPage(itemPage.getPage());
			for (int i = 0 ; i < itemPage.getList().size(); i++) {
				Score item = itemPage.getList().get(i);
				ScoreModel model_ = ScoreModel.instance(item);
				model_.setUserName(item.getUser().getUserName());
				modelList.add(model_);
			}
		}
		modelPage.setList(modelList);
		return modelPage;
	}

	public Boolean updateScore(long userId, int value, String nid) {
		// TODO Auto-generated method stub
		return null;
	}

	public Score getScoreByUserId(long userId) {
		// TODO Auto-generated method stub
		return scoreDao.findObjByProperty("user.userId", userId);
	}

	public int getCreditValueByUserId(long userId, Byte type) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Boolean updateScore(Score Score) {
		// TODO Auto-generated method stub
		return null;
	}

}
