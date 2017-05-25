package com.rongdu.p2psys.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.octo.captcha.service.CaptchaServiceException;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.jcaptcha.CaptchaServiceSingleton;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.NoticeDao;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 数据校验工具类
 * 
 * @author xx
 * @version 2.0
 * @since 2014年1月8日
 */
public class ValidateUtil
{
	private static Map<String, Object> map;

	/**
	 * 校验_验证码
	 * 
	 * @param validCode
	 *            验证码
	 * @return 校验结果 true：通过 false：不通过
	 */
	public static boolean checkValidCode(String validCode)
	{
		boolean b = false;
		// Test LoadRunner测试用万能验证码
		// if ("1".equals("1")) {
		// return true;
		// }
		HttpServletRequest request = ServletActionContext.getRequest();
		try
		{
			b = CaptchaServiceSingleton.getInstance().validateResponseForID(
					request.getSession().getId(), validCode.toLowerCase());
		} catch (CaptchaServiceException e)
		{
			b = false;

			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 校验_验证码
	 * 
	 * @param key
	 * @param userName
	 *            用户名
	 * @param userId
	 *            用户ID
	 * @param email
	 *            电子邮件
	 * @param todo
	 * @param code
	 * @return 校验结果 true：通过 false：不通过
	 * @throws Exception
	 */
	public static boolean checkPwdCode(String key, String userName,
			Long userId, String addr, String todo, String code)
			throws Exception
	{
		boolean b = false;
		HttpServletRequest request = ServletActionContext.getRequest();
		Object obj = request.getSession().getAttribute(key);
		if (obj == null)
		{
			return b;
		}
		NoticeDao noticeDao = (NoticeDao) BeanUtil.getBean("noticeDao");
		// 发送校验码时间取得
		long verifyTime = noticeDao.getNoticeAddTimeByUserId(userId, addr);
		// 验证码有效期取得
		long vtime = StringUtil.toLong(Global.getString("verify_code_time"));
		// 验证码超过有效期的场合
		if (System.currentTimeMillis() - verifyTime > vtime * 60 * 1000)
		{
			return b;
		}
		Map<String, Object> map = (HashMap<String, Object>) obj;
		String userName_ = StringUtil.isNull(map.get("userName"));
		String todo_ = StringUtil.isNull(map.get("todo"));
		String code_ = StringUtil.isNull(map.get("code"));
		if (StringUtil.isNotBlank(userName_) && userName_.equals(userName)
				&& StringUtil.isNotBlank(todo_) && todo_.equals(todo)
				&& StringUtil.isNotBlank(code_) && code_.equals(code))
		{
			b = true;
			request.getSession().removeAttribute(key);
		}
		return b;
	}

	/**
	 * 校验_验证码
	 * 
	 * @param key
	 * @param userName
	 *            用户名
	 * @param userId
	 *            用户ID
	 * @param email
	 *            电子邮件
	 * @param todo
	 * @param code
	 * @return 校验结果 true：通过 false：不通过
	 * @throws Exception
	 */
	public static boolean checkCode(String key, String userName, Long userId,
			String email, String todo, String code) throws Exception
	{
		boolean b = false;
		HttpServletRequest request = ServletActionContext.getRequest();
		Object obj = request.getSession().getAttribute(key);
		if (obj == null)
		{
			return b;
		}
		NoticeDao noticeDao = (NoticeDao) BeanUtil.getBean("noticeDao");
		// 发送校验码时间取得
		long verifyTime = noticeDao.getAddTimeByUserId(userId, email);
		// 验证码有效期取得
		long vtime = StringUtil.toLong(Global.getString("verify_code_time"));
		// 验证码超过有效期的场合
		if (System.currentTimeMillis() - verifyTime > vtime * 1000)
		{
			return b;
		}
		Map<String, Object> map = (HashMap<String, Object>) obj;
		String userName_ = StringUtil.isNull(map.get("userName"));
		String todo_ = StringUtil.isNull(map.get("todo"));
		String code_ = StringUtil.isNull(map.get("code"));
		if (StringUtil.isNotBlank(userName_) && userName_.equals(userName)
				&& StringUtil.isNotBlank(todo_) && todo_.equals(todo)
				&& StringUtil.isNotBlank(code_) && code_.equals(code))
		{
			b = true;
			request.getSession().removeAttribute(key);
		}
		return b;
	}

	/**
	 * 校验用户名 规则：数字与字母组合，字母，汉字，4-16位(?![a-zA-Z]+$)
	 * 
	 * @param userName
	 *            用户名
	 * @return 校验结果 true：通过 false：不通过
	 */
	public static boolean isUserName(String userName)
	{
		if (userName.length() < 4 || userName.length() > 16)
		{
			throw new UserException("用户名长度必须是4-16位！", 1);
		}
		Pattern p = Pattern
				.compile("^(?![0-9]+$)[0-9A-Za-z\u0391-\uFFE5]{4,16}$");
		Matcher m = p.matcher(userName);
		return m.matches();
	}

	/**
	 * 校验是否中文
	 * 
	 * @param str
	 *            字符串
	 * @return 校验结果 true：通过 false：不通过
	 */
	public static boolean isChinese(String str)
	{
		Pattern regex = Pattern.compile("[\\u4e00-\\u9fa5]{2,25}");
		Matcher matcher = regex.matcher(StringUtil.isNull(str));
		return matcher.matches();
	}

	/**
	 * 校验Email格式
	 * 
	 * @param email
	 *            输入邮箱
	 * @return 校验结果 true：通过 false：不通过
	 */
	public static boolean isEmail(String email)
	{
		Pattern regex = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = regex.matcher(StringUtil.isNull(email));
		return matcher.matches();
	}

	/**
	 * 校验手机号格式
	 * 
	 * @param phone
	 *            手机号
	 * @return 校验结果 true：通过 false：不通过
	 */
	public static boolean isPhone(String phone)
	{
		Pattern regex = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher matcher = regex.matcher(StringUtil.isNull(phone));
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 校验手机号
	 * 
	 * @param phone
	 *            手机号
	 * @return 校验结果和消息
	 */
	public static Map<String, Object> checkPhone(String phone)
	{
		map = new HashMap<String, Object>();
		map.put("result", false);
		if (StringUtil.isBlank(phone))
		{
			map.put("message", "请输入手机号！");
			return map;
		}
		if (!isPhone(phone))
		{
			map.put("message", "请输入正确的11位手机号！");
		} else
		{
			map.put("result", true);
		}
		return map;
	}

	/**
	 * 校验手机号格式与是否已经存在
	 * 
	 * @param phone
	 *            手机号
	 * @return 校验结果和消息
	 */
	public static void checkPhoneExist(String phone)
	{
		Pattern regex = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher matcher = regex.matcher(StringUtil.isNull(phone));
		boolean isMatched = matcher.matches();
		if (!isMatched)
		{
			throw new UserException("您输入的手机号格式不对");
		}
		UserService userService = (UserService) BeanUtil.getBean("userService");
		if (userService.countByMobilePhone(phone) > 1)
		{
			throw new UserException("您输入的手机号已存在");
		}
	}

	/**
	 * 校验邮箱是否已存在
	 * 
	 * @param email
	 */
	public static void checkEmailExist(String email, long userId)
	{
		UserService userService = (UserService) BeanUtil.getBean("userService");
		if (userService.countByEmail(email, userId) > 0)
		{
			throw new UserException("您输入的邮箱已存在", 2);
		}

	}

	/**
	 * 校验营业执照编号是否已存在
	 * 
	 * @param businessRegistrationNumber
	 */
	public static void checkBusinessRegistrationNumber(
			String businessRegistrationNumber, long userId)
	{
		UserCacheDao cacheDao = (UserCacheDao) BeanUtil.getBean("userCacheDao");
		if (cacheDao.countByBusinessRegistrationNumber(
				businessRegistrationNumber, userId) > 0)
		{
			throw new UserException("您输入的营业执照已存在", 2);
		}
	}

}
