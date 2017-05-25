package com.rongdu.p2psys.cooperation.model;

import com.rongdu.p2psys.cooperation.domain.CooperationLogin;

/**
 * 联合登陆方式 --QQ登陆，sina登陆等
 * 
 * @author sj
 * @version 2.0
 * @since 2014年5月28日
 */
public interface CooperationLoginWay {

	/**
	 * 点击联合登陆
	 */
	public void cooperationLogin() throws Exception;

	/**
	 * 登陆成功回调
	 */
	public CooperationLogin cooperationLoginSuccess() throws Exception;

}
