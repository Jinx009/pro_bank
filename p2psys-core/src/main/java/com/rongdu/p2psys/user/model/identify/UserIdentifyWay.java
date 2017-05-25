package com.rongdu.p2psys.user.model.identify;

/**
 * 实名认证提交方式 --自动审核，手动审核
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月21日
 */
public interface UserIdentifyWay {

	/**
	 * 实名认证
	 * @return Object
	 * @throws Exception if has error
	 */
	Object doRealname() throws Exception;
	/**
	 * 充值
	 * @return Object
	 * @throws Exception if has error
	 */
	Object doRecharge() throws Exception;
	
	/**
	 * 企业开户
	 * @return
	 * @throws Exception
	 */
	Object doCorpRegister() throws Exception;
}
