package com.rongdu.p2psys.web.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.model.accountlog.BaseAccountLog;
import com.rongdu.p2psys.account.model.accountlog.noac.GetCodeLog;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.rule.RealNameAttestationRuleCheck;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPwdQuestion;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserPwdQuestionService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;
import com.rongdu.p2psys.web.borrow.BorrowAction;

/**
 * 安全中心
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月25日16:57:51
 */

@SuppressWarnings("rawtypes")
public class SecurityAction extends BaseAction implements ModelDriven<UserModel> {
	
	private Logger logger = Logger.getLogger(BorrowAction.class);
	private UserModel model = new UserModel();
	private Map<String, Object> data;
	
	@Override
	public UserModel getModel() {
		return model;
	}

	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserPwdQuestionService userPwdQuestionService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private AccountBankService accountBankService;

	private User user;

	private RealNameAttestationRuleCheck realNameAttestationRuleCheck = (RealNameAttestationRuleCheck) Global
			.getRuleCheck("realNameAttestation");

	/**
	 * 前往安全中心设置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="/member/security/setting",results = { @Result(name = "setting", type = "ftl", location = "/member/security/setting.html"),
			@Result(name = "borrow_setting", type = "ftl", location = "/member_borrow/security/setting.html")
	})
	public String setting() throws Exception {
		
		// 清判断跳转页面用Flag
		request.getSession().removeAttribute("returnFlag");
		
		request.setAttribute("real_name_style", realNameAttestationRuleCheck.style());

		// 更新下userIdentify缓存
		UserIdentify userIdentify = getSessionUserIdentify();
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentifyService.findById(userIdentify.getId()));
		
		// 获取Session中的用户对象
		user = userService.getUserById(getSessionUser().getUserId());
		// 判断支付密码是否已设置
		request.setAttribute("payPwd", user.getPayPwd());
		
		// 密保问题List取得
		List<UserPwdQuestion> list = userPwdQuestionService.pwdQuestion(user);
		// 密保已设置的场合、设置/修改按钮显示判断用Flag设定值
		if (list != null && list.size() > 0) {
			request.setAttribute("pwdQuestion", 1);
		}
		request.setAttribute("realNameStatus", userIdentify.getRealNameStatus());
		
		String str = String.valueOf(userIdentify.getRealNameStatus()) + String.valueOf(userIdentify.getEmailStatus()) + String.valueOf(userIdentify.getMobilePhoneStatus());
		str = str.replaceAll("-1", "");
		String result = str.replaceAll("1", "");
		int securityStatus = (str.length()-result.length());
		request.setAttribute("securityStatus", securityStatus);
		
		UserCache userCache = user.getUserCache();
		//判断用户交易密码是否已被锁定
		request.setAttribute("payPwdLock", UserCacheModel.instance(userCache).isPayPwdLock());
		//判断用户登录密码是否已被锁定
		request.setAttribute("pwdLock", UserCacheModel.instance(userCache).isPwdLock());
		if(!UserCacheModel.instance(userCache).isPayPwdLock() && userCache.getLoginPwdStatus() == 1) {
			userCache.setLoginPwdStatus(0);
			userCache.setLockTime(null);
			userCache.setLoginFailTimes(0);
			userCacheService.update(userCache);
		}
		if(!UserCacheModel.instance(userCache).isPayPwdLock() && userCache.getPayPwdStatus() == 1) {
			userCache.setPayPwdStatus(0);
			userCache.setLockPayTime(null);
			userCache.setPayFailTimes(0);
			userCacheService.update(userCache);
		}
		
		//用户是否绑定银行卡
		List<AccountBank> bankList = accountBankService.list(user.getUserId());
		request.setAttribute("bankList", bankList);
		
		if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2 || user.getUserCache().getUserType() == 4) {
			return "borrow_setting";
		}
		return "setting";
	}

	/**
	 * 修改登录密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/modifyPwd")
	public void modifyPwd() throws Exception {
		data = new HashMap<String, Object>();
		try {
			user = getSessionUser();
			model.validModifyPwdModel(user);
			user.setPwd(model.getNewPwd());
			session.put(Constant.SESSION_USER, userService.modifyPwd(user));
			printWebSuccess();
		} catch (UserException ue) {
			if(userCacheService.doLock(request, user.getUserId(), UserCacheModel.PWD_LOCK)){
				data.put("lock", true);
			}
			data.put("result", false);
			data.put("msg", ue.getMessage());
			printWebJson(getStringOfJpaObj(data));
		} catch (Exception e) {
			logger.error("修改密码失败", e);
			printWebResult("result", false);
		}
	}

	/**
	 * 修改支付密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/modifyPaypwd")
	public void modifyPaypwd() throws Exception {
		data = new HashMap<String, Object>();
		user = getSessionUser();
		try {
			model.validModifyPaypwdModel(user);
			user.setPayPwd(model.getNewPayPwd());
			session.put(Constant.SESSION_USER, userService.modifyPaypwd(user));
			printWebSuccess();
		} catch (UserException ue) {
			if(userCacheService.doLock(request, user.getUserId(), UserCacheModel.PAY_PWD_LOCK)) {
				data.put("lock", true);
			}
			data.put("result", false);
			data.put("msg", ue.getMessage());
			printWebJson(getStringOfJpaObj(data));
		} catch (Exception e) {
			logger.error("修改支付失败", e);
			printWebResult("result", false);
		}
	}
	/**
	 * 前往实名认证页面
	 * 
	 * @return
	 */
	@Action("/member/security/realNameIdentify")
	public String realNameIdentify() {
		user = userService.getUserById(getSessionUser().getUserId());
		session.put(Constant.SESSION_USER, user);
		boolean isOnlineConfig = BaseTPPWay.isOnlineConfig();
		request.setAttribute("isOnlineConfig", isOnlineConfig);
		return "realNameIdentify";
	}
	/**
	 * 实名认证
	 * 
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/member/security/realname",results = { @Result(name = "yjf", type = "ftl", location = "/tpp/yjfrealname.html"),
			@Result(name = "ipsregister", type = "ftl", location = "/tpp/ipsregister.html"),
			@Result(name = "pnrregister", type = "ftl", location = "/tpp/chinapnr/userRegister.html")
	})
	public String realname() throws Exception {
		user = getSessionUser();
		if (isOpenApi()) {
			ValidateUtil.checkPhoneExist(model.getMobilePhone());
			if(!ValidateUtil.isEmail(model.getEmail())){
				throw new UserException("Email格式不正确！",2);
			}
			ValidateUtil.checkEmailExist(model.getEmail(), user.getUserId());
		}
		user.setEmail(model.getEmail());
		Object obj = userCacheService.doRealname(user, model);
		if (obj != null) {
			return madeApiRegisterReturn(obj, user);
		}
		UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
		// 更新session中real_name_status的状态
		userIdentify.setRealNameStatus(2);
		userIdentify.setEmailStatus(2);
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
		if (isOpenApi()) {
			message("实名认证申请成功，等待审核");
			return MSG;
		} else {
			data = new HashMap<String, Object>();
			data.put("result", true);
			printWebJson(getStringOfJpaObj(data));
			return null;
		}
	}
	
	/**
	 * 企业开户
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/member/security/corpRegister", results = 
		{ @Result(name = "pnrCorpregister", type = "ftl", location = "/tpp/chinapnr/corpRegister.html") })
	public String corpRegister() throws Exception {
		user = getSessionUser();
		if (isOpenApi()) {
			ValidateUtil.checkPhoneExist(model.getMobilePhone());
			if(!ValidateUtil.isEmail(model.getEmail())){
				throw new UserException("Email格式不正确！",2);
			}
			ValidateUtil.checkEmailExist(model.getEmail(), user.getUserId());
			if(StringUtil.isBlank(model.getBusinessRegistrationNumber())){
				throw new UserException("营业执照编号不能为空",2);
			}
			ValidateUtil.checkBusinessRegistrationNumber(model.getBusinessRegistrationNumber(),user.getUserId());
		}
		user.setEmail(model.getEmail());
		user.setCardId(model.getCardId());
		user.setRealName(model.getRealName());
		UserCache cache = user.getUserCache();
		cache.setBusinessRegistrationNumber(model.getBusinessRegistrationNumber());
		cache.setZzjgCode(paramString("zzjgCode"));
		userCacheService.update(cache);
		model.setUserCache(cache);
		Object obj = userCacheService.doCorpRegister(user, model);
		request.setAttribute("reg", obj);
		if(obj != null){
			return "pnrCorpregister";
		}
		return null;
	}
	
	/**
	 * 处理不同第三方接口
	 * @param object 
	 * @param user 用户
	 * @return 返回的页面
	 */
	private String madeApiRegisterReturn(Object object, User user) {
		int apiType = TPPWay.API_CODE;
		switch (apiType) {
		case 1:// 易极付接口
			request.setAttribute("yjf", object);
			return "yjf";
		case 2:// 环讯接口
			request.setAttribute("ips", object);
			return "ipsregister";
		case 3:// 汇付接口
			request.setAttribute("reg", object);
			return "pnrregister";			
		default:
			message("实名认证申请成功，请到个人中心查看","/member/main.html");
			return MSG;
		}
	}
	
	/**
	 * 前往修改绑定邮箱页面
	 * 
	 * @return
	 */
	@Action("/member/security/modifyEmail")
	public String modifyEmail() throws Exception {
		user = getSessionUser();
		List<UserPwdQuestion> pwdQuestionList = userPwdQuestionService.listRand(user.getUserId());
		if (pwdQuestionList != null && pwdQuestionList.size() > 0) {
			request.setAttribute("pwdQuestionList", pwdQuestionList);
		} else {
			//throw new UserException("请先设置密保问题，再进行绑定邮箱操作！", 1);
			this.frontRedirect("/member/security/pwdQuestion.html");
		}
		return "modifyEmail";
	}
	
	/**
	 * 修改绑定邮箱-获取校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/modifyEmailCode")
	public void modifyEmailCode() throws Exception {
		user = getSessionUser();
		BaseAccountLog blog = new GetCodeLog(user, user.getUserName(), model.getEmail(), null,
				NoticeConstant.NOTICE_MODIFY_EMAIL);
		blog.initCode(NoticeConstant.NOTICE_MODIFY_EMAIL);
		blog.doEvent();
		printWebSuccess();
	}
	
	/**
	 * 修改绑定邮箱-校验校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/checkModifyEmailCode")
	public void checkModifyEmailCode() throws Exception {
		model.validModifyEmail();
		printWebSuccess();
	}
	
	/**
	 * 绑定邮箱-获取校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/bindEmailCode")
	public void bindEmailCode() throws Exception {
		user = getSessionUser();
		BaseAccountLog blog = new GetCodeLog(user, user.getUserName(), model.getEmail(), null,
				NoticeConstant.NOTICE_BIND_EMAIL);
		blog.initCode("bindEmail");
		blog.doEvent();
		printWebSuccess();
	}
	
	/**
	 * 绑定邮箱
	 * 
	 * @return
	 */
	@Action("/member/security/doBindEmail")
	public void doBindEmail() throws Exception {
		user = getSessionUser();
		UserIdentify userAttestation = getSessionUserIdentify();
		//预先保存用户之前是否已经绑定过邮箱
		int isBind = userAttestation.getEmailStatus();
		long userId = user.getUserId();
		model.validBindEmail();
		userIdentifyService.modifyEmailStatus(userId, 1, 0);
		userService.modifyEmail(userId, model.getEmail());
		// 更新session_user
		user.setEmail(model.getEmail());
		session.put(Constant.SESSION_USER, user);
		// 更新SESSION_USER_IDENTIFY
		userAttestation.setEmailStatus(1);
		session.put(Constant.SESSION_USER_IDENTIFY, userAttestation);
		if(isBind != 1){
			//发放邮箱认证红包
			userRedPacketService.doRedPacket(RedPacket.EMAIL, user, null, null);
		}
		printWebSuccess();
	}

	/**
	 * 修改绑定手机-获取校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/modifyPhoneCode")
	public void modifyPhoneCode() throws Exception {
		user = getSessionUser();
		BaseAccountLog blog = new GetCodeLog(user, user.getUserName(), null, user.getMobilePhone(),
				NoticeConstant.NOTICE_MODIFY_PHONE);
		blog.initCode(NoticeConstant.NOTICE_MODIFY_PHONE);
		blog.doEvent();
		printWebSuccess();
	}
	/**
	 * 修改绑定手机-校验校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/checkModifyPhoneCode")
	public void checkModifyPhoneCode() throws Exception {
		model.validModifyPhone();
		printWebSuccess();
	}
	/**
	 * 修改绑定手机 校验密码和密保
	 * 
	 * @return
	 */
	@Action("/member/security/doModifyPhone")
	public void doModifyPhone() throws Exception {
		user = getSessionUser();
		User u = this.userService.getUserById(user.getUserId());
		UserPwdQuestion question = this.userPwdQuestionService.find(model.getQuestion(), u.getUserId());
		model.validUserQuestion(u, question);
		printWebSuccess();
	}

	/**
	 * 前往绑定手机页面
	 * 
	 * @return
	 */
	@Action("/member/security/bindPhone")
	public String bindPhone() {
		UserIdentifyModel model = new UserIdentifyModel();
		model.validAttestationForBindPhone();
		return "bindPhone";
	}

	/**
	 * 绑定手机-获取校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/bindPhoneCode")
	public void bindPhoneCode() throws Exception {
		user = getSessionUser();
		BaseAccountLog blog = new GetCodeLog(user, user.getUserName(), null, model.getMobilePhone(),
				NoticeConstant.NOTICE_BIND_PHONE);
		blog.initCode("bindPhone");
		blog.doEvent();
		printWebSuccess();
	}

	/**
	 * 绑定手机
	 * 
	 * @return
	 */
	@Action("/member/security/doBindPhone")
	public void doBindPhone() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		model.validBindPhone();
		userService.modifyPhone(userId, model.getMobilePhone(),1);
		// 更新session_user
		user.setMobilePhone(model.getMobilePhone());
		user.setUserName(model.getMobilePhone());
		session.put(Constant.SESSION_USER, user);
		// 更新SESSION_USER_IDENTIFY
		UserIdentify userIdentify = getSessionUserIdentify();
		userIdentify.setMobilePhoneStatus(1);
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
		printWebSuccess();
	}

	/**
	 * 密保问题展示页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/pwdQuestion")
	public String pwdQuestion() throws Exception {
		user = getSessionUser();
		List<UserPwdQuestion> list = userPwdQuestionService.pwdQuestion(user);
		if (list != null && list.size() > 0) {
			request.setAttribute("list", list);
			return "pwdQuestion";
		} else {
			// 判断跳转页面用Flag(null：跳转到设置密保页面;1：跳转到安全中心页面)
			request.getSession().setAttribute("returnFlag", 1);
			// 判断按钮名称显示用Flag(1：确认；1以外：确认修改;)
			// 未设置密保的场合、按钮名称：确认
			request.setAttribute("showFlag", 1);
			return "setPwdQuestion";
		}
	}

	/**
	 * 回答密保问题页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/answerPwdQuestion")
	public String answerPwdQuestion() throws Exception {
		user = getSessionUser();
		List<UserPwdQuestion> list = userPwdQuestionService.pwdQuestion(user);
		request.setAttribute("list", list);
		return "answerPwdQuestion";
	}

	/**
	 * 回答密保问题
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/doAnswerPwdQuestion")
	public void doAnswerPwdQuestion() throws Exception {
		user = getSessionUser();
		userPwdQuestionService.doAnswerPwdQuestion(model, user);
		printWebSuccess();
	}

	/**
	 * 设置密保问题页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/setPwdQuestion")
	public String setPwdQuestion() throws Exception {
		
		// 判断跳转页面用Flag(null：跳转到设置密保页面;1：跳转到安全中心页面)
		if (request.getSession().getAttribute("returnFlag") == null) {
			request.getSession().setAttribute("returnFlag", 1);
		}
		return "setPwdQuestion";
	}

	/**
	 * 设置密保问题
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/security/doSetPwdQuestion")
	public void doSetPwdQuestion() throws Exception {
		user = getSessionUser();
		userPwdQuestionService.doSetPwdQuestion(model, user);
		printWebSuccess();
	}
}
