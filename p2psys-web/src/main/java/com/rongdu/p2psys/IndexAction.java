package com.rongdu.p2psys;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.alibaba.fastjson.JSON;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.PayDataBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.RetBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.UnllBankRet;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.NewTenderModel;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Site;
import com.rongdu.p2psys.core.model.ArticleModel;
import com.rongdu.p2psys.core.model.FinanceSiteModel;
import com.rongdu.p2psys.core.model.RankModel;
import com.rongdu.p2psys.core.model.SiteModel;
import com.rongdu.p2psys.core.rule.IndexRuleCheck;
import com.rongdu.p2psys.core.service.ArticleService;
import com.rongdu.p2psys.core.service.FinanceSiteService;
import com.rongdu.p2psys.core.service.SiteService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.ppfund.service.PpfundService;
import com.rongdu.p2psys.user.domain.User;
//import com.rongdu.p2psys.user.service.UserService;
import com.rongdu.p2psys.user.service.UserIdentifyService;

/**
 * 首页
 * 
 * @author
 * @version 1.0
 * @since 2014年3月4日
 */
@SuppressWarnings("rawtypes")
public class IndexAction extends BaseAction implements ServletRequestAware {

	@Resource
	private BorrowTenderService tenderService;
	@Resource
	private SiteService siteNewService;
	@Resource
	private ArticleService articleService;
//	@Resource
//	private UserService userService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private AccountService accountService;
	@Resource
	private PpfundService ppfundService;
	@Resource
	private PpfundInService ppfundInService;
	@Resource
	private FinanceSiteService financeSiteService;
	@Resource
	private IOrderService orderService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private UserService theUserService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private UserIdentifyService userIdentifyService;

	private IndexRuleCheck indexRuleCheck = (IndexRuleCheck) Global.getRuleCheck("index");

	private Map<String, Object> data;
	private User user;

	@Action("/index2")
	public String index() throws Exception {
		return "investIndex";
	}
	
	@Action("/activity")
	public String activity() throws Exception {
		return "activity";
	}
	
	@Action("/index/indexStatistics")
	public void indexStatistics() throws Exception {
		data = new HashMap<String, Object>();
		QueryParam param = QueryParam.getInstance();
		//注册人数
		int userCount = this.theUserService.count(param);
		data.put("userCount", userCount);
		
		//借款人数
		int borrowUserCount = this.borrowService.getBorrowUserCount();
		data.put("borrowUserCount", borrowUserCount);
		
		Object[] object = this.borrowService.countByFinish();
		Object[] ppfundObject = this.ppfundService.countByFinish();
		if (object != null) {
			//已完成借款标数量
			int borrowCount = NumberUtil.getInt((object[0]==null?0:object[0]).toString());
			int ppfundCount = NumberUtil.getInt((ppfundObject[0]==null?0:ppfundObject[0]).toString());
			int count = borrowCount + ppfundCount;
			data.put("borrowFinishCount", count);

			//已完成借款标金额
			double borrowFinishMoney = NumberUtil.getDouble(((object[1]==null?0:object[1]).toString()));
			double ppfundFinishMoney = NumberUtil.getDouble(((ppfundObject[1]==null?0:ppfundObject[1]).toString()));
			double finishMoney = borrowFinishMoney + ppfundFinishMoney;
			data.put("borrowFinishMoney", finishMoney);
			
			//平均年化利率
			double borrowAvgApr = NumberUtil.getDouble(((object[2]==null?12:object[2])).toString());
			double ppfunAvgApr = NumberUtil.getDouble(((ppfundObject[2]==null?12:ppfundObject[2]).toString()));
			double avgApr = (borrowAvgApr + ppfunAvgApr)/2;
			data.put("averageApr", avgApr);
			
			//已还利息总额
			double ppfundOutInterest = ppfundInService.outInterest();
			double borrowYesInterest = NumberUtil.getDouble(((object[3]==null?0:object[3]).toString()));
			double yesInterest = ppfundOutInterest + borrowYesInterest;
			data.put("repayYesInterest", yesInterest);
		}
		//账户总额
		double accountTotal = accountService.getAllTotal();
		data.put("accountTotal", accountTotal);
		printWebJson(getStringOfJpaObj(data));
	}

	@Action("/investIndex")
	public String investIndex() throws Exception {
		return "investIndex";
	}
	
	@Action("/mainIndex")
	public String mainIndex() throws Exception {
		return "mainIndex";
	}
	
	@Action("/yardIndex")
	public String yardIndex() throws Exception {
		return "yardIndex";
	}
	
	@Action("/index/articleList")
	public void articleList() throws Exception {
		String nid = paramString("nid");
		Site site = siteNewService.findByNid(nid);
		SiteModel m = SiteModel.instance(site, nid);
		m.validSiteForArticleList();
		List<ArticleModel> articleList = articleService.listBySize(nid, site.getSize());
		data = new HashMap<String, Object>();
		data.put("data", articleList);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 获得最新的栏目
	 * @throws Exception
	 */
	@Action("/index/indexFinanceSite")
	public void indexFinanceSite() throws Exception {
		data = new HashMap<String, Object>();
		FinanceSiteModel model = financeSiteService.getNewFinanceSite();
		data.put("data", model);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 联系我们页面
	 * @throws Exception
	 */
	@Action("/aboutUs/contactUs")
	public String contactUs() throws Exception {
		return "contactUs";
	}
	/**
	 * 产品介绍
	 * @throws Exception
	 */
	@Action("/aboutUs/productsIntro")
	public String productsIntro() throws Exception {
		return "productsIntro";
	}
	/**
	 * 关于我们页面
	 * @throws Exception
	 */
	@Action("/aboutUs/siteIntro")
	public String siteIntro() throws Exception {
		return "siteIntro";
	}

	/**
	 * 理财问答
	 * @throws Exception
	 */
	@Action("/aboutUs/questionIntro")
	public String questionIntro() throws Exception {
		return "questionIntro";
	}
	
	/**
	 * 正在招标
	 * 
	 * @return String
	 * @throws Exception if has error
	 */
	@Action("/indexJson")
	public String indexJson() throws Exception {
		BorrowModel model = new BorrowModel();
		int size = Global.getInt("index_borrow_count");
		size = size > 0 ? size : 4;
		model.setSize(size);
		model.setStatus(-2);
		model.setScales(101); //标记为不显示用户信息
		PageDataList<BorrowModel> pageData = borrowService.getList(model);
		data = new HashMap<String, Object>();
		data.put("data", pageData);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}
	
	/**
	 * 成功贷款
	 * 
	 * @return String
	 * @throws Exception if has error
	 */
	@Action("/index/borrowSuccess")
	public String borrowSuccess() throws Exception {
		data = new HashMap<String, Object>();
		if (indexRuleCheck.success == 1) {
			/*
			 * List<Borrow> successList = borrowService.getSuccessListForIndex(); data.put("data", successList);
			 */
			printWebJson(getStringOfJpaObj(data));
		}
		return null;
	}

	/**
	 * 最新借款
	 * 
	 * @return String
	 * @throws Exception if has error
	 */
	@Action("/index/newTender")
	public String newTender() throws Exception {
		data = new HashMap<String, Object>();
		if (indexRuleCheck.newTender == 1) {
			List<NewTenderModel> newTenderList = tenderService.getNewTenderList();
			data.put("data", newTenderList);
			printWebJson(getStringOfJpaObj(data));
		}
		return null;
	}

	/**
	 * 投资排行榜
	 * 
	 * @return String
	 * @throws Exception if has error
	 */
	@Action("/index/rankList")
	public String rankList() throws Exception {
		data = new HashMap<String, Object>();
		if (indexRuleCheck.total_rank_list_ofmonth == 1) {
			List<RankModel> totalRankListOfMonth = tenderService.getRankList();
			data.put("data", totalRankListOfMonth);
			printWebJson(getStringOfJpaObj(data));
		}
		return null;
	}
	
	@Action("/index/recommend")
	public void recommend() throws Exception {
		data = new HashMap<String, Object>();
		//获取类型
		String type = paramString("type");
		if("ppfund".equals(type)){
			PpfundModel ppfundModel = ppfundService.getLastPpfund();
			data.put("ppfund", ppfundModel);
		}else{
			int borrowType = paramInt("borrowType");
			BorrowModel borrowModel = borrowService.getLastBorrow(borrowType);
			data.put("borrow", borrowModel);
		}
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 浏览器升级页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action("/browserUpgrade")
	public String browserUpgrade() throws Exception {
		
		return "browserUpgrade";
	}
	
	/**
	 * 充值支付结果异步返回
	 * @throws IOException 
	 */
	 @Action("/notify")
	public void notifyIndex() throws IOException {
		ServletInputStream sis = request.getInputStream();
		String str = CommonRealize.inputStream2String(sis);
		System.out.println("===================================================回调结果====="+ str);

		response.setCharacterEncoding("UTF-8");
		System.out.println("进入支付异步通知数据接收处理");
		RetBean retBean = new RetBean();
		String reqStr = LLPayUtil.readReqStr(request);

		if (null == reqStr || "".equals(reqStr)) {
			reqStr = str;
		}

		if (LLPayUtil.isnull(reqStr)) {
			retBean.setRet_code("9999");
			retBean.setRet_msg("交易失败");
			response.getWriter().write(JSON.toJSONString(retBean));
			response.getWriter().flush();
			return;
		}
		System.out.println("接收支付异步通知数据：【" + reqStr + "】");
		try {
			PartnerConfig pc = new PartnerConfig();
			if (!LLPayUtil.checkSign(reqStr, pc.getYtpubKey(),
					pc.getMd5Key())) {
				retBean.setRet_code("9999");
				retBean.setRet_msg("交易失败");
				response.getWriter().write(JSON.toJSONString(retBean));
				response.getWriter().flush();
				System.out.println("支付异步通知验签失败");
				return;
			}
		} catch (Exception e) {
			System.out.println("异步通知报文解析异常：" + e);
			retBean.setRet_code("9999");
			retBean.setRet_msg("交易失败");
			response.getWriter().write(JSON.toJSONString(retBean));
			response.getWriter().flush();
			return;
		}
		retBean.setRet_code("0000");
		retBean.setRet_msg("交易成功");
		response.getWriter().write(JSON.toJSONString(retBean));
		response.getWriter().flush();
		System.out.println("支付异步通知数据接收处理成功");
		// 解析异步通知对象
		PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
		showPayResult(payDataBean);
	}
	 
	/**
	 * 支付成功异步实现方法
	 * @param payDataBean
	 */
	private void showPayResult(PayDataBean payDataBean) {
		System.out.println("==================回调的订单号： "+payDataBean.getNo_order());
		OrderInfo oi = orderService.loadOrderByNo(payDataBean.getNo_order());//根据订单号查询信息
		if(oi!=null){			
			user = oi.getUser();
//			OrderResultsRetBean ret = LianlPayHelper.queryOrder(orderNo);
			String flag = payDataBean.getResult_pay();//充值结果
			String addrIp = LLPayUtil.getIpAddr(request);
			if(flag.equals("SUCCESS")){//充值成功
				oi.setStatus(1);//更改订单状态为成功
				oi.setOid_paybill(payDataBean.getOid_paybill());
				//充值成功业务处理
				accountRechargeService.doLLPay(user,1,payDataBean,addrIp);
			}else{
				oi.setStatus(2);//更改订单状态为失败
				accountRechargeService.doLLPay(user,2,payDataBean,addrIp);
			}
			orderService.updateOrder(oi);//更新订单状态
		}
	}
	
	/**
	 * 订单测试
	 * @throws IOException 
	 */
	 @Action("/getOrder")
	public void getOrder() {
		String noOrder = "111508181387119438";
		OrderInfo oi = orderService.loadOrderByNo(noOrder);//根据订单号查询信息
		System.out.println(oi.getRealName());
	}
		
	/**
	 * 连连解卡
	 * @throws IOException 
	 */
	 @Action("/llunbind")
	public void llunbind() {
		String userId = paramString("userId");
		String bindId = paramString("bindId");
		UnllBankRet ret = LianlPayHelper.unbind(bindId, userId);
		if(ret.getRet_code().equals("0000")){
			System.out.println("==================连连解绑成功！");
		}else{
			System.out.println("==================连连解绑失败！");
		}
	}
		 
    /**
	 * 我已经绑定银行卡
	 * 
	 * @return
	 */
	@Action("/myBank")
	public void myBank() throws Exception {
		long userId = paramLong("userId");
		String bankJosn = LianlPayHelper.queryBankcardList(userId);
		System.out.println(bankJosn);
	}
	
	/**
	 * 银联绑卡信息
	 * @throws Exception
	 */
	@Action("/ylBank")
	public void ylBank() throws Exception {
		String bindId = paramString("bindId");
		UnionPayRet ret0 = SignHelper.queryBind(bindId);
		System.out.println("=============================");
	}
}
