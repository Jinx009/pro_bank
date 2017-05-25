package com.rongdu.p2psys.tpp.chinapnr.service;

import java.util.List;
import java.util.Map;

import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCardCashOut;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCorpRegister;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * 调用托管接口的业务处理方法
 * 
 * @author wzh
 * @version 2.0
 * @since 2014年7月22日
 */
public interface ChinapnrService {
	
	/**
	 * 处理用户开户的回调的处理逻辑
	 * @param user 用户信息
	 */
	void apiUserRegister(User user);
	
	void apiCorpRegister(User user, ChinapnrCorpRegister corpRegister);
	
	/**
	 * 投标的回调的处理逻辑
	 * @param bm 投标的信息
	 * @return  处理结果
	 */
	void addTender(BorrowModel bm);	
	
	/**
	 * 统一处理所有的接口任务
	 * @param taskList
	 * @return
	 */
	boolean doApiTask(List<Object> taskList);
	
	/**
	 * 满标复审通过处理
	 * @param borrowModel
	 * @return
	 */
	void fullSuccess(BorrowModel borrowModel);
	
	/**
	 * 满标复审失败处理
	 * @param borrowModel
	 * @return
	 */
	void fullFail(BorrowModel borrowModel);	
	
	/**
	 * 撤标处理
	 * @param borrowModel
	 * @return
	 */
	void cancelBorrow(Borrow borrow);	
	
	/**
	 * 还款处理
	 * @param borrowRepayment
	 * @return
	 */
	void repay(BorrowRepayment borrowRepayment);
	
	/**
	 * VIP还款处理
	 * @return
	 */
	void vipRepay();

	/**
	 * 绑定银行卡处理
	 * @param cardcash 卡信息
	 * @return
	 */
    void addAccountBank(ChinapnrCardCashOut cardcash);
    
	/**
	 * 解绑银行卡处理
	 * @param cardcash 卡信息
	 * @return
	 */
    Map<String, Object> removeAccountBank(String usrCustId, String bankNo);
    
	/**
	 * 计算实际需要还款的金额
	 * @param repayment 还款信息
	 * @return  实际需要还款的金额
	 */
    double getRealRepayMoney(BorrowRepayment repayment);

	public void overdue(BorrowRepayment borrowRepayment);
	
	/**
     * 债权转让承接人付给的利息
     * @return
     */
    double getHappendInterest(BondModel bm);
    
    /**
     * 债权转让管理费
     * @return
     */
    double getManageFee(double capital, double interest);
    
    /**
	 * 债权投资的回调的处理逻辑
	 * @param bm 债权投资的信息
	 * @return  处理结果
	 */
	void addBondTender(BondModel bm);

}
