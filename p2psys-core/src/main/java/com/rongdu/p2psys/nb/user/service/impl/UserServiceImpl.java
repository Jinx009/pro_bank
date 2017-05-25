package com.rongdu.p2psys.nb.user.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.nb.account.dao.AccountDao;
import com.rongdu.p2psys.nb.account.dao.AccountSumDao;
import com.rongdu.p2psys.nb.ppfund.dao.ExperienceGoldDao;
import com.rongdu.p2psys.nb.recommend.dao.RecommendRecordDao;
import com.rongdu.p2psys.nb.recommend.dao.RedPacketDao;
import com.rongdu.p2psys.nb.score.dao.ScoreDao;
import com.rongdu.p2psys.nb.user.dao.UserBaseInfoDao;
import com.rongdu.p2psys.nb.user.dao.UserCacheDao;
import com.rongdu.p2psys.nb.user.dao.UserCreditDao;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.nb.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.nb.user.dao.UserInviteDao;
import com.rongdu.p2psys.nb.user.dao.UserPromotDao;
import com.rongdu.p2psys.nb.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.nb.user.dao.UserVipDao;
import com.rongdu.p2psys.nb.user.service.CouponService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.CouponModel;

@Service("theUserService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao theUserDao;
	@Resource
	private UserPromotDao theUserPromotDao;
	@Resource
	private UserCacheDao theUserCacheDao;
	@Resource
	private AccountDao theAccountDao;
	@Resource
	private UserIdentifyDao theUserIdentifyDao;
	@Resource
	private UserVipDao theUserVipDao;
	@Resource
	private UserBaseInfoDao theUserBaseInfoDao;
	@Resource
	private UserCreditDao theUserCreditDao;
	@Resource
	private AccountSumDao theAccountSumDao;
	@Resource
	private ScoreDao theScoreDao;
	@Resource
	private UserInviteDao theUserInviteDao;
	@Resource
	private UserRedPacketDao theUserRedPacketDao;
	@Resource
	private RedPacketDao theRedPacketDao;
	@Resource
	private RecommendRecordDao recommendRecordDao;
	@Resource
	private ExperienceGoldDao theExperienceGoldDao;
	@Resource
	private CouponService couponService;

	public User getByUserName(String userName) {
		return theUserDao.getByUserName(userName);
	}

	public User doLogin(String userName, String pwd) {
		return theUserDao.doLogin(userName, pwd);
	}

	public User getByAttribute(String attributeName, String attributeValue,
			String attributeFactoryName, String attributeFactoryId) {
		return theUserDao.getByAttribute(attributeName, attributeValue,
				attributeFactoryName, attributeFactoryId);
	}

	public List<User> getByGroupId(Long groupId) {
		return theUserDao.getByGroupId(groupId);
	}

	public User saveUser(User user) {
		return theUserDao.save(user);
	}

	public void updateUser(User user) {
		theUserDao.update(user);
	}

	public User getByUserId(long user_id) {
		return theUserDao.find(user_id);
	}

	public int countByCardId(String cardId, String channelKey) {
		return theUserDao.getUserByCardId(cardId, channelKey);
	}
	
	public User getUserByCardId(String cardId) {
		return theUserDao.getUserByCardId(cardId);
	}
	
	public int countByCardId2(String cardId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("cardId", cardId);
		return this.count(param);
	}

	/*
	 * 统计
	 */
	public int count(QueryParam param) {
		return theUserDao.countByCriteria(param);
	}

	public User saveWechatUser(User user) {
		user.setWechatBindTime(new Date());
		user.setAddTime(new Date());

		return theUserDao.save(user);
	}

	/**
	 * PC注册
	 */
	public User savePcUser(User user, String inviteCode) {
		user = theUserDao.save(user);
		user.setBindId(user.getUserId());
		theUserDao.update(user);

		UserCache userCache = theUserCacheDao.savePcUserCache(user);
		user.setUserCache(userCache);
		Account account = new Account();
		account.setUser(user);
		theAccountDao.save(account);

		theUserIdentifyDao.savePcUserIdentify(user);
		theUserPromotDao.savePcUserPromot(user);
		theUserVipDao.savePcUserVip(user);
		theUserBaseInfoDao.savePcUserBaseInfo(userCache);
		theUserCreditDao.save(new UserCredit(user));
		theAccountSumDao.save(new AccountSum(user));
		theScoreDao.save(new Score(user));

		return user;
	}

	@Override
	public List<User> getWechatUserList(String appId) {
		return theUserDao.findWechatUser(appId);
	}

}
