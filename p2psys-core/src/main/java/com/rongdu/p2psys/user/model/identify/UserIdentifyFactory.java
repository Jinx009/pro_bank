package com.rongdu.p2psys.user.model.identify;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.rule.RealNameAttestationRuleCheck;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * 实名认证方式
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月21日
 */
public class UserIdentifyFactory {

	private long userId;
	private UserModel model;
	private RealNameAttestationRuleCheck realNameAttestationRuleCheck = (RealNameAttestationRuleCheck) Global
			.getRuleCheck("realNameAttestation");

	public UserIdentifyFactory(long userId, UserModel model) {
		this.userId = userId;
		this.model = model;
	}

	public UserIdentifyWay realname() throws Exception {
		String style = realNameAttestationRuleCheck.style();
		//if ("ID5".equals(style)) {// 自动认证审核
		if("1".equals(Global.getValue("is_open_deposit"))){//是否开启托管接口 1:开启, 0关闭
			return new AutoUserIdentifyWay(userId, model);
		} else if ("manually".equals(style)) {// 手动认证审核
			return new ManualUserIdentifyWay(userId, model);
		}
		return null;
	}

	public UserIdentifyWay corpRegister() throws Exception {
		if("1".equals(Global.getValue("is_open_deposit"))){//是否开启托管接口 1:开启, 0关闭
			return new AutoUserIdentifyWay(userId, model);
		} 
		return null;
	}
}
