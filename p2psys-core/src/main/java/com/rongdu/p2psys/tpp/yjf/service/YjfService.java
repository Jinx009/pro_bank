package com.rongdu.p2psys.tpp.yjf.service;

import java.util.List;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;

/**
 * 
 * TODO 调用托管接口的业务处理方法
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月22日
 */
public interface YjfService {
	
	
	/**
	 * Vip审核
	 * @param cache 用户信息
	 * @param vipFee VIP费用
	 * @param taskList 需要处理的数据集合
	 */
	void verifyVipSuccess(User cache , double vipFee, List<Object> taskList);
	/**
	 * 统一处理所有接口任务
	 * @param taskList 
	 * @return 处理结果
	 */
	boolean doApiTask(List<Object> taskList);
	
	
	/**
	 * 发标，调用接口产生订单号并保存。
	 * @param model 标
	 * @param uc 用户信息
	 * @param taskList 任务集合
	 */
	void addBorrowCreateTradeNo(Borrow model, User uc, List<Object> taskList);
	/**
	 * 流转标，投标放款。
	 * @param money 投标金额
	 * @param tenderUserCache  投标人
	 * @param borrow  标
	 * @param borrowUserCahche  借款人
	 * @param taskList  任务集合
	 */
	void flowBorrowLoan(double money, User tenderUserCache,
			Borrow borrow, User borrowUserCahche, List<Object> taskList);
	/**
	 *  投标，冻结资金
	 * @param money  投标金额
	 * @param uc 投标用户信息
	 * @param borrow  标
	 * @param borrowUserCahche  借款人
	 * @param taskList  任务集合
	 */
	void addTenderFreezeMoney(double money, User uc, Borrow borrow, 
			User borrowUserCahche, List<Object> taskList);
	/**
	 * 复审失败、撤标的解冻投资人资金
	 * @param taskList  任务集合
	 * @param model 标
	 */
	void failBorrow(List<Object> taskList, Borrow model);
	/**
	 * 满标复审，调用放款
	 * @param apiNo 第三方账户
	 * @param model 标
	 * @param tender 投标
	 * @param borrowUser 借款人
	 * @param tenderUser 投资人
	 * @param taskList 任务集合
	 */
	void fullSuccessLoanMoney(String apiNo, Borrow model, BorrowTender tender, User borrowUser,
			User tenderUser, List<Object> taskList);
	/**
	 * 放款，分发奖励
	 * @param model 标
	 * @param tender 投标
	 * @param borrowUser 借款人
	 * @param tenderUser 投资人
	 * @param taskList 任务集合
	 * @param awardValue 奖励金额
	 */
	void fullSuccessAward(Borrow model, BorrowTender tender, User borrowUserCache,
			User tenderUserCache, List<Object> taskList, double awardValue);
	
	/**
	 * 扣除借款管理费
	 * @param taskList 任务集合
	 * @param borrowfee 管理费
	 * @param borrow   标
	 * @param uc 借款人
	 */
	void fullSuccessDeductFee(List<Object> taskList,double borrowfee, Borrow borrow, User uc);
	void repayBorrowFee(Borrow model, BorrowRepayment repay, User tenderUser, double borrowFee, double interest,
			List<Object> taskList, BorrowCollection c, String apiMethodType);
	void payManageFee(String value, double round, List<Object> taskList, Borrow borrow);
	void repayLoanMoney(String apiId, double money, List<Object> taskList, Borrow borrow, BorrowCollection c,
			String apiMethodType);
	
	/**
	 * 投标的具体处理方法
	 * @param money 投标金额
	 * @param uc 投资人
	 * @param borrow  标
 	 * @param toUc   借款人
	 * @return  处理结果
	 */
	boolean doTender(double money, User uc, Borrow borrow, User toUc);
	
	
}
