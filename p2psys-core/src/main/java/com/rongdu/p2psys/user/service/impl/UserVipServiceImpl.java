package com.rongdu.p2psys.user.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.core.dao.UserVipRuleDao;
import com.rongdu.p2psys.core.domain.UserVipRule;
import com.rongdu.p2psys.ppfund.dao.PpfundInDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.dao.UserVipDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserVip;
import com.rongdu.p2psys.user.service.UserVipService;

@Service("userVipService")
public class UserVipServiceImpl implements UserVipService {

	@Resource
	private UserVipDao userVipDao;
	@Resource
	private UserVipRuleDao userVipRuleDao;
	@Resource
	private UserIdentifyDao userIdentifyDao;
	@Resource
	private BorrowTenderDao borrowTenderDao;
	@Resource
	private PpfundInDao ppfundInDao;

	@Override
	public void doUserVip() {
		List<UserVipRule> list = userVipRuleDao.findAll();
		for (int i = 0; i < list.size(); i++) {
			UserVipRule rule = list.get(i);
			if(i == list.size() - 1){//顶级会员特殊处理
				userVipDao.updateUserVip(rule.getTenderMoney(), rule);
			}else {
				userVipDao.updateUserVip(rule.getTenderMoney(), list.get(i+1).getTenderMoney(), rule);
			}
		}
	}

	@Override
	public UserVip getUserVipRuleByUser(User user) {
		return userVipDao.getLateYearInvestByUser(user);
	}

	@Override
	public void doUserVip(double money, User user) {
		UserIdentify userIdentify = userIdentifyDao.findByUserId(user.getUserId());
		List<UserVipRule> list = userVipRuleDao.findAll();
		
		UserVip userVip = userVipDao.getLateYearInvestByUser(user);
		
		//设置用户投资总额
		userVip.setInvestMoney(userVip.getInvestMoney() + money);
		
		//获取用户最近一年投资金额
		Date startTime = DateUtil.rollYear(new Date(), -1);
		double lastYearInvestTotal = borrowTenderDao.getLastYearInvest(startTime, new Date(), user);
		double lastYearPpfundInTotal = ppfundInDao.getLastYearInMoney(startTime, new Date(), user);
		
		lastYearInvestTotal += lastYearPpfundInTotal;
		userVip.setLastYearInvest(lastYearInvestTotal);
		
		for (int i = 0; i < list.size(); i++) {
			UserVipRule rule = list.get(i);
			if (i == list.size() - 1) {// 最后处理
				if(lastYearInvestTotal > rule.getTenderMoney()){
					userVip.setLevel(i + 1);
					userVip.setName(rule.getName());
					userVip.setUpdateTime(new Date());
				}
			} else {
				UserVipRule nexRule = list.get(i + 1);
				if (rule.getTenderMoney() <= lastYearInvestTotal
						&& lastYearInvestTotal < nexRule.getTenderMoney()) {
					userVip.setLevel(i+1);
					userVip.setName(rule.getName());
					userVip.setUpdateTime(new Date());
					break;
				}
			}
		}
		
		//更新用户VIP状态
		if(userVip.getLevel() > 0 && userIdentify.getVipStatus() != 1){
			userIdentify.setVipStatus(1);
			userIdentifyDao.update(userIdentify);
		}else if(userVip.getLevel() == 0 && userIdentify.getVipStatus() != 0) {
			userIdentify.setVipStatus(0);
			userIdentifyDao.update(userIdentify);
		}
		userVipDao.update(userVip);
	}

	@Override
	public void doUserVipCheck() {
		List<UserVip> list = userVipDao.findAll();
		if(list != null && list.size() > 0){
			for (UserVip userVip : list) {
				User user = userVip.getUser();
				//获取用户最近一年投资金额
				Date startTime = DateUtil.rollYear(userVip.getUpdateTime(), -1);
				double lastYearInvestTotal = borrowTenderDao.getLastYearInvest(startTime, new Date(), user);
				double lastYearPpfundInTotal = ppfundInDao.getLastYearInMoney(startTime, new Date(), user);
				
				lastYearInvestTotal += lastYearPpfundInTotal;
				userVip.setLastYearInvest(lastYearInvestTotal);
				userVipDao.update(userVip);
			}
		}
		//修改用户VIP等级
		this.doUserVip();
	}
	
	
	
}
