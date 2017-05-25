package com.rongdu.p2psys.web.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.service.UserBaseInfoService;

/**
 * 用户基本信息
 * 
 * @author wzh
 * @version 2.0
 * @since 2014年11月4日
 */
public class UserBaseInfoAction extends BaseAction<UserBaseInfo> implements ModelDriven<UserBaseInfo> {

	@Resource
	private UserBaseInfoService userBaseInfoService;
	private User user;
	private Map<String, Object> data;
	
	/**
	 * 获取个人信息
	 * @throws Exception
	 */
	@Action("/member_borrow/info/detail")
	public String detail() throws Exception {
		user = getSessionUser();
		UserBaseInfo userBaseInfo = userBaseInfoService.findByUserId(user.getUserId());
		if(userBaseInfo == null){
			userBaseInfo = new UserBaseInfo();
			userBaseInfo.setBirthday(new Date());
		}
		request.setAttribute("userBaseInfo", userBaseInfo);
		return "detail";
	}
	
	/**
	 * 更新个人信息
	 * @throws Exception
	 */
	@Action("/member_borrow/info/saveUserBaseInfo")
	public void saveUserBaseInfo() throws Exception {
		user = getSessionUser();
		model.setUser(user);
		userBaseInfoService.save(model);
		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", MessageUtil.getMessage("I90001"));
		printWebJson(getStringOfJpaObj(data));
	}
}
