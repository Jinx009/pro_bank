package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.domain.UserVipRule;
import com.rongdu.p2psys.core.model.UserVipRuleModel;
import com.rongdu.p2psys.core.service.UserVipRuleService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.service.UserVipService;

/**
 * 用户VIP规则
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月9日
 */
public class UserVipRuleAction extends BaseAction<UserVipRuleModel> implements
		ModelDriven<UserVipRuleModel> {
	@Resource
	private UserVipRuleService userVipRuleService;
	@Resource
	private UserVipService userVipService;

	private UserVipRuleModel model = new UserVipRuleModel();

	public UserVipRuleModel getModel() {
		return model;
	}
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	/**
	 * 用户VIP规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/userVipRule/userVipRulePage")
	public String userVipRulePage() throws Exception {
		return "userVipRulePage";
	}

	/**
	 * 用户vip规则列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/userVipRule/userVipRuleList")
	public void userVipRuleList() throws Exception {
		PageDataList<UserVipRule> pageList = userVipRuleService.list(model);
		int totalPage = pageList.getPage().getTotal(); // 总页数
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 添加VIP规则页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/userVipRule/userVipRuleAddPage")
	public String userVipRuleAddPage() throws Exception {
		return "userVipRuleAddPage";
	}
	
	/**
	 * 添加VIP规则
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/userVipRule/userVipAddRule")
	public void userVipAddRule() throws Exception {
		UserVipRule rule = (UserVipRule) paramModel(UserVipRule.class);
		userVipRuleService.addUserVipRule(rule);
		
		/**
		 * 根据设定的限制金额自动分级
		 */
		userVipRuleService.doUserVipRule();

		
		/**
		 * 规则修改以后需要立即对之前用户VIP等级做出判断和修改
		 */
		//执行用户VIP等级逻辑
		userVipService.doUserVip();
		printResult(MessageUtil.getMessage("I10001"), true);
	}
	
	/**
	 * VIP规则修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/userVipRule/userVipRuleEditPage")
	public String userVipRuleEditPage() throws Exception {
		UserVipRule rule = userVipRuleService.find(model.getId());
		request.setAttribute("rule", rule);
		return "userVipRuleEditPage";
	}
	
	/**
	 * VIP规则修改
	 * 
	 * @throws Exception
	 */
	@Action("/modules/system/userVipRule/userVipRuleEdit")
	public void userVipRuleEdit() throws Exception {
		UserVipRule rule = (UserVipRule) paramModel(UserVipRule.class);
		userVipRuleService.updateUserVipRule(rule);
		/**
		 * 规则修改以后需要立即对之前用户VIP等级做出判断和修改
		 */
		//执行用户VIP等级逻辑
		userVipService.doUserVip();
		printResult(MessageUtil.getMessage("I10002"), true);
	}
	
	/**
	 * VIP规则删除
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/system/userVipRule/userVipRuleDelete")
	public void userVipRuleDelete() throws Exception {
		userVipRuleService.deleteUserVipRule(model.getId());
		/**
		 * 根据设定的限制金额自动分级
		 */
		userVipRuleService.doUserVipRule();
		
		/**
		 * 规则修改以后需要立即对之前用户VIP等级做出判断和修改
		 */
		//执行用户VIP等级逻辑
		userVipService.doUserVip();
		printResult(MessageUtil.getMessage("I10003"), true);
	}
}
