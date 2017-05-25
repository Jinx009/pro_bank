package com.rongdu.p2psys.web.ppfund;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductMaterialsTypeConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundUpload;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.ppfund.service.PpfundService;
import com.rongdu.p2psys.ppfund.service.PpfundUploadService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.util.WechatData;

/**
 * PPfund资金管理产品
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月18日
 */
public class PpfundAction extends BaseAction<PpfundModel> implements
		ModelDriven<PpfundModel> {
	@Resource
	private PpfundService ppfundService;
	@Resource
	private AccountService accountService;
	@Resource
	private PpfundInService ppfundInService;
	@Resource
	private PpfundUploadService ppfundUploadService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductMaterialsService productMaterialsService;

	private Map<String, Object> data;

	private User user;

	/**
	 * PPfund资金管理产品首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/ppfund/index", results = { @Result(name = "index", type = "ftl", location = "/ppfund/index.html") })
	public String index() throws Exception {
		return "index";
	}

	/**
	 * PPfund资金管理产品列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/ppfund/ppfundList")
	public void ppfundList() throws Exception {
		data = new HashMap<String, Object>();
		PageDataList<PpfundModel> pageData = ppfundService.list(model);
		data.put("data", pageData);
		data.put("nowTime", new Date());
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * PPfund资金管理产品详情页
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/ppfund/detail",interceptorRefs = { @InterceptorRef("globalStack") },
			results = {
			@Result(name = "detail", type = "ftl", location = "/nb/pc/product/ppfund_detail.html"),
			@Result(name = "wxdetail", type = "ftl", location = "/nb/wechat/product/product_detail.html")})	
	public String detail() throws Exception {
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
			if(getNBSessionUser() != null){
				if(null != product){
					request.setAttribute("prodId", product.getId());
					request.setAttribute("flagId", flagId);
					request.setAttribute("ppfundId", relatedId);
				}else{
					throw new BorrowException("当前借款标不存在或已流标");
				}
				
			}
			String productId = product.getId().toString();
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
		}else{
			Long relatedId =  paramLong("id");
			Long flagId = paramLong("flagId");
			ProductBasic product = productBasicService.getProductBasicInfoByFlagId(flagId, relatedId);
			if(getNBSessionUser() != null){
				if(null != product){
					request.setAttribute("prodId", product.getId());
					request.setAttribute("flagId", flagId);
					request.setAttribute("ppfundId", relatedId);
				}else{
					throw new BorrowException("当前借款标不存在或已流标");
				}
				
			}
			Ppfund ppfund = ppfundService.getPpfundById(relatedId);
			// 判断该标为体验标
			if (ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE.equals(ppfund
					.getProductType().getTypeCategory())) {
				User user = getNBSessionUser();
				// 当前用户体验金
				boolean em = theExperienceGoldService.getCanExperienceGold(user);

				request.setAttribute("isEGold", em);
				List<AccountBank> ablist = accountBankService.list(user
						.getUserId());
				if (ablist == null || ablist.size() <= 0) {
					//该用户未绑卡，是否线下充值认证？
					UserIdentify userIdentify = userIdentifyService.getUserIdentifyByUserId(user.getUserId());
					Account account = accountService.getAccount(user.getUserId());
					if(userIdentify.getRealNameStatus() == 1 || account.getTotal()>0){
						request.setAttribute("isBindC", 1);
					}else{
						request.setAttribute("isBindC", 0);
					}
				} else {
					request.setAttribute("isBindC", 1);
				}
			}
			return "detail";
		}
	}

	/**
	 * 详情页具体数据
	 * 
	 * @throws Exception
	 */
	@Action(value = "/ppfund/ppfundDetail", interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public void ppfundDetail() throws Exception {
		this.saveToken("ppfundTenderToken");
		String ppfundTenderToken = (String) session.get("ppfundTenderToken");
		user = getSessionUser();
		data = new HashMap<String, Object>();
		long ppfundId = paramLong("id");
		
		//获取产品资料图片
		List<PpfundUpload> ppfundUploads = ppfundUploadService.findByPpfundIdAndType(ppfundId, PpfundUpload.PPFUND_DATA);
		data.put("ppfundUploads", ppfundUploads);
		//获取产品头像
		PpfundUpload ppfundImag = ppfundUploadService.getPpfundImg(ppfundId);
		data.put("ppfundImag", ppfundImag);
		
		Ppfund ppfund = ppfundService.getPpfundById(ppfundId);
		ppfund.setPidNo(ppfund.getProductType().getTypeName());
		// 判断该标为体验标
		if (ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE.equals(ppfund.getProductType().getTypeCategory()))
		{
			//当前用户体验金
			ExperienceGoldModel em = theExperienceGoldService.getEGByUserId(user.getUserId());
			double experienceMoney=0;
			if(null !=em && em.getStatus()==0){
				experienceMoney = em.getMoney();
			}
			data.put("goldMoney",experienceMoney);
			data.put("isEGold", true);
			List<AccountBank> ablist = accountBankService
					.list(user.getUserId());
			if (ablist == null || ablist.size() <= 0)
			{
				//该用户未绑卡，是否线下充值认证？
				UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
				Account account = accountService.getAccount(user.getUserId());
				if(userIdentify.getRealNameStatus() == 1 || account.getTotal()>0){
					data.put("isBindC", 1);
				}else{
					data.put("isBindC", 0);
				}
			} else
			{
				data.put("isBindC", 1);
			}
		}
		PageDataList<PpfundInModel> list = ppfundInService.list(ppfund.getId(), this.model.getPage(), 0);
		data.put("ppfund", ppfund);
		data.put("ppfundTenderToken", ppfundTenderToken);
		data.put("accountWait", BigDecimalUtil.sub(ppfund.getAccount(), ppfund.getAccountYes()));
		data.put("data", list);
		data.put("url", Global.getValue("adminurl"));
		if(user != null){
			Account acc = accountService.findByUser(user.getUserId());
			AccountModel account = AccountModel.instance(acc);
			account.setUseMoney(accountService.getSumAccount(user.getUserId()));
			data.put("account", account);
			data.put("payPwd", StringUtil.isBlank(user.getPayPwd()));
			
			//统计用户累计购买金额
			double mostAccountTotal = ppfundInService.getMostAccountTotalByUserAndPpfund(user, ppfund);
			data.put("mostAccountTotal", mostAccountTotal);
			
			//获取当前登录用户基本信息
			UserCache login_user_cache = userCacheService.findByUserId(user.getUserId());
			data.put("payPwdLock", UserCacheModel.instance(login_user_cache).isPayPwdLock());
			data.put("login_user_cache", login_user_cache);
			
//			List<UserRedPacketModel> packets = userRedPacketService.findByUser(user);
//			data.put("packets", packets);//现金管理不能用红包
			
			//用户认证信息
			UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
			UserIdentifyModel userIdentifyModel = UserIdentifyModel.instance(userIdentify);
			userIdentifyModel.setUser(null);
			data.put("userInvestIdentify", userIdentifyModel);

			data.put("userStates", user.getUserCache().getUserNature());
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * PPfund购买记录分页
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/ppfund/ppfundInForJson")
	public void ppfundInForJson() throws Exception {
		data = new HashMap<String, Object>();
		long ppfundId = paramLong("id");
		PageDataList<PpfundInModel> list = ppfundInService.list(ppfundId, this.model.getPage(), 0);
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
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
