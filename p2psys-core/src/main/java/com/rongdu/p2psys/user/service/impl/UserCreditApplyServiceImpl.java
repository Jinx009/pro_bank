package com.rongdu.p2psys.user.service.impl;

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
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.user.dao.UserCreditApplyDao;
import com.rongdu.p2psys.user.domain.UserCreditApply;
import com.rongdu.p2psys.user.model.UserCreditApplyModel;
import com.rongdu.p2psys.user.service.UserCreditApplyService;

@Service("userCreditApplyService")
public class UserCreditApplyServiceImpl implements UserCreditApplyService {

	@Resource
	private UserCreditApplyDao userCreditApplyDao;

	@Override
	public int count(long userId) {
		return userCreditApplyDao.count(userId);
	}

	@Override
	public int count(long userId, int status) {
		return userCreditApplyDao.count(userId, status);
	}

	@Override
	public PageDataList<UserCreditApply> list(long userId, BorrowModel model) {
		QueryParam param = QueryParam.getInstance().addParam("user.userId", userId).addOrder(OrderType.DESC, "addTime")
				.addPage(model.getPage());
		if (StringUtil.isNotBlank(model.getStartTime())) {
			Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
			param.addParam("addTime", Operators.GTE, start);
		}
		Date nowdate = DateUtil.getDate(System.currentTimeMillis()/1000 + "");
		if (model.getTime() == 7) {
			param.addParam("addTime", Operators.GTE,DateUtil.rollDay(nowdate, -7));
			param.addParam("addTime", Operators.LTE, nowdate);
		} else if (model.getTime() > 0 && model.getTime() < 4){
			param.addParam("addTime", Operators.GTE,DateUtil.rollMon(nowdate, -model.getTime()));
			param.addParam("addTime", Operators.LTE, nowdate);
		}
		if (StringUtil.isNotBlank(model.getEndTime())) {
			Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
			param.addParam("addTime", Operators.LTE, end);
		}
		if (model.getStatus() != 99) {
			param.addParam("status", model.getStatus());
		}
		return userCreditApplyDao.findPageList(param);
	}

	@Override
	public void save(UserCreditApply userCreditApply) {
		userCreditApplyDao.save(userCreditApply);
	}

	/**
	 * 后台申请额度列表
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public PageDataList<UserCreditApplyModel> list(UserCreditApplyModel model) {
		QueryParam param = QueryParam.getInstance();
		if(StringUtil.isNotBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else{
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
			if (model.getStatus() != 99 && model.getStatus() != 0) {
				param.addParam("status", model.getStatus());
			}
			if (!StringUtil.isBlank(model.getStartTime())) {
				param.addParam("addTime", Operators.GT, DateUtil.valueOf(model.getStartTime()));
			}
			if (!StringUtil.isBlank(model.getEndTime())) {
				param.addParam("addTime", Operators.LT, DateUtil.valueOf(model.getEndTime()));
			}
		}
		param.addPage(model.getPage(), model.getRows());
		if (model.getOrder().equals("desc")) {
			param.addOrder(OrderType.DESC, model.getSort());
		} else {
			param.addOrder(OrderType.ASC, model.getSort());
		}
		PageDataList<UserCreditApply> pList = userCreditApplyDao.findPageList(param);
		PageDataList<UserCreditApplyModel> modelList = new PageDataList<UserCreditApplyModel>();
		List<UserCreditApplyModel> list = new ArrayList<UserCreditApplyModel>();
		modelList.setPage(pList.getPage());
		if (pList.getList().size() > 0) {
			for (int i = 0; i < pList.getList().size(); i++) {
				UserCreditApply apply = (UserCreditApply) pList.getList().get(i);
				UserCreditApplyModel uam = UserCreditApplyModel.instance(apply);
				uam.setUserName(apply.getUser().getUserName());
				uam.setRealName(apply.getUser().getRealName());
				uam.setUserType(apply.getUser().getUserCache().getUserType());
				list.add(uam);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	@Override
	public UserCreditApply findById(long id) {
		return userCreditApplyDao.find(id);
	}

}
