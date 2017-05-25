package com.rongdu.manage.action.crowdfunding;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.model.LeaderFactoryModel;
import com.rongdu.p2psys.crowdfunding.service.LeaderFactoryService;
import com.rongdu.p2psys.crowdfunding.service.LeaderFlagService;
import com.rongdu.p2psys.nb.util.ConstantUtil;

/**
 * 领头人仓库
 * @author Jinx
 *
 */
public class LeaderFactoryAction extends BaseAction<LeaderFactoryModel> implements ModelDriven<LeaderFactoryModel>{

	@Resource
	private LeaderFactoryService leaderFactoryService;
	@Resource
	private LeaderFlagService leaderFlagService;
	
	private Map<String,Object> data;

	/**
	 * 通过状态
	 * @throws IOException
	 */
	@Action(value = "/cf/leaderFactory/changeStatus")
	public void changeStatus() throws IOException{
		Integer id = paramInt("id");
		int status = paramInt("status");
		LeaderFactory leaderFactory = leaderFactoryService.find(id);
		leaderFactory.setStatus(status);
		leaderFactoryService.doUpdate(leaderFactory);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"保存成功");
		
		printWebJson(getStringOfJpaObj(data));
	}
	

	/***
	 * 显示状态
	 * @throws IOException
	 */
	@Action(value = "/cf/leaderFactory/changeShowStatus")
	public void changeShowStatus() throws IOException{
		Integer id = paramInt("id");
		int status = paramInt("status");
		LeaderFactory leaderFactory = leaderFactoryService.find(id);
		leaderFactory.setShowStatus(status);
		leaderFactoryService.doUpdate(leaderFactory);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"保存成功");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 领投人仓库列表
	 * @throws IOException
	 */
	@Action("/cf/leaderFactory/list")
	public void allLeaderFactory() throws IOException{
		List<LeaderFactoryModel> list = leaderFactoryService.getModelList();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
		
	}
	
	/**
	 * 领头人仓库添加
	 * @throws IOException
	 */
	@Action("/cf/leaderFactory/add")
	public void addLeaderFactory() throws IOException{
		LeaderFactory leaderFactory = model.prototype();
		leaderFactory = leaderFactoryService.save(leaderFactory);
		
		String[] str = request.getParameterValues("flag");
		leaderFlagService.saveList(leaderFactory,str);
		String path = imgUpload();
		if (path != null && !"".equals(path)){
			leaderFactory.setPicPath(path);
		}
		leaderFactory.setShowStatus(0);
		leaderFactory.setStatus(0);
		leaderFactory.setPicUrl(1);
		leaderFactoryService.doUpdate(leaderFactory);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"添加成功!");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 领投人仓库首页
	 * @return
	 */
	@Action("/modules/crowdfunding/leaderFactory/leaderFactory")
	public String index(){
		return "leaderFactory";
	}
	
	/**
	 * 删除领头人仓库
	 * @throws IOException
	 */
	@Action(value = "/cf/leaderFactory/delete")
	public void deleteLeaderFactory() throws IOException{
		leaderFactoryService.delete(paramInt("id"));
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"删除成功!");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
