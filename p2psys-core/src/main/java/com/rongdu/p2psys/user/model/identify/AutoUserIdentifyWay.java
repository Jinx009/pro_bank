package com.rongdu.p2psys.user.model.identify;

import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 自动审核方式
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月21日
 */
public class AutoUserIdentifyWay extends BaseUserIdentifyWay {
	//private UserCacheService userCacheService;
//	@Resource
//	private UserService userService;
	private long userId;
	private UserModel model;

	public AutoUserIdentifyWay(long userId, UserModel model) {
		this.userId = userId;
		this.model = model;
	}

	@Override
	public Object doRealname() throws Exception {
		UserService userService = (UserService)BeanUtil.getBean("userService");
		//调用托管接口实名
		TPPWay depositWay = TPPFactory.getTPPWay(model, userService.getUserById(userId), null, null,null);
		Object o = depositWay.doRealname();
		return o;
	}

	@Override
	public Object doCorpRegister() throws Exception {
		UserService userService = (UserService)BeanUtil.getBean("userService");
		//调用托管接口实名
		TPPWay depositWay = TPPFactory.getTPPWay(model, userService.getUserById(userId), null, null,null);
		Object o = depositWay.doCorpRegister();
		return o;
	}
}
