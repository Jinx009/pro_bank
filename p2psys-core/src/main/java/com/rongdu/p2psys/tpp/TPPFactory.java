package com.rongdu.p2psys.tpp;

import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * 托管接口工厂类
 * @author lx
 * @version 2.0
 * @since 2014年7月22日
 */
public final class TPPFactory {
	private TPPFactory(){}
	public static TPPWay getTPPWay(UserModel model, User user, AccountRechargeModel rechargeModel, String extra, Borrow borrow) {
		int apiCode = TPPWay.API_CODE;
		TPPWay dw = null;
		switch (apiCode) {
			case 1:
				dw = new YjfTPPWay(model, user, rechargeModel, extra,borrow);
				break;
			case 2:
				dw = new IpsTPPWay(model, user, rechargeModel, extra,borrow);
				break;
			case 3:
				dw = new ChinapnrTPPWay(model, user, rechargeModel, extra, borrow);
				break;				
			default:
				break;
		}
		return dw;
	}
	public static TPPWay getTPPWay() {
		return getTPPWay(null, null, null,null,null);
	}
	
	public static TPPWay getTPPWay(User user) {
        return getTPPWay(null, user, null,null,null);
    }
	
	public static TPPWay getTPPWay(double money) {
		AccountRechargeModel rechargeModel=new AccountRechargeModel();
		rechargeModel.setMoney(money);
		return getTPPWay(null, null, rechargeModel,null,null);
	}
	
}
