package com.rongdu.p2psys.tpp.ips;
/**
 * 易极付后台任务计划表对应的类型。
 * @author Administrator
 */
public final class IpsType {
	/*********************** 转账类型： ****************************************/
	/** 投资*/
	public static final String TRANSFERTYPE_TENDER = "1"; 
	/**
	 * 代偿
	 */
	public static final String TRANSFERTYPE_COMPENSATE = "2"; 
	/**
	 * 代偿还款
	 */
	public static final String TRANSFERTYPE_COMPENSATE_REPAY = "3"; 
	/**
	 * 债权转让
	 */
	public static final String TRANSFERTYPE_ANSPRUCHSABTRETUNG = "4"; 
	
	
	/*********************** 转账方式： ****************************************/
	/** 逐条转账*/
	public static final String TRANSFERMODE_ONE = "1";
	/**
	 * 批量转账
	 */
	public static final String TRANSFERMODE_BATCH = "2";
	
	
	/*********************** 交易類型： ****************************************/
	/**
	 * 轉帳
	 */
	public static final String TRANFER = "TRANFER"; 
	
	/**
	 * 环讯转账
	 */
	 //public static final String WS_URL="http://p2p.ips.net.cn/CreditWS/Service.asmx";
	/**
	 * 构造函数私有
	 */
	private IpsType() { 
		
	}
	 
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
