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
import com.rongdu.p2psys.user.dao.UserBaseInfoDao;
import com.rongdu.p2psys.user.dao.UserCertificationApplyDao;
import com.rongdu.p2psys.user.dao.UserCertificationDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCertification;
import com.rongdu.p2psys.user.domain.UserCertificationApply;
import com.rongdu.p2psys.user.model.UserCertificationApplyModel;
import com.rongdu.p2psys.user.service.UserCertificationApplyService;

@Service("userCertificationApplyService")
public class UserCertificationApplyServiceImpl implements
		UserCertificationApplyService {

	@Resource
	UserCertificationApplyDao userCertificationApplyDao;
	@Resource
	UserCertificationDao userCertificationDao;
	@Resource
	UserBaseInfoDao userBaseInfoDao;

	@Override
	public void add(UserCertificationApply userCertificationApply) {

		userCertificationApplyDao.save(userCertificationApply);
	}

	@Override
	public PageDataList<UserCertificationApplyModel> applyList(
			UserCertificationApplyModel model) {
		QueryParam param = QueryParam.getInstance();
		//模糊查询条件 start
		if (StringUtil.isNotBlank(model.getSearchName())) {
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}
		//模糊查询条件 end
		//精确查询条件 start
		if (StringUtil.isNotBlank(model.getUserName())) {
			param.addParam("user.userName", Operators.EQ, model.getUserName());
		}
		if (StringUtil.isNotBlank(model.getRealName())) {
			param.addParam("user.realName", Operators.EQ, model.getRealName());
		}
		if (!StringUtil.isBlank(model.getStartTime())) {
			param.addParam("addTime", Operators.GT, DateUtil.valueOf(model.getStartTime()));
		}
		if (!StringUtil.isBlank(model.getEndTime())) {
			param.addParam("addTime", Operators.LT, DateUtil.valueOf(model.getEndTime()));
		}
		if (model.getStatus() != 99) {
			param.addParam("status", model.getStatus());
		}
		param.addOrder(OrderType.DESC, "id");
		//精确查询条件 end
		param.addPage(model.getPage(), model.getRows());
		PageDataList<UserCertificationApply> pList = userCertificationApplyDao.findPageList(param);
		PageDataList<UserCertificationApplyModel> modelList = new PageDataList<UserCertificationApplyModel>();
		List<UserCertificationApplyModel> list = new ArrayList<UserCertificationApplyModel>();
		modelList.setPage(pList.getPage());
		for (UserCertificationApply apply : pList.getList()) {
			UserCertificationApplyModel ucm = UserCertificationApplyModel
					.instance(apply);
			User user = apply.getUser();
			UserBaseInfo userInfo = userBaseInfoDao.findObjByProperty("user.userId", user.getUserId());
			if(userInfo != null){
				ucm.setBirthday(userInfo.getBirthday());
				ucm.setEducation(userInfo.getEducation());
				ucm.setMaritalStatus(userInfo.getMaritalStatus());
				ucm.setProvince(userInfo.getProvince());
				ucm.setCity(userInfo.getCity());
				ucm.setWorkExperience(userInfo.getWorkExperience());
				ucm.setMonthIncomeRange(userInfo.getMonthIncomeRange());
				ucm.setCarStatus(userInfo.getCarStatus());
				ucm.setHouseStatus(userInfo.getHouseStatus());
			}
			ucm.setRealName(user.getRealName());
			ucm.setUserName(user.getUserName());
			ucm.setUserId(user.getUserId());
			ucm.setTypeName(apply.getType().getName());
			ucm.setTypeId(apply.getType().getId());
			ucm.setUser(null);
			ucm.setType(null);
			list.add(ucm);
		}
		modelList.setList(list);
		return modelList;
	}

	@Override
	public void certificationVerify(UserCertificationApplyModel model) {
		UserCertificationApply apply = userCertificationApplyDao.find(model.getId());
		apply.setStatus(model.getStatus());
		apply.setScore(model.getScore());
		apply.setVerifyRemark(model.getVerifyRemark());
		apply.setVerifyTime(new Date());
		apply.setOperator(model.getOperator());
		userCertificationApplyDao.merge(apply);
	}

	@Override
	public int count() {
		QueryParam param = QueryParam.getInstance().addParam("status", 0);
		return userCertificationApplyDao.countByCriteria(param);
	}

	@Override
	public int getStatusByUserAndTypeId(User user, long typeId) {
		return userCertificationApplyDao.getStatusByUserAndTypeId(user, typeId);
	}

	@Override
	public List<UserCertificationApplyModel> findByUser(User user) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user", user);
		param.addParam("status", 1);
		List<UserCertificationApply> list = userCertificationApplyDao.findByCriteria(param);
		List<UserCertificationApplyModel> models = new ArrayList<UserCertificationApplyModel>();
		for (UserCertificationApply ua : list) {
			UserCertificationApplyModel model = UserCertificationApplyModel.instance(ua);
			model.setTypeName(ua.getType().getName());
			model.setType(null);
			model.setUser(null);
			models.add(model);
		}
		return models;
	}

	@Override
	public void add(UserCertificationApply userCertificationApply,
			List<UserCertification> list) {
		userCertificationApplyDao.save(userCertificationApply);
		userCertificationDao.save(list);
	}
	
}
