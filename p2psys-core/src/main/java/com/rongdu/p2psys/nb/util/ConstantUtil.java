package com.rongdu.p2psys.nb.util;

import java.util.Arrays;
import java.util.List;

/**
 * 常量数据
 * 
 * @author Jinx
 *
 */
public final class ConstantUtil {
	public static final int OK_CODE = 200;

	public static final String ERROR_CODE = "error_time";
	/**
	 * 登陆后隐藏手机号
	 */
	public static final String HIDE_SESSION_USERNAME = "hide_session_username";

	/**
	 * 未登陆
	 */
	public static final String NO_LOGIN_USER = "no_login";

	/**
	 * 投资状态
	 */
	public static final String INVEST_STATUS = "invest_status";

	/**
	 * 多身份联合唯一标识
	 */
	public static final String SESSION_GROUPID = "session_groupid";

	/**
	 * 当前账户
	 */
	public static final String SESSION_USER = "nb_user";

	/**
	 * 全局字符串编码格式
	 */
	public static final String CHAR_SET = "UTF-8";

	/**
	 * 现金管理投资
	 */
	public static final String PPFUND_TENDER = "ppfund_tender";

	/**
	 * 推荐
	 */
	public static final String RED_PACKET_RECOMMEND = "recommend";

	/**
	 * 结果
	 */
	public static final String RESULT = "result";

	/**
	 * data
	 */
	public static final String DATA = "data";

	/**
	 * 失败
	 */
	public static final String FAILURE = "failure";

	/**
	 * 成功
	 */
	public static final String SUCCESS = "success";

	/**
	 * 返回给前端的CODE
	 */
	public static final String CODE = "code";
	/**
	 * 返回给前端的MESSAGE
	 */
	public static final String MESSAGE = "message";

	/**
	 * 成功
	 */
	public static final Boolean RESULT_TRUE = true;

	/**
	 * 失败
	 */
	public static final Boolean RESULT_FALSE = false;

	/**
	 * 有效标识
	 */
	public static final Integer FLAG_TRUE = 1;

	/**
	 * 无效标识
	 */
	public static final Integer FLAG_FALSE = 0;

	/**
	 * 信息
	 */
	public static final String MSG = "msg";

	/**
	 * 错误信息
	 */
	public static final String ERRORMSG = "errorMsg";

	/**
	 * 总记录数
	 */
	public static final String TOTAL = "total";

	/**
	 * 当页记录
	 */
	public static final String ROWS = "rows";

	/**
	 * 获取验证码的手机号
	 */
	public static final String SESSION_MOBILE_PHONE = "session_mobile_phone";

	/**
	 * 没有登陆用户
	 */
	public static final String NO_LOGIN_USER_MSG = "no_user_login";

	/**
	 * Web端连连充值入口URL
	 */
	public static final String WEB_LLPAY_RECHARGE_URL = "/cf/recharge/dollPayRecharge.html";

	/**
	 * Web端银联充值入口URL
	 */
	public static final String WEB_UNIONPAY_RECHARGE_URL = "/cf/recharge/doUnionPayRecharge.html";

	/**
	 * 微信端连连充值入口URL
	 */
	public static final String WAP_LLPAY_RECHARGE_URL = "/cf/wechat/recharge/dollPayRecharge.html";

	/**
	 * 微信端银联充值入口URL
	 */
	public static final String WAP_UNIONPAY_RECHARGE_URL = "/cf/wechat/recharge/doUnionPayRecharge.html";

	/**
	 * 微信端提现入口URL
	 */
	public static final String WAP_PAY_CASH_URL = "/nb/wechat/cash/doAllCode.html";

	/**
	 * Web端提现入口URL
	 */
	public static final String WEB_PAY_CASH_URL = "/nb/pc/cash/doAllCash.html";

	/**
	 * 前台地址
	 */
	public static final String WEB_URL = "con_weburl";

	/**
	 * 后台地址
	 */
	public static final String ADMIN_URL = "con_adminurl";

	/**
	 * 用户注册时间限制
	 */
	public static final String USER_ADD_TIME = "2015-09-28 23:59:59";

	/**
	 * 每日提现次数
	 */
	public static final Integer CASH_COUNT = 2;

	/**
	 * 线下充值
	 */
	public static final String OFFLINE_RECHARGE = "offline_recharge";

	/**
	 * 线下提现
	 */
	public static final String OFFLINE_CASH = "offline_cash";

	public static final String CARD_NO = "cardNo";

	public static final String CARD_PASSWORD = "cardPassword";
	
	
	//众筹错误提示码
	/**
	 * 非法参数
	 */
	public static final Integer  ILLEGAL_PARAMETER =100;
	/**
	 * 参数错误
	 */
	public static final Integer Parameter_ERROR=101;
	
	/**
	 * 没有加载到数据
	 */
	public static final Integer DATA_IS_NULL=102;
	

	// 首页抢加息券 //
	// |
	// v

	/**
	 * 成功
	 */
	public static final int CODE_SUCCESS = 0;

	/**
	 * 失败：系统错误
	 */
	public static final int CODE_FAILURE = 1;

	/**
	 * 验证码错误
	 */
	public static final int CODE_WRONG_VALID = 2;

	/**
	 * 已拥有
	 */
	public static final int CODE_ALREADY_HAS = 3;

	/**
	 * 已抢完
	 */
	public static final int CODE_NO_COUPON = 4;

	/**
	 * 未抢到
	 */
	public static final int CODE_LOOT_FAILURE = 5;

	// ^
	// |
	// 首页抢加息券 //

	/** 多通道KEY-CGW **/
	public enum channelKey {
		/** 银联通道key **/
		unionpay("unionpay_channel_key"),
		/** 连连通道key **/
		llpay("llpay_channel_key");

		private final String value;

		channelKey(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	/** 通道入口KEY-CGW **/
	public enum gatewayKey {
		/** 微信端提现key **/
		wapCash("wap_cash_key"),
		/** 微信端充值key **/
		wapRecharge("wap_recharge_key"),
		/** WEB端提现key **/
		webCash("web_cash_key"),
		/** WEB端充值key **/
		webRecharge("web_recharge_key");

		private final String value;

		gatewayKey(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public static Long[] getIds(String idsString) {
		Long[] ids = null;

		if (!"".equals(idsString) && null != idsString) {
			String[] strArray = idsString.split("str");

			if (null != strArray && strArray.length > 0) {
				ids = new Long[strArray.length];

				for (int i = 0; i < strArray.length; i++) {
					if (!"".equals(strArray[i]) && null != strArray[i]) {
						ids[i] = Long.valueOf(strArray[i]);
					}
				}
			}
		}
		return ids;
	}
	
	public static Integer[] getIntIds(String idsString) {
		Integer[] ids = null;

		if (!"".equals(idsString) && null != idsString) {
			String[] strArray = idsString.split("str");

			if (null != strArray && strArray.length > 0) {
				ids = new Integer[strArray.length];

				for (int i = 0; i < strArray.length; i++) {
					if (!"".equals(strArray[i]) && null != strArray[i]) {
						ids[i] = Integer.valueOf(strArray[i]);
					}
				}
			}
		}
		return ids;
	}
	
	public static List<Integer> notNeedMoney(){
		Integer[] ids = new Integer[]{ 
			2,3
		};
		return Arrays.asList(ids);
	}

	public static String[] getStringIds(String idsString) {
		String[] ids = null;

		if (!"".equals(idsString) && null != idsString) {
			String[] strArray = idsString.split("str");

			if (null != strArray && strArray.length > 0) {
				ids = strArray;
			}
		}
		return ids;
	}

}
