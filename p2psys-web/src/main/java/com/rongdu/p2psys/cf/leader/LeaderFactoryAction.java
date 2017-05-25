package com.rongdu.p2psys.cf.leader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFlag;
import com.rongdu.p2psys.crowdfunding.model.LeaderFactoryModel;
import com.rongdu.p2psys.crowdfunding.service.FlagService;
import com.rongdu.p2psys.crowdfunding.service.LeaderFactoryService;
import com.rongdu.p2psys.crowdfunding.service.LeaderFlagService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

public class LeaderFactoryAction  extends BaseAction<LeaderFactoryModel> implements ModelDriven<LeaderFactoryModel>{

	private Map<String,Object> data;
	
	@Resource
	private LeaderFactoryService leaderFactoryService;
	@Resource
	private UserCacheService theUserCacheService;
	@Resource
	private FlagService flagService;
	@Resource
	private LeaderFlagService leaderFlagService;

	/**
	 * 首页领投人仓库数据
	 * @throws IOException
	 */
	@Action(value = "/cf/leaderFactory/list")
	public void leaderFactoryData() throws IOException{
		List<LeaderFactory> list = leaderFactoryService.getList();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 领投人仓库列表页数据
	 * @throws IOException
	 */
	@Action(value = "/cf/leaderFactory/model")
	public void leaderFactoryModel() throws IOException{
		List<LeaderFactoryModel> list = leaderFactoryService.getModelList();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 领投人列表页面
	 * @return
	 */
	@Action(value="/leaderFactory",results={@Result(name="leader-factory",type="ftl",location="/nb/cf/leaderFactory/leader-factory.html")})
	public String leaderFactory(){
		return "leader-factory";
	}
	
	/**
	 * 保存优秀领投人信息
	 * @throws IOException 
	 */
	@Action(value = "/cf/leaderFactory/saveLeaderFactory")
	public void saveLeaderFactory() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		User user = getNBSessionUser();
		
		String name = paramString("name");
		String info = paramString("info");
		String reason = paramString("reason");
		String history = paramString("history");
		String ids = paramString("flag");
		String picPath = paramString("picPath");
		int id = paramInt("id");
		if(0==id){
			LeaderFactory leaderFactory = new LeaderFactory();
			leaderFactory.setName(name);
			leaderFactory.setInfo(info);
			leaderFactory.setHistory(history);
			leaderFactory.setReason(reason);
			leaderFactory.setShowStatus(0);
			leaderFactory.setStatus(0);
			leaderFactory.setPicPath(picPath);
			leaderFactory.setUserId(user.getUserId());
			leaderFactory.setPicUrl(0);
			leaderFactory = leaderFactoryService.save(leaderFactory);
			
			String[] flags = ConstantUtil.getStringIds(ids);
			leaderFlagService.saveList(leaderFactory,flags);
		}else{
			LeaderFactory leaderFactory = leaderFactoryService.find(id);
			leaderFactory.setName(name);
			leaderFactory.setInfo(info);
			leaderFactory.setHistory(history);
			leaderFactory.setReason(reason);
			leaderFactory.setPicPath(picPath);
			leaderFactory.setStatus(0);
			leaderFactory.setShowStatus(0);
			
			leaderFactoryService.doUpdate(leaderFactory);
			
			List<LeaderFlag> list = leaderFlagService.getByFactory(id);
			
			if(null!=list&&!list.isEmpty()){
				for(int i = 0;i<list.size();i++){
					leaderFlagService.doDelete(list.get(i).getId());
				}
			}
			
			String[] flags = ConstantUtil.getStringIds(ids);
			leaderFlagService.saveList(leaderFactory,flags);
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 成为优秀领投人
	 * @return
	 */
	@Action(value = "/cf/user/beFactory",results={@Result(name="beFactory",type="ftl",location="/nb/cf/leaderFactory/beFactory.html")})
	public String beFactory(){
		User user = getNBSessionUser();
		LeaderFactory leaderFactory = leaderFactoryService.findByUserId(user.getUserId());
		if(null!=leaderFactory){
			request.setAttribute("isStatus",leaderFactory.getId());
		}else{
			request.setAttribute("isStatus",0);
		}
		request.setAttribute("redirectUrl",paramString("redirectUrl"));
		return "beFactory";
	}
	
	/**
	 * 领头人仓库个人详情
	 * @throws IOException
	 */
	@Action(value = "/cf/user/factoryDetail")
	public void factoryDetail() throws IOException{
		User user = getNBSessionUser();
		LeaderFactory leaderFactory = leaderFactoryService.findByUserId(user.getUserId());
		List<LeaderFlag> list = leaderFlagService.getByFactory(leaderFactory.getId());
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,leaderFactory);
		data.put(ConstantUtil.ERROR_CODE,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
