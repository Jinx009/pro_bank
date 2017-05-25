package com.rongdu.p2psys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.FinanceArticle;
import com.rongdu.p2psys.core.domain.FinanceArticleExpert;
import com.rongdu.p2psys.core.model.FinanceArticleModel;
import com.rongdu.p2psys.core.model.FinanceSiteModel;
import com.rongdu.p2psys.core.service.FinanceArticleService;
import com.rongdu.p2psys.core.service.FinanceSiteService;
import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class SchoolAction extends BaseAction {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SchoolAction.class);
	
	@Resource
	private FinanceSiteService financeSiteService;
	
	@Resource
	private FinanceArticleService financeArticleService;
	
	Map<String, Object> data;
	
	/**
	 * 商学院首页 
	 * @return
	 */
	@Action("/lcschool/index")
	public String index() {
		return "schoolIndex";
	}
	
	/**
	 * 显示商学院栏目列表
	 * @throws Exception
	 */
	@Action(value = "/lcschool/showFinanceSiteList")
	public void showFinanceSiteList() throws Exception {
		data = new HashMap<String, Object>();
		List<FinanceSiteModel> financeSiteModelList = financeSiteService.showFinanceSiteList();
		data.put("financeSiteModelList", financeSiteModelList);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
		
	}
	
	/**
	 * 专家观点
	 * @return
	 */
	@Action("/lcschool/view")
	public String view() {
		return "view";
	}
	
	/**
	 * 专家观点详情
	 * @return
	 */
	@Action("/lcschool/detail")
	public String viewDetail() {
		return "detail";
	}
	
	/**
	 * 专家介绍
	 * @return
	 */
	@Action("/lcschool/expert")
	public String expert() {
		return "expert";
	}
	
	/**
	 * 进入栏目
	 * @throws Exception
	 */
	@Action("/lcschool/enterFinanceSite")
	public void enterFinanceSite() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = 12; // 每页每页总数
		long financeSiteId = paramLong("financeSiteId");
		PageDataList<FinanceArticleModel> pageDataList = financeArticleService.enterFinanceSite(pageNumber, pageSize, financeSiteId);
		data.put("data", pageDataList);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 专家观点（显示文章和专家信息）
	 * @throws Exception
	 */
	@Action("/lcschool/expertOpinion")
	public void expertOpinion() throws Exception {
		data = new HashMap<String, Object>();
		long financeArticleId = paramLong("financeArticleId");
		FinanceArticle financeArticle = financeArticleService.find(financeArticleId);
		data.put("financeArticle", financeArticle);
		data.put("finance_site_id", financeArticle.getFinanceSite().getId());
		//专家介绍
		FinanceArticleExpert financeArticleExpert = financeArticleService.getFinanceArticleExpertById(financeArticle.getFinanceArticleExpert().getId());
		data.put("financeArticleExpert", financeArticleExpert);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 专家介绍
	 * @throws Exception
	 */
	@Action("/lcschool/expertIntroduce")
	public void expertIntroduce() throws Exception {
		data = new HashMap<String, Object>();
		long financeSiteId = paramLong("financeSiteId");
		List<FinanceArticleExpert> list = financeArticleService.expertIntroduce(financeSiteId);
		data.put("list", list);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}
	
	@Action("/lcschool/financeArticleList")
	public void financeArticleList() throws Exception {
		data = new HashMap<String, Object>();
		long financeArticleId = paramLong("financeArticleId");
		FinanceArticle financeArticle = financeArticleService.find(financeArticleId);
		//最新专栏文章
		List<FinanceArticle> financeArticleList = financeArticleService.getNewFinanceArticle(financeArticle.getFinanceSite());
		data.put("financeArticleList", financeArticleList);

		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 获取单个专家
	 * @throws Exception
	 */
	@Action("/lcschool/expertDetail")
	public void expertDetail() throws Exception {
		data = new HashMap<String, Object>();
		long expertId = paramLong("expertId");
		FinanceArticleExpert expert = financeArticleService.findExpertById(expertId);
		data.put("expert", expert);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}
}
