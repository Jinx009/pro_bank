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
import com.rongdu.p2psys.crowdfunding.service.LeaderFactoryService;
import com.rongdu.p2psys.crowdfunding.service.LeaderFlagService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 微信端 领投人智库 
 * @author Jinx
 *
 */
public class WechatLeaderFactoryAction extends BaseAction<LeaderFactoryModel> implements ModelDriven<LeaderFactoryModel>{

	private Map<String,Object> data;
	
	@Resource
	private LeaderFactoryService leaderFactoryService;
	@Resource
	private LeaderFlagService leaderFlagService;
	
	/**
	 * 领投人智库权益 -- 页面
	 * @return
	 */
	@Action(value="/cf/wechat/leaderRights",results={@Result(name="itr-rights",type="ftl",location="/nb/cf/wechat/leader/ltr_rights.html")})
	public String leaderRights(){
		return "itr-rights";
	}
	
	
	/**
	 * 微信端领投人智库列表 -- 页面
	 * @return
	 */
	@Action(value="/cf/wechat/user/leaderFactoryList",results={@Result(name="ltr-list",type="ftl",location="/nb/cf/wechat/leader/ltr-list.html")})
	public String listPage(){
		User user = getNBSessionUser();
		LeaderFactory leaderFactory = leaderFactoryService.findByUserId(user.getUserId());
		if(null!=leaderFactory){
			request.setAttribute("size",1);
		}else{
			request.setAttribute("size",0);
		}
		return "ltr-list";
	}
	
	/**
	 * 领投人智库列表 -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/leaderFactoryData")
	public void listData() throws IOException{
		List<LeaderFactoryModel> leaModels = leaderFactoryService.getModelList();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.DATA,leaModels);
		data.put(ConstantUtil.MSG,"领投人智库列表信息");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 我的领投人智库资料  -- 页面
	 * @return
	 */
	@Action(value="/cf/wechat/user/leaderFactory",results={@Result(name="leader-factory",type="ftl",location="/nb/cf/wechat/leader/be-factory.html")})
	public String leaderFactoryPage() {
		return "leader-factory";
	}
	
	/**
	 * 我的领投人智库资料 -- 数据
	 * @throws IOException
	 */
	@Action(value="/cf/wechat/user/leaderFactoryData")
	public void leaderFactoryData() throws IOException{
		User user = getNBSessionUser();
		LeaderFactory leaderFactory = leaderFactoryService.findByUserId(user.getUserId());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("factory",leaderFactory);
		map.put("flag",null);
		map.put("size",0);
		if(null!=leaderFactory){
			map.put("size",1);
			List<LeaderFlag> flags= leaderFlagService.getByFactory(leaderFactory.getId());
			map.put("flag",flags);
			if(1==leaderFactory.getStatus()){
				map.put("size",-1);
			}
		}
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"我的智库资料");
		data.put(ConstantUtil.DATA,map);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 微信端保存领投人智库资料  -- 操作
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/user/saveFactory")
	public void saveFactory() throws IOException{
		Integer id = paramInt("id");
		String name = paramString("name");
		String reason = paramString("reason");
		String flag = paramString("flag");
		String info = paramString("info");
		String history = paramString("history");
		User user = getNBSessionUser();
		
		if(0==id){
			LeaderFactory leaderFactory = new LeaderFactory();
			leaderFactory.setName(name);
			leaderFactory.setInfo(info);
			leaderFactory.setHistory(history);
			leaderFactory.setReason(reason);
			leaderFactory.setShowStatus(0);
			leaderFactory.setStatus(0);
			leaderFactory.setPicPath(null);
			leaderFactory.setUserId(user.getUserId());
			leaderFactory.setPicUrl(0);
			leaderFactory = leaderFactoryService.save(leaderFactory);
			
			String[] flags = ConstantUtil.getStringIds(flag);
			leaderFlagService.saveList(leaderFactory,flags);
		}else{
			LeaderFactory leaderFactory = leaderFactoryService.find(id);
			leaderFactory.setName(name);
			leaderFactory.setInfo(info);
			leaderFactory.setHistory(history);
			leaderFactory.setReason(reason);
			leaderFactory.setStatus(0);
			leaderFactory.setShowStatus(0);
			
			leaderFactoryService.doUpdate(leaderFactory);
			
			List<LeaderFlag> list = leaderFlagService.getByFactory(id);
			
			if(null!=list&&!list.isEmpty()){
				for(int i = 0;i<list.size();i++){
					leaderFlagService.doDelete(list.get(i).getId());
				}
			}
			String[] flags = ConstantUtil.getStringIds(flag);
			leaderFlagService.saveList(leaderFactory,flags);
		}
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"保存领投人智库资料");
		data.put(ConstantUtil.DATA,"操作成功！");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	/**
	 * 活动保存领投人智库  -- 操作
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/saveInviteFactrory")
	public void saveInviteFactory() throws IOException{
		String realname = paramString("realname");
		String tel = paramString("tel");
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		LeaderFactory leaderFactory = leaderFactoryService.getByNameAndTel(realname,tel);
		if(null==leaderFactory){
			leaderFactory = new LeaderFactory();
			leaderFactory.setName(realname);
			leaderFactory.setShowStatus(0);
			leaderFactory.setStatus(0);
			leaderFactory.setTel(tel);
			leaderFactory.setResult(0);
			leaderFactory = leaderFactoryService.save(leaderFactory);
			data.put(ConstantUtil.DATA,0);
			data.put(ConstantUtil.MSG,leaderFactory.getId());
		}else{
			data.put(ConstantUtil.DATA,leaderFactory.getResult());
			data.put(ConstantUtil.MSG,leaderFactory.getId());
		}

		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
