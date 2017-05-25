package com.rongdu.p2psys.pc.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.model.accountlog.BaseAccountLog;
import com.rongdu.p2psys.account.model.accountlog.noac.GetCodeLog;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.borrow.model.InvestDetailModel;
import com.rongdu.p2psys.nb.util.ConstantUtil;
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
	@Override
	public UserModel getModel() {
		return model;
	}

	/**
	 * 前往安全中心设置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="/nb/pc/securityCenter",results = { @Result(name = "setting", type = "ftl", location = "/nb/pc/member/securityCenter.html"),
	})
	public String securityCenter() throws Exception {
		// 清判断跳转页面用Flag
//		user = getNBSessionUser();
//		UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
//		session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
//		session.put(Constant.SESSION_USER, user);
//		
//		// 判断支付密码是否已设置
//		request.setAttribute("payPwd", user.getPayPwd());
//		
//		UserCache userCache = user.getUserCache();
//		//判断用户交易密码是否已被锁定
//		request.setAttribute("payPwdLock", UserCacheModel.instance(userCache).isPayPwdLock());
//		//判断用户登录密码是否已被锁定
//		request.setAttribute("pwdLock", UserCacheModel.instance(userCache).isPwdLock());
		
//		data.put("payPwdLock",  UserCacheModel.instance(userCache).isPayPwdLock());
//		data.put("pwdLock", UserCacheModel.instance(userCache).isPwdLock());
//		data.put("payPwd", user.getPayPwd());
		return "setting";
	}
	
	@Action("/nb/pc/securityInfo")
	public void securityInfo() throws IOException{
		// 更新下userIdentify缓存
		data= new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
			session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
			session.put(Constant.SESSION_USER, user);
			data.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
			data.put(Constant.SESSION_USER, user);
			
			// 判断支付密码是否已设置
//			request.setAttribute("payPwd", user.getPayPwd());
			
			UserCache userCache = user.getUserCache();
			//判断用户交易密码是否已被锁定
//			request.setAttribute("payPwdLock", UserCacheModel.instance(userCache).isPayPwdLock());
//			//判断用户登录密码是否已被锁定
//			request.setAttribute("pwdLock", UserCacheModel.instance(userCache).isPwdLock());
			data.put("payPwdLock",  UserCacheModel.instance(userCache).isPayPwdLock());
			data.put("pwdLock", UserCacheModel.instance(userCache).isPwdLock());
			data.put("payPwd", user.getPayPwd());
			data.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	

	/**
	 * 修改登录密码
	 * @return
	 * @throws Exception
	 */
	@Action("/nb/pc/security/modifyPwd")
	public void modifyPwd() throws Exception {
		data = new HashMap<String, Object>();
		try {
			user = getNBSessionUser();	
;
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
	@Action("/nb/pc/security/modifyPaypwd")
	public void modifyPaypwd() throws Exception {
		data = new HashMap<String, Object>();
		user = getNBSessionUser();
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
	 * 前往修改绑定邮箱页面
	 * 
	 * @return
	 */
	@Action("/nb/pc/security/modifyEmail")
	public String modifyEmail() throws Exception {
		user = getNBSessionUser();
		List<UserPwdQuestion> pwdQuestionList = userPwdQuestionService.listRand(user.getUserId());
		if (pwdQuestionList != null && pwdQuestionList.size() > 0) {
			request.setAttribute("pwdQuestionList", pwdQuestionList);
		} else {
			//throw new UserException("请先设置密保问题，再进行绑定邮箱操作！", 1);
			this.frontRedirect("/nb/pc/security/pwdQuestion.html");
		}
		return "modifyEmail";
	}
	
	/**
	 * 修改绑定邮箱-获取校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/nb/pc/security/modifyEmailCode")
	public void modifyEmailCode() throws Exception {
		user = getNBSessionUser();
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
	@Action("/nb/pc/security/checkModifyEmailCode")
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
	@Action("/nb/pc/security/bindEmailCode")
	public void bindEmailCode() throws Exception {
		user = getNBSessionUser();
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
	@Action("/nb/pc/security/doBindEmail")
	public void doBindEmail() throws Exception {
		user = getNBSessionUser();
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
	@Action("/nb/pc/security/modifyPhoneCode")
	public void modifyPhoneCode() throws Exception {
		user = getNBSessionUser();
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
	@Action("/nb/pc/security/checkModifyPhoneCode")
	public void checkModifyPhoneCode() throws Exception {
		model.validModifyPhone();
		printWebSuccess();
	}
	/**
	 * 修改绑定手机 校验密码和密保
	 * 
	 * @return
	 */
	@Action("/nb/pc/security/doModifyPhone")
	public void doModifyPhone() throws Exception {
		user = getSessionUser();
		User u = this.userService.getUserById(user.getUserId());
		UserPwdQuestion question = this.userPwdQuestionService.find(model.getQuestion(), u.getUserId());
		model.validUserQuestion(u, question);
		printWebSuccess();
	}

	/**
	 * 前往绑定手机页面
	 * @return
	 */
	@Action("/nb/pc/security/bindPhone")
	public String bindPhone() {
		UserIdentifyModel model = new UserIdentifyModel();
		model.validAttestationForBindPhone();
		return "bindPhone";
	}

	/**
	 * 绑定手机-获取校验码
	 * @return
	 * @throws Exception
	 */
	@Action("/nb/pc/security/bindPhoneCode")
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
	 * @return
	 */
	@Action("/nb/pc/security/doBindPhone")
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

}
