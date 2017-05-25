package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.Page;
import com.rongdu.p2psys.crowdfunding.dao.ProjectBaseinfoDao;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;

@Repository("projectBaseinfoDao")
public class ProjectBaseinfoDaoImpl extends BaseDaoImpl<ProjectBaseinfo>
		implements ProjectBaseinfoDao {

	@Override
	public PageDataList<ProjectBaseinfoModel> getListOfNewAdded(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {

		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("status", ProjectBaseinfoModel.STATUS_SAVED);

		PageDataList<ProjectBaseinfo> domainList = super.findPageList(param);
		PageDataList<ProjectBaseinfoModel> modelList = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		modelList.setPage(domainList.getPage());
		if (domainList.getList().size() > 0) {
			for (ProjectBaseinfo tempDomain : domainList.getList()) {
				ProjectBaseinfoModel tempModel = ProjectBaseinfoModel
						.instance(tempDomain);
				list.add(tempModel);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	@Override
	public PageDataList<ProjectBaseinfoModel> getListOfWaitingForApprove(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		// 待审核
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("status",
				ProjectBaseinfoModel.STATUS_WAITING_FOR_APPROVE);

		PageDataList<ProjectBaseinfo> domainList = super.findPageList(param);
		PageDataList<ProjectBaseinfoModel> modelList = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		modelList.setPage(domainList.getPage());
		if (domainList.getList().size() > 0) {
			for (ProjectBaseinfo tempDomain : domainList.getList()) {
				ProjectBaseinfoModel tempModel = ProjectBaseinfoModel
						.instance(tempDomain);
				list.add(tempModel);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	@Override
	public long saveProjectBaseinfo(ProjectBaseinfo baseinfo) {
		return save(baseinfo).getId();
	}

	// TODO

	@Override
	public PageDataList<ProjectBaseinfoModel> dataList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize, int type) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		if (type == 1)// 项目发标 保存和未通过
		{
			SearchFilter orFilter1 = new SearchFilter("status", Operators.EQ, 0);
			SearchFilter orFilter2 = new SearchFilter("status", Operators.EQ, 3);
			param.addOrFilter(orFilter1, orFilter2);
		} else if (type == 2)// 项目审核 1提交审核
		{
			param.addParam("status", 1);
		} else if (type == 3)// 项目管理 审核通过
		{
			param.addParam("status", 2);
		}
		PageDataList<ProjectBaseinfo> pageDataList = super.findPageList(param);
		PageDataList<ProjectBaseinfoModel> pageDataList_ = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				ProjectBaseinfo projectBaseinfo = (ProjectBaseinfo) pageDataList
						.getList().get(i);
				ProjectBaseinfoModel projectBaseinfoModel = ProjectBaseinfoModel
						.instance(projectBaseinfo);
				list.add(projectBaseinfoModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public PageDataList<ProjectBaseinfoModel> projectFailedList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {

		StringBuffer buffer = new StringBuffer(
				""
						+ " select p.* from ehb_zc_project_baseinfo p, rd_verify_log l where p.id=l.fid and l.type='crowdfunding' and "
						+ " p.end_time < now() and p.status = 2 and (p.planed_min_amount > "
						+ " (select  sum(money) from ehb_zc_invest_record where status = 3 and zc_projectid = p.id) "
						+ " or p.planed_min_investor > "
						+ " (select  count(distinct user_id) from ehb_zc_invest_record where  status = 3 and zc_projectid = p.id)) ");
		buffer.append(" order by p.id  desc");

		Query query = em.createNativeQuery(buffer.toString(),
				ProjectBaseinfo.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<ProjectBaseinfo> dataList = (List<ProjectBaseinfo>) query
				.getResultList();
		PageDataList<ProjectBaseinfoModel> pageDataList_ = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		pageDataList_.setPage(page);
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				ProjectBaseinfo projectBaseinfo = (ProjectBaseinfo) dataList
						.get(i);
				ProjectBaseinfoModel projectBaseinfoModel = ProjectBaseinfoModel
						.instance(projectBaseinfo);
				list.add(projectBaseinfoModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public PageDataList<ProjectBaseinfoModel> projectFullList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		StringBuffer buffer = new StringBuffer(
				" select b.* from ehb_zc_project_baseinfo b where b.planed_max_amount<= "
						+ " (select sum(r.money) from ehb_zc_invest_record r where r.status = 3 and r.zc_projectid = b.id)"
						+ " and b.status = 2  order by b.id  desc ");
		Query query = em.createNativeQuery(buffer.toString(),
				ProjectBaseinfo.class);
		Page page = new Page(query.getResultList().size(), pageNumber, pageSize);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<ProjectBaseinfo> dataList = (List<ProjectBaseinfo>) query
				.getResultList();
		PageDataList<ProjectBaseinfoModel> pageDataList_ = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		pageDataList_.setPage(page);
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				ProjectBaseinfo projectBaseinfo = (ProjectBaseinfo) dataList
						.get(i);
				ProjectBaseinfoModel projectBaseinfoModel = ProjectBaseinfoModel
						.instance(projectBaseinfo);
				list.add(projectBaseinfoModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public void updataBySql(String sql) {
		String[] strArray = new String[0];

		Object[] objectArray = new Object[0];

		updateBySql(sql, strArray, objectArray);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProjectBaseinfo> findDueProject() {
		String sql = " select * from ehb_zc_project_baseinfo where status in (2,4) and date(date_add(right_duetime, interval 1 day)) = curdate() ";
		Query query = em.createNativeQuery(sql, ProjectBaseinfo.class);
		@SuppressWarnings("rawtypes")
		List resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<ProjectBaseinfo> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<ProjectBaseinfo> list = query.getResultList();
		if (null != list && !list.isEmpty()) {
			return list;
		}
		return null;
	}

	@Override
	public PageDataList<ProjectBaseinfoModel> getCfFunction(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("status", ProjectBaseinfoModel.STATUS_APPROVED);

		PageDataList<ProjectBaseinfo> domainList = super.findPageList(param);
		PageDataList<ProjectBaseinfoModel> modelList = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		modelList.setPage(domainList.getPage());
		if (domainList.getList().size() > 0) {
			for (ProjectBaseinfo tempDomain : domainList.getList()) {
				ProjectBaseinfoModel tempModel = ProjectBaseinfoModel
						.instance(tempDomain);
				list.add(tempModel);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	public PageDataList<ProjectBaseinfoModel> getSuccessList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("status", ProjectBaseinfoModel.STATUS_CONFIRM);

		PageDataList<ProjectBaseinfo> domainList = super.findPageList(param);
		PageDataList<ProjectBaseinfoModel> modelList = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		modelList.setPage(domainList.getPage());
		if (domainList.getList().size() > 0) {
			for (ProjectBaseinfo tempDomain : domainList.getList()) {
				ProjectBaseinfoModel tempModel = ProjectBaseinfoModel
						.instance(tempDomain);
				list.add(tempModel);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	public PageDataList<ProjectBaseinfoModel> getFailList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("status",5);

		PageDataList<ProjectBaseinfo> domainList = super.findPageList(param);
		PageDataList<ProjectBaseinfoModel> modelList = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		modelList.setPage(domainList.getPage());
		if (domainList.getList().size() > 0) {
			for (ProjectBaseinfo tempDomain : domainList.getList()) {
				ProjectBaseinfoModel tempModel = ProjectBaseinfoModel
						.instance(tempDomain);
				list.add(tempModel);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	@Override
	public PageDataList<ProjectBaseinfoModel> getCacleList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("status",ProjectBaseinfoModel.STATUS_CANCEL);

		PageDataList<ProjectBaseinfo> domainList = super.findPageList(param);
		PageDataList<ProjectBaseinfoModel> modelList = new PageDataList<ProjectBaseinfoModel>();
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		modelList.setPage(domainList.getPage());
		if (domainList.getList().size() > 0) {
			for (ProjectBaseinfo tempDomain : domainList.getList()) {
				ProjectBaseinfoModel tempModel = ProjectBaseinfoModel
						.instance(tempDomain);
				list.add(tempModel);
			}
		}
		modelList.setList(list);
		return modelList;
	}
}
