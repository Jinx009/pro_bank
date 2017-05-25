package com.rongdu.p2psys.web.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 用户认证信息
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月20日
 */
public class UserIdentifyAction extends BaseAction implements ModelDriven<UserIdentifyModel> {

	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserService userService;
	@Autowired
	private UserCacheService userCacheService;

	private UserIdentifyModel userIdentifyModel = new UserIdentifyModel();

	@Override
	public UserIdentifyModel getModel() {
		return userIdentifyModel;
	}

	private User user;

	private Map<String, Object> data;

	/**
	 * 显示更改头像页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/head")
	public String head() throws Exception {
		return "head";
	}

	/**
	 * 认证页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/identify")
	public String identify() throws Exception {
		return "identify";
	}

	/**
	 * 邮箱激活/认证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/emailActive")
	public String emailActive() throws Exception {
		user = getSessionUser();
		if (StringUtil.isNotBlank(userIdentifyModel.getEmail())
				&& !user.getEmail().equals(userIdentifyModel.getEmail())) {
			userService.modifyEmail(user.getUserId(), userIdentifyModel.getEmail());
		}
		Global.setTransfer("user", user);
		Global.setTransfer("activeUrl", "/member/identify/active.html?id=");
		BaseMsg msg = new BaseMsg(NoticeConstant.NOTICE_EMAIL_ACTIVE);
		return null;
	}

	/**
	 * 提交手机认证申请
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/phoneApply")
	public String phoneApply() throws Exception {
		user = getSessionUser();
		data = new HashMap<String, Object>();
		if (!user.getMobilePhone().equals(userIdentifyModel.getMobilePhone())) {
			userService.modifyPhone(user.getUserId(), userIdentifyModel.getMobilePhone(),2);
		}
		data.put("result", true);
		data.put("real_name", userIdentifyModel.getMobilePhone());
		data.put("msg", "手机认证正在审核！");
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 进行现场认证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/doScene")
	public String doScene() throws Exception {
		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "现场认证申请提交成功，等待管理员审核！");
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 进行视频认证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/doVideo")
	public String doVideo() throws Exception {
		data = new HashMap<String, Object>();
		UserIdentify userIdentify = userIdentifyService.findByUserId(getSessionUserId());
		if(userIdentify.getVideoStatus() == 0 || userIdentify.getVideoStatus() == -1) {
			userIdentify.setVideoStatus(2);
			data.put("result", true);
			data.put("msg", "视频认证申请提交成功，等待管理员审核！");
			printWebJson(getStringOfJpaObj(data));
		} else {
			data.put("result", false);
			if (userIdentify.getVideoStatus() == 1) {
				data.put("msg", "您已通过视频认证！");
			} else {
				data.put("msg", "您已提交视频认证，请等待审核！");
			}
			printWebJson(getStringOfJpaObj(data));
		}
		return null;
	}

	/**
	 * 
	 * 用户查询
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/realNameStatus")
	public String realNameStatus() throws Exception {
		user = userService.getUserById(getSessionUser().getUserId());
		session.put(Constant.SESSION_USER, user);
		data = new HashMap<String, Object>();
		if (getSessionUserIdentify().getRealNameStatus() == 1) {
			data.put("result", true);
			data.put("msg", "已实名认证");
		} else {
			data.put("result", false);
			data.put("msg", "未实名认证");
		}
		
		printWebJson(getStringOfJpaObj(data));
		return null;
	}
	
	
	/**
	 * vip申请页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/vip")
	public String vip() throws Exception {
		return "vip";
	}

	/**
	 * 用户申请vip
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/useridentify/applyVip")
	public String applyVip() throws Exception {

		String vipRemark = paramString("vipRemark");
		if (StringUtil.isNull(vipRemark).length() > 125) {
			throw new BussinessException("备注字数限制在125内，请重新输入", 1);
		}

		User user = getSessionUser();
		UserModel userModel = UserModel.instance(user);
		userModel.setValidCode(paramString("validCode"));
		// 校验验证码
		userModel.validRegRule();
		// 后台传参
		Global.setTransfer("vipRemark", vipRemark);
		UserIdentify userIdentify = userCacheService.applyVip(user);

		// 更新缓存
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "vip申请成功！");
		printWebJson(getStringOfJpaObj(data));

		return null;
	}

}
