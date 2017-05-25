package com.rongdu.p2psys.user.model.login;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.rule.LoginRuleCheck;
import com.rongdu.p2psys.user.domain.User;

public class UserLoginComposite {
	LoginRuleCheck loginRuleCheck = (LoginRuleCheck) Global.getRuleCheck("login");
	List<LoginWay> loginWayList = new ArrayList<LoginWay>();

	public User login(User user) throws Exception {
		User u = null;
		loginWayList.add(new UserNameLoginWay(loginRuleCheck.userName_login));
		loginWayList.add(new EmailLoginWay(loginRuleCheck.email_login));
		loginWayList.add(new MobilePhoneLoginWay(loginRuleCheck.mobile_phone_login));
		for (int i = 0; i < loginWayList.size(); i++) {
			LoginWay loginWay = loginWayList.get(i);
			u = loginWay.doLogin(user);
			if (u != null && i == 0 && u.getUserCache().getUserNature() != 1) {
			    u = null;
			    continue;
			}
			if(user.getPwd().length() == 32){
				if (u != null && user.getPwd().equals(u.getPwd())) {
					break;
				}
                u = null;
			}else{
				if (u != null && MD5.encode(user.getPwd()).equals(u.getPwd())) {
					break;
				}
                u = null;
			}
		}
		return u;
	}

}
