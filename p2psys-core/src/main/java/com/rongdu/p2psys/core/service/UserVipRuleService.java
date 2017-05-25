package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.UserVipRule;
import com.rongdu.p2psys.core.model.UserVipRuleModel;

/**
 * 用户VIP规则
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月9日
 */
public interface UserVipRuleService {
	/**
	 * 列表
	 * 
	 * @return
	 */
	PageDataList<UserVipRule> list(UserVipRuleModel model);
	
	/**
	 * 添加VIP规则
	 * @param rule
	 */
	void addUserVipRule(UserVipRule rule);
	
	/**
	 * 修改VIP规则
	 * @param rule
	 */
	void updateUserVipRule(UserVipRule rule);
	
	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	UserVipRule find(long id);
	
	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteUserVipRule(long id);
	
	/**
	 * 列表
	 * @return
	 */
	List<UserVipRule> list();
	
	/**
	 * 根据新增VIP规则的限制金额自动分级
	 */
	void doUserVipRule();
}
