package com.rongdu.p2psys.cooperation.service.impl;

import java.net.URLDecoder;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.util.RSAUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.cooperation.dao.CooperationLoginDao;
import com.rongdu.p2psys.cooperation.domain.CooperationLogin;
import com.rongdu.p2psys.cooperation.service.CooperationLoginService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.rule.RsaFormEncryptRuleCheck;
import com.rongdu.p2psys.core.util.mail.Mail;
import com.rongdu.p2psys.score.dao.ScoreDao;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserCreditDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.login.UserLoginComposite;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

@Service("cooperationLoginService")
public class CooperationLoginServiceImpl implements CooperationLoginService {

	@Resource
	private CooperationLoginDao cooperationLoginDao;
	@Resource
	private UserDao userDao;
	@Resource
	private UserIdentifyDao userIdentifyDao;
	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private AccountSumDao accountSumDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private UserCreditDao userCreditDao;
	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private ScoreDao scoreDao;

	public void addCooperationLogin(CooperationLogin cooperation) {
		// 添加联合登陆信息时，须先判断是否已有联合登陆的记录信息，如果无，则添加
		CooperationLogin cooperationLogin = cooperationLoginDao
				.getCooperationLogin(cooperation.getOpenId(),
						cooperation.getType());
		if (cooperationLogin == null) {
			cooperationLoginDao.save(cooperation);
		}

	}

	public CooperationLogin getCooperationLogin(String openId, int type) {
		return cooperationLoginDao.getCooperationLogin(openId, type);
	}

	public User doQQRegister(User user, String openType, String openId) {
		user = this.doRegister(user);
		CooperationLogin coop = cooperationLoginDao.getCooperationLogin(openId,
				Integer.parseInt(openType));
		coop.setUserId(user.getUserId());
		cooperationLoginDao.update(coop);
		return user;
	}

	@Override
	public User doQQLogin(User user, int isRsa, String openType, String openId)
			throws Exception {
		RsaFormEncryptRuleCheck rsaCheck = (RsaFormEncryptRuleCheck) Global
				.getRuleCheck("rsaFormEncrypt");
		if (rsaCheck.enable_formEncrypt && isRsa == 1) {
			user.setPwd(RSAUtil.getRSADecrypt(user.getPwd()));
		}
		String pwd = user.getPwd();
		pwd = URLDecoder.decode(user.getPwd(), "utf-8");
		user.setPwd(pwd);
		User u = null;
		UserLoginComposite composite = new UserLoginComposite();
		u = composite.login(user);
		CooperationLogin coop = cooperationLoginDao.getCooperationLogin(openId,
				Integer.parseInt(openType));
		coop.setUserId(u.getUserId());
		cooperationLoginDao.update(coop);
		return u;
	}

	public User doRegister(User user) {
		int hasEmail = userService.countByEmail(user.getEmail());
		if (hasEmail > 0)
			throw new UserException("邮箱已经被使用.", 1);

		int hasUsername = userService.countByUserName(user.getUserName());
		if (hasUsername > 0)
			throw new UserException("用户名已经被使用.", 1);

		user.setAddTime(new Date());
		user = userDao.save(user);

		// 用户认证信息
		userIdentifyDao.save(new UserIdentify(user));
		// 用户附属信息
		userCacheDao.save(new UserCache(user, Global.getIP()));
		// 资金账户
		accountDao.save(new Account(user));
		// 资金合计
		accountSumDao.save(new AccountSum(user));
		// 用户amount
		userCreditDao.save(new UserCredit(user));
		// 添加用户积分信息
		scoreDao.save(new Score(user));

		// 发送激活邮件
		Global.setTransfer("activeUrl", "/user/doRegisterStep1.html?id="
				+ Mail.getInstance().getdecodeIdStr(user));
		Global.setTransfer("user", user);
		AbstractExecuter executer = ExecuterHelper
				.doExecuter("userRegisterExecuter");
		executer.execute(0, user);
		return user;
	}
}
