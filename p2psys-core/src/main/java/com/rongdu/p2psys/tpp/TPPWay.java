package com.rongdu.p2psys.tpp;

import java.util.List;
import java.util.Map;

import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrModel;
import com.rongdu.p2psys.tpp.ips.model.IpsMerchentUserInfo;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * TODO 调用托管接口的业务处理方法
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月22日
 */
public interface TPPWay {
	
	// 前台同步回调地址
	public static final String URL_WEB = Global.getValue("weburl");
	// 后台同步回调地址
	public static final String URL_ADMIN = Global.getValue("adminurl");
	// 前台异步回调地址
	public static final String URL_WEBS2S = Global.getValue("webs2surl");	
	// 后台异步回调地址
	public static final String URL_ADMINS2S = Global.getValue("admins2surl");
	// 托管接口类型(数据库配置)
	public static final int API_CODE = Global.getInt("api_code");	
	// 托管接口类型(易极付)
	public static final int API_CODE_YJF = 1;
	// 托管接口类型(环讯)
	public static final int API_CODE_IPS = 2;
	// 托管接口类型(汇付)
	public static final int API_CODE_PNR = 3;	
	
	/**
	 * 实名认证
	 * @return Object 
	 * @throws Exception if has error
	 */
	Object doRealname() throws Exception;
	
	/**
	 * 企业开户
	 * @return
	 * @throws Exception
	 */
	Object doCorpRegister() throws Exception;
	
	/**
	 * 充值
	 * @return Recharge 
	 * @throws Exception if has error
	 */
	Object doRecharge() throws Exception;
	/**
	 * 投标
	 * @return TradePayerApplyPoolTogether 
	 * @throws Exception if has error
	 */
	Object doTender(Object obj) throws Exception;
	/**
	 * 绑卡
	 * @param bank 用户银行信息
	 * @param uc 用户信息
	 * @param bankType 银行简称
	 * @return 绑卡结果
	 */
	Object bindBank(AccountBank bank, User user, String bankType);
	
	/**
	 * 银行卡解绑
	 * @param ab 绑定的银行卡
	 * @param uc  用户信息
	 * @return 解绑结果
	 */
	Map<String, Object> bankBindRemove(AccountBank ab, User user);
	
	/**
	 * 提现
	 * @param cash 提现对象
	 * @param uc 用户信息
	 * @param cashnum 成功提现笔数
	 * @return 提现对象
	 */
	Object doNewCash(AccountCash cash, User user, int cashnum,String province, String city, String bankCode);

	/**
	 * 创建还款交易号
	 * @param apiId 第三方用户ID
	 * @param money 资金
	 * @return TradeCreatePoolReverse
	 */
	Object tradeCreatePoolReverse(String apiId, double money);
	/**
	 * 发标 跳转页面的
	 * @return Object 
	 * @throws Exception if has error
	 */
	Object doBorrow() throws Exception;
	
	/**
	 * 撤标处理
	 * @return Object 
	 * @throws Exception if has error
	 */
	Object doCancelBorrow(Borrow borrow) throws Exception;	
	
	/**
	 * 还款处理
	 * @param repay 还款信息
	 * @param repayType 还款类型：1#手劢还款，2#自劢还款 
	 * @return
	 * @throws Exception
	 */
    Object doRepayment(BorrowRepayment repay, byte repayType);
    
    /**
     * 自动签约还款
     * @return
     * @throws Exception
     */
    Object doAutoRepaymentSigning();
    
    /**
     * 自动代扣充值
     * @param user 用户信息
     * @param ipsFeeType 谁付IPS手续费:1平台支付,2用户支付
     * @param acctType 账户类型 固定值为  1，表示为类型为 IPS 个人账户
     * @param trdAmt 充值金额
     * @param merFee 平台手续费
     * @return
     */
    Object doAutoRecharge(User user, String ipsFeeType, String acctType, double trdAmt, double merFee);
    
    /**
     * 添加用户第三方登陆配置信息
     * @param userId 用户ID
     */
    void addUserTppConfig(long userId);
    /**
     * 登记担保方
     * @param borrow
     * @return
     */
    Object registerGuarantor(Borrow borrow);
    
    /**
     * 查询第三方支付帐户信息
     * @return
     * @throws Exception
     */
    IpsMerchentUserInfo queryMerUserInfo() throws Exception ;
    
    
    /**
     * 查询第三方支付信息列表
     * @param map 参数
     * @return List第三方支付记录
     */
    Object getTppPay(Map<String, Object> map);
    
    /**
     * 根据ID查询第三方支付信息列表
     * @param id 参数
     * @return 第三方支付记录
     */
    Object getTppPayById(int id);
    
    /**
     * 第三方支付还款补单任务处理
     * @param taskList 支付List
     * @return 是否成功
     */
    Boolean doTppPayTask(List<Object> taskList);
    
	/**
	 * 用户第三方登陆
	 * @param user
	 * @return
	 */
	Object apiLogin(User user);

	Object doBorrowAuto(BorrowAuto auto);

	public ChinapnrModel autoTender(User user, String[][] args, long id,
			double validAccount);

	/**
	 * 债权转让发标
	 * @return
	 */
	Object creditAssign(BondModel bondModel);
	
	/**
	 * 查询用户第三方资金
	 * @param user
	 * @return
	 */
	Object getTppAccount(User user);
}
