package com.rongdu.p2psys.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountBackDao;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.AccountDeduct;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountBackModel;
import com.rongdu.p2psys.account.service.AccountBackService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * 扣款service
 *  
 * @author：Administrator 
 * @version 1.0
 * @since 2014年6月12日
 */
@Service("accountBackService")
@Transactional
public class AccountBackServiceImpl implements AccountBackService {

	@Resource
	private AccountBackDao accountBackDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private VerifyLogDao verifyLogDao;
	@Resource
	private OperationLogDao operationLogDao;

	@Override
	public PageDataList<AccountBackModel> list(AccountBackModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null && !StringUtil.isBlank(model.getSearchName())){//模糊条件查询
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else{//精确条件查询
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
			if (model.getStatus() != 99) {
				param.addParam("status", model.getStatus());
			}
		}
		param.addPage(model.getPage(), model.getRows());
		if (model.getOrder().equals("desc")) {
			param.addOrder(OrderType.DESC, model.getSort());
		} else {
			param.addOrder(OrderType.ASC, model.getSort());
		}
		PageDataList<AccountDeduct> pageDateList = accountBackDao.findPageList(param);
		PageDataList<AccountBackModel> pageDateList_ = new PageDataList<AccountBackModel>();
		List<AccountBackModel> list = new ArrayList<AccountBackModel>();
		pageDateList_.setPage(pageDateList.getPage());

		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountDeduct accountBack = (AccountDeduct) pageDateList.getList().get(i);
				AccountBackModel abm = AccountBackModel.instance(accountBack);
				VerifyLog verifyLog = verifyLogDao.findByType(abm.getId(), "verifyAccountBack", 1);
				if (verifyLog != null) {
					abm.setVerifyUserName(verifyLog.getVerifyUser().getUserName());
				}
				try {
    				abm.setUserName(accountBack.getUser().getUserName());
    				abm.setRealName(accountBack.getUser().getRealName());
    				list.add(abm);
                } catch (Exception e){
                    e.printStackTrace();
                }
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public AccountDeduct add(AccountDeduct accountBack, double money, Operator operator) {
		accountBack = accountBackDao.save(accountBack);

		Global.setTransfer("money", money);
		AbstractExecuter executer = ExecuterHelper.doExecuter("deductAccountBackFreezeExecuter");
		executer.execute(money, accountBack.getUser().getUserId(), operator);

		return accountBack;
	}

	@Override
	public AccountDeduct find(long id) {
		return accountBackDao.find(id);
	}

	@Override
	public void verifyAccountBack(AccountBackModel model, Operator operator) {
		AccountDeduct accountBack = this.find(model.getId());
		User user = accountBack.getUser();
		if (accountBack.getStatus() != 0) {
			throw new AccountException("不允许操作该条记录！", 1);
		}
		accountBack.setStatus(model.getStatus());
		accountBack.setRemark(model.getRemark());
		accountBackDao.update(accountBack);
		Global.setTransfer("money", model.getMoney());
		Global.setTransfer("user", user);

		OperationLog operationLog = new OperationLog(user, operator, Constant.ACCOUNT_BACK_UNFREEZE);
		operationLog.setOperationResult("解冻（" + user.getUserName() + "）后台扣款冻结资金" + "（"
				+ model.getMoney() + "元）");
		operationLogDao.save(operationLog);
		if (model.getStatus() == 1) {
			// 审核通过
			AbstractExecuter executer = ExecuterHelper.doExecuter("accountBackSuccessExecuter");
			executer.execute(model.getMoney(), accountBack.getUser().getUserId(), operator);
		} else if (model.getStatus() == 2) {
			// 审核不通过
			AbstractExecuter executer = ExecuterHelper.doExecuter("accountBackFailExecuter");
			executer.execute(model.getMoney(), accountBack.getUser().getUserId(), operator);
		}
		// 审核日志
		VerifyLog verifyLog = new VerifyLog(operator, "verifyAccountBack", model.getId(), 1, model.getStatus(),
				model.getRemark());
		verifyLogDao.save(verifyLog);
	}

	@Override
	public List<AccountBackModel> find(String userName, String realName, int status) {
		QueryParam param = QueryParam.getInstance();
		if (StringUtil.isNotBlank(userName) && !"undefined".equals(userName)) {
			param.addParam("user.userName", Operators.EQ, userName);
		}
		if (StringUtil.isNotBlank(realName) && !"undefined".equals(realName)) {
			param.addParam("user.realName", Operators.EQ, realName);
		}
		param.addParam("status", status);
		List<AccountDeduct> list = accountBackDao.findByCriteria(param);
		List<AccountBackModel> modelList = new ArrayList<AccountBackModel>();
		for (int i = 0; i < list.size(); i++) {
			AccountDeduct accountBack = list.get(i);
			AccountBackModel model_ = AccountBackModel.instance(accountBack);
			model_.setMoneyString(new BigDecimal(accountBack.getMoney()).toPlainString());
			VerifyLog verifyLog = verifyLogDao.findByType(model_.getId(), "verifyAccountBack", 1);
			if (verifyLog != null) {
				model_.setVerifyUserName(verifyLog.getVerifyUser().getUserName());
			}

			if (model_.getType() == 2) {
				model_.setTypeName("账户扣款");
			} else {
				model_.setTypeName("");
			}

			if (model_.getStatus() == 0) {
				model_.setStatusName("待审核");
			} else if (model_.getStatus() == 1) {
				model_.setStatusName("审核成功");
			} else if (model_.getStatus() == 2) {
				model_.setStatusName("审核失败");
			}
			try {
    			model_.setUserName(accountBack.getUser().getUserName());
    			model_.setRealName(accountBack.getUser().getRealName());
    			modelList.add(model_);
			} catch (Exception e){
			    e.printStackTrace();
			}
		}
		return modelList;
	}

}
