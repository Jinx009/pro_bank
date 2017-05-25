package com.rongdu.p2psys.account.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.AccountMoneyModel;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.PayDataBean;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.tpp.ips.model.IpsRechargeBank;
import com.rongdu.p2psys.user.domain.User;

/**
 * 充值Service
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月17日10:49:14
 */
public interface AccountRechargeService {

	/**
	 * 新增充值记录，但是Account记录并未到账
	 * 
	 * @param r
	 * @return
	 */
	AccountRecharge add(AccountRecharge r);

	/**
	 * 获取充值记录列表，含分页
	 * 
	 * @param userId
	 * @param page
	 * @return
	 */
	PageDataList<AccountRechargeModel> list(long userId, AccountRechargeModel model);

	/**
	 * 成功充值信息
	 * 
	 * @param userId
	 * @return
	 */
	AccountRechargeModel getRechargeSummary(long userId);

	/**
	 * 充值列表
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<AccountRechargeModel> list(AccountRechargeModel model);

	/**
	 * 等待审核的充值
	 * 
	 * @param status
	 * @return
	 */
	int count(int status);

	/**
	 * @param id
	 * @return
	 */
	AccountRecharge find(long id);

	/**
	 * 充值审核
	 * 
	 * @param model
	 * @param operator
	 */
	void verifyAccountRecharge(AccountRechargeModel model, Operator operator);
	
	/**
	 * 获取环讯可充值的银行列表
	 * @return 环讯可充值的银行列表
	 */
	List<IpsRechargeBank> getIpsRechargeBankList();
	/**
     * 更改充值状态
     */
    void setAccountRecharge();
    /**
     * 后台取消充值，将充值设置为失败
     * @param accountRecharge
     */
    void cancelCash(AccountRecharge accountRecharge);
    /**
     * 后台充值确认
     * @param accountRecharge
     */
    void verifyRecharge(AccountRecharge accountRecharge);
    
    /**
     * 充值用户数
     * 
     * @param status
     * @return
     */
    int rechargedUserCount();
    
    /**
     * 根据时间搜索充值用户数
     * @param startTime
     * @param endTime
     * @return
     */
    int rechargedUserCount(String startTime, String endTime);
    
    /**
     * 投资人累计充值金额总和
     * 
     * @param status
     * @return
     */
    double rechargedAllMomeny(String startTime, String endTime);
    
    /**
     * 根据订单号修改状态
     * 
     * @param status
     * @return
     */
    void updateStatusByTradeNo(String tradeNo, int status, byte ipsStatus);
    
    /**
     * 根据订单号修改备注
     * 
     * @param tradeNo 订单号
     * @param payMent 充值入口
     * @return
     */
    void updatePayMentByTradeNo(String tradeNo, String payMent);

	void verifyRecharge(AccountRecharge ar, Operator operator);
	
	/**
	 *保存后台充值
	 * @param recharge
	 */
	void saveBackRecharge(AccountRecharge recharge, Operator operator);
	
	/**
	 * 处理银联在线充值
	 * 
	 * @param recharge
	 * @param payment
	 * @param ab
	 * @return
	 */
	Map<String, Object> doUnionPay(String payment,AccountRechargeModel model,AccountBank ab);
	
	/**
	 * 处理银联在线处理中充值
	 */
	void doRechargeWait();

	public PageDataList<AccountRechargeModel> accountRechargeTotalList(
			AccountRechargeModel model);

	public AccountRechargeModel sumAmount(AccountRechargeModel model);
	
	/**
	 * 根据时间获取充值金额
	 * @param date
	 * @return 充值金额
	 */
	public double getAccountRechargeSumByDate(String date);

	/**
	 * 初审
	 * @param model
	 * @param operator
	 */
	public void accountRechargeVerifyEdit(AccountRechargeModel model, Operator operator);

	/**
	 * 充值复审List
	 * @param model
	 * @return
	 */
	public PageDataList<AccountRechargeModel> accountRechargeVerifyFullList(
			AccountRechargeModel model);
	
	/**
	 * 处理连连在线充值
	 * 
	 * @param user
	 * @param type 1成功，2失败
	 * @param payDataBean 支付结果类
	 * @param addrIp 添加的IP
	 * @return
	 */
	Map<String, Object> doLLPay(User user,int type,PayDataBean payDataBean,String addrIp);
	
	/**
	 * 线下充值
	 * @param model
	 * @param user
	 * @param type
	 * @return
	 */
	boolean offLineRecharge(AccountRechargeModel model,User user,String type);
	
	/***
	 * 
	 * @param request
	 * @param user
	 * @param no_agree
	 * @param accname
	 * @param accid
	 * @param mobile
	 * @param cardno
	 * @return
	 */
	LianlPay setllpay(HttpServletRequest request,User user,String no_agree,String accname,String accid,String mobile,String cardno);
	double getNewAccountRechargeSumByDate(String startTime,String endTime);
	double getAccessAccontMoney(String startTime,String endTime);
	PageDataList<AccountMoneyModel> getBorrowCollectionMoney(String startTime,String endTime,int pageNo,int rowCount);
	PageDataList<AccountMoneyModel> getBorrowCollectionMoney2(String startTime,String endTime,int pageNo,int rowCount);
	PageDataList<AccountMoneyModel> getPpfundCollectionMoney(String startTime,String endTime,int pageNo,int rowCount);
	PageDataList<AccountMoneyModel> getRedPacketMoney(String startTime,String endTime,int pageNo,int rowCount);
	PageDataList<AccountMoneyModel> getRecommendMoney(String startTime,String endTime,int pageNo,int rowCount);
}
