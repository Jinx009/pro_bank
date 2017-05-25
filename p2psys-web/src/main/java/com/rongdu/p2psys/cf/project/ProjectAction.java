package com.rongdu.p2psys.cf.project;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.AttentionList;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.AttentionListModel;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;
import com.rongdu.p2psys.crowdfunding.service.AttentionListService;
import com.rongdu.p2psys.crowdfunding.service.LeaderService;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;

/**
 * 
 * @author Jinx
 *
 */
public class ProjectAction extends BaseAction<ProjectBaseinfo> implements ModelDriven<ProjectBaseinfo> {

	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private OrderService cfOrderService;
	@Resource
	private AttentionListService attentionListService;
	@Resource
	private LeaderService leaderService;
	
	private Map<String, Object> data;
	
	/**
	 * 添加关注
	 * @throws IOException
	 */
	@Action(value = "/cf/pro/addAttention")
	public void addAttention() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		User user = getNBSessionUser();
		Long projectId = paramLong("id");
		List<AttentionList> list = attentionListService.getByDoubleId(projectId,user.getUserId());
		if(null!=list){
			data.put(ConstantUtil.ERRORMSG,"您已经关注过此项目!");
		}else{
			AttentionList attentionList = new AttentionList();
			attentionList.setProjectId(projectId);
			attentionList.setUserId(user.getUserId());
			attentionListService.save(attentionList);
			
			ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
			projectBaseinfo.setAttentionNum(projectBaseinfo.getAttentionNum()+1);
			projectBaseinfoService.update(projectBaseinfo);
			
			data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			data.put(ConstantUtil.ERRORMSG,projectBaseinfo.getAttentionNum());
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 我的关注页面
	 * @return
	 */
	@Action(value="/cf/user/myAttention",results={@Result(name="main_list_attention",type="ftl",location="/nb/cf/user/main_list_attention.html")})
	public String myAttentionPage(){
		return "main_list_attention";
	}
	
	/**
	 * 添加关注
	 * @throws IOException
	 */
	@Action(value = "/cf/pro/myAttentionData")
	public void myAttention() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		
		User user = getNBSessionUser();
		List<AttentionListModel> list = attentionListService.getByUserId(user.getUserId());
		data.put(ConstantUtil.ERRORMSG,list) ;
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 增加喜欢人数
	 * @throws IOException
	 */
	@Action(value = "/cf/pro/addLike")
	public void addLike() throws IOException{
		Long projectId = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		projectBaseinfo.setLikeNum(projectBaseinfo.getLikeNum()+1);
		projectBaseinfoService.update(projectBaseinfo);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,projectBaseinfo.getLikeNum());
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * PC产品列表
	 * @return
	 */
	@Action(value="/pro/list",results={@Result(name="list",type="ftl",location="/nb/cf/pro/list.html")})
	public String pcList(){
		request.setAttribute("type",paramString("id"));
		return "list";
	}
	
	/**
	 * PC产品详情
	 * @return
	 */
	@Action(value="/pro/detail",results={@Result(name="detail",type="ftl",location="/nb/cf/pro/detail.html"),
										 @Result(name="meet",type="ftl",location="/nb/cf/user/meet.html")})
	public String pcDetail(){
		Long projectId = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.find(projectId);
		User user = getNBSessionUser();
		List<AttentionList> list = attentionListService.getByDoubleId(projectId, user.getUserId());
		if(null!=list){
			request.setAttribute("attention",list.size());
		}else{ 
			request.setAttribute("attention",0);
		}
		request.setAttribute("projectType",getProjectName(projectBaseinfo.getType()));
		request.setAttribute("projectUrl","/pro/list.html?id="+projectBaseinfo.getType());
		if(null!=user){
			UserCache userCache = user.getUserCache();
			if(1!=userCache.getInvestStatus()){
				request.setAttribute("redirectUrl","/pro/detail.html?id="+projectBaseinfo.getId());
				request.setAttribute("invester",userCache.getInvestStatus());
				return "meet";
			}
		}
		request.setAttribute("id",projectId);
		request.setAttribute("projectBasicinfo",projectBaseinfo);
//		request.setAttribute("timeStatus", checkDate(projectBaseinfo));
		int leaderStatus = -1;
		request.setAttribute("leaderStatus", leaderStatus);
		List<Leader> leaderList = leaderService.getByProjectId(projectId);
		if(null !=leaderList && !leaderList.isEmpty()){

			for (Leader leader : leaderList) {
				if(user.getUserId() == leader.getUser().getUserId()){
					if(leader.getStatus() == 1){
						leaderStatus = 1;
						break;
					}
					leaderStatus  = 0;
				}else{
					leaderStatus  = -1;
				}
			}
			request.setAttribute("leaderStatus",leaderStatus);
//			if(projectBaseinfo.getLeader().getStatus() == 1){//有领投人
//				request.setAttribute("leaderStatus", 1);
//			}else if(projectBaseinfo.getLeader().getStatus() == 0){
//				if(user.getUserId() == projectBaseinfo.getLeader().getUser().getUserId()){
//					request.setAttribute("leaderStatus", 0);
//				}else{
//					request.setAttribute("leaderStatus", -1);
//				}
//			}
		}
		return "detail";
	}
	
	/**
	 * Pc产品列表
	 * @throws IOException
	 */
	@Action(value = "/p/list")
	public void listPc() throws IOException{
		List<ProjectBaseinfoModel> list = projectBaseinfoService.getByTypePc(paramInt("id"));
		data = new HashMap<String,Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG, list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * Pc首页热销榜
	 * @throws IOException
	 */
	@Action(value = "/cf/popular/pc")
	public void pcPopular() throws IOException{
		List<ProjectBaseinfoModel> list = projectBaseinfoService.getPopularPc();
		
		data = new HashMap<String,Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG, list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 微信首页热销榜
	 * @throws IOException
	 */
	@Action(value = "/cf/popular/wechat")
	public void wechatPopular() throws IOException{
		List<ProjectBaseinfoModel> list = projectBaseinfoService.getPopularWechat();
		
		data = new HashMap<String,Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG, list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * Pc产品列表
	 * @throws IOException
	 */
	@Action(value = "/p/detail")
	public void detailPc() throws IOException{
		ProjectBaseinfoModel projectBaseinfoModel = projectBaseinfoService.getById(paramLong("id"));
		data = new HashMap<String,Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,projectBaseinfoModel);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 微信首页产品
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/projectIndex")
	public void wechatIndexProject() throws IOException{
		Integer id = paramInt("id");
		List<ProjectBaseinfoModel> list = projectBaseinfoService.getWechatByType(id);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		printWebJson(getStringOfJpaObj(data));
	} 
	
	
	private static String getProjectName(int type){
		if(2==type){
			return "股权众筹";
		}else if(1==type){
			return "实物众筹";
		}else if(4==type){
			return "公益众筹";
		}
		return "";
	}
	
	/**
	 * 获取项目真实状态
	 * @param base
	 * @return
	 */
	@SuppressWarnings("unused")
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
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
