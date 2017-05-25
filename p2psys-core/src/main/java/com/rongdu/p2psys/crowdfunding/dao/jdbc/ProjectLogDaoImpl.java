package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.ProjectLogDao;
import com.rongdu.p2psys.crowdfunding.domain.ProjectLog;
import com.rongdu.p2psys.crowdfunding.model.ProjectLogModel;

@Repository("projectLogDao")
public class ProjectLogDaoImpl extends BaseDaoImpl<ProjectLog> implements ProjectLogDao{

	@SuppressWarnings("unchecked")
	public List<ProjectLog> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<ProjectLog> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ProjectLog> getByProjectId(Long id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM ProjectLog WHERE projectId = ");
		buffer.append(id);
		buffer.append(" ORDER BY addTime  DESC ");
		Query query = em.createQuery(buffer.toString());
		List<ProjectLog> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ProjectLogModel> getNotices(String sql, long userId) {
		Query query = em.createNativeQuery(sql.toString(), ProjectLog.class);
		query.setParameter("userId",userId);

		if (query.getResultList().size() > 0) {
			List<ProjectLog> list =query.getResultList();
			List<ProjectLogModel> res = new ArrayList<ProjectLogModel>();
			for(ProjectLog projectLog:list){
				ProjectLogModel projectLogModel = ProjectLogModel.instance(projectLog);
				projectLogModel.setTypeName(getTypeName(projectLog.getType()));
				res.add(projectLogModel);
			}
			return res;
			
		} else {
			return null;
		}
	}

	private String getTypeName(int type){
		//日志类别 0 提交审核 1被驳回 2审核中3审核通过 4募集金额满 5募集过期
		if(0==type){
			return "提交审核中";
		}else if(1==type){
			return "被驳回";
		}else if(2==type){
			return "提交审核中";
		}else if(3==type){
			return "审核通过";
		}else if(4==type){
			return " 募集金额已满";
		}else if(5==type){
			return "募集过期";
		}else if(6==type){
			return "募集撤销";
		}else if(7==type){
			return "募集完成";
		}else{
			return  "募集过期";
		}
	}
}
