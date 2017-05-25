package com.rongdu.p2psys.borrow.service;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;

public interface AutoBorrowService {

	/**
	 * 还款
	 * 
	 * @param borrowRepayment
	 */
	void autoBorrowRepay(BorrowRepayment borrowRepayment);
	
	/**
	 * 后台逾期垫付
	 * @param borrowRepayment
	 */
	public void overdue(BorrowRepayment borrowRepayment);
	
	/**
	 * 前台逾期垫付
	 * @param borrowRepayment
	 */
	public void overduePayment(BorrowRepayment borrowRepayment);
	
	/**
	 * 提前还款
	 * @param borrowRepayment
	 */
	public void doPriorRepay(BorrowRepayment borrowRepayment);

	/**
	 * 自动投标
	 * 
	 * @param model
	 * @param auto
	 * @throws Exception
	 */
	void autoDealTender(BorrowModel model) throws Exception;
	
	/**
	 * 满标复审
	 * 
	 * @throws Exception
	 */
	void autoVerifyFull(Borrow borrow) throws Exception;	

	/**
	 * 复审通过
	 * 
	 * @throws Exception
	 */
	void autoVerifyFullSuccess(BorrowModel model) throws Exception;

	/**
	 * 复审不通过
	 * 
	 * @throws Exception
	 */
	void autoVerifyFullFail(BorrowModel model) throws Exception;
	
	/**
	 * 撤回标处理
	 * @param borrow
	 */
	public void autoCancel(Borrow borrow);
    
    /**
     * 代偿成功后处理
     * 
     * @param borrowRepayment
     */
    void autoCompensateSuccess(BorrowRepayment borrowRepayment);   
    
	void updateStatus(long id, int status, int preStatus);

}
