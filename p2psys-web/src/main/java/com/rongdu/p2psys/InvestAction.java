package com.rongdu.p2psys;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.MoneyUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowHelper;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.borrow.service.BorrowUploadService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductMaterialsTypeConstant;
import com.rongdu.p2psys.core.domain.ExchangeRatePacketCapture;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.domain.UserUpload;
import com.rongdu.p2psys.user.domain.UserVip;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserBaseInfoModel;
import com.rongdu.p2psys.user.model.UserCertificationApplyModel;
import com.rongdu.p2psys.user.model.UserCertificationModel;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserRedPacketModel;
import com.rongdu.p2psys.user.service.UserBaseInfoService;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserCertificationApplyService;
import com.rongdu.p2psys.user.service.UserCertificationService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;
import com.rongdu.p2psys.user.service.UserUploadService;
import com.rongdu.p2psys.user.service.UserVipService;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;
import com.rongdu.p2psys.util.WechatUtil;

/**
 * 我要投资
 * 
 * @author cx
 * @version 2.0
 */
public class InvestAction extends BaseAction<BorrowModel> implements
		ModelDriven<BorrowModel> {

	@Resource
	private BorrowService borrowService;
	@Resource
	private BorrowTenderService borrowTenderService;
	@Resource
	private UserCertificationService userCertificationService;
	@Resource
	private UserCertificationApplyService userCertificationApplyService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private AccountService accountService;
	@Resource
	private UserService userService;
	@Resource
	private UserUploadService userUploadService;
	@Resource
	private BorrowUploadService borrowUploadService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private VerifyLogService verifyLogService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserBaseInfoService userBaseInfoService;
	@Resource
	private BorrowInterestRateService borrowInterestRateService;
	@Resource
	private UserVipService userVipService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductMaterialsService productMaterialsService;

	private User user;
	private Map<String, Object> data;

	public static final boolean isOpenAip = BaseTPPWay.isOpenApi();

	/**
	 * 我要投资页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/invest/index")
	public String index() throws Exception {
		return "investIndex";
	}

	/**
	 * 我要投资页面（海外投资产品）
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/invest/estate")
	public String estate() throws Exception {
		return "estateIndex";
	}

	/**
	 * 我要投资页面（浮动收益类产品）
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/invest/entrust")
	public String entrust() throws Exception {
		return "entrustIndex";
	}

	/**
	 * 我要投资列表页（输出json字符串）
	 * 
	 * @throws Exception
	 *             if has error
	 */
	@SuppressWarnings("static-access")
	@Action("/invest/investJson")
	public void investJson() throws Exception {
		data = new HashMap<String, Object>();
		Borrow borrow = BorrowHelper.getWorker(model).prototype();
		model = model.instanceCurr(borrow, model);
		model.setSize(12);
		PageDataList<BorrowModel> list = borrowService.getList(model);
		data.put("data", list);
		data.put("nowTime", new Date());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 新手标
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Action("/noviceJson")
	public void noviceJson() throws Exception {
		data = new HashMap<String, Object>();
		model.setType(100);
		model.setSize(4);
		Borrow borrow = BorrowHelper.getWorker(model).prototype();
		model = model.instanceCurr(borrow, model);
		model.setStatus(-2);
		model.setScales(101); // 标记为不显示用户信息
		model.setIsNovice(1);
		PageDataList<BorrowModel> list = borrowService.getList(model);
		data.put("data", list);
		data.put("nowTime", new Date());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 标详情页面
	 * 
	 * @return String
	 * @throws Exception
	 */
	@Action(value = "/invest/detail", interceptorRefs = {@InterceptorRef("globalStack")},
			 results = {
				@Result(name = "detail", type = "ftl", location = "/nb/pc/product/borrow_detail.html"),
				@Result(name = "wxdetail", type = "ftl", location = "/nb/wechat/product/product_detail.html")})
	public String detail() throws Exception {
		Borrow borrow = borrowService.findNotFlow(paramLong("id"));
		Object obj = session.get(Constant.SYSTEM);
		String system = "";
		if(obj!=null)
		{
			system = (String) session.get(Constant.SYSTEM);
		}
		if(!system.equals(Constant.COOPERATE_TYPE__PC))
		{
			Long relatedId =  paramLong("id");
			Long flagId = paramLong("flagId");
			ProductBasic product = productBasicService.getProductBasicInfoByFlagId(flagId, relatedId);
			String productId = "";
			if (this.getNBSessionUser()!= null) {
				if (borrow.getStatus() != 1 && borrow.getStatus() != 3
						&& borrow.getStatus() != 4 && borrow.getStatus() != 49
						&& borrow.getStatus() != 6 && borrow.getStatus() != 7
						&& borrow.getStatus() != 8) {
					throw new BorrowException("当前借款标不存在或已流标");
				}else if (product==null)
				{
					throw new BorrowException("当前借款标不存在或已流标");
				}
			}
			productId = product.getId().toString();
			request.setAttribute("type", paramString("type"));
			request.setAttribute("redirectURL", paramString("redirectURL"));
			request.setAttribute("product_id", productId);
			request.setAttribute("userCode", "");
			request.setAttribute("appId", "");
			request.setAttribute("timestamp", "");
			request.setAttribute("nonceStr", "");
			request.setAttribute("signature", "");
			request.setAttribute("title","800bank投资产品年化收益7-11%，快来投");
			request.setAttribute("desc","优质投资产品，灵活投资方式，尽在800bank");
			request.setAttribute("promot",paramString("promot"));
			request.setAttribute("listSize","");
			request.setAttribute("url",getShareJumpPage(productId,paramString("promot")));
			List <ProductMaterials> productMaterialsList = productMaterialsService.getMaterialsByProductId(StringUtil.toLong(productId)); 
			
			for(int i = 0;i<productMaterialsList.size();i++)
			{
				if(ProductMaterialsTypeConstant.WECHAT_SHARE_TITLE.equals(productMaterialsList.get(i).getMaterialType()))
				{
					request.setAttribute("title",productMaterialsList.get(i).getMaterial());
				}
				if(ProductMaterialsTypeConstant.WECHAT_SHARE_DESC.equals(productMaterialsList.get(i).getMaterialType()))
				{
					request.setAttribute("desc",productMaterialsList.get(i).getMaterial());
				}
			}
			return "wxdetail";
		}else
		{
			Long relatedId =  paramLong("id");
			Long flagId = paramLong("flagId");
			ProductBasic product = productBasicService.getProductBasicInfoByFlagId(flagId, relatedId);
			if (this.getNBSessionUser()!= null) {
				if (borrow.getStatus() != 1 && borrow.getStatus() != 3
						&& borrow.getStatus() != 4 && borrow.getStatus() != 49
						&& borrow.getStatus() != 6 && borrow.getStatus() != 7
						&& borrow.getStatus() != 8) {
					throw new BorrowException("当前借款标不存在或已流标");
				}else if (product==null)
				{
					throw new BorrowException("当前借款标不存在或已流标");
				}
			}
			request.setAttribute("prodId", product.getId());
			request.setAttribute("flagId", paramLong("flagId"));
			request.setAttribute("investId", paramLong("investId"));
//			request.setAttribute("investId", "123");
			return "detail";
		}
		
	}

	/**
	 * 标详情页面（浮动收益类产品）
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/invest/entrustDetail", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") })
	public String entrustDetail() throws Exception {
		Borrow borrow = borrowService.findNotFlow(paramLong("id"));
		String borrowPreview = request.getParameter("borrowPreview");
		if (borrowPreview == null) {
			if (borrow.getStatus() != 1 && borrow.getStatus() != 3
					&& borrow.getStatus() != 4 && borrow.getStatus() != 49
					&& borrow.getStatus() != 6 && borrow.getStatus() != 7
					&& borrow.getStatus() != 8) {
				throw new BorrowException("当前借款标不存在或已流标");
			}
		}
		request.setAttribute("borrowPreview", borrowPreview);
		return "entrustDetail";
	}

	/**
	 * 标详情JSON数据
	 * 
	 * @throws Exception
	 */
	@Action("/invest/borrowDetail")
	public void borrowDetail() throws Exception {
		this.saveToken("tenderToken");
		String tenderToken = (String) session.get("tenderToken");
		user = getSessionUser();
		data = new HashMap<String, Object>();
		long borrowId = paramLong("id");

		// 获取借款标资料图片
		List<BorrowUpload> borrowUploads = borrowUploadService
				.findByBorrowIdAndType(borrowId, 0);
		data.put("borrowUploads", borrowUploads);
		Borrow b = borrowService.findNotFlow(borrowId);

		// 获取借款标头像
		List<BorrowUpload> borrowImgs = borrowUploadService
				.findByBorrowIdAndType(borrowId, 5);
		if (borrowImgs != null && borrowImgs.size() > 0) {
			data.put("borrowImg", borrowImgs.get(0));
		}

		List<UserUpload> userUploadlist = userUploadService.findByUser(b
				.getUser());
		data.put("userUploadlist", userUploadlist);
		// UserCache userCache =
		// userCacheService.findByUserId(b.getUser().getUserId());
		// UserCacheModel uc = UserCacheModel.instance(userCache);
		// uc.setUser(null);
		// String companyName = uc.getCompanyName();
		// if (companyName != null && companyName.length() > 5) {
		// uc.setCompanyName(companyName.substring(0,
		// 2)+"****"+companyName.substring(companyName.length()-4));
		// }
		// data.put("uc", uc);
		data.put("size", borrowUploadService.findByBorrowIdAndType(borrowId)
				.size());
		double interest = BigDecimalUtil.mul(b.getApr(), b.getTimeLimit()) * 100 / 30;
		PageDataList<BorrowTenderModel> list = this.borrowTenderService.list(
				b.getId(), this.model.getPage(), 0);
		BorrowModel borrow = BorrowModel.instance(b);
		VerifyLog log = verifyLogService
				.findByType(borrow.getId(), "borrow", 1);
		if (log != null) {
			borrow.setStartTime(DateUtil.dateStr2(log.getTime()));
			borrow.setEndTime(DateUtil.dateStr2(DateUtil.rollDay(log.getTime(),
					borrow.getValidTime())));
		}
		String username = borrow.getUser().getUserName();
		String hideName = username.charAt(0) + "******"
				+ username.charAt(username.length() - 1);
		borrow.setUserName(hideName);
		borrow.setUser(null);
		if (!StringUtil.isBlank(borrow.getPwd())) {
			data.put("isDirectional", true);
		} else {
			data.put("isDirectional", false);
		}
		if (borrow.getType() == Borrow.TYPE_ESTATE
				&& (borrow.getStatus() == 6 || borrow.getStatus() == 8)) {
			double floatRate = 0;

			List<ExchangeRatePacketCapture> erpcList = borrowService
					.getCaptureRate(borrow.getId());
			if (erpcList != null && erpcList.size() > 0) {
				double verifyFullCaptureRate = erpcList.get(0)
						.getCashPurchasePrice();
				double nowCaptureRate = erpcList.get(erpcList.size() - 1)
						.getCashPurchasePrice();
				floatRate = BigDecimalUtil.div(BigDecimalUtil.sub(
						nowCaptureRate, verifyFullCaptureRate),
						verifyFullCaptureRate);
				if (floatRate <= 0) {
					floatRate = 0;
				}
			}
			data.put("floatRate",
					BigDecimalUtil.round(BigDecimalUtil.mul(floatRate, 100)));
		}
		double sumBidMoney = borrowService.sumBidMoney(borrow);
		data.put("sumBidMoney",
				BigDecimalUtil.sub(borrow.getAccount(), sumBidMoney));
		data.put("borrow", borrow);
		data.put("tenderToken", tenderToken);
		data.put("accountWait",
				BigDecimalUtil.sub(borrow.getAccount(), borrow.getAccountYes()));
		data.put("interest", BigDecimalUtil.round(interest));
		data.put("data", list);
		data.put("url", Global.getValue("adminurl"));
		data.put("isOpenAip", isOpenAip);
		data.put("apiCode", TPPWay.API_CODE);
		if (user != null) {
			// 获取当前登录用户基本信息
			UserCache login_user_cache = userCacheService.findByUserId(user
					.getUserId());
			data.put("login_user_cache", login_user_cache);

			List<UserRedPacketModel> packets = userRedPacketService
					.findByUserAndSelType(user, 0);
			data.put("packets", packets);

			List<UserRedPacketModel> spackets = userRedPacketService
					.findByUserAndSelType(user, 1);
			data.put("spackets", spackets);

			// 查询用户对该标已投资金额
			double hasTender = borrowTenderService
					.hasTenderTotalPerBorrowByUserid(borrow.getId(),
							user.getUserId());
			data.put("hasTender", hasTender);
			data.put("payPwd", StringUtil.isBlank(user.getPayPwd()));

			// 加息劵
			List<BorrowInterestRate> birList = borrowInterestRateService
					.findByStatusAndUser(1, user);// status=1未使用
			data.put("birList", birList);

			// 用户认证信息
			UserIdentify userIdentify = userIdentifyService.findByUserId(user
					.getUserId());
			UserIdentifyModel userIdentifyModel = UserIdentifyModel
					.instance(userIdentify);
			userIdentifyModel.setUser(null);
			data.put("userInvestIdentify", userIdentifyModel);
		}
		if ("".equals(paramString("borrowPreview"))) {
			if (user != null) {
				data.put("userStates", user.getUserCache().getUserNature());
				Account account = accountService.findByUser(user.getUserId());
				AccountModel accountModel = AccountModel.instance(account);
				accountModel.setUser(null);
				data.put("account", accountModel);
			}
			data.put("borrowPreview", 2);// 代表非后台预览标
		}
		List<UserCertificationApplyModel> certificationApply = userCertificationApplyService
				.findByUser(b.getUser());
		data.put("certificationApply", certificationApply);

		// 借款人信息
		UserBaseInfo userBaseInfo = userBaseInfoService.findByUserId(b
				.getUser().getUserId());
		if (userBaseInfo != null) {
			UserBaseInfoModel userBaseInfoModel = UserBaseInfoModel
					.instance(userBaseInfo);
			userBaseInfoModel.setUser(null); // 防止前台查看用户信息
			data.put("userBaseInfo", userBaseInfoModel);
		}
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 标详情JSON数据（浮动收益类产品）
	 * 
	 * @throws Exception
	 */
	@Action("/invest/borrowEntrustDetail")
	public void borrowEntrustDetail() throws Exception {
		this.saveToken("tenderToken");
		String tenderToken = (String) session.get("tenderToken");
		user = getSessionUser();
		data = new HashMap<String, Object>();
		long borrowId = paramLong("id");

		// 获取借款标资料图片
		List<BorrowUpload> borrowUploads = borrowUploadService
				.findByBorrowIdAndType(borrowId, 0);
		data.put("borrowUploads", borrowUploads);
		Borrow b = borrowService.findNotFlow(borrowId);

		// 获取借款标头像
		List<BorrowUpload> borrowImgs = borrowUploadService
				.findByBorrowIdAndType(borrowId, 5);
		if (borrowImgs != null && borrowImgs.size() > 0) {
			data.put("borrowImg", borrowImgs.get(0));
		}

		data.put("size", borrowUploadService.findByBorrowIdAndType(borrowId)
				.size());
		double interest = BigDecimalUtil.mul(b.getApr(), b.getTimeLimit()) * 100 / 30;
		PageDataList<BorrowTenderModel> list = this.borrowTenderService.list(
				b.getId(), this.model.getPage(), 0);
		BorrowModel borrow = BorrowModel.instance(b);
		VerifyLog log = verifyLogService
				.findByType(borrow.getId(), "borrow", 1);
		if (log != null) {
			borrow.setStartTime(DateUtil.dateStr2(log.getTime()));
			borrow.setEndTime(DateUtil.dateStr2(DateUtil.rollDay(log.getTime(),
					borrow.getValidTime())));
		}
		borrow.setUser(null);
		if (!StringUtil.isBlank(borrow.getPwd())) {
			data.put("isDirectional", true);
		} else {
			data.put("isDirectional", false);
		}
		data.put("borrow", borrow);
		data.put("tenderToken", tenderToken);
		data.put("accountWait",
				BigDecimalUtil.sub(borrow.getAccount(), borrow.getAccountYes()));
		data.put("interest", BigDecimalUtil.round(interest));
		data.put("data", list);
		data.put("url", Global.getValue("adminurl"));
		data.put("isOpenAip", isOpenAip);
		data.put("apiCode", TPPWay.API_CODE);
		if (user != null) {
			// 获取当前登录用户基本信息
			UserCache login_user_cache = userCacheService.findByUserId(user
					.getUserId());
			data.put("login_user_cache", login_user_cache);

			List<UserRedPacketModel> packets = userRedPacketService
					.findByUser(user);
			data.put("packets", packets);

			// 查询用户对该标已投资金额
			double hasTender = borrowTenderService
					.hasTenderTotalPerBorrowByUserid(borrow.getId(),
							user.getUserId());
			data.put("hasTender", hasTender);
			data.put("payPwd", StringUtil.isBlank(user.getPayPwd()));

			data.put("userStates", user.getUserCache().getUserNature());
		}
		if ("".equals(paramString("borrowPreview"))) {
			Account account = accountService.findByUser(user.getUserId());
			AccountModel accountModel = AccountModel.instance(account);
			accountModel.setUser(null);
			data.put("account", accountModel);
			data.put("borrowPreview", 2);// 代表非后台预览标
		}
		double sumBidMoney = borrowService.sumBidMoney(borrow);
		data.put("sumBidMoney",
				BigDecimalUtil.sub(borrow.getAccount(), sumBidMoney));
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 投标记录详细
	 * 
	 * @return String
	 * @throws Exception
	 *             if has error
	 */
	@Action("/invest/detailTenderForJson")
	public String detailTenderForJson() throws Exception {
		long borrowId = paramLong("id");
		data = new HashMap<String, Object>();
		PageDataList<BorrowTenderModel> list = this.borrowTenderService.list(
				borrowId, this.model.getPage(), 0);
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 资料审核
	 * 
	 * @return String
	 * @throws Exception
	 *             if has error
	 */
	@Action("/invest/detailForAuditInfo")
	public String detailForAuditInfo() throws Exception {
		long userId = paramLong("userId");
		data = new HashMap<String, Object>();
		PageDataList<UserCertificationModel> attestationList = userCertificationService
				.findByUserId(userId, 1, this.model.getPage());
		data.put("data", attestationList);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 投标 较为特殊需要跳转页面
	 * 
	 * @throws Exception
	 *             if has error
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Action(value = "/invest/tender", interceptorRefs = {
			@InterceptorRef("session"), @InterceptorRef("globalStack") }, results = {
			@Result(name = "ipsTender", type = "ftl", location = "/tpp/ipstender.html"),
			@Result(name = "pnrTender", type = "ftl", location = "/tpp/chinapnr/initiativeTende.html"),
			@Result(name = "result", type = "ftl", location = "/tpp/result.html") })
	public String tender() throws Exception {
		user = getSessionUser();
		try {
			this.checkToken("tenderToken");
			if (user.getUserCache().getUserType() == 2) {
				throw new BussinessException("借款账户不能投资");
			}
		} catch (BussinessException e) {
			throw new BussinessException(e.getMessage(), 2);
		}
		Borrow borrow = borrowService.find(this.model.getId());
		if (borrow.getFixedTime() != null) {
			if (new Date().before(borrow.getFixedTime())) {
				throw new UserException("标还未到定时时间", 2);
			}
		}

		// 投标前model值校验
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		BorrowModel m = BorrowModel.instance(worker.prototype());

		m.setPayPwd(model.getPayPwd());
		m.setBorrowInterestRateValue(model.getBorrowInterestRateValue());
		m.setMoney(model.getMoney());
		m.checkTenderModel(borrow, user);
		Long[] ids = null;
		if (model.getIds() != null && model.getRedPacketId() > 0) {
			ids = new Long[model.getIds().length + 1];

			for (int i = 0; i < ids.length - 1; i++) {
				ids[i] = model.getIds()[i];
			}
			ids[model.getIds().length] = model.getRedPacketId();
		} else if (model.getRedPacketId() > 0) {
			ids = new Long[1];
			ids[0] = model.getRedPacketId();
		}
		if (ids != null) {
			model.setIds(ids);
		}

		if (model.getIds() != null && model.getIds().length > 0) {
			ids = model.getIds();
			for (int i = 0; i < ids.length; i++) {
				UserRedPacket uRedPacket = userRedPacketService
						.findUserRedPacketById(ids[i]);
				if (uRedPacket.isUsed()) {
					throw new BussinessException("红包已经使用，请重新投资！");
				}
			}
		}
		double totalPacketMoney = userRedPacketService
				.getTotalPacketMoneyByIds(model.getIds());

		// 加息券值
		double interestRate = 0;
		// VIP加息劵
		double vipInterestRate = 0;
		UserVip userVipRule = userVipService.getUserVipRuleByUser(user);
		if (userVipRule != null) {
			vipInterestRate = userVipRule.getApr();
		}

		double borrowInterestRateValue = model.getBorrowInterestRateValue();
		interestRate = borrowInterestRateValue;

		worker.checkTender(m, model.getMoney(), totalPacketMoney, user,
				StringUtil.isNull(model.getPwd()));

		double sumBidMoney = borrowService.sumBidMoney(m);
		if (borrow.getAccount() - sumBidMoney < model.getMoney()) {
			throw new BorrowException("您对该标的投资已达到最多投标总额(￥"
					+ (borrow.getAccount() - sumBidMoney) + ")，不能继续投标，投标失败！",
					"/invest/detail.html?id=" + model.getId());
		}

		// 获取分类ID（flag_id）
		Long flagId = productBasicService
				.getProductBasicInfo(new Long(borrow.getType()), borrow.getId())
				.getProductTypeFlag().getId();

		// 投标处理
		model.setUser(user);
		model.setAddIp(Global.getIP());
		ConcurrentUtil.tender(model, borrow);
		String resultFlag = System.currentTimeMillis() + "" + Math.random()
				* 10000;
		request.setAttribute("resultFlag", resultFlag);
		request.setAttribute("left_url", model.findReturnUrl(borrow.getType())
				+ "?id=" + this.model.getId() + "&flagId=" + flagId); // 成功返回地址
		request.setAttribute("right_url", "/member/main.html"); // 成功返回地址
		request.setAttribute("left_msg", MessageUtil.getMessage("I10008"));
		request.setAttribute("right_msg", MessageUtil.getMessage("I10001"));
		
		if(231==this.model.getId()||232==this.model.getId())
		{
			request.setAttribute("r_msg", "恭喜您，投标成功!"+"<p style='font-size:12px;' >快想想喜欢什么颜色的手机，客服人员会尽快与您联系。</p>");
		}
		else
		{
			request.setAttribute("r_msg", MessageUtil.getMessage("I10007"));
		}
		Global.RESULT_MAP.put(resultFlag, "success");
		return "result";
	}

	/**
	 * 抵押物图片
	 * 
	 * @throws Exception
	 *             if has error
	 */
	@Action("/invest/getMortgagePics")
	public void getMortgagePics() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		long type = paramLong("type");
		List<BorrowUpload> list = borrowUploadService.findByMortgageIdAndType(
				id, type);
		data.put("list", list);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 用户实物照
	 * 
	 * @throws Exception
	 *             if has error
	 */
	@Action("/invest/getUserUpload")
	public void getUserUpload() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		Borrow borrow = borrowService.find(id);
		User user = borrow.getUser();
		List<UserUpload> list = userUploadService.findByUser(user);
		data.put("list", list);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 协议预览
	 * 
	 * @throws Exception
	 *             if has error
	 */
	@Action("/invest/protocolPreview")
	public String protocolPreview() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		Borrow borrow = borrowService.find(id);
		User borrowUser = borrow.getUser();
		request.setAttribute("borrow", borrow);
		request.setAttribute("borrowUser", borrowUser);
		request.setAttribute("userCache", borrowUser.getUserCache());
		VerifyLog verifyLog = verifyLogService.findByType(id, "borrow", 1);
		request.setAttribute("verifyTime", verifyLog.getTime());
		request.setAttribute("bigMoney",
				MoneyUtil.convert(borrow.getAccount() + ""));
		String repayStyle;
		if (borrow.getStyle() == Borrow.STYLE_MONTHLY_INTEREST) {
			repayStyle = "每月还息到期还本";
		} else if (borrow.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) {
			repayStyle = "一次性还款";
		} else {
			repayStyle = "按月分期付款";
		}
		request.setAttribute("repayStyle", repayStyle);
		List<BorrowRepayment> list = borrowRepaymentService
				.getRepaymentByBorrowId(id);
		request.setAttribute("list", list);
		return "protocolPreview";
	}

	/**
	 * 理财介绍页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/invest/productList")
	public String productList() throws Exception {
		return "productList";
	}

	/**
	 * 海外投资产品（预约投标）
	 * 
	 * @throws Exception
	 */
	@Action("/invest/appointmentBid")
	public String appointmentBid() throws Exception {
		data = new HashMap<String, Object>();
		long borrowId = paramLong("borrowId");
		double money = paramDouble("money");
		user = getSessionUser();
		try {
			this.checkToken("tenderToken");
			if (user.getUserCache().getUserType() == 2) {
				throw new BussinessException("借款账户不能投资", 1);
			}
		} catch (BussinessException e) {
			throw new BussinessException(e.getMessage(), 1);
		}
		// 预约投资提前5分钟关闭
		Borrow borrow = borrowService.find(borrowId);
		long nowTime = DateUtil.getNowTime();
		long fiexTime = NumberUtil.getLong(DateUtil.getTimeStr(borrow
				.getFixedTime()));
		if (fiexTime - nowTime <= 300) {
			throw new BussinessException("该借款标已停止预约", 1);
		}
		// 投标前model值校验
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		BorrowModel m = BorrowModel.instance(worker.prototype());
		m.setPayPwd(model.getPayPwd());
		m.setMoney(money);
		m.checkTenderModel(borrow, user);
		worker.checkTender(m, model.getMoney(), 0, user,
				StringUtil.isNull(model.getPwd()));
		borrowService.appointmentBid(user, borrow, money);
		data.put("result", true);
		data.put("msg", "预约投标成功！");
		printWebJson(getStringOfJpaObj(data));
		return null;
	}
	
	public String getShareJumpPage(String product_id,String promot)
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(WechatData.OAUTH_URL_ONE);
		buffer.append(WechatData.OAUTH_URL_TWO);
		buffer.append(WechatData.OAUTH_URL_THREE_ANOTHER);
		buffer.append(promot);
		buffer.append("%26redirectURL=");
		buffer.append("/nb/wechat/product/productDetail.action?product_id=");
		buffer.append(product_id);
		buffer.append("%26promot=");
		buffer.append(promot);
		buffer.append(WechatData.OAUTH_URL_FOUR);
		
		return buffer.toString();
	}

}
