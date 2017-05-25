package com.rongdu.p2psys.web.nb;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;
import com.rongdu.p2psys.nb.vip.domain.WealthManagerUser;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;
import com.rongdu.p2psys.nb.vip.model.WealthManagerModel;
import com.rongdu.p2psys.nb.vip.service.WealthManagerService;
import com.rongdu.p2psys.nb.vip.service.WealthManagerUserService;
import com.rongdu.p2psys.nb.vip.service.WealthUserService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 项目查询
 * 
 * @author zzg
 * @version 2.0
 * @since 2014年4月17日
 */
public class WealthManagerAction extends BaseAction<WealthManagerModel> implements ModelDriven<WealthManagerModel> {


	@Resource
	private UserService userService;
	
	private User user;
	
	@Resource
	private WealthUserService wealthUserService;

	@Resource
	private WealthManagerService wealthManagerService;
	@Resource
	private WealthManagerUserService wealthManagerUserService;
	
	private Map<String, Object> data;

	@Action(value = "/nb/wealthManager_vip/findWealthManager" )
	public void findIsWealthManager() throws IOException{
		data = new HashMap<String, Object>();
		user = getSessionUser();
		long userId = user.getUserId();
		List<WealthUser> list = wealthUserService.findByWealthUserId(new Long(userId).intValue());
		System.out.println("list....=="+list.size()+",,,new Long(userId).intValue()=="+new Long(userId).intValue());
		if(list.size()==0){
			data.put("list", 0);
			printJson(getStringOfJpaObj(data));
		}else{
			data.put("list", 1);
			WealthUser wealthUser = wealthUserService.findById(list.get(0).getId());
			Integer id = wealthUser.getId();
			System.out.println("id="+id);
			List<WealthManagerUser> userIds = wealthManagerUserService.findByUserId(id);
			for(int i = 0;i<userIds.size();i++){
				Integer wealth_manager_id = userIds.get(i).getWealthManager().getId();
				System.out.println("wealth_manager_id="+wealth_manager_id);
				WealthManager wealthManager = wealthManagerService.findById(wealth_manager_id);
				data.put("wealthManager", wealthManager);
				data.put("userIds",userIds);
				printJson(getStringOfJpaObj(data));
			}
		}
	}
	
	
	@Action(value = "/nb/wealthManager_vip/wealthManager_vip" )
	public String wealthManager() throws IOException{
		return "wealthManager_vip";
		
	}
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
