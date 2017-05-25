package com.rongdu.p2psys.borrow.executer;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.yjf.service.YjfService;
import com.rongdu.p2psys.tpp.yjf.service.impl.YjfServiceImpl;

/**
 * 复审失败
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowFullVerifyFailExecuter extends BaseExecuter {

	protected AccountLogDao accountLogDao;
	protected AccountDao accountDao;
	private String accountLogType = Constant.BORROW_FULL_VERIFY_FAIL;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");

	}

	@Override
	public void addAccountLog() {
	/*	Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		log.setMoney(super.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setRemark("满标审核失败。");
		log.setPaymentsType((byte)0);
		accountLogDao.save(log);*/
	}

	@Override
	public void handleAccount() {
		
	}

	@Override
	public void handleAccountSum() {

	}

	@Override
	public void handlePoints() {

	}

	@Override
	public void handleNotice() {
        BaseMsg msg = new BaseMsg(NoticeConstant.BORROW_FULL_FAIL);
        msg.doEvent();
	}

	@Override
	public void addOperateLog() {

	}

	@Override
	public void handleInterface() {
		//普通标满标复审失败调用接口解冻资金，
		if (BaseTPPWay.isOpenApi()) {
			if(TPPWay.API_CODE == TPPWay.API_CODE_YJF){
				YjfService apiService = new YjfServiceImpl();
				List<Object> taskList = new ArrayList<Object>();
				Borrow borrow = (Borrow) Global.getTransfer().get("borrow");
				apiService.failBorrow(taskList, borrow);
				apiService.doApiTask(taskList);
			}
			
		}
		
	}

	@Override
	public void extend() {

	}
}
