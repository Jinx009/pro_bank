package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.model.InvestRecordModel;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountLogService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.nb.borrow.service.BorrowService;
import com.rongdu.p2psys.nb.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.nb.invest.service.FrozenUserService;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserRedPacketService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.service.PpfundProtocolService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserRedPacketModel;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;

public class WechatAccountAction extends BaseAction<AccountModel> implements
		ModelDriven<AccountModel> {

	@Resource
	private AccountService theAccountService;
	@Resource
	private WechatCacheService wechatCacheService;
	@Resource
	private UserService theUserService;
	@Resource
	private BorrowCollectionService theBorrowCollectionService;
	@Resource
	private FrozenUserService frozenUserService;
	@Resource
	private AccountLogService theAccountLogService;
	@Resource
	private BorrowTenderService theBorrowTenderService;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private UserRedPacketService theUserRedPacketService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private PpfundProtocolService ppfundProtocolService;
	@Resource
	private PpfundService thePpfundService;
	@Resource
	private BorrowService theBorrowService;
	@Resource
	private UserIdentifyService theUserIdentifyService;

	private User user;

	private Map<String, Object> map;

	/**
	 * 跳转
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/main")
	public String accountMain() throws IOException {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
		request.setAttribute("randTime", sim.format(new Date()));
		return "main";
	}

	@Action(value = "/nb/wechat/account/getAccountData")
	public void getAccountDat() throws IOException, ParseException {

		map = new HashMap<String, Object>();

		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);

		List<User> userList = theUserService.getByGroupId(user.getBindId());

		if (userList.size() > 0) {
			double total = 0, useMoney = 0, noUseMoney = 0, collection = 0, sumInMoney = 0;

			for (User user_ : userList) {
				Account account_ = theAccountService.getAccountByUserId(user_
						.getUserId());

				// 被锁住金额
				double lockUserMoney = frozenUserService
						.getLockUseMoney(Integer.parseInt(String.valueOf(user_
								.getUserId())));

				// 在投金额
				double sumInMoney_ = theBorrowCollectionService.inInvestAmount(
						user_, 0);

				// 总资产
				total += account_.getTotal();
				// 可用余额
				useMoney += (account_.getUseMoney() - lockUserMoney);
				// 冻结金额
				noUseMoney += account_.getNoUseMoney();
				// 待收金额
				collection += account_.getCollection();

				sumInMoney += sumInMoney_;
			}

			map.put("total", total);
			map.put("useMoney", useMoney);
			map.put("noUseMoney", noUseMoney);
			map.put("collection", collection);
			map.put("sumInMoney", sumInMoney);
		} else {
			Account account = theAccountService.getAccountByUserId(user
					.getUserId());
			// 被锁住金额
			double lockUserMoney = frozenUserService.getLockUseMoney(Integer
					.parseInt(String.valueOf(user.getUserId())));
			// 在投金额
			double sumInMoney_ = theBorrowCollectionService.inInvestAmount(
					user, 0);

			map.put("total", account.getTotal());
			map.put("useMoney", (account.getUseMoney() - lockUserMoney));
			map.put("noUseMoney", account.getNoUseMoney());
			map.put("collection", account.getCollection());
			map.put("sumInMoney", sumInMoney_);
		}

		// 累计净收益(多身份)
		double netProfit = theBorrowCollectionService.netProfit(user);

		map.put("netProfit", netProfit);

		// 待收利息总额(多身份)
		double collectionInterest = theBorrowCollectionService
				.getInterestByUser(user);
		map.put("collectionInterest", collectionInterest);

		// 当前用户体验金
		ExperienceGoldModel em = theExperienceGoldService.getEGByUserId(user
				.getUserId());
		double experienceMoney = 0;
		if (null != em && em.getStatus() == 0) {
			experienceMoney = em.getMoney();
		}
		map.put("experienceMoney", experienceMoney);

		// 当前用户红包总金额
		List<UserRedPacketModel> notUsedList = theUserRedPacketService
				.findNotUsed(user.getUserId());
		double redPacketMoney = 0;
		if (notUsedList.size() > 0) {
			for (UserRedPacketModel userRedPacketModel : notUsedList) {
				redPacketMoney += userRedPacketModel.getAmount();
			}
		}
		map.put("redPacketMoney", redPacketMoney);

		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 资金明细页面跳转
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Action(value = "/nb/wechat/account/log")
	public String capitalLog() throws ClientProtocolException, IOException {

		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");

		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();

		if (null != queryString && !"".equals(queryString)) {
			url = url + "?" + queryString;
		}

		String appId = WechatData.A_APP_ID;
		String appSecret = WechatData.A_APP_SECRET;
		WechatCache wechatCache = wechatCacheService.getByAppId(appId, "JsApi");
		String jsapi_ticket = new WechatJSSign().checkWechatCache(appId,
				appSecret, wechatCache);

		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket, url,
				appId, appSecret);

		request.setAttribute("appId", appId);
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());

		return "accountLog";
	}

	/**
	 * 资金明细列表
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/logList")
	public void capitalLogList() throws IOException {
		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		AccountLogModel accountLog = new AccountLogModel();
		accountLog.setPage(paramInt("page"));
		accountLog.setUser(user);
		PageDataList<AccountLogModel> list = theAccountLogService
				.multipleIdentities(accountLog);

		map = new HashMap<String, Object>();
		map.put("data", list);
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 资产配置记录跳转页面
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/account/assets")
	public String assetsPage() {
		return "invest";
	}

	/**
	 * 弹出协议
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/account/protocol")
	public String protocolPage() {
		return "protocol";
	}

	/**
	 * 资产配置记录(现金、海外、固定)
	 * 
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */
	@Action(value = "/nb/wechat/account/assetsList")
	public void assetsList() throws IOException, ParseException {
		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		InvestRecordModel model = new InvestRecordModel();
		model.setPage(paramInt("page"));
		model.setUser(user);
		model.setFlag(1);
		PageDataList<InvestRecordModel> pageDataList = theBorrowTenderService
				.multipleIdentitiesList(model);
		map = new HashMap<String, Object>();
		map.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 未到期资产配置记录(现金、海外、固定)
	 * 
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */
	@Action(value = "/nb/wechat/account/assetsListByTime")
	public void assets() throws IOException, ParseException {
		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		InvestRecordModel model = new InvestRecordModel();
		model.setPage(paramInt("page"));
		model.setUser(user);
		model.setFlag(0);
		PageDataList<InvestRecordModel> pageDataList = theBorrowTenderService
				.multipleIdentitiesList(model);
		map = new HashMap<String, Object>();
		map.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 资产配置记录(浮动)
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/entrustList")
	public void entrustList() throws IOException {
		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		BorrowTenderModel model = new BorrowTenderModel();
		model.setPage(paramInt("page"));
		model.setUser(user);
		model.setStatus(paramInt("status"));
		model.setFlag(1);
		PageDataList<BorrowTenderModel> pageDataList = theBorrowTenderService
				.multiple(model);
		map = new HashMap<String, Object>();
		map.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 未到期资产配置记录(浮动)
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/entrustListByTime")
	public void entrust() throws IOException {
		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		BorrowTenderModel model = new BorrowTenderModel();
		model.setPage(paramInt("page"));
		model.setUser(user);
		model.setStatus(paramInt("status"));
		model.setFlag(0);
		PageDataList<BorrowTenderModel> pageDataList = theBorrowTenderService
				.multiple(model);
		map = new HashMap<String, Object>();
		map.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(map));
	}

	@Action(value = "/nb/wechat/account/protocolContent")
	public void tenderProcol() throws IOException {
		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		String type = paramString("type");
		Long id = paramLong("id");
		map = new HashMap<String, Object>();
		String content = "";
		if (type.equals("ppfund")) {
			content = ppfundProtocolService.buildPpfundProtocol(type, id);
			map.put("content", content);
		} else if (type.equals("borrow")) {
			content = theBorrowTenderService.builderTenderProcol(type, id);
			map.put("content", content);
		} else if (type.equals("product")) {
			ProductBasic productBasic = productBasicService.findById(id);
			if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND
					.equals(productBasic.getProductType().getTypeCategory())) {
				Ppfund ppfund = thePpfundService.getById(productBasic
						.getRelatedId());
				content = ppfundProtocolService.buildPpfundProtocol(type,
						ppfund.getId());
			}
			if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW
					.equals(productBasic.getProductType().getTypeCategory())
					|| ProductTypeConstant.PRODUCT_CATEGORY__VIP
							.equals(productBasic.getProductType()
									.getTypeCategory())) {
				Borrow borrow = theBorrowService.getById(productBasic
						.getRelatedId());
				content = theBorrowTenderService.builderTenderProcol(type,
						borrow.getId());
			}

			map.put("content", content);
		}
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 我的体验金
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/account/experienceGold", results = { @Result(name = "gold", type = "ftl", location = "/nb/wechat/account/experienceGold.html") })
	public String bank() throws Exception {
		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		boolean flag = theExperienceGoldService.getCanExperienceGold(user);
		request.setAttribute("egFlag", flag);
		List<AccountBank> ablist = accountBankService.list(user.getUserId());
		if (ablist == null || ablist.size() <= 0) {
			// 该用户未绑卡，是否线下充值认证？
			UserIdentify userIdentify = theUserIdentifyService
					.getUserIdentifyByUserId(user.getUserId());
			Account account = theAccountService.getAccountByUserId(user
					.getUserId());
			if (userIdentify.getRealNameStatus() == 1 || account.getTotal() > 0) {
				request.setAttribute("isBindC", 1);
			} else {
				request.setAttribute("isBindC", 0);
			}
		} else {
			request.setAttribute("isBindC", 1);
		}
		return "gold";
	}

	/**
	 * 锁定金额明细页面
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/account/lock")
	public String lockMoneyPage() {
		return "lockCash";
	}

	/**
	 * 锁定金额明细
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/lockRecord")
	public void lockMoneyRecord() throws IOException {
		user = (User) request.getSession().getAttribute(
				ConstantUtil.SESSION_USER);
		InvestRecordModel model = new InvestRecordModel();
		model.setPage(paramInt("page"));
		model.setUser(user);
		PageDataList<InvestRecordModel> pageDataList = theBorrowTenderService
				.lockCashRecord(model);
		map = new HashMap<String, Object>();
		map.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 提现到账规则描述页面
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/account/rules")
	public String cashRules() {
		return "rules";
	}

	/**
	 * 待收金额页面
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/account/collect")
	public String collectPage() {
		return "collect";
	}

	public static boolean getTimestamp(int timestamp) {
		int currentTimestamp = (int) (System.currentTimeMillis() / 1000);

		if ((currentTimestamp - timestamp) >= 7000) {
			return false;
		}

		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
