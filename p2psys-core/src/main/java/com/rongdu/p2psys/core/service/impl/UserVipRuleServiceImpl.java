package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.UserVipRuleDao;
import com.rongdu.p2psys.core.domain.UserVipRule;
import com.rongdu.p2psys.core.model.UserVipRuleModel;
import com.rongdu.p2psys.core.service.UserVipRuleService;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 用户VIP规则
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月9日
 */
@Service("userVipRuleService")
public class UserVipRuleServiceImpl implements UserVipRuleService {
	@Resource
	private UserVipRuleDao userVipRuleDao;

	@Override
	public PageDataList<UserVipRule> list(UserVipRuleModel model) {
		QueryParam param = QueryParam.getInstance()
				.addPage(model.getPage(), model.getRows())
				.addOrder("level");
		return userVipRuleDao.findPageList(param);
	}

	@Override
	public void addUserVipRule(UserVipRule rule) {
		userVipRuleDao.save(rule);
	}

	@Override
	public void updateUserVipRule(UserVipRule rule) {
		//检测该级别是否已存在
		UserVipRule rule2 = userVipRuleDao.findObjByProperty("level", rule.getLevel());
		if(rule2 != null && rule.getId() != rule2.getId()){
			throw new UserException("该级别规则已存在", 1);
		}
		//检测限制金额是否大于上一级别规则
		UserVipRule upperRule = userVipRuleDao.findObjByProperty("level", rule.getLevel() - 1);
		if(upperRule != null && rule.getTenderMoney() <= upperRule.getTenderMoney()){
			throw new UserException("限制金额必须大于上一级别限制金额", 1);
		}
		//检测限制金额是否大于下一级别规则
		UserVipRule nextRule = userVipRuleDao.findObjByProperty("level", rule.getLevel() + 1);
		if(nextRule != null && rule.getTenderMoney() >= nextRule.getTenderMoney()){
			throw new UserException("限制金额必须大于下一级别限制金额", 1);
		}
		userVipRuleDao.update(rule);
	}

	@Override
	public UserVipRule find(long id) {
		return userVipRuleDao.find(id);
	}

	@Override
	public void deleteUserVipRule(long id) {
		userVipRuleDao.delete(id);
	}

	@Override
	public List<UserVipRule> list() {
		return userVipRuleDao.findAll();
	}

	@Override
	public void doUserVipRule() {
		QueryParam param = QueryParam.getInstance()
				.addOrder("tenderMoney");
		List<UserVipRule> list = userVipRuleDao.findByCriteria(param);
		for (int i = 0; i < list.size(); i++) {
			UserVipRule rule = list.get(i);
			rule.setLevel(i+1);
			userVipRuleDao.update(rule);
		}
	}

}
