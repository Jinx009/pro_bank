package com.rongdu.p2psys.account.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.core.dao.DictDao;

@Repository("accountLogDao")
public class AccountLogDaoImpl extends BaseDaoImpl<AccountLog> implements AccountLogDao {
	@Resource
	private DictDao dictDao;

	@Override
	public PageDataList<AccountLogModel> accountLogList(AccountLogModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		
		if(!StringUtil.isBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1, orFilter1);
		}else{
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime());
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime());
				param.addParam("addTime", Operators.LTE, end);
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getType())) {
				param.addParam("type", Operators.EQ, model.getType());
			}
			if (model.getUser() != null && model.getUser().getUserCache() != null 
					&& model.getUser().getUserCache().getUserType() != 0){
				param.addParam("user.userCache.userType", Operators.EQ, model.getUser().getUserCache().getUserType());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<AccountLog> pageDateList = super.findPageList(param);
		PageDataList<AccountLogModel> pageDateList_ = new PageDataList<AccountLogModel>();
		List<AccountLogModel> list = new ArrayList<AccountLogModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountLog c = (AccountLog) pageDateList.getList().get(i);
				AccountLogModel cm = AccountLogModel.instance(c);
				try{
				cm.setToUserName("800ZF");
    				cm.setUserName(c.getUser().getUserName());
    				cm.setRealName(c.getUser().getRealName());
    				cm.setTypeName(c.getTypeName());
    				list.add(cm);
				}
				catch(Exception e){
                    e.printStackTrace();
				}
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}
	
	@Override
	public PageDataList<AccountLogModel> accountLogSingleList(AccountLogModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		param.addParam("type", model.getAccountType());
		if(!StringUtil.isBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1, orFilter1);
		}else{
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime());
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime());
				param.addParam("addTime", Operators.LTE, end);
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<AccountLog> pageDateList = super.findPageList(param);
		PageDataList<AccountLogModel> pageDateList_ = new PageDataList<AccountLogModel>();
		List<AccountLogModel> list = new ArrayList<AccountLogModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountLog c = (AccountLog) pageDateList.getList().get(i);
				AccountLogModel cm = AccountLogModel.instance(c);
				try{
    				cm.setToUserName(c.getToUser().getUserName());
    				cm.setUserName(c.getUser().getUserName());
    				cm.setTypeName(c.getTypeName());
    				cm.setRealName(c.getUser().getRealName());
    				list.add(cm);
				}
				catch(Exception e){
                    e.printStackTrace();
				}
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public double getAccountSingleTotal(AccountLogModel model) {
		String sql = "SELECT sum(al.money) from rd_account_log al, rd_user u where al.user_id=u.user_id ";
		if(!StringUtil.isBlank(model.getAccountType())){
			sql += " and al.type = '" + model.getAccountType() + "'";
		}
		if(!StringUtil.isBlank(model.getSearchName())){
			sql += " and u.user_name like '%" + model.getSearchName() + "%'";
		}else{
			if (StringUtil.isNotBlank(model.getStartTime())) {
				String start = "'" + model.getStartTime() + "'";
				sql += " and al.add_time >= " + start;
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				String end = "'" + model.getEndTime() + "'";
				sql += " and al.add_time <= " + end;
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				sql += " and u.user_name = " + "'" + model.getUserName() + "'";
			}
		}
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if(obj != null){
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getAccountSingleTotal(String startTime, String endTime,
			String accountType) {
		String sql = "SELECT sum(al.money) from rd_account_log al, rd_user u where al.user_id=u.user_id ";
		if(!StringUtil.isBlank(accountType)){
			sql += " and al.type in (" + accountType + ")";
		}
		if (StringUtil.isNotBlank(startTime)) {
			String start = "'" + startTime + "'";
			sql += " and al.add_time >= " + start;
		}
		if (StringUtil.isNotBlank(endTime)) {
			String end = "'" + endTime + "'";
			sql += " and al.add_time <= " + end;
		}
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if(obj != null){
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}
	
}
