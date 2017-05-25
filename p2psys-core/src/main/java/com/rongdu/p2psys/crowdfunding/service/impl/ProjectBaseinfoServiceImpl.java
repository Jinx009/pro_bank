package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.crowdfunding.dao.LeaderDao;
import com.rongdu.p2psys.crowdfunding.dao.ProfitRuleDao;
import com.rongdu.p2psys.crowdfunding.dao.ProjectBaseinfoDao;
import com.rongdu.p2psys.crowdfunding.dao.ProjectLogDao;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.domain.ProjectLog;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;
import com.rongdu.p2psys.crowdfunding.service.MaterialsService;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.account.dao.AccountLogDao;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;

@Service("projectBaseinfoService")
public class ProjectBaseinfoServiceImpl implements ProjectBaseinfoService {

	@Resource
	private ProjectBaseinfoDao projectBaseinfoDao;
	@Resource
	private ProfitRuleDao profitRuleDao;
	@Resource
	private VerifyLogDao verifyLogDao;
	@Resource
	private MaterialsService materialsService;
	@Resource
	private OrderService cfOrderService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private LeaderDao leaderDao;
	@Resource
	private UserDao theUserDao;
	@Resource
	private AccountLogDao theAccountLogDao;
	@Resource
	private ProjectLogDao projectLogDao;

	public PageDataList<ProjectBaseinfoModel> getListOfNewAdded(ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		return projectBaseinfoDao.getListOfNewAdded(model, pageNumber, pageSize);
	}

	public PageDataList<ProjectBaseinfoModel> getListOfWaitingForApprove(ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		return projectBaseinfoDao.getListOfWaitingForApprove(model, pageNumber,pageSize);
	}

	public ProjectBaseinfo getProjectBaseinfoById(Long id) {
		return projectBaseinfoDao.find(ProjectBaseinfo.class, id);
	}

	
	public ProjectBaseinfoModel find(Long id) {
		ProjectBaseinfo baseinfo = projectBaseinfoDao.find(ProjectBaseinfo.class, id);
		ProjectBaseinfoModel baseinfoModel = ProjectBaseinfoModel.instance(baseinfo);

		return baseinfoModel;
	}

	/**
	 * 保存项目
	 */
	public long saveProjectBaseinfo(ProjectBaseinfo baseinfo) {
		return projectBaseinfoDao.saveProjectBaseinfo(baseinfo);
	}

	/**
	 * 更新项目
	 */
	public void update(ProjectBaseinfo projectBaseinfo) {
		projectBaseinfoDao.update(projectBaseinfo);
	}

	public void verifyProject(ProjectBaseinfo project, Operator operator) {
		projectBaseinfoDao.update(project);
		VerifyLog verifyLog = new VerifyLog(operator, "crowdfunding",
				project.getId(), 1, project.getStatus(), "众筹审核");
		verifyLogDao.save(verifyLog);
	}

	/**
	 * 初审通过项目列表
	 */
	public PageDataList<ProjectBaseinfoModel> list(ProjectBaseinfoModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(),model.getRows());
		if (model.getType() != -1) {
			param.addParam("type", model.getType());
		}
		param.addParam("status", "2");
		param.addParam("endTime", Operators.GTE, new Date());

		PageDataList<ProjectBaseinfo> pageList = projectBaseinfoDao.findPageList(param);
		List<ProjectBaseinfoModel> list = new ArrayList<ProjectBaseinfoModel>();
		PageDataList<ProjectBaseinfoModel> pageList_ = new PageDataList<ProjectBaseinfoModel>();

		for (ProjectBaseinfo projectBaseinfo : pageList.getList()) {
			QueryParam param2 = QueryParam.getInstance().addParam("projectBaseinfo", projectBaseinfo);
			List<Materials> materialsList = materialsService.findByCriteria(param2);

			ProjectBaseinfoModel projectBaseinfoModel = ProjectBaseinfoModel.instance(projectBaseinfo);
			if (materialsList != null) {
				// projectBaseinfoModel.setMaterials(materialsList);
			}
			list.add(projectBaseinfoModel);
		}
		pageList_.setList(list);
		pageList_.setPage(pageList.getPage());
		return pageList_;
	}

	/**
	 * 项目列表 分页
	 */
	public PageDataList<ProjectBaseinfoModel> dataList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize, int type) {
		if (type == 4) {
			return projectBaseinfoDao.projectFailedList(model, pageNumber,pageSize);
		} else if (type == 5) {
			return projectBaseinfoDao.projectFullList(model, pageNumber,pageSize);
		} else {
			return projectBaseinfoDao.dataList(model, pageNumber, pageSize,type);
		}
	}

	/**
	 * PC项目列表
	 */
	public List<ProjectBaseinfoModel> getByTypePc(int type) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("  FROM ProjectBaseinfo where isShowPc = 1 and (status = 2 or status = 3) and type =  ");
		buffer.append(type);
		buffer.append("  order by is_recommend,end_time  ");
		
		List<ProjectBaseinfo> list = projectBaseinfoDao.getByHql(buffer.toString());
		List<ProjectBaseinfoModel> modelList = null;
		if (null != list) {
			modelList = new ArrayList<ProjectBaseinfoModel>();
			for (int i = 0; i < list.size(); i++) {
				ProjectBaseinfoModel model = new ProjectBaseinfoModel();

				ProjectBaseinfo base = list.get(i);
				List<Materials> materials = materialsService.findByProjectId(base.getId());
				model = ProjectBaseinfoModel.instance(base);
				model.setMaterialsList(materials);
				model.setTimeStatus(checkDate(base));
				modelList.add(model);
			}
		}

		return modelList;
	}

	/**
	 * 获取项目真实状态
	 * @param base
	 * @return
	 */
	private static int checkDate(ProjectBaseinfo base) {
		Date nowdate = new Date();
		Date s = base.getStartTime();
		Date e = base.getEndTime();

		boolean sflag = nowdate.before(s);
		boolean eflag = e.before(nowdate);
		if (3 == base.getStatus()) {
			return 4;
		}
		if (sflag) {
			return 1;// 预热
		}
		if (eflag) {
			return 3;// 失败
		}
		return 2;// 众筹中
	}

	/**
	 * 产品详情
	 */
	public ProjectBaseinfoModel getById(Long projectId) {
		ProjectBaseinfo projectBaseinfo = projectBaseinfoDao.find(projectId);
		ProjectBaseinfoModel projectBaseinfoModel = ProjectBaseinfoModel.instance(projectBaseinfo);
		List<Materials> materials = materialsService.findByProjectId(projectBaseinfo.getId());
		projectBaseinfoModel.setTimeStatus(checkDate(projectBaseinfo));
		projectBaseinfoModel.setMaterialsList(materials);
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM ProfitRule WHERE  projectId =");
		buffer.append(projectId);
		List<ProfitRule> profitRuleList = profitRuleDao.getByHql(buffer.toString());
		buffer = new StringBuffer();
		buffer.append(" FROM Leader WHERE project.id =");
		buffer.append(projectId);
		buffer.append(" AND status = 1 ");
		List<Leader> list = leaderDao.getByHql(buffer.toString());
		if(null!=list&&!list.isEmpty()){
			projectBaseinfoModel.setLeader(list.get(0));
		}else{
			projectBaseinfoModel.setLeader(null);
		}
		projectBaseinfoModel.setProfitRuleList(profitRuleList);

		return projectBaseinfoModel;
	}

	/**
	 * 正在众筹中的产品列表
	 */
	public PageDataList<ProjectBaseinfoModel> getFunctionList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		return projectBaseinfoDao.getCfFunction(model, pageNumber, pageSize);
	}

	/**
	 * 众筹成功
	 */
	public void confirmProject(Long id) {
		ProjectBaseinfo pojo = projectBaseinfoDao.find(id);
		pojo.setStatus(ProjectBaseinfoModel.STATUS_CONFIRM);
		projectBaseinfoDao.update(pojo);
	}

	/**
	 * 撤销项目
	 */
	public void cancelProject(Long id) {
		ProjectBaseinfo pojo = projectBaseinfoDao.find(id);
		pojo.setStatus(ProjectBaseinfoModel.STATUS_CANCEL);
		projectBaseinfoDao.update(pojo);

		List<OrderModel> orderModelList = cfOrderService.getList(id);
		if(null!=orderModelList&&!orderModelList.isEmpty()){
			for (OrderModel orderModel : orderModelList) {
				if(0!=orderModel.getPayStatus()){
					InvestOrder order = cfOrderService.find(orderModel.getId());
					order.setPayStatus(3);
					cfOrderService.updateOrder(order);
					Account account = theAccountService.getAccountByUserId(orderModel.getUser().getUserId());
					account.setTotal(BigDecimalUtil.add(account.getTotal(),orderModel.getPayMoney()));
					account.setUseMoney(BigDecimalUtil.add(account.getUseMoney(),orderModel.getPayMoney()));
					theAccountService.updateAccount(account);
					
					AccountLog accountLog = new AccountLog();
					accountLog.setAddTime(new Date());
					accountLog.setCollection(account.getCollection());
					accountLog.setMoney(orderModel.getPayMoney());
					accountLog.setNoUseMoney(account.getNoUseMoney());
					accountLog.setPaymentsType((byte) 0);
					accountLog.setRemark(pojo.getProjectName()+"项目撤回返回您的支付金额"+orderModel.getPayMoney());
					accountLog.setTotal(account.getTotal());
					accountLog.setToUser(theUserDao.find(1l));
					accountLog.setType("invest");
					accountLog.setUseMoney(account.getUseMoney());
					accountLog.setUser(orderModel.getUser());
					theAccountLogDao.save(accountLog);
				}
			}
		}
	}

	/**
	 * PC端热销榜
	 */
	public List<ProjectBaseinfoModel> getPopularPc() {
		String hql = " FROM ProjectBaseinfo where isShowPc = 1 and (status = 2 or status = 3) and pcIndexStatus = 1  order by isRecommend DESC,addTime DESC ";
		List<ProjectBaseinfo> list = projectBaseinfoDao.getByHql(hql);
		List<ProjectBaseinfoModel> modelList = null;
		if (null != list&&!list.isEmpty()) {
			modelList = new ArrayList<ProjectBaseinfoModel>();
			for (int i = 0; i < list.size(); i++) {
				ProjectBaseinfoModel model = new ProjectBaseinfoModel();

				ProjectBaseinfo base = list.get(i);
				List<Materials> materials = materialsService
						.findByProjectId(base.getId());
				model = ProjectBaseinfoModel.instance(base);
				model.setMaterialsList(materials);
				model.setTimeStatus(checkDate(base));
				modelList.add(model);
			}
		}
		return modelList;	
	}

	/**
	 * 微信端热销榜
	 */
	public List<ProjectBaseinfoModel> getPopularWechat() {
		String hql = " FROM ProjectBaseinfo where isShowWechat = 1 and (status = 2 or status = 3) and wechatIndexStatus = 1  order by isRecommend DESC,addTime DESC ";
		List<ProjectBaseinfo> list = projectBaseinfoDao.getByHql(hql);
		List<ProjectBaseinfoModel> modelList = null;
		if (null != list&&!list.isEmpty()) {
			modelList = new ArrayList<ProjectBaseinfoModel>();
			for (int i = 0; i < list.size(); i++) {
				ProjectBaseinfoModel model = new ProjectBaseinfoModel();

				ProjectBaseinfo base = list.get(i);
				List<Materials> materials = materialsService
						.findByProjectId(base.getId());
				model = ProjectBaseinfoModel.instance(base);
				model.setMaterialsList(materials);
				model.setTimeStatus(checkDate(base));
				modelList.add(model);
			}
		}
		return modelList;	
	}
	
	/**
	 * 项目时间到期 定时任务
	 */
	public void changeType() {
		String hql = " FROM ProjectBaseinfo where status =2  ";
		List<ProjectBaseinfo> list = projectBaseinfoDao.getByHql(hql);
		if(null!=list&&!list.isEmpty()){
			for(int i = 0;i<list.size();i++){
				ProjectBaseinfo projectBaseinfo = list.get(i);
				Date nowDate = new Date();
				if(nowDate.after(projectBaseinfo.getEndTime())){
					projectBaseinfo.setStatus(5);
					System.out.println("定时任务:更新产品id="+projectBaseinfo.getId());
					ProjectLog projectLog = new ProjectLog();
					projectLog.setAddTime(new Date());
					projectLog.setContent("项目：【"+projectBaseinfo.getProjectName()+"】到期未募集成功。");
					projectLog.setProjectId(projectBaseinfo.getId());
					projectLog.setType(5);
					projectLogDao.save(projectLog);
					projectBaseinfoDao.update(projectBaseinfo);
					List<OrderModel> orderModelList = cfOrderService.getList(projectBaseinfo.getId());
					if(null!=orderModelList&&!orderModelList.isEmpty()){
						for (OrderModel orderModel : orderModelList) {
							InvestOrder order = cfOrderService.find(orderModel.getId());
							order.setPayStatus(3);
							cfOrderService.updateOrder(order);
							Account account = theAccountService.getAccountByUserId(orderModel.getUser().getUserId());
							account.setTotal(BigDecimalUtil.add(account.getTotal(),orderModel.getPayMoney()));
							account.setUseMoney(BigDecimalUtil.add(account.getUseMoney(),orderModel.getPayMoney()));
							theAccountService.updateAccount(account);
							
							AccountLog accountLog = new AccountLog();
							accountLog.setAddTime(new Date());
							accountLog.setCollection(account.getCollection());
							accountLog.setMoney(orderModel.getPayMoney());
							accountLog.setNoUseMoney(account.getNoUseMoney());
							accountLog.setPaymentsType((byte) 0);
							accountLog.setRemark(projectBaseinfo.getProjectName()+"项目过期返回您的支付金额"+orderModel.getPayMoney());
							accountLog.setTotal(account.getTotal());
							accountLog.setToUser(theUserDao.find(1l));
							accountLog.setType("invest");
							accountLog.setUseMoney(account.getUseMoney());
							accountLog.setUser(orderModel.getUser());
							theAccountLogDao.save(accountLog);
						}
					}
				}
			}
		}
	}

	/**
	 * 众筹成功项目列表
	 */
	public PageDataList<ProjectBaseinfoModel> successList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		return projectBaseinfoDao.getSuccessList(model, pageNumber, pageSize);
	}

	/**
	 * 已经撤销项目列表
	 */
	public PageDataList<ProjectBaseinfoModel> cacleList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		return projectBaseinfoDao.getCacleList(model, pageNumber, pageSize);
	}

	/**
	 * 众筹失败项目列表
	 */
	public PageDataList<ProjectBaseinfoModel> failList(
			ProjectBaseinfoModel model, int pageNumber, int pageSize) {
		return projectBaseinfoDao.getFailList(model, pageNumber, pageSize);
	}

	/**
	 * 微信首页项目列表
	 */
	public List<ProjectBaseinfoModel> getWechatByType(Integer id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM ProjectBaseinfo WHERE isShowWechat = 1 and (status = 2 or status = 3) and type=");
		buffer.append(id);
		buffer.append(" order by is_recommend,end_time ");
		List<ProjectBaseinfo> list = projectBaseinfoDao.getByHql(buffer.toString());
		List<ProjectBaseinfoModel> modelList = null;
		if (null != list&&!list.isEmpty()) {
			modelList = new ArrayList<ProjectBaseinfoModel>();
			for (int i = 0; i < list.size(); i++) {
				ProjectBaseinfoModel model = new ProjectBaseinfoModel();

				ProjectBaseinfo base = list.get(i);
				List<Materials> materials = materialsService.findByProjectId(base.getId());
				model = ProjectBaseinfoModel.instance(base);
				model.setMaterialsList(materials);
				model.setTimeStatus(checkDate(base));
				modelList.add(model);
			}
		}
		return modelList;	
	}

	/**
	 * 客户用待审核项目
	 */
	public List<ProjectBaseinfoModel> getWaitList(User user) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM ProjectBaseinfo WHERE ( status = -1 or status = 1 ) AND userId =");
		buffer.append(user.getUserId());
		List<ProjectBaseinfo> list = projectBaseinfoDao.getByHql(buffer.toString());
		List<ProjectBaseinfoModel> res = null;
		if(null!=list&&!list.isEmpty()){
			 res = new ArrayList<ProjectBaseinfoModel>();
			for(ProjectBaseinfo projectBaseinfo :list){
				ProjectBaseinfoModel projectBaseinfoModel = ProjectBaseinfoModel.instance(projectBaseinfo);
				List<ProjectLog> logs = projectLogDao.getByProjectId(projectBaseinfo.getId());
				projectBaseinfoModel.setLog(logs);
				res.add(projectBaseinfoModel);
			}
		}
		return res;
	}

	/**
	 * 提交审核中的数据
	 */
	public List<ProjectBaseinfoModel> getAuditing(User user) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM ProjectBaseinfo WHERE status=0 AND userId =");
		buffer.append(user.getUserId());
		List<ProjectBaseinfo> list = projectBaseinfoDao.getByHql(buffer.toString());
		List<ProjectBaseinfoModel> res = null;
		if(null!=list&&!list.isEmpty()){
			 res = new ArrayList<ProjectBaseinfoModel>();
			for(ProjectBaseinfo projectBaseinfo :list){
				ProjectBaseinfoModel projectBaseinfoModel = ProjectBaseinfoModel.instance(projectBaseinfo);
				List<ProjectLog> logs = projectLogDao.getByProjectId(projectBaseinfo.getId());
				projectBaseinfoModel.setLog(logs);
				res.add(projectBaseinfoModel);
			}
		}
		return res;
	}

	public List<ProjectBaseinfo> getMyDream(User user) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM ProjectBaseinfo WHERE userId =");
		buffer.append(user.getUserId());
		buffer.append(" AND status = 2 ORDER BY startTime DESC ");
		List<ProjectBaseinfo> list = projectBaseinfoDao.getByHql(buffer.toString());
		return list;
	}
}
