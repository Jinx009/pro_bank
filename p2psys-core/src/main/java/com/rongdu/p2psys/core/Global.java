package com.rongdu.p2psys.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.core.domain.LogTemplate;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.domain.Rule;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.model.SystemConfigModel;
import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;
import com.rongdu.p2psys.core.rule.RuleCheck;

/**
 * @author fxx
 * @date 2012-8-29
 * @version 2.0
 */
public class Global {

	public static String VERSION = "v2.0.0.0";

	public static final String DB_PREFIX = "rd_";

	public static SystemConfigModel SYSTEMINFO;
	public static Map BORROWCONFIG;
	public static List<BorrowConfig> BORROW_CONFIG_LIST;
	public static Map<String, Rule> RULES = new HashMap<String, Rule>();
	public static Map<String, RuleCheck> RULECHECKS = new HashMap<String, RuleCheck>();
	public static Map<String, LogTemplate> LOG_TEMPLATE_MAP = new HashMap<String, LogTemplate>();
	/**
	 * 系统executer初始化
	 */
	public static Map<String, AbstractExecuter> EXECUTOR_MAP = Collections
			.synchronizedMap(new HashMap<String, AbstractExecuter>());
	/**
	 * 系统protocol初始化
	 */
	public static Map<String, AbstractProtocolBean> PROTOCOL_MAP = Collections
			.synchronizedMap(new HashMap<String, AbstractProtocolBean>());

	public static Set TRADE_NO_SET = Collections.synchronizedSet(new HashSet());
	public static Set TENDER_SET = Collections.synchronizedSet(new HashSet());
	public static Map TENDER_MAP = Collections.synchronizedMap(new HashMap<String, String>());
	public static Map<String, Object> SESSION_MAP = Collections.synchronizedMap(new HashMap<String, Object>());
    //接口回调处理结果
	public static Map RESULT_MAP=Collections.synchronizedMap(new HashMap<String,String>());
	
	
	public static String[] SYSTEMNAME = new String[] { "webname", "meta_keywords", "meta_description", "beian",
			"copyright", "fuwutel", "address", "weburl", "vip_fee", "theme_dir", "version", "normal_rate",
			"overdue_rate", "bad_rate", "enable_online_recharge","api_code", "adminurl", "is_open_deposit","bank_num","manage_img_url","manage_img_real_url"};

	public static ThreadLocal<String> IP_THREADLOCAL = new ThreadLocal<String>();

	public static final ThreadLocal transferThreadLocal = new ThreadLocal();

	public static Map SMSTYPECONFIG;

	public static Map NOTICE_TYPE_CONFIG;

	public static BorrowConfig getBorrowConfig(int borrowType) {
		BorrowConfig config = null;
		if (BORROWCONFIG == null) {
			return null;
		}
		Object obj = BORROWCONFIG.get(new Long(borrowType));
		if (obj == null)
			return null;
		config = (BorrowConfig) obj;
		return config;
	}


	public static String getValue(String key) {
		Object o = null;
		if (SYSTEMINFO != null) {
			o = SYSTEMINFO.getValue(key);
		}
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	public static String getString(String key) {
		String s = StringUtil.isNull(getValue(key));
		return s;
	}

	public static int getInt(String key) {
		int i = StringUtil.toInt(getValue(key));
		return i;
	}

	public static double getDouble(String key) {
		double i = BigDecimalUtil.round(getValue(key));
		return i;
	}


	public static String getIP() {
		Object retObj = Global.IP_THREADLOCAL.get();
		return retObj == null ? "" : retObj.toString();
	}

	public static String getVersion() {
		return Global.getString("version");
	}

	public static Rule getRule(String nid) {
		Rule rule = Global.RULES.get(nid);
		if (rule != null) {
			return rule;
		}
		return null;
	}

	public static RuleCheck getRuleCheck(String nid) {
		return Global.RULECHECKS.get(nid);
	}

	public static String getLogTempValue(byte type, String nid) {
		if (type <= 0 || nid == null || nid.length() <= 0) {
			return null;
		}
		String key = type + "_" + nid;
		LogTemplate temp = Global.LOG_TEMPLATE_MAP.get(key);
		if (temp == null)
			return null;
		return temp.getValue();
	}

	public static String getLogType(byte type, String nid) {
		if (type <= 0 || nid == null || nid.length() <= 0) {
			return null;
		}
		String key = type + "_" + nid;
		LogTemplate temp = Global.LOG_TEMPLATE_MAP.get(key);
		if (temp == null)
			return null;
		return temp.getLogType();
	}

	public static NoticeType getNoticeType(String noticeTypeNid, byte notice_type) {
		NoticeType noticeType = new NoticeType();
		if (NOTICE_TYPE_CONFIG == null) {
			return noticeType;
		}
		Object obj = NOTICE_TYPE_CONFIG.get(noticeTypeNid + "_" + notice_type);
		if (obj == null)
			return noticeType;
		noticeType = (NoticeType) obj;
		return noticeType;
	}

	public static Map<String, Object> getTransfer() {
		Map<String, Object> map = (Map<String, Object>) transferThreadLocal.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			transferThreadLocal.set(map);
		}
		return map;
	}

	public static void setTransfer(String key, Object value) {
		Map<String, Object> map = getTransfer();
		map.put(key, value);
		transferThreadLocal.set(map);
	}

}
