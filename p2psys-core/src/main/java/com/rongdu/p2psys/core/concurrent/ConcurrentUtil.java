package com.rongdu.p2psys.core.concurrent;

import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCorpRegister;
import com.rongdu.p2psys.tpp.ips.model.IpsRegister;
import com.rongdu.p2psys.tpp.yjf.model.CashModel;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;
import com.rongdu.p2psys.user.domain.User;

public class ConcurrentUtil {

	/**
	 * 投标
	 * 
	 * @param model
	 * @param borrow
	 * @throws Exception
	 */
	public static void tender(BorrowModel model, Borrow borrow)
			throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("tender");
		event.setBorrowModel(model);
		event.setBorrow(borrow);
		JobQueue.getTenderInstance().offer(event);

	}

	/**
	 * 自动投标
	 * 
	 * @param model
	 * @throws Exception
	 */
	public static void autoTender(BorrowModel model) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoTender");
		event.setBorrowModel(model);
		JobQueue.getTenderInstance().offer(event);
	}

	/**
	 * 复审通过
	 * 
	 * @param model
	 * @throws Exception
	 */
	public static void autoVerifyFullSuccess(BorrowModel model)
			throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoVerifyFullSuccess");
		event.setBorrowModel(model);
		JobQueue.getVerifyBorrowInstance().offer(event);
	}

	/**
	 * 复审不通过
	 * 
	 * @param model
	 * @throws Exception
	 */
	public static void autoVerifyFullFail(BorrowModel model) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoVerifyFullFail");
		event.setBorrowModel(model);
		JobQueue.getVerifyBorrowInstance().offer(event);
	}

	/**
	 * 还款
	 * 
	 * @param borrowRepayment
	 */
	public static void repay(BorrowRepayment borrowRepayment) {
		ValueEvent event = new ValueEvent();
		event.setOperate("repay");
		event.setBorrowRepayment(borrowRepayment);
		JobQueue.getBorrowInstance().offer(event);
	}
	
	/**
	 * vip还款
	 * @param borrowRepayment
	 */
	public static void vipRepay() {
		ValueEvent event = new ValueEvent();
		event.setOperate("vipRepay");
		JobQueue.getBorrowInstance().offer(event);
	}

	/**
	 * 撤标
	 * 
	 * @param model
	 */
	public static void autoCancel(Borrow borrow) {
		ValueEvent event = new ValueEvent();
		event.setOperate("autoCancel");
		event.setBorrow(borrow);
		JobQueue.getBorrowInstance().offer(event);
	}

	/**
	 * 提现
	 * 
	 * @param cashModel
	 *            提现的参数封装
	 * @throws Exception
	 *             异常
	 */
	public static void doVerifyCashBackTask(CashModel cashModel,
			String resultFlag) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("verifyCashBack");
		event.setCashModel(cashModel);
		event.setResultFlag(resultFlag);
		JobQueue.getCashInstance().offer(event);
	}

	/**
	 * 充值
	 * 
	 * @param reModel
	 *            充值的参数封装
	 * @param log
	 *            充值日志的参数封装
	 */
	public static void doRechargeBackTask(RechargeModel reModel,
			String resultFlag) {
		ValueEvent event = new ValueEvent();
		event.setOperate("verifyRecharge");
		event.setRechargeModel(reModel);
		event.setResultFlag(resultFlag);
		JobQueue.getCashInstance().offer(event);
	}

	/**
	 * 提前还款
	 * 
	 * @param borrowRepayment
	 */
	public static void doPriorRepay(BorrowRepayment borrowRepayment) {
		ValueEvent event = new ValueEvent();
		event.setOperate("doPriorRepay");
		event.setBorrowRepayment(borrowRepayment);
		JobQueue.getBorrowInstance().offer(event);
	}

	/**
	 * 前台逾期垫付
	 * 
	 * @param borrowRepayment
	 */
	public static void overduePayment(BorrowRepayment borrowRepayment) {
		ValueEvent event = new ValueEvent();
		event.setOperate("overduePayment");
		event.setBorrowRepayment(borrowRepayment);
		JobQueue.getBorrowInstance().offer(event);
	}

	/**
	 * 环迅开户
	 * 
	 * @param user
	 * @param ips
	 */
	public static void ipsRegister(User user, IpsRegister ips, String resultFlag) {
		ValueEvent event = new ValueEvent();
		event.setOperate("ipsRegister");
		event.setUser(user);
		event.setIpsRegister(ips);
		event.setResultFlag(resultFlag);
		JobQueue.getUserInstance().offer(event);

	}

	/**
	 * 第三方开户接口
	 */
	public static void apiUserRegister(User user, String resultFlag) {
		ValueEvent event = new ValueEvent();
		event.setOperate("apiUserRegister");
		event.setUser(user);
		event.setResultFlag(resultFlag);
		JobQueue.getUserInstance().offer(event);
	}

	public static void apiCorpRegister(User user,
			ChinapnrCorpRegister corpRegister) {
		ValueEvent event = new ValueEvent();
		event.setOperate("apiCorpRegister");
		event.setUser(user);
		event.setCorpRegister(corpRegister);
		JobQueue.getUserInstance().offer(event);
	}

	/**
	 * 发标回调
	 * 
	 * @param user
	 * @param ips
	 */
	public static void doAddBorrow(BorrowModel bm) {
		ValueEvent event = new ValueEvent();
		event.setOperate("doAddBorrow");
		event.setBorrowModel(bm);
		JobQueue.getBorrowInstance().offer(event);

	}

	/**
	 * 登记担保方
	 * 
	 * @param bm
	 */
	public static void doIpsRegisterGuarantor(BorrowModel bm, String resultFlag) {
		ValueEvent event = new ValueEvent();
		event.setOperate("doIpsRegisterGuarantor");
		event.setBorrowModel(bm);
		event.setResultFlag(resultFlag);
		JobQueue.getUserInstance().offer(event);
	}

	/**
	 * 投标回调
	 * 
	 * @param bm
	 */
	public static void doAddTender(BorrowModel bm, String resultFlag) {
		ValueEvent event = new ValueEvent();
		event.setOperate("doAddTender");
		event.setBorrowModel(bm);
		event.setResultFlag(resultFlag);
		JobQueue.getTenderInstance().offer(event);

	}

	/**
	 * 代偿回调
	 * 
	 * @param borrowRepayment
	 */
	public static void doCompensateSuccess(BorrowRepayment borrowRepayment) {
		ValueEvent event = new ValueEvent();
		event.setOperate("doCompensateSuccess");
		event.setBorrowRepayment(borrowRepayment);
		JobQueue.getBorrowInstance().offer(event);
	}

	/**
	 * 债权投标
	 * 
	 * @param model
	 * @param borrow
	 * @throws Exception
	 */
	public static void bondTender(BondModel model) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("bondTender");
		event.setBondModel(model);
		JobQueue.getBondInstance().offer(event);
	}

	/**
	 * PPfund资金管理产品投资
	 * 
	 * @param model
	 * @throws Exception
	 */
	public static void ppfundTender(PpfundInModel model) throws Exception {
		ValueEvent event = new ValueEvent();
		event.setOperate("ppfundTender");
		event.setPpfundInModel(model);
		JobQueue.getPpfundInstance().offer(event);
	}
}
