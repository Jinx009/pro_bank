package com.rongdu.p2psys.core.constant;

/**
 * 消息模板常量类
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月26日
 */
public class NoticeConstant {

	/** 邮箱认证-发送认证链接 **/
	public static final String NOTICE_EMAIL_ACTIVE = "get_email";
	/** 通过邮箱找回密码-发送校验码 **/
	public static final String NOTICE_GET_PWD_EMAIL = "get_pwd_email";
	/** 通过邮箱找回交易密码-发送校验码 **/
	public static final String NOTICE_GET_PAY_PWD_EMAIL = "get_pay_pwd_email";
	/** 通过手机找回密码-发送校验码 **/
	public static final String NOTICE_GET_PWD_PHONE = "get_pwd_phone";
	/** 通过手机找回交易密码-发送校验码 **/
	public static final String NOTICE_GET_PAY_PWD_PHONE = "get_pay_pwd_phone";
	/** 绑定邮箱-发送校验码 **/
	public static final String NOTICE_BIND_EMAIL = "bind_email";
	/** 修改绑定邮箱-发送校验码 **/
	public static final String NOTICE_MODIFY_EMAIL = "modify_email";
	/** 绑定手机-发送校验码 **/
	public static final String NOTICE_BIND_PHONE = "bind_phone";
	/** 修改绑定手机-发送校验码 **/
	public static final String NOTICE_MODIFY_PHONE = "modify_phone";
	/** 找回支付密码 **/
	public static final String NOTICE_GET_PAYPWD = "get_paypwd";
	/** 新借款标发布 **/
	public static final String NEW_BORROW = "new_borrow";
	/** 借款标取消 **/
	public static final String BORROW_CANCEL = "borrow_cancel";
	/** 后台初审通过 **/
	public static final String BORROW_VERIFY_SUCC = "borrow_verify_succ";
	/** 后台初审不通过 **/
	public static final String BORROW_VERIFY_FAIL = "borrow_verify_fail";
	/** 投标成功 **/
	public static final String INVEST_SUCC = "invest_succ";
	/** 投标失败 **/
	public static final String INVEST_FAIL = "invest_fail";
	/** 自动投标成功 **/
	public static final String AUTO_TENDER = "auto_tender";
	/** 满标审核通过 **/
	public static final String BORROW_FULL_SUCC = "borrow_full_succ";
	/** 满标审核失败 **/
	public static final String BORROW_FULL_FAIL = "borrow_full_fail";
	/** 借款人还款 **/
	public static final String RECEIVE_REPAY = "receive_repay";
	/** 还款成功 ----利随本清**/
	public static final String REPAY_SUCC = "repay_succ";
	/** 还款成功---每月还息，到期还本 **/
	public static final String REPAY_SUCC_FILC = "repay_succ_filc";
	/** 还款成功---每月还息，到期还本  最后一期 **/
	public static final String REPAY_SUCC_FILC_LAST = "repay_succ_filc_last";
	/** 还款成功 --等额本息**/
	public static final String REPAY_SUCC_ACPI = "repay_succ_acpi";
	
	/** 代偿成功 **/
    public static final String COMPENSATE_SUCC = "compensate_succ";
	/** 收到投标奖励 **/
	public static final String RECEIVE_TENDER_AWARD = "receive_tender_award";
	/** 支付投标奖励 **/
	public static final String DEDUCT_BORROWER_AWARD = "deduct_borrower_award";
	/** 线上充值成功 **/
	public static final String RECHARGE_SUCC = "recharge_succ";
	/** 提现申请 **/
	public static final String CASH_APPLY = "cash_apply";
	/** 提现成功审核 **/
	public static final String CASH_VERIFY_SUCC = "cash_verify_succ";
	/** 提现失败审核 **/
	public static final String CASH_VERIFY_FAIL = "cash_verify_fail";
	/** 登录密码修改 **/
	public static final String PASSWORD_UPDATE = "password_update";
	/** 交易密码修改 **/
	public static final String PAYPWD_UPDATE = "paypwd_update";
	/** 还款提前通知 **/
    public static final String BORROWER_REPAY_NOTICE = "borrower_repay_notice";
    /** 后台扣款通知 **/
    public static final String HOUTAI_DEDUCT_SUCC = "houtai_deduct_succ";
    /** 认证通过 **/
    public static final String CERTIFY_SUCC = "certify_succ";
    /** 认证未通过 **/
    public static final String CERTIFY_FAIL = "certify_fail";
    
    public static final String REGISTER_GET_CODE = "register_get_code";
    
    /** 提现发送短信 **/
    public static final String CASH_GET_CODE = "cash_get_code";
    
    /** 注册成功发送短信 **/
    public static final String REGISTER_SUCC = "register_succ";
    
    /** 后台注册用户成功发送初始密码 **/
    public static final String REGISTER_PASSWORD = "register_password";
    
    /** 后台定向发送红包短信 **/
    public static final String REDPACKET_SEND = "redpacket_send";
    /** 后台定向发送红包短信 **/
    public static final String REDPACKET_DUEREMIND = "redpacket_dueremind";
    
    
    /** 债权转让成功出让人 **/
    public static final String BOND_SELL_SUCC = "bond_sell_succ";
    /** 债权转让成功受让人 **/
    public static final String BOND_BUY_SUCC = "bond_buy_succ";
    /** 债权转让成功受让人 **/
    public static final String BOND_NEW_SUCC = "bond_new_succ";
	/** 债权投资人发送还款成功通知 **/
	public static final String BOND_RECEIVE_SUCC = "bond_receive_succ";
    /** 债权转让撤回出让人 **/
    public static final String BOND_SELL_STOP = "bond_sell_stop";
    /** PPfund转出通知 **/
    public static final String PPFUND_OUT_SUCC = "ppfund_out_succ";
    /**PPfund关闭通知 **/
    public static final String PPFUND_CLOSE = "ppfund_close";
    
    /** 预约产品上线通知 **/
    public static final String APPOINTMENT_BID = "appointment_bid";
    
    /** 流标和满标提醒 **/
    public static final String REMIND_BID = "remind_bid";
    
    /** 资金监管通知 **/
    public static final String PPFUND_SUPERVISION_NOTICE = "ppfund_supervision_notice";
    public static final String CASH_SUPERVISION_NOTICE = "cash_supervision_notice";
    public static final String RECHARGE_OFFLINE_NOTICE = "recharge_offline_notice";
    
    /** 还款提醒通知指定手机号 **/
    public static final String REPAYMENT_SUPERVISION_NOTICE = "repayment_supervision_notice";
    
    /** 节假日发送祝福短信 **/
    public static final String NEW_YEAR_DAY = "new_year_day";//元旦
    public static final String LABOR_DAY = "labor_day";//劳动节
    public static final String NATIONAL_DAY = "national_day";//国庆节
    public static final String SPRING_FESTIVAL = "spring_festival";//春节
    public static final String DRAGON_BOAT_FESTIVAL = "dragon_boat_festival";//端午节
    public static final String MID_AUTUMN_FESTIVAL = "mid_autumn_festival";//中秋节
    
	public static final byte NOTICE_SEND = 1;
	public static final byte NOTICE_NOT_SEND = 0;
	public static final byte NOTICE_RECEIVE = 1;
	public static final byte NOTICE_NOT_RECEIVE = 0;
	public static final byte NOTICE_SMS = 1;
	public static final byte NOTICE_EMAIL = 2;
	public static final byte NOTICE_MESSAGE = 3;
	
	/**
	 * 账号绑定验证码
	 */
	public static final String BIND_CODE = "bind_code";
	public static final String CARD_CODE = "card_code";

	/**
	 * 消息模板用变量
	 */
	private NoticeConstant() {

	}

}
