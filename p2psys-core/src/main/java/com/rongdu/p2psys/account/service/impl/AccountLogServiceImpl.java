package com.rongdu.p2psys.account.service.impl;

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
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.account.service.AccountLogService;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.user.domain.User;

@Service("accountLogService")
public class AccountLogServiceImpl implements AccountLogService {

	@Resource
	private AccountLogDao accountLogDao;
	@Resource
	private DictDao dictDao;

	@Override
	public void add(AccountLog log) {
		accountLogDao.save(log);
	}

	@Override
	public PageDataList<AccountLogModel> list(AccountLogModel model) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", model.getUser().getUserId());
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

		PageDataList<AccountLog> pageDateList = accountLogDao.findPageList(param);
		PageDataList<AccountLogModel> pageDateList_ = new PageDataList<AccountLogModel>();
		List<AccountLogModel> list = new ArrayList<AccountLogModel>();
		pageDateList_.setPage(pageDateList.getPage());
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountLog c = (AccountLog) pageDateList.getList().get(i);
				AccountLogModel cm = AccountLogModel.instance(c);
				Dict d = dictDao.find("account_type", c.getType());
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
	public PageDataList<AccountLogModel> accountLogList(AccountLogModel model, int pageNumber, int pageSize) {
		return accountLogDao.accountLogList(model, pageNumber, pageSize);
	}

    @Override
    public List<AccountLogModel> accountTransactionLog(User user) {
        QueryParam param = QueryParam.getInstance();
        param.addParam("user.userId", user.getUserId());
        param.addOrder(OrderType.DESC, "id");
        List<AccountLog> list = accountLogDao.findByCriteria(param, 0, 4);
        List<AccountLogModel> accountLogModelList = new ArrayList<AccountLogModel>();
        for (int i = 0; i < list.size(); i++) {
            AccountLog c = list.get(i);
            AccountLogModel cm = AccountLogModel.instance(c);
            cm.setTypeName(c.getTypeName());
            if (c.getToUser() != null) {
                cm.setToUserName(c.getToUser().getUserName());
            } else {
                cm.setToUserName("");
            }
            cm.setToUser(null);
            accountLogModelList.add(cm);
        }
        return accountLogModelList;
    }
    
    @Override
	public PageDataList<AccountLogModel> accountLogSingleList(AccountLogModel model, int pageNumber, int pageSize) {
		return accountLogDao.accountLogSingleList(model, pageNumber, pageSize);
	}

	@Override
	public double getAccountSingleTotal(AccountLogModel model) {
		return accountLogDao.getAccountSingleTotal(model);
	}

	@Override
	public double getAccountSingleTotal(String startTime, String endTime,
			String accountType) {
		return accountLogDao.getAccountSingleTotal(startTime, endTime, accountType);
	}

}
