package com.rongdu.p2psys.user.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountBankDao;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.core.util.mail.Mail;
import com.rongdu.p2psys.score.dao.ScoreDao;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderScorePhoneLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderScoreRealnameLog;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.domain.YjfPay;
import com.rongdu.p2psys.tpp.ips.model.IpsRegister;
import com.rongdu.p2psys.tpp.yjf.YjfType;
import com.rongdu.p2psys.tpp.yjf.dao.YjfDao;
import com.rongdu.p2psys.tpp.yjf.model.ForwardConIdentify;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserCreditDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.dao.UserUploadDao;
import com.rongdu.p2psys.user.dao.UserVipApplyDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserUpload;
import com.rongdu.p2psys.user.domain.UserVipApply;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.identify.UserIdentifyFactory;
import com.rongdu.p2psys.user.model.identify.UserIdentifyWay;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserService;

@Service("userCacheService")
public class UserCacheServiceImpl implements UserCacheService
{

	Logger logger = Logger.getLogger(UserCacheServiceImpl.class);

	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private UserIdentifyDao userIdentifyDao;
	@Resource
	private UserVipApplyDao userVipApplyDao;
	@Resource
	private YjfDao yjfDao;
	@Resource
	private AccountBankDao accountBankDao;
	@Resource
	private AccountSumDao accountSumDao;
	@Resource
	private UserCreditDao userCreditDao;
	@Resource
	private ScoreDao scoreDao;
	@Resource
	private UserUploadDao userUploadDao;
	@Resource
	private UserService userService;

	@Override
	public UserCacheModel getUserCache(long userId)
	{
		return userCacheDao.getUserCache(userId);
	}

	public UserCache findByUserId(long userId)
	{
		return userCacheDao.findObjByProperty("user.userId", userId);
	}

	@Override
	public String getUserCard_id(long userId)
	{
		return "";
		// return userCacheDao.getUserCard_id(userId);
	}

	@Override
	public Object doRealname(User user, UserModel model) throws Exception
	{
		/*
		 * if (u.getApiStatus()==1 && BaseTPPWay.isOpenApi()) { throw new
		 * BussinessException
		 * ("已实名认证,请勿重复认证","/member/security/realNameIdentify.html"); }
		 */
		model = makeModel(model, user);
		UserIdentifyFactory attestationFactory = new UserIdentifyFactory(
				user.getUserId(), model);
		UserIdentifyWay attestationWay = attestationFactory.realname();
		Object o = attestationWay.doRealname();
		if (BaseTPPWay.isOpenApi() && TPPWay.API_CODE == TPPWay.API_CODE_YJF)
		{
			ForwardConIdentify fci = (ForwardConIdentify) o;
			YjfPay yp = new YjfPay(null, null, null, YjfType.REALNAME,
					fci.getOrderNo(), fci.getService(), null, null, null,
					user.getUserId() + "");
			yjfDao.save(yp);
		}
		return o;

	}

	@Override
	public Object doCorpRegister(User user, UserModel model) throws Exception
	{
		/*
		 * if (u.getApiStatus()==1 && BaseTPPWay.isOpenApi()) { throw new
		 * BussinessException
		 * ("已申请企业开户,请勿重复认证","/member/security/realNameIdentify.html"); }
		 */
		model = makeModel(model, user);
		UserIdentifyFactory attestationFactory = new UserIdentifyFactory(
				user.getUserId(), model);
		UserIdentifyWay attestationWay = attestationFactory.corpRegister();
		Object o = attestationWay.doCorpRegister();
		return o;
	}

	private static UserModel makeModel(UserModel model, User user)
	{
		if (TPPWay.API_CODE == TPPWay.API_CODE_YJF)
		{
			model.setMobilePhone(user.getMobilePhone());
		}
		model.setUserName(user.getUserName());
		model.setEmail(user.getEmail());
		model.setUserId(user.getUserId());
		return model;
	}

	@Override
	public void modify(long userId, String card_id)
	{
		userCacheDao.modify(userId, card_id);
	}

	@Override
	public void modify(long userId, String card_id, String card_positive,
			String card_opposite)
	{
		userCacheDao.modify(userId, card_id, card_positive, card_opposite);
	}

	@Override
	public void modify(long userId, UserModel model, String cardPositive,
			String cardOpposite)
	{
		String cardId = model.getCardId();
		if (!StringUtil.isCard(model.getCardId()))
		{
			throw new UserException("身份证格式不对，请检查！", 1);
		}
		char sexNum;
		if (cardId.length() == 15)
		{
			sexNum = cardId.charAt(cardId.length() - 1);
		} else
		{
			sexNum = cardId.charAt(cardId.length() - 2);
		}
		// 获取性别 1:男,0:女
		int sex = sexNum % 2;

		User user = userService.getUserById(userId);
		user.setRealName(model.getRealName());
		user.setCardId(cardId);
		UserCache userCache = user.getUserCache();
		userCache.setCardOpposite(cardOpposite);
		userCache.setCardPositive(cardPositive);
		userCache.setSex(sex);
		userCacheDao.update(userCache);
	}

	@Override
	public void userLockEdit(long userId, int status)
	{
		userCacheDao.userLockEdit(userId, status);
	}

	/**
	 * 申请vip业务处理
	 */
	@Override
	public UserIdentify applyVip(User user)
	{
		logger.debug("vip条件判断");

		double vipFee = Global.getDouble("vip_fee");
		Account account = accountDao.getAccountByUserId(user.getUserId());

		UserIdentify userIdentify = userIdentifyDao.findByUserId(user
				.getUserId());
		if (userIdentify.getVipStatus() == 1)
		{
			throw new BussinessException("您已经是vip用户，请您查看", 1);
		}
		if (userIdentify.getVipStatus() == 2)
		{
			throw new BussinessException("您申请的vip审核，正在审核中，请您查看", 1);
		}
		if (account.getUseMoney() < vipFee)
		{
			throw new BussinessException("余额不足，请您充值", 1);
		}

		logger.debug("用户资金、操作日志、通知处理");
		AbstractExecuter executer = ExecuterHelper
				.doExecuter("applyVipExecuter");
		executer.execute(vipFee, user, null);

		logger.debug("进入vip申请业务处理...");
		UserVipApply userVipApply = new UserVipApply();
		userVipApply.setAddIp(Global.IP_THREADLOCAL.get());
		userVipApply.setRemark(Global.getTransfer().get("vipRemark") + "");// 备注
		userVipApply.setAddTime(new Date());
		userVipApply.setMoney(vipFee);
		userVipApply.setStatus((byte) 0);
		userVipApply.setUser(user);
		userVipApplyDao.save(userVipApply);

		userIdentify.setVipStatus(2);// 待审核状态
		userIdentifyDao.update(userIdentify);

		return userIdentify;
	}

	public void verifyVip(long userId)
	{
		// ApplyVipLog
		// "verfiVip"="com.rongdu.p2psys.user.executor.ApplyVipExecuter"
		// "verfiVip"="com.rongdu.p2psys.user.executor.CustomApplyVipExecuter"
		ExecuterHelper.doExecuter("verfiVip");
		// VerfiVipVipExecuter e=new VerfiVipyVipExecuter();
		// e.execute();
	}

	@Override
	public void update(UserCache userCache)
	{
		userCacheDao.merge(userCache);
	}

	@Override
	public void update(UserCache userCache, List<UserUpload> list, long[] delIds)
	{
		userUploadDao.save(list);
		userCacheDao.merge(userCache);
		if (delIds != null)
		{
			for (long id : delIds)
			{
				UserUpload uu = userUploadDao.find(id);
				userUploadDao.delete(id);
				String realPath = ServletActionContext.getServletContext()
						.getRealPath(uu.getPicPath());
				new File(realPath).delete();
			}
		}
	}

	@Override
	public PageDataList<UserCache> userCacheList(int pageNumber, int pageSize,
			UserCache model)
	{
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		PageDataList<UserCache> plist = userCacheDao.findPageList(param);
		return plist;
	}

	@Override
	public void updateStatus()
	{
		userCacheDao.updateStatus();
	}

	@Override
	public void ipsRegisterCall(User user, IpsRegister ips)
	{

		// if (user.getApiStatus() != 1) {
		user.setMobilePhone(ips.getMobileNo());
		// userDao.modifyPhone(user.getUserId(), ips.getMobileNo());
		user.setRealName(ips.getRealName());
		// userDao.modifyRealname(user.getUserId(), ips.getRealName());
		if (user.getUserCache().getUserNature() != 1)
		{
			user.setUserName(ips.getRealName());
			// userDao.modifyUsername(user.getUserId(), ips.getRealName());
		}
		user.setCardId(ips.getIdentNo());
		// userDao.modify(user.getUserId(), ips.getIdentNo());
		// user.setApiId(ips.getIpsAcctNo());
		// user.setApiUsercustId(ips.getEmail());
		// user.setApiStatus(1);
		userService.updateUserInfo(user);
		// userDao.modifyApi(user.getUserId(), ips.getIpsAcctNo(),
		// "1",ips.getEmail());
		userIdentifyDao.modifyMobilePhoneStatus(user.getUserId(), 1);
		userIdentifyDao.modifyRealnameStatus(user.getUserId(), 1);
		String cardId = ips.getIdentNo();
		int length = cardId.length();
		String sexNum;
		if (length == 15)
		{
			sexNum = cardId.substring(length - 1);
		} else
		{
			sexNum = cardId.substring(length - 2, length - 1);
		}
		// 获取性别 1:男,0:女
		int sex = Integer.parseInt(sexNum) % 2;
		UserCache userCache = user.getUserCache();
		userCache.setSex(sex);
		userCacheDao.update(userCache);
		BaseScoreLog bLog = new TenderScorePhoneLog(user.getUserId());
		bLog.doEvent();
		BaseScoreLog bLog_ = new TenderScoreRealnameLog(user.getUserId());
		bLog_.doEvent();
		AccountBank bank = new AccountBank();
		bank.setUser(user);
		bank.setBankNo(ips.getBkAccNo());
		bank.setBank(ips.getBankName());
		bank.setAddTime(new Date());
		bank.setStatus(1);
		accountBankDao.save(bank);
		// 用户第三方资金托管配置信息初始化
		TPPWay way = TPPFactory.getTPPWay();
		if (way != null)
		{
			way.addUserTppConfig(user.getUserId());
		}
		// }
	}

	@Override
	public void save(UserCache model, List<UserUpload> list)
	{
		User user = model.getUser();
		checkInfo(model);
		user.setRealName(user.getRealName().trim());
		user.setUserName(user.getRealName());
		user.setAddTime(new Date());
		// 用户附属信息
		userCacheDao.save(model);
		// 后台开户实物照信息
		for (UserUpload uu : list)
		{
			uu.setUser(user);
			userUploadDao.save(uu);
		}
		// 用户认证信息
		userIdentifyDao.save(new UserIdentify(user));
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
	}

	/**
	 * 校验后台开户
	 * 
	 * @param model
	 */
	private void checkInfo(UserCache model)
	{
		User user = model.getUser();
		int count = userService.countByMobilePhone(user.getMobilePhone());
		if (count > 0)
		{
			throw new UserException("该手机号码已被使用！", 1);
		}
		if (!StringUtil.isPhone(user.getMobilePhone()))
		{
			throw new UserException("手机号码格式不正确！", 1);
		}
		if (!ValidateUtil.isEmail(user.getEmail()))
		{
			throw new UserException("借款人（负责人）邮箱格式错误！", 1);
		}
		int hasEmail = userService.countByEmail(user.getEmail());
		if (hasEmail > 0)
		{
			throw new UserException("借款人邮箱已经被使用！", 1);
		}
		if (!"".equals(model.getCompanyEmail()))
		{
			if (!ValidateUtil.isEmail(model.getCompanyEmail()))
			{
				throw new UserException("公司邮箱格式错误！", 1);
			}
			int hasCompanyEmail = userService.countByEmail(model
					.getCompanyEmail());
			if (hasCompanyEmail > 0)
			{
				throw new UserException("公司邮箱已经被使用！", 1);
			}
		}
		if (!StringUtil.isCard(user.getCardId()))
		{
			throw new UserException("身份证格式不对，请检查！", 1);
		}
	}

	@Override
	public PageDataList<UserCacheModel> userList(int pageNumber, int pageSize,
			UserCache model)
	{
		QueryParam param = QueryParam.getInstance();
		if (!StringUtil.isBlank(model.getCompanyName()))
		{
			param.addParam("companyName", Operators.EQ, model.getCompanyName());
		}
		if (!StringUtil.isBlank(model.getFrdbName()))
		{
			param.addParam("frdbName", Operators.EQ, model.getFrdbName());
		}
		if (model.getStatus() != 99)
		{
			param.addParam("status", model.getStatus());
		}
		param.addPage(pageNumber, pageSize);
		param.addParam("userNature", model.getUserNature());
		param.addOrder(OrderType.DESC, "id");
		PageDataList<UserCache> plist = userCacheDao.findPageList(param);
		PageDataList<UserCacheModel> uList = new PageDataList<UserCacheModel>();
		uList.setPage(plist.getPage());
		List<UserCacheModel> list = new ArrayList<UserCacheModel>();
		if (plist.getList().size() > 0)
		{
			for (int i = 0; i < plist.getList().size(); i++)
			{
				UserCache userCache = (UserCache) plist.getList().get(i);
				UserCacheModel userCacheModel = UserCacheModel
						.instance(userCache);
				userCacheModel.setEmail(userCache.getUser().getEmail());
				userCacheModel.setContactsCardId(userCache.getUser()
						.getCardId());
				userCacheModel.setContactsRealName(userCache.getUser()
						.getRealName());
				userCacheModel.setContactsPhone(userCache.getUser()
						.getMobilePhone());
				if (userCache.getUser().getPwd() != null
						&& !"".equals(userCache.getUser().getPwd()))
				{
					userCacheModel.setEmailStatus(true);
				}
				list.add(userCacheModel);
			}
		}
		uList.setList(list);

		return uList;
	}

	@Override
	public UserCache findById(long id)
	{

		return userCacheDao.find(id);
	}

	@Override
	public void modifyPwdTime(long userId)
	{

		userCacheDao.modifyPwdTime(userId);
	}

	@Override
	public void modifyPayPwdTime(long userId)
	{

		userCacheDao.modifyPayPwdTime(userId);
	}

	@Override
	public boolean doLock(HttpServletRequest request, long userId, String type)
	{
		if (UserCacheModel.PWD_LOCK.equals(type))
		{
			return doPwdLock(request, userId);
		}
		if (UserCacheModel.PAY_PWD_LOCK.equals(type))
		{
			return doPayPwdLock(request, userId);
		}
		return false;
	}

	public boolean doPwdLock(HttpServletRequest request, long userId)
	{
		User user = userService.getUserById(userId);
		UserCache uc = user.getUserCache();

		long timeRange = 120; // 连续输入错误时间范围
		int failMaxTimes = 5; // 最大错误次数
		int failTimes = uc.getLoginFailTimes(); // 已经错误次数
		String sessionName = user.getUserName() + "_pwd";
		if (request.getSession().getAttribute(sessionName) == null)
		{
			request.getSession().setAttribute(sessionName,
					System.currentTimeMillis());
		}
		long firstTime = (Long) request.getSession().getAttribute(sessionName);
		if ((System.currentTimeMillis() - firstTime < timeRange * 1000)
				&& (failTimes + 1 == failMaxTimes))
		{
			uc.setLoginFailTimes(failTimes + 1);
			uc.setLockTime(new Date());
			uc.setLoginPwdStatus(1);
			userCacheDao.update(uc);
			return true;
		} else if (System.currentTimeMillis() - firstTime > timeRange * 1000)
		{
			uc.setLoginFailTimes(1);
			uc.setLoginPwdStatus(0);
			request.getSession().setAttribute(sessionName,
					System.currentTimeMillis());
		} else
		{
			uc.setLoginFailTimes(uc.getLoginFailTimes() + 1);
		}
		userCacheDao.update(uc);
		return false;
	}

	public boolean doPayPwdLock(HttpServletRequest request, long userId)
	{
		User user = userService.getUserById(userId);
		UserCache uc = user.getUserCache();

		long timeRange = 120; // 连续输入错误时间范围
		int failMaxTimes = 5; // 最大错误次数
		int failTimes = uc.getPayFailTimes(); // 已经错误次数
		String sessionName = user.getUserName() + "_payPwd";
		if (request.getSession().getAttribute(sessionName) == null)
		{
			request.getSession().setAttribute(sessionName,
					System.currentTimeMillis());
		}
		long firstTime = (Long) request.getSession().getAttribute(sessionName);
		if ((System.currentTimeMillis() - firstTime < timeRange * 1000)
				&& (failTimes + 1 == failMaxTimes))
		{
			uc.setPayFailTimes(failTimes + 1);
			uc.setLockPayTime(new Date());
			uc.setPayPwdStatus(1);
			userCacheDao.update(uc);
			return true;
		} else if (System.currentTimeMillis() - firstTime > timeRange * 1000)
		{
			uc.setPayFailTimes(1);
			uc.setPayPwdStatus(0);
			request.getSession().setAttribute(sessionName,
					System.currentTimeMillis());
		} else
		{
			uc.setPayFailTimes(uc.getPayFailTimes() + 1);
		}
		userCacheDao.update(uc);
		return false;
	}
}
