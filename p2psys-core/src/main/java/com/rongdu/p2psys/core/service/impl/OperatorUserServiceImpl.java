package com.rongdu.p2psys.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountRechargeDao;
import com.rongdu.p2psys.core.dao.OperatorUserDao;
import com.rongdu.p2psys.core.domain.OperatorRole;
import com.rongdu.p2psys.core.domain.OperatorUser;
import com.rongdu.p2psys.core.service.OperatorUserService;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.model.UserFinancialModel;

@Service("OperatorUserService")
public class OperatorUserServiceImpl implements OperatorUserService{
	@Resource
	private AccountRechargeDao accountRechargeDao;
	@Resource
	private UserDao userDao;
	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private OperatorUserDao operatorUserDao;
	
	@Override
	public PageDataList<UserFinancialModel> getList(int pageNumber, int pageSize,UserFinancialModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		param.addOrder(OrderType.DESC, "rd_user.user_id");
		String sql = "from rd_user,rd_user_cache,rd_user_identify where rd_user.user_id=rd_user_identify.user_id and rd_user.user_id=rd_user_cache.user_id";
		if(!StringUtil.isBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("user_name", Operators.LIKE, model.getSearchName());
			SearchFilter orFilter2 = new SearchFilter("real_name", Operators.LIKE, model.getSearchName());
			param.addOrFilter(orFilter1, orFilter2);
		}else{
			if (model.getUserCache() != null && model.getUserCache().getUserType() != 0) {
				param.addParam("user_type", model.getUserCache().getUserType());
			}
			if (model.getUserCache() != null && model.getStatus() != 99) {
			    param.addParam("status", model.getStatus());
			}
			if (model.getUserIdentify() != null && model.getUserIdentify().getRealNameStatus() != -2) {
			    param.addParam("real_name_status", model.getUserIdentify().getRealNameStatus());
			}
			if (!StringUtil.isBlank(model.getUserName())) {
				param.addParam("user_name", Operators.EQ, model.getUserName());
			}
			if (!StringUtil.isBlank(model.getRealName())) {
				param.addParam("real_name", Operators.EQ, model.getRealName());
			}
			if (model.getfStatus() != 99){
				if(model.getfStatus() == 1){
					sql = "from rd_user,rd_user_cache,rd_user_identify,rd_operator_user where rd_user.user_id=rd_user_identify.user_id and rd_user.user_id=rd_user_cache.user_id and rd_user.user_id=rd_operator_user.user_id ";
				}else{
					sql = "from rd_user,rd_user_cache,rd_user_identify where rd_user.user_id=rd_user_identify.user_id and rd_user.user_id=rd_user_cache.user_id and rd_user.user_id not in "
							+ "(select rd_operator_user.user_id from rd_operator_user) ";
				}
			}
		}
		PageDataList<User> ulist = userDao.findPageListBySql(sql, param);
		PageDataList<UserFinancialModel> pList = new PageDataList<UserFinancialModel>();
		pList.setPage(ulist.getPage());
		List<UserFinancialModel> list = new ArrayList<UserFinancialModel>();
		if(ulist.getList().size()>0){
			for (int i = 0; i < ulist.getList().size(); i++) {
				User user = ulist.getList().get(i);
				UserFinancialModel userFinancialModel = UserFinancialModel.instance(user);
				double rechargeTotal = accountRechargeDao.getRechargeTotal(user.getUserId(), model.getrStartTime(), model.getrEndTime());
				userFinancialModel.setRechargeTotal(rechargeTotal);
				UserCache userCache = userCacheDao.findByUserId(user.getUserId());
				userFinancialModel.setUserCache(userCache);
				OperatorUser operatorUser = operatorUserDao.findObjByProperty("user", user);
				if(operatorUser != null){
					userFinancialModel.setOpName(operatorUser.getOperator().getName());;
				}
				list.add(userFinancialModel);
			}
		}
		pList.setList(list);
		return pList;
	}

	@Override
	public void addFinancial(List<OperatorUser> list) {
		for (int i = 0; i < list.size(); i++) {
			OperatorUser operatorUser = list.get(i);
			List<OperatorUser> oldList = operatorUserDao.findByProperty("user", operatorUser.getUser());
			if(oldList != null && oldList.size() > 0){
				operatorUserDao.delete(oldList.get(0).getId());
			}
			operatorUserDao.save(operatorUser);
		}
	}

	@Override
	public PageDataList<UserFinancialModel> getFinancialUserList(int pageNumber, int pageSize, UserFinancialModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		param.addOrder(OrderType.DESC, "rd_user.user_id");
		String sql = "from rd_user,rd_user_cache,rd_user_identify where rd_user.user_id=rd_user_identify.user_id and rd_user.user_id=rd_user_cache.user_id";
		if(!StringUtil.isBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("user_name", Operators.LIKE, model.getSearchName());
			SearchFilter orFilter2 = new SearchFilter("real_name", Operators.LIKE, model.getSearchName());
			param.addOrFilter(orFilter1, orFilter2);
		}else{
			if (model.getUserCache() != null && model.getUserCache().getUserType() != 0) {
				param.addParam("user_type", model.getUserCache().getUserType());
			}
			if (model.getUserCache() != null && model.getStatus() != 99) {
			    param.addParam("status", model.getStatus());
			}
			if (model.getUserIdentify() != null && model.getUserIdentify().getRealNameStatus() != -2) {
			    param.addParam("real_name_status", model.getUserIdentify().getRealNameStatus());
			}
			if (!StringUtil.isBlank(model.getUserName())) {
				param.addParam("user_name", Operators.EQ, model.getUserName());
			}
			if (!StringUtil.isBlank(model.getRealName())) {
				param.addParam("real_name", Operators.EQ, model.getRealName());
			}
			for (OperatorRole operatorRole : model.getOperator().getOperatorRole()) {
				//理财经理
				if(operatorRole.getRole().getId() == 2){
					//获取名下所有理财顾问
					sql = "from rd_user,rd_user_cache,rd_user_identify,rd_operator_user where rd_user.user_id=rd_user_identify.user_id and rd_user.user_id=rd_user_cache.user_id and rd_user.user_id=rd_operator_user.user_id "
							+ "and rd_operator_user.operator_id in (select operator_id from s_parent_operator where p_operator_id = "+model.getOperator().getId()+") ";
				}
				if(operatorRole.getRole().getId() == 3){
					sql = "from rd_user,rd_user_cache,rd_user_identify,rd_operator_user where rd_user.user_id=rd_user_identify.user_id and rd_user.user_id=rd_user_cache.user_id and rd_user.user_id=rd_operator_user.user_id "
							+ "and rd_operator_user.operator_id  = "+model.getOperator().getId()+" ";
				}
			}
		}
		PageDataList<User> ulist = userDao.findPageListBySql(sql, param);
		PageDataList<UserFinancialModel> pList = new PageDataList<UserFinancialModel>();
		pList.setPage(ulist.getPage());
		List<UserFinancialModel> list = new ArrayList<UserFinancialModel>();
		if(ulist.getList().size()>0){
			for (int i = 0; i < ulist.getList().size(); i++) {
				User user = ulist.getList().get(i);
				UserFinancialModel userFinancialModel = UserFinancialModel.instance(user);
				double rechargeTotal = accountRechargeDao.getRechargeTotal(user.getUserId());
				userFinancialModel.setRechargeTotal(rechargeTotal);
				UserCache userCache = userCacheDao.findByUserId(user.getUserId());
				userFinancialModel.setUserCache(userCache);
				OperatorUser operatorUser = operatorUserDao.findObjByProperty("user", user);
				if(operatorUser != null){
					userFinancialModel.setOpName(operatorUser.getOperator().getName());;
				}
				list.add(userFinancialModel);
			}
		}
		pList.setList(list);
		return pList;
	}
}
