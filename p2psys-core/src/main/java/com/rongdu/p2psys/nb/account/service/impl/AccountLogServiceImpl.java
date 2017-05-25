package com.rongdu.p2psys.nb.account.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.nb.account.dao.AccountLogDao;
import com.rongdu.p2psys.nb.account.service.AccountLogService;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.user.domain.User;

@Service("theAccountLogService")
public class AccountLogServiceImpl implements AccountLogService
{

	@Resource
	private AccountLogDao theAccountLogDao;
	@Resource
	private UserDao theUserDao;
	@Resource
	private DictDao theDictDao;
	
	public AccountLog saveAccountLog(AccountLog accountLog)
	{
		return theAccountLogDao.save(accountLog);
	}

	public void saveNewAccountLog(Ppfund ppfund,Account account, User user,String  ip_address,double money)
	{
		User to_user = theUserDao.find(1);
		AccountLog accountLog = new AccountLog();
		
		accountLog.setAddIp(ip_address);
		accountLog.setAddTime(new Date());
		accountLog.setCollection(account.getCollection());
		accountLog.setMoney(money);
		accountLog.setPaymentsType((byte) 0);
		accountLog.setNoUseMoney(account.getNoUseMoney());
		accountLog.setRemark("[<a href='http://www.800bank.com.cn/ppfund/detail.html?id="+ppfund.getId()+"' target=_blank>"+ppfund.getName()+"</a>]投资成功，扣除投资者的投标资金"+money+"元，并生成待收本金"+money+"元。");
		accountLog.setToUser(to_user);
		accountLog.setTotal(account.getTotal());
		accountLog.setType(ConstantUtil.PPFUND_TENDER);
		accountLog.setToUser(user);
		accountLog.setUseMoney(account.getUseMoney());
		
		theAccountLogDao.save(accountLog);
	}
	
	@Override
	public PageDataList<AccountLogModel> multipleIdentities(AccountLogModel model) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.bindId", model.getUser().getBindId());
		if (StringUtil.isNotBlank(model.getStartTime())) {
			Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
			param.addParam("addTime", Operators.GTE, start);
		}
		Date nowdate = DateUtil.getDate(System.currentTimeMillis()/1000 + "");
		if (model.getTime() == 7) {
			param.addParam("addTime", Operators.GTE,DateUtil.rollDay(nowdate, -7));
			param.addParam("addTime", Operators.LTE, nowdate);
		} else if (model.getTime()>0 && model.getTime()<4){
			param.addParam("addTime", Operators.GTE,DateUtil.rollMon(nowdate, -model.getTime()));
			param.addParam("addTime", Operators.LTE, nowdate);
		}
		if (StringUtil.isNotBlank(model.getEndTime())) {
			Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
			param.addParam("addTime", Operators.LTE, end);
		}
		if (StringUtil.isNotBlank(model.getStatus()) && !("0").equals(model.getStatus()) && !"undefined".equals(model.getStatus())) {
			if (Constant.AL_ONLINE_RECHARGE.equals(model.getStatus())) {
				SearchFilter filter1 = new SearchFilter("type", Constant.AL_OFF_RECHARGE);
				SearchFilter filter2 = new SearchFilter("type", Constant.AL_ONLINE_RECHARGE);
				SearchFilter filter3 = new SearchFilter("type", Constant.AL_BACK_RECHARGE);
				param.addOrFilter(filter1, filter2, filter3);
			} else {
				param.addParam("type", model.getStatus());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		param.addPage(model.getPage());

		PageDataList<AccountLog> pageDateList = theAccountLogDao.findPageList(param);
		PageDataList<AccountLogModel> pageDateList_ = new PageDataList<AccountLogModel>();
		List<AccountLogModel> list = new ArrayList<AccountLogModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountLog c = (AccountLog) pageDateList.getList().get(i);
				AccountLogModel cm = AccountLogModel.instance(c);
				Dict d = theDictDao.find("account_type", c.getType());
				if (d != null) {
					cm.setTypeName(d.getName());
				}
				if (c.getToUser() != null) {
					cm.setToUserName(c.getToUser().getUserName());
				} else {
					cm.setToUserName("");
				}
				cm.setToUser(null);
				list.add(cm);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public void addAccountLog(AccountLogModel model) {
		AccountLog al = AccountLogModel.instance(model);
		theAccountLogDao.save(al);
	}

	@Override
	public PageDataList<AccountLogModel> getAccountLog(AccountLogModel model) {
		QueryParam param = QueryParam.getInstance();
		if(null != model){
			param.addParam("user.bindId", model.getUser().getBindId());
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			if (StringUtil.isNotBlank(model.getStatus()) && !("0").equals(model.getStatus()) && !"undefined".equals(model.getStatus())) {
				if (Constant.AL_ONLINE_RECHARGE.equals(model.getStatus())) {
					SearchFilter filter1 = new SearchFilter("type", Constant.AL_OFF_RECHARGE);
					SearchFilter filter2 = new SearchFilter("type", Constant.AL_ONLINE_RECHARGE);
					SearchFilter filter3 = new SearchFilter("type", Constant.AL_BACK_RECHARGE);
					param.addOrFilter(filter1, filter2, filter3);
				} else if(Constant.CASH_FROST.equals(model.getStatus())) {
					SearchFilter filter1 = new SearchFilter("type", Constant.CASH_FROST);
					SearchFilter filter2 = new SearchFilter("type", Constant.CASH_SUCCESS);
					SearchFilter filter3 = new SearchFilter("type",Constant.CASH_CANCEL);
					SearchFilter filter4 = new SearchFilter("type",Constant.CASH_FAIL);
					param.addOrFilter(filter1,filter2,filter3,filter4);
				}
			}
			param.addOrder(OrderType.DESC, "id");
			param.addPage(model.getPage());
		}
	
		PageDataList<AccountLog> pageDateList = theAccountLogDao.findPageList(param);
		PageDataList<AccountLogModel> pageDateList_ = new PageDataList<AccountLogModel>();
		List<AccountLogModel> list = new ArrayList<AccountLogModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountLog c = (AccountLog) pageDateList.getList().get(i);
				AccountLogModel cm = AccountLogModel.instance(c);
				Dict d = theDictDao.find("account_type", c.getType());
				if (d != null) {
					cm.setTypeName(d.getName());
				}
				if (c.getToUser() != null) {
					cm.setToUserName(c.getToUser().getUserName());
				} else {
					cm.setToUserName("");
				}
				cm.setToUser(null);
				list.add(cm);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	public List<AccountLog> getByUserId(Long userId) {
		String hql = " FROM AccountLog where  user.userId ="+userId;
		return theAccountLogDao.findByHql(hql);
	}

}
