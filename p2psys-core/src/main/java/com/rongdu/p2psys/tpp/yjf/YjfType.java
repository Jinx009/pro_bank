package com.rongdu.p2psys.tpp.yjf;
/**
 * 易极付后台任务计划表对应的类型。
 * @author Administrator
 */
public final class YjfType {
	/**
	 * 构造函数私有
	 */
	private YjfType() { 
		
	}
    //交易类型：
	public static final String TRADE_PAYER_QUIT_POOL_TOGETHER = "tradePayerQuitPoolTogether"; // 退出投标
	public static final String TRADE_PAYER_APPLY_POOL_TOGETHER = "tradePayerApplyPoolTogether";// 投标
	public static final String TRADE_PAY_POOL_TOGETHER = "tradePayPoolTogether"; // 放款
	public static final String TRADE_POOL_RECEIVE_BORROW = "tradePoolReceiveBorrow"; //
	public static final String TRADE_PAY_POOL_REVERSE = "tradePayPoolReverse"; //还款
	public static final String TRADE_FINISH_POOL_REVERSE = "tradeFinishPoolReverse"; // 还款接口完成
	public static final String TRADE_FINISH_POOL = "tradeFinishPool"; // 借款交易接口
	public static final String TRADE_CLOSE_POOL_REVERSE = "tradeClosePoolReverse";//还款交易关闭
	public static final String TRADE_CLOSE_POOL_TOGETHER = "tradeClosePoolTogether";// 接口交易关闭
	public static final String TRADE_CLOSE_POOL = "tradeClosePool";// 集资关闭接口
	public static final String TRADE_CREATE_POOL_REVERSE = "tradeCreatePoolReverse"; // 还款创建交易
	/**
	 *借款创建交易号，每一个交易必须有一个交易号
	 */
	public static final String TRADE_CREATE_POOL_TOGETHER = "tradeCreatePoolTogether"; //普通表创建交易号 
	
	public static final String TRADE_CREATE_POOL = "tradeCreatePool"; //  流转标创建交易号
	public static final String TRADE_TRANSFER = "tradeTransfer";  //转账
	public static final String APPLY_WITHDRAW = "applyWithdraw";//提现
	
	//系统操作类型
	public static final String REALNAME = "REALNAME";
	public static final String ADDBORROW = "ADDBORROW"; //发标
	public static final String ADDTENDER  = "ADDTENDER"; //投标
	public static final String APPLYVIP = "APPLYVIP"; //申请vip
	public static final String DOREPAY  = "DOREPAY "; // 还款
	public static final String PREREPAY  = "PREREPAY "; // 提前还款
	public static final String AUTOREPAY = "AUTOREPAY"; //自动还款
	public static final String AUTOVERIFYFULLSUCCESS  = "AUTOVERIFYFULLSUCCESS "; //后台满标复审通过
	public static final String VERIFYBORROW = "VERIFYBORROW";//满标初审
	public static final String FAIL = "FAIL"; //后台满标复审不通过
	public static final String AUTOCANCEL = "AUTOCANCEL";//取消的标
	public static final String AUTODOREPAYFORSECOND = "AUTODOREPAYFORSECOND";//秒标冻结
	public static final String VERIFYFULLBORROW = "VERIFYFULLBORROW"; //前台满标处理
	public static final String VERIFYRECHARGE = "VERIFYRECHARGE"; //充值审核
	public static final String VERIFYVIPSUCCESS = "VERIFYVIPSUCCESS"; //vip 审核通过
	public static final String VERIFYVIPFAIL = "VERIFYVIPFAIL";//vip审核不通过
	public static final String WEBSITEPAYFORLATEBORROW = "WEBSITEPAYFORLATEBORROW "; //垫付
	public static final String NEWCASH = "NEWCASH"; //提现申请
	public static final String VERIFYCASH = "VERIFYCASH"; //提现审核
	public static final String CASHBACK = "CASHBACK";//扣款
	public static final String CANCELCASH = "CANCELCASH";//取消提现
	public static final String FLOWREPAY = "FLOWREPAY";//流标还款
	public static final String CANCELBORROW = "CANCELBORROW";//标初审，取消
	public static final String WEB_TRANSFER = "web_transfer";//网站划款
}
