package com.rongdu.common.util;

import java.util.Calendar;

import com.rongdu.common.util.DateUtil;

/**
 * 工具类-充值
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月26日
 */
public class RechargeUtil {
	/**
	 * 生成流水号(根据类型+用户ID+时间)
	 * 
	 * @param userid
	 * @param type
	 * @return
	 */
	public synchronized static String generateTradeNO(long userid, String type) {
		return type + userid + DateUtil.dateStr3(Calendar.getInstance().getTime());
	}
}
