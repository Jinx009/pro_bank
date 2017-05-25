package com.rongdu.p2psys.user.service.impl;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.RSAUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.BASE64Decoder;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.model.accountlog.BaseAccountLog;
import com.rongdu.p2psys.account.model.accountlog.noac.GetCodeLog;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.rule.RsaFormEncryptRuleCheck;
import com.rongdu.p2psys.core.util.festive.Festival;
import com.rongdu.p2psys.core.util.festive.Lunar;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.recommend.dao.RecommendRecordDao;
import com.rongdu.p2psys.nb.recommend.dao.RedPacketDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.score.dao.ScoreDao;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderScoreEmailLog;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserCreditDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.dao.UserInviteDao;
import com.rongdu.p2psys.user.dao.UserPromotDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.dao.UserVipDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserInvite;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.domain.UserVip;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.login.UserLoginComposite;
import com.rongdu.p2psys.user.service.UserBaseInfoService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService
{

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
	private UserIdentifyService userIdentifyService;
	@Resource
	private ScoreDao scoreDao;
	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserInviteDao userInviteDao;
	@Resource
	private UserVipDao userVipDao;
	@Resource
	private BorrowInterestRateService borrowInterestRateService;
	@Resource
	private UserBaseInfoService baseInfoService;
	@Resource
	private UserPromotDao userPromotDao;
	@Resource
	private OperationLogDao operationLogDao;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private RedPacketDao theRedPacketDao;
	@Resource
	private RecommendRecordDao recommendRecordDao;

	@Override
	public User find(long userId)
	{
		return userDao.findObjByProperty("userId", userId);
	}

	/*
	 * 统计
	 */

	@Override
	public int count(QueryParam param)
	{
		return userDao.countByCriteria(param);
	}

	@Override
	public int count(String startTime, String endTime)
	{
		return userDao.count(startTime, endTime);
	}

	@Override
	public int countByUserName(String userName)
	{
		QueryParam param = QueryParam.getInstance();
		param.addParam("userName", userName);
		return this.count(param);
	}

	@Override
	public int countByCardId(String cardId)
	{
		QueryParam param = QueryParam.getInstance();
		param.addParam("cardId", cardId);
		return this.count(param);
	}

	@Override
	public int countByMobilePhone(String mobilePhone)
	{
		QueryParam param = QueryParam.getInstance();
		param.addParam("mobilePhone", mobilePhone);
		return this.count(param);
	}

	@Override
	public int countByEmail(String email)
	{
		QueryParam param = QueryParam.getInstance();
		param.addParam("email", email);
		return this.count(param);
	}

	@Override
	public int countByEmail(String email, long userId)
	{
		return userDao.countByEmail(email, userId);
	}

	/*
	 * 获取用户信息
	 */

	@Override
	public Collection<User> getUsersByBindId(long bindId)
	{
		return userDao.findByProperty("bindId", bindId);
	}

	@Override
	public User getUserById(long userId)
	{
		return userDao.findObjByProperty("userId", userId);
	}

	@Override
	public User getUserByUserName(String userName)
	{
		return (User) userDao.findObjByProperty("userName", userName);
	}

	@Override
	public User getUserByMobilePhone(String mobilePhone)
	{
		return (User) userDao.findObjByProperty("mobilePhone", mobilePhone);
	}

	@Override
	public User getUserByEmail(String email)
	{
		return (User) userDao.findObjByProperty("email", email);
	}

	@Override
	public User getUserByRealName(String realName)
	{
		return (User) userDao.findObjByProperty("realName", realName);
	}

	@Override
	public PageDataList<UserModel> userList(int pageNumber, int pageSize,
			UserModel model)
	{
		return userDao.userList(pageNumber, pageSize, model);
	}

	/*
	 * 修改用户信息
	 */

	@Override
	public User modifyPwd(User user)
	{
		userCacheDao.modifyPwdTime(user.getUserId());
		Global.setTransfer("noticeTime", DateUtil.getTime(new Date()));
		Global.setTransfer("user", user);
		AbstractExecuter executer = ExecuterHelper
				.doExecuter("userModifyPwdExecuter");
		executer.execute(0, user);
		return userDao.modifyPwd(user);
	}

	@Override
	public User modifyPaypwd(User user)
	{
		Global.setTransfer("noticeTime", DateUtil.getTime(new Date()));
		Global.setTransfer("user", user);
		AbstractExecuter executer = ExecuterHelper
				.doExecuter("userModifyPayPwdExecuter");
		executer.execute(0, user);
		return userDao.modifyPaypwd(user);
	}

	@Override
	public void modifyEmail(long userId, String email)
	{
		userDao.modifyEmail(userId, email);
	}

	@Override
	public void modifyPhone(long userId, String phone, int status)
	{
		userDao.modifyPhone(userId, phone);
		userIdentifyService.modifyMobilePhoneStatus(userId, status, 0);
	}

	@Override
	public void updateUserInfo(User user)
	{
		userDao.update(user);
	}

	@SuppressWarnings("static-access")
	@Override
	public User doRegister(UserModel model) throws Exception
	{
		User user = model.prototype();

		// 校验手机
		if (StringUtil.isNotBlank(user.getMobilePhone()))
		{
			int hasMobilePhone = this.countByMobilePhone(user.getMobilePhone());
			if (hasMobilePhone > 0)
				throw new UserException("手机已经被使用", 1);
		}

		user.setPwd(MD5.encode(user.getPwd()));
		user.setUserName(user.getMobilePhone());
		user.setAddTime(new Date());

		// 用户附属信息
		UserCache uc = new UserCache(user, Global.getIP());
		uc.setUserNature(1);
		uc.setUserType(model.getUserType());
		userCacheDao.save(uc);
		userCacheDao.save(uc);

		user = userDao.save(user);
		user.setBindId(user.getUserId());
		userDao.update(user);
		
		// 用户认证信息
		UserIdentify ui = new UserIdentify(user);
		ui.setMobilePhoneStatus(1);
		ui.setMobilePhoneVerifyTime(new Date());
		userIdentifyDao.save(ui);

		// 手机认证赠送红包
		userRedPacketService.doRedPacket(RedPacket.PHONE, user, null, null);

		// 资金账户
		accountDao.save(new Account(user));
		// 资金合计
		accountSumDao.save(new AccountSum(user));
		// 用户amount
		userCreditDao.save(new UserCredit(user));
		// 添加用户积分信息
		scoreDao.save(new Score(user));
		// 用户基本信息
		baseInfoService.save(uc);

		if (model.getUserType() == 1)
		{// 投资者送加息劵
			// 添加用户vip规则信息
			UserVip uvr = new UserVip();
			uvr.setUser(user);
			uvr.setName("普通会员");
			uvr.setLevel(0);
			uvr.setApr(0);
			uvr.setInvestMoney(0.00);
			uvr.setLastYearInvest(0.00);
			uvr.setAddTime(new Date());
			uvr.setUpdateTime(new Date());
			uvr.setAddIp(Global.getIP());
			userVipDao.save(uvr);
		}

		// 注册赠送红包
		userRedPacketService.doRedPacket(RedPacket.REGISTER, user, null, null);
		User inviteUser = model.getInviteUser();
		if (inviteUser != null)
		{
			UserInvite iu = new UserInvite();
			iu.setInviteTime(new Date());
			iu.setUser(user);
			iu.setInviteUser(inviteUser);
			userInviteDao.save(iu);
			// 推荐人邀请码已使用次数减一
			UserPromot userPromot = userPromotDao
					.getUserPromotByUserId(inviteUser);
			if (userPromot != null)
			{
				if (userPromot.getCanUseTimes() - userPromot.getUsedTimes() > 0)
				{
					userPromot.setUsedTimes(userPromot.getUsedTimes() + 1);
					userPromot.setCanUseTimes(userPromot.getCanUseTimes()-1);
					userPromotDao.update(userPromot);
				}
			}
			// 发放推荐好友注册红包
			userRedPacketService.doRedPacket(RedPacket.INVENT_FRIEND,inviteUser, null, null);
			
			Date date=new Date();
			Calendar calendar = new GregorianCalendar(); 
			calendar.setTime(date); 
			
			String hql = " from RedPacket where serviceType = '"+ConstantUtil.RED_PACKET_RECOMMEND+"'  ";
			RedPacket redPacket = theRedPacketDao.findByHql(hql);
			
			calendar.add(calendar.DATE,redPacket.getDay());
			date=calendar.getTime();  
			
			if(1==redPacket.getStatus())
			{
				UserRedPacket userRedPacket = new UserRedPacket();
				userRedPacket.setAddTime(new Date());
				userRedPacket.setAmount(redPacket.getMoney());
				userRedPacket.setExpiredTime(date);
				userRedPacket.setRedPacketType(2);
				userRedPacket.setType(redPacket);
				userRedPacket.setUsed(false);
				userRedPacket.setUser(inviteUser);
				
				userRedPacket =  userRedPacketDao.save(userRedPacket);
				
				RecommendRecord recommendRecord = new RecommendRecord();
				recommendRecord.setAddTime(new Date());
				recommendRecord.setUserRedPacket(userRedPacket);
				recommendRecord.setAccount(userRedPacket.getAmount());
				recommendRecord.setInviteUser(inviteUser);
				recommendRecord.setName(userRedPacket.getType().getServiceName());
				recommendRecord.setUser(user);
				
				recommendRecordDao.save(recommendRecord);
			}
		}
		// 注册成功发放体验金
		theExperienceGoldService.addExperienceGold(user);
		
		return user;
	}

	@Override
	public User doLogin(User user, int encrypt) throws Exception
	{
		RsaFormEncryptRuleCheck rsaCheck = (RsaFormEncryptRuleCheck) Global
				.getRuleCheck("rsaFormEncrypt");
		if (rsaCheck.enable_formEncrypt && encrypt == 1)
		{
			user.setPwd(RSAUtil.getRSADecrypt(user.getPwd()));
		}
		String pwd = user.getPwd();
		pwd = URLDecoder.decode(user.getPwd(), "utf-8");
		user.setPwd(pwd);
		User u = null;
		UserLoginComposite composite = new UserLoginComposite();
		u = composite.login(user);
		
		return u;
	}

	@Override
	public User activationEmail(String idstr) throws Exception
	{
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] idstrBytes = decoder.decodeBuffer(idstr);
		String decodeidstr = new String(idstrBytes);
		String[] idstrArr = decodeidstr.split(",");
		if (idstrArr.length < 4)
		{
			throw new UserException("链接无效，请重新获取邮件！");
		}
		long activeTime = StringUtil.toLong(idstrArr[2]);
		if (System.currentTimeMillis() - activeTime * 1000 > 24 * 60 * 60 * 1000)
		{
			throw new UserException("链接有效时间为一天，已失效，请重新获取激活邮件!", "/");
		}
		String useridStr = idstrArr[0];
		long userId = Long.parseLong(useridStr);
		User user = userDao.findObjByProperty("userId", userId);
		UserIdentify userIdentify = userIdentifyDao.findObjByProperty(
				"user.userId", userId);
		if (userIdentify.getEmailStatus() == 1 // 已激活
				&& (user.getPwd() != null && !"".equals(user.getPwd())))
		{
			throw new UserException("您的邮箱已激活！", "login.html");
		}
		else
		{
			BaseScoreLog bLog = new TenderScoreEmailLog(userId);
			bLog.doEvent();
			userIdentifyDao.modifyEmailStatus(userId, 1, 0);
		}
		return user;
	}

	@Override
	public User getPwdByEmail(String userName, String email)
	{
		User user = userDao.findObjByProperty("email", email);
		if (user == null)
		{
			throw new UserException("该邮箱不存在！");
		}
		BaseAccountLog blog = new GetCodeLog(user, user.getUserName(),
				NoticeConstant.NOTICE_GET_PWD_EMAIL);
		blog.initCode("get_pwd_email");
		blog.doEvent();
		return user;
	}

	@Override
	public User getPwdByPhone(String userName, String mobile_phone)
	{
		User user = userDao.findObjByProperty("mobilePhone", mobile_phone);
		if (user == null)
		{
			throw new UserException("该手机号不存在！");
		}
		BaseAccountLog blog = new GetCodeLog(user, user.getUserName(),
				NoticeConstant.NOTICE_GET_PWD_PHONE);
		blog.initCode("get_pwd_phone");
		blog.doEvent();
		return user;
	}

	@Override
	public User getPayPwdByEmail(String userName, String email)
	{
		User user = userDao.findObjByProperty("email", email);
		if (user == null)
		{
			throw new UserException("该邮箱不存在！");
		}
		BaseAccountLog blog = new GetCodeLog(user, user.getUserName(),
				NoticeConstant.NOTICE_GET_PAY_PWD_EMAIL);
		blog.initCode("get_pay_pwd_email");
		blog.doEvent();
		return user;
	}

	@Override
	public User getPayPwdByPhone(String userName, String mobile_phone)
	{
		User user = userDao.findObjByProperty("mobilePhone", mobile_phone);
		if (user == null)
		{
			throw new UserException("该手机号不存在！");
		}
		BaseAccountLog blog = new GetCodeLog(user, user.getUserName(),
				NoticeConstant.NOTICE_GET_PAY_PWD_PHONE);
		blog.initCode("get_pay_pwd_phone");
		blog.doEvent();
		return user;
	}

	@Override
	public void doNoticeMsg()
	{
		Calendar today = Calendar.getInstance();
		int month = today.get(Calendar.MONTH);
		int day = today.get(Calendar.DATE);
		// 公历假期
		String ftv = new Festival().showSFtv(month + 1, day);
		// 农历假期
		String ltv = new Lunar(today).lunarFestival();

		List<User> list = userDao.findAll();
		for (User user : list)
		{
			if (user.getUserId() != 1L)
			{
				Global.setTransfer("user", user);
				if (!StringUtils.isBlank(ftv))
				{
					if ("1".equals(ftv))
					{// 元旦
						AbstractExecuter executer = ExecuterHelper
								.doExecuter("newYearDayExecuter");
						executer.execute(0, user);
					}
					else if ("2".equals(ftv))
					{// 劳动节
						AbstractExecuter executer = ExecuterHelper
								.doExecuter("laborDayExecuter");
						executer.execute(0, user);
					}
					else if ("3".equals(ftv))
					{// 国庆节
						AbstractExecuter executer = ExecuterHelper
								.doExecuter("nationalDayExecuter");
						executer.execute(0, user);
					}
				}
				if (!StringUtils.isBlank(ltv))
				{// 春节
					if ("4".equals(ltv))
					{
						AbstractExecuter executer = ExecuterHelper
								.doExecuter("springFestivalExecuter");
						executer.execute(0, user);
					}
					else if ("5".equals(ltv))
					{// 端午节
						AbstractExecuter executer = ExecuterHelper
								.doExecuter("dragonBoatFestivalExecuter");
						executer.execute(0, user);
					}
					else if ("6".equals(ltv))
					{// 中秋节
						AbstractExecuter executer = ExecuterHelper
								.doExecuter("midAutumnFestivalExecuter");
						executer.execute(0, user);
					}
				}
			}
		}

	}

	@Override
	public void updateUser(User user)
	{
		userDao.update(user);
	}

	@Override
	public List<User> findByBindId(Long bindId)
	{
		return userDao.findByProperty("bindId", bindId);
	}

	@Override
	public List<User> getByGroupId(Long bindId)
	{
		return userDao.getByGroupId(bindId);
	}

}
