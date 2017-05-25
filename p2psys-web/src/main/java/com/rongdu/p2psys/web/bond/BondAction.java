package com.rongdu.p2psys.web.bond;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.MoneyUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.bond.model.BondTenderModel;
import com.rongdu.p2psys.bond.service.BondProtocolService;
import com.rongdu.p2psys.bond.service.BondService;
import com.rongdu.p2psys.bond.service.BondTenderService;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowUploadService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;
import com.rongdu.p2psys.core.rule.BondConfigRuleCheck;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserBaseInfoModel;
import com.rongdu.p2psys.user.model.UserCertificationApplyModel;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserRedPacketModel;
import com.rongdu.p2psys.user.service.UserBaseInfoService;
import com.rongdu.p2psys.user.service.UserCertificationApplyService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserRedPacketService;

/**
 * 债权action
 * @author zhangyz
 */
public class BondAction extends BaseAction<BondModel> implements ModelDriven<BondModel> {
	
	private static Logger logger = Logger.getLogger(BondAction.class);
	
	@Resource
	private BondService bondService;
	@Resource
	private BondTenderService bondTenderService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private UserBaseInfoService userBaseInfoService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private AccountService accountService;
	@Resource
	private BorrowUploadService borrowUploadService;
	@Resource
	private UserCertificationApplyService userCertificationApplyService;
	@Resource
	private VerifyLogService verifyLogService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private BondProtocolService bondProtocolService;
	@Resource
	private BorrowCollectionService borrowCollectionService;
	
	private User user;
	
	private Map<String, Object> data;
	
	
	/**
	 * 可转让债权页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="/bond/bond",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") },
				results={@Result(name = "bond", type = "ftl", location = "/bond/bond.html")})
	public String bond() throws Exception {
		// 债权规则
    	BondConfigRuleCheck bondConfigRuleCheck = (BondConfigRuleCheck) Global.getRuleCheck("bondConfig");
    	request.setAttribute("feeType", bondConfigRuleCheck.feeType);
    	request.setAttribute("sellFee", bondConfigRuleCheck.sellFee);
		return "bond";
	}
	
	/**
	 * 可转让债权列表（输出json字符串）
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/saleableBondList",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void saleableBondList() throws Exception {
		
		data = new HashMap<String, Object>();
		// 债权发布者
		user = getSessionUser();
		model.setUser(user);
		PageDataList<BondModel> list = bondService.getBondModelPage(model);
		data.put("data", list);
		
    	// 债权规则
    	BondConfigRuleCheck bondConfigRuleCheck = (BondConfigRuleCheck) Global.getRuleCheck("bondConfig");
    	data.put("bondAprL", bondConfigRuleCheck.bondAprL);
    	data.put("bondAprH", bondConfigRuleCheck.bondAprH);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 转让专区债权列表（输出json字符串）
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/bondList")
	public void bondList() throws Exception {
		
		data = new HashMap<String, Object>();
		
		PageDataList<BondModel> list = bondService.getBondList(model);
		List<BondModel> allList = list.getList();
		if("1".equals(paramString("limitListFlag")) && allList != null && allList.size() > 4){
			List<BondModel> limitList = new ArrayList<BondModel>();
			for (int i = 0; i < 4; i++) {
				limitList.add(allList.get(i));
			}
			list.setList(limitList);
		}
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}	
	
	/**
	 * 发布债权
	 * @throws Exception if has error
	 */
	@Action(value="/bond/addBond",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void addBond() throws Exception {

		// 债权发布者
		user = getSessionUser();
		model.setUser(user);
		// 标信息
		Borrow borrow = borrowService.getBorrowById(model.getBorrowId());
		model.setBorrow(borrow);
		// 发布债权前校验
		model.checkBeforAddBond(model);
		// 债权信息
		Bond bond = model.prototype();
		bondService.addBond(bond);
		
		printSuccess();
	}
	
	/**
	 * 转让中债权列表（输出json字符串）
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/sellingBondList",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void sellingBondList() throws Exception {
		
		data = new HashMap<String, Object>();
		// 债权发布者
		user = getSessionUser();
		model.setUser(user);
		PageDataList<BondModel> list = bondService.getSellingBondList(model);
		data.put("data", list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 已转出债权列表（输出json字符串）
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/soldBondList",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void soldBondList() throws Exception {
		
		data = new HashMap<String, Object>();
		// 债权发布者
		user = getSessionUser();
		model.setUser(user);
		PageDataList<BondModel> list = bondService.getSoldBondList(model);
		data.put("data", list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 已转入债权列表（输出json字符串）
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/boughtBondList",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void boughtBondList() throws Exception {
		
		data = new HashMap<String, Object>();
		// 债权发布者
		user = getSessionUser();
		model.setUser(user);
		PageDataList<BondTenderModel> list = bondTenderService.getBoughtBondList(model);
		data.put("data", list);
		
		printWebJson(getStringOfJpaObj(data));
	}	
	
	/**
	 * 债权投资页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/bond/index" ,results = { @Result(name = "index", type = "ftl", location = "/bond/index.html")})
	public String index() throws Exception {
		return "index";
	}

	/**
	 * 债权详情页面
	 * @return String
	 * @throws Exception
	 */
	@Action(value = "/bond/detail")
	public String detail() throws Exception {
		return "detail";
	}
	
	/**
	 * 债权详情页面收益
	 * @throws Exception
	 */
	@Action(value="/bond/detailInterest")
	public void detailInterest()throws Exception{
		Map<String, Object> data = new HashMap<String, Object>();
		double money = paramDouble("money");
		Bond bond = bondService.getBondById(paramLong("bondId"));
		Object[] obj  = borrowCollectionService.getBorrowCollectionList(bond.getBorrowTenderId());
		if(obj !=null && obj.length==2){
			double percent = BigDecimalUtil.div(money, Double.parseDouble(obj[0].toString()));
			double interest = BigDecimalUtil.mul(Double.parseDouble(obj[1].toString()),percent);
			data.put("interest", interest);
			printJson(getStringOfJpaObj(data));
		}
	}
	
	
	/**
	 * 债权详情JSON数据
	 * @throws Exception
	 */
	@Action("/bond/bondDetail")
	public void bondDetail() throws Exception {
		this.saveToken("bondTenderToken");
		String bondTenderToken = (String) session.get("bondTenderToken");
		
		user = getSessionUser();
		
		data = new HashMap<String, Object>();

		data.put("bondTenderToken", bondTenderToken);
		
		// 债权信息
		BondModel bond = bondService.getBondDetail(model);
		data.put("bond", bond);
		
		// 标信息
		long borrowId = bond.getBorrowId();
		Borrow borrow = borrowService.find(borrowId);
		User borrowUser = borrow.getUser();

		
		// 借款人信息
		UserBaseInfo userBaseInfo = userBaseInfoService.findByUserId(borrowUser.getUserId());
		if(userBaseInfo != null){
			UserBaseInfoModel userBaseInfoModel = UserBaseInfoModel.instance(userBaseInfo);
			userBaseInfoModel.setUser(null); //防止前台查看用户信息
			data.put("userBaseInfo", userBaseInfoModel);
		}
		
		// 资料审核状态
		List<UserCertificationApplyModel> certificationApply = userCertificationApplyService.findByUser(borrowUser);
		data.put("certificationApply", certificationApply);
		
		// 借款资料
		List<BorrowUpload> borrowUploads = borrowUploadService.findByBorrowId(borrowId);
		data.put("borrowUploads", borrowUploads);
		
		String username = borrowUser.getUserName();
		String hideName = username.charAt(0)+"******"+username.charAt(username.length()-1);
		
		BorrowModel borrowModel = BorrowModel.instance(borrow);
		borrowModel.setUserName(hideName);
		borrowModel.setUser(null);
		data.put("borrow", borrowModel);
		data.put("isOpenApi", isOpenApi());

		if (user != null) {
			List<UserRedPacketModel> packets = userRedPacketService.findByUser(user);
			data.put("packets", packets);
			UserIdentify userIdentify = userIdentifyService.findByUserId(getSessionUserId());
			UserIdentifyModel userIdentifyModel = UserIdentifyModel.instance(userIdentify);
			String uName = userIdentify.getUser().getUserName();
			userIdentifyModel.setUserName(uName.charAt(0)+"******"+uName.charAt(uName.length()-1));
			userIdentifyModel.setUser(null);
			data.put("userInvestIdentify", userIdentifyModel);
			data.put("payPwd", StringUtil.isBlank(user.getPayPwd()));
			
			if ("".equals(paramString("borrowPreview"))) {
				data.put("userStates", user.getUserCache().getUserNature());
				Account account = accountService.findByUser(user.getUserId());
				AccountModel accountModel = AccountModel.instance(account);
				accountModel.setUser(null);
				data.put("account", accountModel);
			}
		}
		
		// 债权转让记录
		BondTenderModel bondTenderModel = new BondTenderModel();
		bondTenderModel.setBondId(model.getId());
		PageDataList<BondTenderModel> bondTenderList = bondTenderService.getTenderModelPage(bondTenderModel);
		data.put("bondTenderList", bondTenderList);
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	/**
	 * 债权转让记录列表
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/latestTenerList")
	public void latestTenerList() throws Exception {
		data = new HashMap<String, Object>();
		// 最新债权转让成交记录
		data.put("latestTenerList", bondTenderService.getLatestTenerList());
		data.put("serverTime", new Date());
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 债权转让记录列表
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/bondTenderList")
	public void bondTenderList() throws Exception {
		
		data = new HashMap<String, Object>();
		BondTenderModel bondTenderModel = new BondTenderModel();
		bondTenderModel.setBondId(model.getId());
		bondTenderModel.setPage(model.getPage());
		PageDataList<BondTenderModel> bondTenderList = bondTenderService.getTenderModelPage(bondTenderModel);
		data.put("bondTenderList", bondTenderList);
		
		printWebJson(getStringOfJpaObj(data));
	}	
	
	
	/**
	 * 债权投资
	 * @throws Exception if has error
	 */
	@SuppressWarnings("unchecked")
	@Action(value="/bond/bondTender",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack")}, results={
			@Result(name = "pnrCreditAssign", type = "ftl", location = "/tpp/chinapnr/pnrCreditAssign.html"),
			@Result(name = "result", type = "ftl", location = "/tpp/result.html")})
	public String bondTender() throws Exception {
		// 重复提交校验
		try {
			this.checkToken("bondTenderToken");
		} catch (BussinessException e) {
			throw new BussinessException(e.getMessage(), 2);
		}
		// 债权投资者
		user = getSessionUser();
		if (user.getUserCache().getUserType() == 2) {
			throw new BussinessException("借款账户不能投资");
		}
		model.setUser(user);
		// 债权信息
		Bond bond = bondService.getBondById(model.getId());
		// 债权投资前校验
		model.checkBeforBondTender(bond);
		
		String name = Global.getValue("cooperation_interface");
		// 债权投资处理
		// 托管版
		if(isOpenApi()){
			TPPWay way = TPPFactory.getTPPWay(null, user, null, model.getMoney() + "", null);
			BondModel bm = BondModel.instance(bond);
			bm.setMoney(model.getMoney());
			bm.setIds(model.getIds());
	        Object creditAssign = way.creditAssign(bm);
	        request.setAttribute(name, creditAssign);
	    	return name + "CreditAssign";
	    // 标准版
		}else{
			ConcurrentUtil.bondTender(model);
		}

		String resultFlag = System.currentTimeMillis()+""+Math.random()*10000;
		request.setAttribute("resultFlag", resultFlag);
		request.setAttribute("left_url", "/bond/detail.html?id="+this.model.getId());
		request.setAttribute("right_url", "/member/main.html");
		request.setAttribute("left_msg", MessageUtil.getMessage("I50001")); 
		request.setAttribute("right_msg", MessageUtil.getMessage("I10001")); 
		request.setAttribute("r_msg", MessageUtil.getMessage("I50002"));
		Global.RESULT_MAP.put(resultFlag, "success");
		
		return "result";
	}
	
	/**
	 * 停止债权转让
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/stopBond",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void stopBond() throws Exception {
		
		bondService.stopBond(model.getId());
		printSuccess();
	}
	
	
	/**
	 * 转让中,已转出详情（原始标相关信息）
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/sellDetail",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void sellDetail() throws Exception {
		
		data = new HashMap<String, Object>();
		
		BondModel bondModel = bondService.getBondModelByBondId(model.getId());
		data.put("bond", bondModel);

		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 已转入详情（原始标相关信息）
	 * 
	 * @throws Exception if has error
	 */
	@Action(value="/bond/buyDetail",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void buyDetail() throws Exception {
		
		data = new HashMap<String, Object>();
		
		BondModel bondModel = bondService.getBondModelByBondTenderId(model.getId());
		data.put("bond", bondModel);

		printWebJson(getStringOfJpaObj(data));
	}	
	
	/**
	 * 债权转让协议预览
	 * @throws Exception if has error
	 */
	@Action("/bond/bondSellProtocolPreview")
	public String bondSellProtocolPreview() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		Borrow borrow = borrowService.find(id);
		User borrowUser = borrow.getUser();
		request.setAttribute("borrow", borrow);
		request.setAttribute("borrowUser", borrowUser);
		request.setAttribute("userCache", borrowUser.getUserCache());
		VerifyLog verifyLog = verifyLogService.findByType(id, "borrow", 1);
		request.setAttribute("verifyTime",verifyLog.getTime());
		request.setAttribute("bigMoney",MoneyUtil.convert(borrow.getAccount()+""));
		String repayStyle;
		if (borrow.getStyle() == Borrow.STYLE_MONTHLY_INTEREST) {
			repayStyle = "每月还息到期还本";
		} else if (borrow.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) {
			repayStyle = "一次性还款";
		} else {
			repayStyle = "按月分期付款";
		}
		request.setAttribute("repayStyle", repayStyle);
		List<BorrowRepayment> list = borrowRepaymentService.getRepaymentByBorrowId(id);
		request.setAttribute("list", list);

		return "bondSellProtocolPreview";
	}
	
	/**
	 * 债权出让人协议下载
	 * 
	 * @return
	 */
	@Action(value="/bond/bondSellProtocol",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public String bondSellProtocol() throws Exception {
		User user = getSessionUser();
		long borrowId = paramLong("id");
		AbstractProtocolBean protocolBean = bondProtocolService.buildBondSellProtocol(borrowId, user.getUserId());
		try {
			generateDownloadFile(protocolBean.getInPdfName(), protocolBean.getDownloadFileName());
		} catch (FileNotFoundException e) {
			logger.error("协议pdf文件" + protocolBean.getDownloadFileName() + "未找到！");
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 债权受让人协议下载
	 * 
	 * @return
	 */
	@Action(value="/bond/bondBuyProtocol",interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public String bondBuyProtocol() throws Exception {
		User user = getSessionUser();
		long bondTenderId = paramLong("id");
		AbstractProtocolBean protocolBean = bondProtocolService.buildBondBuyProtocol(bondTenderId, user.getUserId());
		try {
			generateDownloadFile(protocolBean.getInPdfName(), protocolBean.getDownloadFileName());
		} catch (FileNotFoundException e) {
			logger.error("协议pdf文件" + protocolBean.getDownloadFileName() + "未找到！");
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	

}
