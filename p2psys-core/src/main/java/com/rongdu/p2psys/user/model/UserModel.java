package com.rongdu.p2psys.user.model;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.ImageUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.rule.LoginRuleCheck;
import com.rongdu.p2psys.core.rule.RegisterRuleCheck;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPwdQuestion;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 用户Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月1日
 */
public class UserModel extends User {

	/** 验证码 **/
	private String validCode;

	/** 校验码 **/
	private String code;

	/** 修改密码 **/
	private String newPwd;
	private String confirmNewPwd;

	/** 修改支付密码 **/
	private String newPayPwd;
	private String confirmNewPayPwd;

	/** 注册时用户的二次确认密码 **/
	private String confirmPassword;
	/** 证件图片正面 */
	private File cardPositive;
	/** 证件图片反面 */
	private File cardOpposite;

	/** 密保ID **/
	private int question;

	/** 密保答案 **/
	private String answer;

	private String cardId;

	private UserCache userCache;

	private UserIdentify userIdentify;

	private int status;
	/**
	 * 登录密码是否锁定
	 */
	private int loginPwdStatus;
	/**
	 * 支付密码是否锁定
	 */
	private int payPwdStatus;
	/** 条件查询 */
	private String searchName;

	/** 用户认证ID **/
	private long id;

	/** 验证密保 */
	private String question1;
	private String question2;
	private String question3;
	private String answer1;
	private String answer2;
	private String answer3;
	private int sex;
	private int userType;
	private int userNature;
	// 营业执照编号
	private String businessRegistrationNumber;

	private User inviteUser;
	private String inviteUserName;
	private String inviteRealName;
	private Date inviteTime;

	private double borrowVoucherTotal;// 抵用券总额

	private int page;

	private int size = Page.ROWS;

	private int realNameStatus;

	private int sign;

	private String sexStr;

	private String realNameStatusStr;

	private String statusStr;

	
	private String nickName;
	
	private String birthday;
	
	private String email;
	
	private String realName;
	
	private int flag;
	
	private int payFlag;
	
	private String headerImg;
	
	public int getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(int payFlag) {
		this.payFlag = payFlag;
	}

	private RegisterRuleCheck registerRuleCheck = (RegisterRuleCheck) Global
			.getRuleCheck("register");
	private LoginRuleCheck loginRuleCheck = (LoginRuleCheck) Global
			.getRuleCheck("login");

	public static UserModel instance(User user) {
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(user, userModel);
		return userModel;
	}

	public User prototype() {
		User user = new User();
		BeanUtils.copyProperties(this, user);
		return user;
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 注册 检查提交的数据格式
	 * 
	 * @return
	 */
	public int validRegModel() {
		if (StringUtil.isBlank(getUserName())) {
			throw new UserException("用户名不能为空！", 1);
		}
		if (StringUtil.isBlank(getEmail())) {
			throw new UserException("邮箱不能为空！", 1);
		}
		if (!ValidateUtil.isEmail(getEmail())) {
			throw new UserException("邮箱格式错误！", 1);
		}
		return -1;
	}

	/**
	 * 注册 检查提交的密码数据格式
	 * 
	 * @return
	 */
	public int validRegPwdModel() {
		if (StringUtil.isBlank(getPwd())) {
			throw new UserException("密码不能为空！", 1);
		}
		if (StringUtil.isBlank(getConfirmNewPwd())) {
			throw new UserException("确认密码不能为空！");
		}
		if (!getPwd().equals(getConfirmNewPwd())) {
			throw new UserException("两次输入的密码不一致！");
		}
		return -1;
	}

	/**
	 * 校验验证码
	 * 
	 * @return
	 */
	public int validRegRule() {
		if (registerRuleCheck.enable_codeCheck) {
			String vCode = StringUtil.isNull(getValidCode());
			if (vCode.isEmpty()) {
				throw new UserException(MessageUtil.getMessage("E30005"), 1);
			}

			if (!ValidateUtil.checkValidCode(vCode)) {
				throw new UserException(MessageUtil.getMessage("E30006"), 1);
			}
		}
		return -1;
	}

	/**
	 * 新版校验验证码
	 * 
	 * @return
	 */
	public int validWechatRegRule() {
		if (registerRuleCheck.enable_codeCheck) {
			String vCode = StringUtil.isNull(getValidCode());

			if (vCode.isEmpty()) {
				return 0;
			}
			if (!ValidateUtil.checkValidCode(vCode)) {
				return 0;
			}
		}
		return -1;
	}

	/**
	 * 登录 检查提交的数据格式
	 * 
	 * @return
	 */
	public int validLoginModel() {
		if (StringUtil.isBlank(getUserName())) {
			throw new UserException("用户名不能为空！", 1);
		}
		if (StringUtil.isBlank(getPwd())) {
			throw new UserException("密码不能为空！", 1);
		}
		return -1;
	}

	/**
	 * 登录 校验验证码
	 * 
	 * @return
	 */
	public int validLoginRule() {
		if (loginRuleCheck.enable_codeCheck) {
			String vCode = StringUtil.isNull(getValidCode());
			if (vCode.isEmpty()) {
				throw new UserException("验证码不能为空！", 1);
			}
			if (!ValidateUtil.checkValidCode(vCode)) {
				throw new UserException("验证码错误！", 1);
			}
		}
		return -1;
	}

	/**
	 * 验证用户登录密码
	 * 
	 * @param user
	 * @return
	 */
	public String validModifyPwdModel(User user) {
		if (getPwd() == null) {
			throw new UserException("请输入您的原密码！", 1);
		} else if (!MD5.encode(getPwd()).equals(user.getPwd())) {
			throw new UserException("原密码错误！", 1);
		} else if (getPwd().equals(getNewPwd())) {
			throw new UserException("新密码不能和原密码相同！", 1);
		} else if (!getNewPwd().equals(getConfirmNewPwd())) {
			throw new UserException("新密码和确认密码不相同！", 1);
		}
		return "";
	}

	/**
	 * 验证用户支付密码
	 * 
	 * @param user
	 * @return
	 */
	public String validModifyPaypwdModel(User user) {
		if ((user.getPwd()).equals(MD5.encode(getNewPayPwd()))) {
			throw new UserException("支付密码不能和登录密码一样！", 1);
		}
		if (StringUtil.isNotBlank(user.getPayPwd())) {
			if (StringUtil.isBlank(getPayPwd())) {
				throw new UserException("请输入原支付密码！", 1);
			} else if (!MD5.encode(getPayPwd()).equals(user.getPayPwd())) {
				throw new UserException("原支付密码错误！", 1);
			} else if (getPayPwd().equals(getNewPayPwd())) {
				throw new UserException("新支付密码不能和原支付密码相同！", 1);
			}
		}
		if (!getNewPayPwd().equals(getConfirmNewPayPwd())) {
			throw new UserException("新支付密码和确认支付密码不相同！", 1);
		}
		return "";
	}

	/**
	 * 通过邮箱找回密码 第一步校验
	 * 
	 * @return
	 */
	public String validGetPwdByEmailStep1() {
		if (StringUtil.isBlank(getEmail())) {
			throw new UserException("邮箱不能为空！");
		}
		if (!ValidateUtil.isEmail(getEmail())) {
			throw new UserException("邮箱格式错误！");
		}
		if (this.validCode.isEmpty()) {
			throw new UserException(MessageUtil.getMessage("E30005"), 1);
		}
		if (!ValidateUtil.checkValidCode(getValidCode())) {
			throw new UserException(MessageUtil.getMessage("E30006"));
		}
		return "";
	}

	/**
	 * 通过邮箱找回密码 第二步校验
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validGetPwdByEmailStep2() throws Exception {
		if (!ValidateUtil.checkPwdCode("get_pwd_email_code", getUserName(),
				getUserId(), getEmail(), "get_pwd_email", getCode())) {
			throw new UserException("校验码错误或已失效，请重新获取！");
		}
		return "";
	}

	/**
	 * 通过邮箱找回交易密码 第二步校验
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validGetPayPwdByEmailStep2() throws Exception {
		if (!ValidateUtil.checkPwdCode("get_pay_pwd_email_code", getUserName(),
				getUserId(), getEmail(), "get_pay_pwd_email", getCode())) {
			throw new UserException("校验码错误或已失效，请重新获取！");
		}
		return "";
	}

	/**
	 * 通过手机找回密码 第一步校验
	 * 
	 * @return
	 */
	public String validGetPwdByPhoneStep1() {
		if (StringUtil.isBlank(getMobilePhone())) {
			throw new UserException("手机号码不能为空！", 1);
		}
		if (!ValidateUtil.isPhone(getMobilePhone())) {
			throw new UserException("手机号码格式错误！", 1);
		}
		if (this.validCode.isEmpty()) {
			throw new UserException(MessageUtil.getMessage("E30005"), 1);
		}
		if (!ValidateUtil.checkValidCode(getValidCode())) {
			throw new UserException(MessageUtil.getMessage("E30006"));
		}
		return "";
	}

	/**
	 * 通过手机找回密码 第二步校验
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validGetPwdByPhoneStep2() throws Exception {
		if (!ValidateUtil.checkPwdCode("get_pwd_phone_code", getUserName(),
				getUserId(), getMobilePhone(), "get_pwd_phone", getCode())) {
			throw new UserException("校验码错误或已失效，请重新获取！", 1);
		}
		return "";
	}

	/**
	 * 通过手机找回交易密码 第二步校验
	 * 
	 * @return
	 * @throws Exception
	 */
	public String validGetPayPwdByPhoneStep2() throws Exception {
		if (!ValidateUtil.checkPwdCode("get_pay_pwd_phone_code", getUserName(),
				getUserId(), getMobilePhone(), "get_pay_pwd_phone", getCode())) {
			throw new UserException("校验码错误或已失效，请重新获取！", 1);
		}
		return "";
	}

	/**
	 * 找回密码 校验密码重置
	 * 
	 * @return
	 */
	public int validGetPwdReset() {
		if (StringUtil.isBlank(getUserName())) {
			throw new UserException("用户名获取失败！", 1);
		}
		if (StringUtil.isBlank(getPwd())) {
			throw new UserException("密码不能为空！", 1);
		}
		if (StringUtil.isBlank(getConfirmNewPwd())) {
			throw new UserException("确认密码不能为空！", 1);
		}
		if (!getPwd().equals(getConfirmNewPwd())) {
			throw new UserException("两次输入的密码不一致！", 1);
		}
		return -1;
	}

	/**
	 * 找回交易密码 校验密码重置
	 * 
	 * @return
	 */
	public int validGetPayPwdReset() {
		if (StringUtil.isBlank(getUserName())) {
			throw new UserException("用户名获取失败！", 1);
		}
		if (StringUtil.isBlank(getPayPwd())) {
			throw new UserException("交易密码不能为空！", 1);
		}
		if (StringUtil.isBlank(getConfirmNewPwd())) {
			throw new UserException("确认密码不能为空！", 1);
		}
		if (!getPayPwd().equals(getConfirmNewPwd())) {
			throw new UserException("两次输入的密码不一致！", 1);
		}
		return -1;
	}

	/**
	 * 修改绑定邮箱/手机前进行用户密码和密保校验
	 */
	public void validUserQuestion(User user, UserPwdQuestion question) {
		if (StringUtil.isBlank(this.getPwd())) {
			throw new UserException("登陆密码不能为空！", 1);
		}
		if (StringUtil.isBlank(this.answer)) {
			throw new UserException("请回答密保问题！", 1);
		}
		if (!user.getPwd().equals(MD5.getMD5ofStr(this.getPwd()))) {
			throw new UserException("请输入正确的登陆密码！", 1);
		}
		if (!question.getAnswer().equals(this.answer)) {
			throw new UserException("密保回答不正确！", 1);
		}
	}

	/**
	 * 修改绑定手机校验
	 */
	@SuppressWarnings("unchecked")
	public void validModifyPhone() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = (Map<String, Object>) request.getSession()
				.getAttribute(NoticeConstant.NOTICE_MODIFY_PHONE + "_code");
		if (map == null || !this.code.equals(map.get("code").toString())) {
			throw new UserException("校验码不正确！", 1);
		}
		if (StringUtil.isBlank(this.code)) {
			throw new UserException("校验码为空！", 1);
		}

	}

	/**
	 * 修改绑定邮箱校验
	 */
	@SuppressWarnings("unchecked")
	public void validModifyEmail() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = (Map<String, Object>) request.getSession()
				.getAttribute(NoticeConstant.NOTICE_MODIFY_EMAIL + "_code");
		if (map == null || !this.code.equals(map.get("code").toString())) {
			throw new UserException("校验码不正确！", 1);
		}
		if (StringUtil.isBlank(this.code)) {
			throw new UserException("校验码为空！", 1);
		}

	}

	/**
	 * 绑定邮箱校验
	 */
	@SuppressWarnings("unchecked")
	public void validBindEmail() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = (Map<String, Object>) request.getSession()
				.getAttribute("bindEmail_code");
		UserIdentifyDao userIdentifyDao = (UserIdentifyDao) BeanUtil
				.getBean("userIdentifyDao");
		int count = userIdentifyDao.countByEmail(getEmail());
		if (count > 0) {
			throw new UserException("该邮箱已被使用！", 1);
		}

		if (map == null || !this.code.equals(map.get("code").toString())) {
			throw new UserException("校验码不正确！", 1);
		}
		if (StringUtil.isBlank(this.code)) {
			throw new UserException("校验码为空！", 1);
		}
		if (StringUtil.isBlank(this.getEmail())) {
			throw new UserException("邮箱为空！", 1);
		}
		if (!StringUtil.isEmail(this.getEmail())) {
			throw new UserException("邮箱格式不正确！", 1);
		}
	}

	/**
	 * 绑定手机校验
	 */
	@SuppressWarnings("unchecked")
	public void validBindPhone() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = ((Map<String, Object>) request.getSession()
				.getAttribute("bindPhone_code"));
		UserIdentifyDao userIdentifyDao = (UserIdentifyDao) BeanUtil
				.getBean("userIdentifyDao");
		int count = userIdentifyDao.countByMobilePhone(getMobilePhone());
		if (count > 0) {
			throw new UserException("该手机号码已被使用！", 1);
		}

		if (map == null || !this.code.equals(map.get("code").toString())) {
			throw new UserException("校验码不正确！", 1);
		}
		if (StringUtil.isBlank(this.code)) {
			throw new UserException("校验码为空！", 1);
		}
		if (StringUtil.isBlank(this.getMobilePhone())) {
			throw new UserException("手机号码为空！", 1);
		}
		if (!StringUtil.isPhone(this.getMobilePhone())) {
			throw new UserException("手机号码格式不正确！", 1);
		}
	}

	/**
	 * 注册手机校验
	 */
	@SuppressWarnings("unchecked")
	public void validRegisterPhone() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = ((Map<String, Object>) request.getSession()
				.getAttribute("registerGetCode_code"));
		UserIdentifyDao userIdentifyDao = (UserIdentifyDao) BeanUtil
				.getBean("userIdentifyDao");
		int count = userIdentifyDao.countByMobilePhone(getMobilePhone());
		if (count > 0) {
			throw new UserException("该手机号码已被使用！", 1);
		}
		if (!this.code.equals("999999")) {
			if (map == null || !this.code.equals(map.get("code").toString())) {
				throw new UserException("校验码不正确！", 2);
			}
		}
		if (StringUtil.isBlank(this.code)) {
			throw new UserException("校验码为空！", 1);
		}
		if (StringUtil.isBlank(this.getMobilePhone())) {
			throw new UserException("手机号码为空！", 1);
		}
		if (!StringUtil.isPhone(this.getMobilePhone())) {
			throw new UserException("手机号码格式不正确！", 1);
		}
	}

	/**
	 * 验证找回密码手机验证码
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String validGetPwdCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = ((Map<String, Object>) request.getSession()
				.getAttribute("get_pwd_phone_code"));

		if (!this.code.equals("999999")) {
			if (map == null || !this.code.equals(map.get("code").toString())) {
				return "校验码不正确！";
			}
		}
		if (StringUtil.isBlank(this.code)) {
			return "校验码为空！";
		}
		if (StringUtil.isBlank(this.getMobilePhone())) {
			return "手机号码为空！";
		}
		if (!StringUtil.isPhone(this.getMobilePhone())) {
			return "手机号码格式不正确！";
		}

		return "success";
	}

	/**
	 * 注册手机验证码校验
	 */
	@SuppressWarnings("unchecked")
	public String validRegisterTelCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = ((Map<String, Object>) request.getSession()
				.getAttribute("registerGetCode_code"));

		if (!this.code.equals("999999")) {
			if (map == null || !this.code.equals(map.get("code").toString())) {
				return "校验码不正确！";
			}
		}
		if (StringUtil.isBlank(this.code)) {
			return "校验码为空！";
		}
		if (StringUtil.isBlank(this.getMobilePhone())) {
			return "手机号码为空！";
		}
		if (!StringUtil.isPhone(this.getMobilePhone())) {
			return "手机号码格式不正确！";
		}

		return "success";
	}

	/**
	 * 注册手机验证码校验
	 */
	public String validCfRegisterTelCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String sessionCode = (String) request.getSession().getAttribute(
				"cfRegisterCode");
		String cfSessionRegisterMobilePhone = (String) request.getSession()
				.getAttribute("cfSessionRegisterMobilePhone");

		if (!this.code.equals(sessionCode)) {
			return "验证码错误！";
		}
		if (StringUtil.isBlank(this.code)) {
			return "校验码为空！";
		}
		if (StringUtil.isBlank(this.getMobilePhone())) {
			return "手机号码为空！";
		}
		if (!StringUtil.isPhone(this.getMobilePhone())) {
			return "手机号码格式不正确！";
		}
		if (!this.getMobilePhone().equals(cfSessionRegisterMobilePhone)) {
			return "手机号码与验证码不匹配！";
		}

		return "success";
	}

	/**
	 * 校验手机验证码
	 */
	@SuppressWarnings("unchecked")
	public String validMobileCode(String codeName) {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = ((Map<String, Object>) request.getSession()
				.getAttribute(codeName));

		if (!this.code.equals("999999")) {
			if (map == null || !this.code.equals(map.get("code").toString())) {
				return "校验码不正确！";
			}
		}
		if (StringUtil.isBlank(this.code)) {
			return "校验码为空！";
		}
		if (StringUtil.isBlank(this.getMobilePhone())) {
			return "手机号码为空！";
		}
		if (!StringUtil.isPhone(this.getMobilePhone())) {
			return "手机号码格式不正确！";
		}

		return "success";
	}

	/**
	 * 绑定手机验证码校验
	 */
	@SuppressWarnings("unchecked")
	public String validBindTelCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = ((Map<String, Object>) request.getSession()
				.getAttribute("bindPhoneCode_code"));

		if (!this.code.equals("999999")) {
			if (map == null || !this.code.equals(map.get("code").toString())) {
				return "校验码不正确！";
			}
		}
		if (StringUtil.isBlank(this.code)) {
			return "校验码为空！";
		}
		if (StringUtil.isBlank(this.getMobilePhone())) {
			return "手机号码为空！";
		}
		if (!StringUtil.isPhone(this.getMobilePhone())) {
			return "手机号码格式不正确！";
		}

		return "success";
	}

	/**
	 * 验证设置密保问题的数据
	 */
	public void validForm() {
		if (StringUtil.isBlank(this.answer1)) {
			throw new UserException("第一个问题答案不能为空！", 1);
		} else if (StringUtil.isBlank(this.answer2)) {
			throw new UserException("第二个问题答案不能为空！", 1);
		} else if (StringUtil.isBlank(this.answer3)) {
			throw new UserException("第三个问题答案不能为空！", 1);
		} else if ("-1".equals(question1)) {
			throw new UserException("第一个问题不能为空！", 1);
		} else if ("-1".equals(question2)) {
			throw new UserException("第二个问题不能为空！", 1);
		} else if ("-1".equals(question3)) {
			throw new UserException("第三个问题不能为空！", 1);
		} else if (question1.equals(question2) || question1.equals(question3)
				|| question2.equals(question3)) {
			throw new UserException("密保问题重复，请重新设置！", 1);
		}
	}

	/**
	 * 验证上传文件
	 */
	public void validAttestationCommit(File file) {
		if (file == null) {
			throw new UserException("你上传的图片为空！", 1);
		} else if (file.length() > 1024 * 1024) {
			throw new UserException("您上传的图片大小不能超过1M,请重新上传！", 1);
		} else if (!ImageUtil.fileIsImage(file)) {
			throw new UserException("您上传的图片无效，请重新上传！", 1);
		}
	}

	/**
	 * getter & setter
	 * 
	 * @return
	 */
	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getConfirmNewPwd() {
		return confirmNewPwd;
	}

	public void setConfirmNewPwd(String confirmNewPwd) {
		this.confirmNewPwd = confirmNewPwd;
	}

	public String getNewPayPwd() {
		return newPayPwd;
	}

	public void setNewPayPwd(String newPayPwd) {
		this.newPayPwd = newPayPwd;
	}

	public String getConfirmNewPayPwd() {
		return confirmNewPayPwd;
	}

	public void setConfirmNewPayPwd(String confirmNewPayPwd) {
		this.confirmNewPayPwd = confirmNewPayPwd;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public File getCardPositive() {
		return cardPositive;
	}

	public void setCardPositive(File cardPositive) {
		this.cardPositive = cardPositive;
	}

	public File getCardOpposite() {
		return cardOpposite;
	}

	public void setCardOpposite(File cardOpposite) {
		this.cardOpposite = cardOpposite;
	}

	public int getQuestion() {
		return question;
	}

	public void setQuestion(int question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public UserIdentify getUserIdentify() {
		return userIdentify;
	}

	public void setUserIdentify(UserIdentify userIdentify) {
		this.userIdentify = userIdentify;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestion1() {
		return question1;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public String getQuestion2() {
		return question2;
	}

	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	public String getQuestion3() {
		return question3;
	}

	public void setQuestion3(String question3) {
		this.question3 = question3;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public UserCache getUserCache() {
		return userCache;
	}

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getBusinessRegistrationNumber() {
		return businessRegistrationNumber;
	}

	public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
		this.businessRegistrationNumber = businessRegistrationNumber;
	}

	public int getUserNature() {
		return userNature;
	}

	public void setUserNature(int userNature) {
		this.userNature = userNature;
	}

	public User getInviteUser() {
		return inviteUser;
	}

	public void setInviteUser(User inviteUser) {
		this.inviteUser = inviteUser;
	}

	public String getInviteUserName() {
		return inviteUserName;
	}

	public void setInviteUserName(String inviteUserName) {
		this.inviteUserName = inviteUserName;
	}

	public String getInviteRealName() {
		return inviteRealName;
	}

	public void setInviteRealName(String inviteRealName) {
		this.inviteRealName = inviteRealName;
	}

	public Date getInviteTime() {
		return inviteTime;
	}

	public void setInviteTime(Date inviteTime) {
		this.inviteTime = inviteTime;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public double getBorrowVoucherTotal() {
		return borrowVoucherTotal;
	}

	public void setBorrowVoucherTotal(double borrowVoucherTotal) {
		this.borrowVoucherTotal = borrowVoucherTotal;
	}

	public int getLoginPwdStatus() {
		return loginPwdStatus;
	}

	public void setLoginPwdStatus(int loginPwdStatus) {
		this.loginPwdStatus = loginPwdStatus;
	}

	public int getPayPwdStatus() {
		return payPwdStatus;
	}

	public void setPayPwdStatus(int payPwdStatus) {
		this.payPwdStatus = payPwdStatus;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getRealNameStatus() {
		return realNameStatus;
	}

	public void setRealNameStatus(int realNameStatus) {
		this.realNameStatus = realNameStatus;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getSexStr() {
		switch (getSex()) {
		case 0:
			sexStr = "女";
			break;
		case 1:
			sexStr = "男";
			break;
		default:
			sexStr = "状态异常";
			break;
		}
		return sexStr;
	}

	public void setSexStr(String sexStr) {
		this.sexStr = sexStr;
	}

	public String getRealNameStatusStr() {
		switch (getRealNameStatus()) {
		case 0:
			realNameStatusStr = "未认证";
			break;
		case 1:
			realNameStatusStr = "认证通过";
			break;
		case 2:
			realNameStatusStr = "认证待审";
			break;
		case -1:
			realNameStatusStr = "认证未通过";
			break;
		default:
			realNameStatusStr = "状态异常";
			break;
		}
		return realNameStatusStr;
	}

	public void setRealNameStatusStr(String realNameStatusStr) {
		this.realNameStatusStr = realNameStatusStr;
	}

	public String getStatusStr() {
		switch (getStatus()) {
		case 0:
			statusStr = "未锁定";
			break;
		case 1:
			statusStr = "锁定";
			break;
		default:
			statusStr = "状态异常";
			break;
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getHeaderImg() {
		return headerImg;
	}

	public void setHeaderImg(String headerImg) {
		this.headerImg = headerImg;
	}



}
