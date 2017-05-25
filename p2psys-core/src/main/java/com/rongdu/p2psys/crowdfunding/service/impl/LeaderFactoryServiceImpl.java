package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.LeaderDao;
import com.rongdu.p2psys.crowdfunding.dao.LeaderFactoryDao;
import com.rongdu.p2psys.crowdfunding.dao.LeaderFlagDao;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFlag;
import com.rongdu.p2psys.crowdfunding.model.LeaderFactoryModel;
import com.rongdu.p2psys.crowdfunding.service.LeaderFactoryService;

@Service("leaderFactoryService")
public class LeaderFactoryServiceImpl implements LeaderFactoryService {

	@Resource
	private LeaderFactoryDao leaderFactoryDao;
	@Resource
	private LeaderDao leaderDao;
	@Resource
	private LeaderFlagDao leaderFlagDao;
	
	public LeaderFactory save(LeaderFactory leaderFactory) {
		return leaderFactoryDao.save(leaderFactory);
	}

	public void delete(Integer id) {
		leaderFactoryDao.delete(id);
	}

	public List<LeaderFactory> getList() {
		return leaderFactoryDao.getByHql("");
	}

	public LeaderFactory find(Integer id) {
		return leaderFactoryDao.find(id);
	}

	public List<LeaderFactoryModel> getModelList() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM LeaderFactory WHERE  status = 1 AND showStatus = 1 ");
		List<LeaderFactory> list = leaderFactoryDao.getByHql(buffer.toString());
		List<LeaderFactoryModel> modelList = null;
		if(null!=list){
			modelList = new ArrayList<LeaderFactoryModel>();
			for(int i =0;i<list.size();i++){
				LeaderFactoryModel leaderFactoryModel = new LeaderFactoryModel();
				leaderFactoryModel = LeaderFactoryModel.instance(list.get(i));
				//专注领投标签
				buffer = new StringBuffer();
				buffer.append(" FROM LeaderFlag WHERE leaderFactory.id =");
				buffer.append(leaderFactoryModel.getId());
				
				List<LeaderFlag> flagList = leaderFlagDao.getByHql(buffer.toString());
				String flagNames = "";
				if(null!=flagList){
					for(int j = 0;j<flagList.size();j++){
						flagNames += flagList.get(j).getFlag().getName()+"|";
					}
					flagNames = flagNames.substring(0,flagNames.length()-1);
				}
				//领投过的产品
				buffer = new StringBuffer();
				buffer.append(" FROM Leader WHERE leaderFactory.id= ");
				buffer.append(leaderFactoryModel.getId());
				
				List<Leader> leaderList = leaderDao.getByHql(buffer.toString());
				String projectNames = "";
				if(null!=leaderList){
					for(int k = 0;k<leaderList.size();k++){
						projectNames += leaderList.get(k).getProject().getProjectName();
					}
				}
				
				leaderFactoryModel.setProjectNames(projectNames);
				leaderFactoryModel.setFlagNames(flagNames);
				modelList.add(leaderFactoryModel);
			}
		}
		return modelList;
	}

	public void doUpdate(LeaderFactory leaderFactory) {
		leaderFactoryDao.update(leaderFactory);
	}

	public LeaderFactory findByUserId(long userId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM LeaderFactory WHERE  userId= ");
		buffer.append(userId);
		List<LeaderFactory> list = leaderFactoryDao.getByHql(buffer.toString());
		if(null!=list&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public LeaderFactory getByNameAndTel(String realname, String tel) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM LeaderFactory WHERE  name='");
		buffer.append(realname);
		buffer.append("'  AND tel='");
		buffer.append(tel);
		buffer.append("'  ");
		List<LeaderFactory> list = leaderFactoryDao.getByHql(buffer.toString());
		if(null!=list&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

}
