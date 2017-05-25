package com.rongdu.p2psys.account.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.model.AccountCashModel;
import com.rongdu.p2psys.account.model.payment.llPay.model.PayDataBean;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.tpp.yjf.model.CashModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 提现Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月17日
 */
public interface AccountCashService {

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	AccountCash find(long id);

	/**
	 * 提现
	 * 
	 * @param accountCash 提现
	 * @return 提现
	 */
	AccountCash doCash(AccountCash accountCash, User user);
	
	/**
	 * 提现
	 * 
	 * @param accountCash
	 * @param channlKey 通道key
	 * @return
	 */
	Map<String, Object> doCash(AccountCash accountCash,String channlKey);

	/**
	 * 取消提现 注意判断id与userId是否对应
	 * 
	 * @param userId
	 * @param id
	 */
	void doCancleCash(AccountCash cash, Operator operator);
	/**
	 * 确认银联提现成功
	 * @param cash
	 */
    void verifyYLCash(AccountCashModel model, Operator operator);
    /**
	 * 确认连连提现成功
	 * @param cash
	 */
    void verifyLLCash(AccountCashModel model, Operator operator);
    
    /**
	 * 确认线下提现成功
	 * @param cash
	 */
    void verifyOffLineCash(AccountCashModel model, Operator operator);

	/**
	 * 提现记录
	 * 
	 * @param userId
	 * @return
	 */
	PageDataList<AccountCashModel> list(long userId, int startPage, AccountCashModel model);

	/**
	 * 提现金额信息
	 * 
	 * @param userId
	 * @return
	 */
	AccountCashModel getCashMessage(long userId);

	/**
	 * 提现审核记录列表
	 * 
	 * @param accountCashModel
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageDataList<AccountCashModel> accountCashList(AccountCashModel accountCashModel);

	/**
	 * 统计等待审核的体现总数
	 * 
	 * @param status
	 * @return
	 */
	int count(int status);

	/**
	 * 客服提现审核
	 * 
	 * @param model
	 * @param cash
	 * @param operator
	 */
	void kfVerifyCash(AccountCash cash, Operator operator);

	/**
	 * 财务提现审核
	 * 
	 * @param cash
	 * @param operator
	 */
	void cwVerifyCash(AccountCash cash, Operator operator);
	/**
	 * 提现回调业务处理
	 * @param cash 提现参数封装
	 */
	void cashCallBack(CashModel cash);
	
	/**
	 * 更改提现状态
	 */
	void setAccountCash();
	
    /**
     * 根据提现时间段
     * 投资人累计提现金额总和
     * 
     * @return
     */
    double allCashMomeny(String startTime, String endTime);
    
    /**
     * 根据订单号更改提现状态
     */
    void updateStatusByOrderNo(String orderNo, int status, byte ipsStatus);  

    /**
	 * 获得本月成功提现的次数
	 * @param userId
	 * @return
	 */
	int countMonth(long userId);
	
	/**
	 * 处理银联处理中提现
	 */
	void doCashWait();

	/**
	 * 提现初审
	 * 
	 * @param model AccountCashModel
	 * @param operator Operator
	 */
	public void verifyCashStatus(AccountCashModel model, Operator operator);

	public PageDataList<AccountCashModel> accountCashVerifyFullList(
			AccountCashModel model);
	
	/**
	 * 根据id获取提现记录
	 * @param ids
	 * @return
	 */
	List<AccountCash> list(String ids);
	
	/**
	 * 批量初审
	 * @param model
	 * @param operator
	 * @param list
	 * @return
	 */
	public Map<String, Object> verifyCashBatchStatus(AccountCashModel model, Operator operator, List<AccountCash> list);
	
	/**
	 * 批量复审
	 * @param model
	 * @param operator
	 * @param list
	 * @return
	 */
	public Map<String, Object> verifyCashBatchReview(AccountCashModel model, Operator operator, List<AccountCash> list);
	
	/**
	 * 根据时间获取提现金额
	 * @param date
	 * @return 提现金额
	 */
	public double getAccountCashSumByDate(String date);
	
	/**
	 * 发送短信
	 * @param noticeKey
	 * @param user
	 * @param supervisionMoney
	 */
	public void sendSMSNotice(String noticeKey,User user, double supervisionMoney);
	
	/**
	 * 连连提现申请
	 * @param request
	 * @param bankNo
	 * @param userName
	 * @param bankcode
	 * @param money
	 * @param province 开户行省
	 * @param city  开户行市
	 * @param brabank 开户支行名称
	 * @return
	 */
	public String cashhandle(HttpServletRequest request,String bankNo, String userName, String bankcode,
			String money,String province,String city,String brabank) ;

	/**
	 * 增加提现记录
	 * @param request
	 * @param user
	 * @param bank
	 * @param money
	 */
	void addCash(HttpServletRequest request,User user,AccountBank bank,String money);
	
	/**
	 * 获取该用户当天提现次数
	 * @param userId
	 * @return
	 */
	public int getTodayCashCountByUserId(long userId);
	
	/**
	 * 处理连连在线提现
	 * 
	 * @param user
	 * @param type 1成功，2失败
	 * @param payDataBean 提现结果类
	 * @return
	 */
	public void operllCash(User user,int type,PayDataBean payDataBean);
	
	/**
	 * 银联对独立用户提现打款
	 * 
	 * @param user 用户
	 * @param ab 提现银行卡
	 * @param money 提现金额
	 * @return
	 */
	public String oneToCash(User user,AccountBank ab,double money);
}
