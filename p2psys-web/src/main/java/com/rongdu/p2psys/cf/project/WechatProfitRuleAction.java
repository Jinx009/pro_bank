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
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.ProfitRuleModel;
import com.rongdu.p2psys.crowdfunding.service.AttentionListService;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProfitRuleService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 微信项目权益
 * @author Jinx
 *
 */
public class WechatProfitRuleAction extends BaseAction<ProfitRuleModel> implements ModelDriven<ProfitRuleModel>{

	private Map<String,Object> data;
	
	@Resource
	private ProfitRuleService profitRuleService;
	@Resource
	private AttentionListService attentionListService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private OrderService cfOrderService;
	
	/**
	 * 权益详情 -- 页面
	 */
	@Action(value="/cf/wechat/user/rightsDetail",results={@Result(name="rights_detail",type="ftl",location="/nb/cf/wechat/pro/rights_detail.html")})
	public String projectRights(){
		long projectId = paramLong("projectId");
		request.setAttribute("projectId", projectId);
		return "rights_detail";
	}
	
	/**
	 * 权益详情数据 -- 数据
	 * @throws IOException 
	 */
	@Action(value="/cf/wechat/user/rightData")
	public void projectRightList() throws IOException{
		long projectId = paramLong("projectId");
		User user = getNBSessionUser();
		data = new HashMap<String,Object>();
		if(projectId >0){
			List<ProfitRuleModel> list = profitRuleService.findByProjectIdforWechat(projectId);
			List<AttentionList> attentionList = attentionListService.getByDoubleId(projectId, user.getUserId());
			
			if(null !=list && !list.isEmpty()){
				Map<String,Object> result = new HashMap<String,Object>();
				int flage =0;
				if(null != attentionList && !attentionList.isEmpty()){
					flage =1;
				}
				ProjectBaseinfo projectBaseinf = projectBaseinfoService.getProjectBaseinfoById(projectId);
				
				
				if(null != projectBaseinf){
					result.put("showSupport", checkDate(projectBaseinf));
					if(projectBaseinf.getType() == 2){//股权众筹
						List<InvestOrder> orderList = cfOrderService.getInvestOrderById(user.getUserId(), projectId);
						if(null !=orderList &&  !orderList.isEmpty()){
							result.put("showSupport", 5);//股权已支持
						}
					}
					result.put("ProjectType", projectBaseinf.getType());
					
				}else{
					result.put("showSupport", 0);
				}
				result.put("list", list);
				result.put("attetionFlage", flage);
				data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
				data.put(ConstantUtil.MSG, ConstantUtil.SUCCESS);
				data.put(ConstantUtil.DATA,result);
			}else{
				data.put(ConstantUtil.CODE,ConstantUtil.DATA_IS_NULL);
				data.put(ConstantUtil.MSG,"没有数据对应");
			}
			
		}else{
			data.put(ConstantUtil.CODE,ConstantUtil.ILLEGAL_PARAMETER);
			data.put(ConstantUtil.MSG,"非合法参数");
		}
		printWebJson(getStringOfJpaObj(data));
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

	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
