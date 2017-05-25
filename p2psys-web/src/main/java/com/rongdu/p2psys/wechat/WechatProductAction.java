package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductMaterialsTypeConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountLogService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.borrow.service.BorrowService;
import com.rongdu.p2psys.nb.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.nb.invest.service.FrozenUserService;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundInService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserPromotService;
import com.rongdu.p2psys.nb.user.service.UserRedPacketService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.model.UserRedPacketModel;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;
import com.rongdu.p2psys.util.WechatUtil;

public class WechatProductAction extends BaseAction<PpfundModel> implements
		ModelDriven<PpfundModel>
{
	private Map<String, Object> map;
	private User user;

	@Resource
	private PpfundService thePpfundService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private PpfundInService thePpfundInService;
	@Resource
	private AccountLogService theAccountLogService;
	@Resource
	private UserService theUserService;
	@Resource
	private BorrowService theBorrowService;
	@Resource
	private BorrowTenderService theBorrowTenderService;
	@Resource
	private FrozenUserService frozenUserService;
	@Resource
	private WechatCacheService wechatCacheService;
	@Resource
	private UserCacheService theUserCacheService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private UserPromotService theUserPromotService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private UserRedPacketService theUserRedPacketService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private ProductMaterialsService productMaterialsService;

	@Action(value = "/nb/wechat/account/pay")
	public String goToPay() throws ClientProtocolException, IOException
	{
		user = getNBSessionUser();

		Long productBasicId = paramLong("productBasicId");
		
		request.setAttribute("appId","");
		request.setAttribute("pwd", "not");
		request.setAttribute("borrow", "");
		request.setAttribute("isBindC", 1);
		request.setAttribute("appId", "");
		request.setAttribute("timestamp","");
		request.setAttribute("nonceStr","");
		request.setAttribute("signature","");
		request.setAttribute("leftAccountMoney",-1);

		ProductBasic productBasic = productBasicService.findById(productBasicId);

		if(ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(productBasic.getProductType().getTypeCategory())||ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE.equals(productBasic.getProductType().getTypeCategory()))
		{
			List<AccountBank> ablist = accountBankService.list(user.getUserId());
			
			if (ablist == null || ablist.size() <= 0)
			{
				//该用户未绑卡，是否线下充值认证？
				UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
				Account account = theAccountService.getAccountByUserId(user.getUserId());
				if(userIdentify.getRealNameStatus() == 1 || account.getTotal()>0){
					request.setAttribute("isBindC", 1);
				}else{
					request.setAttribute("isBindC", 0);
				}
			} 
			else
			{
				request.setAttribute("isBindC", 1);
			}
			
			Ppfund ppfund = thePpfundService.getById(productBasic.getRelatedId());
			
			double leftAccountMoney = ppfund.getAccount() - ppfund.getAccountYes();
			request.setAttribute("leftAccountMoney",leftAccountMoney);
		}
		else
		{
			Borrow borrow = theBorrowService.getById(productBasic.getRelatedId());

			if (null != borrow.getPwd() && !"".equals(borrow.getPwd()))
			{
				request.setAttribute("pwd", "is");
			
			}
			double leftAccountMoney = borrow.getAccount() - borrow.getAccountYes();
			
			request.setAttribute("borrow", "borrow");
			request.setAttribute("leftAccountMoney",leftAccountMoney);
			
		}
		List<Account> list = theAccountService.getAccountListByGroupId(user.getBindId());

		double leftMoney = 0;

		if (list.size() > 0 && !list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				leftMoney += list.get(i).getUseMoney();
			}
		}
		leftMoney = leftMoney- frozenUserService.getLockUseMoney((int) user.getUserId());

		String payPwd = user.getPayPwd();

		if ("".equals(payPwd) || null == payPwd)
		{
			request.setAttribute("payPwd", "nothas");
		}
		else
		{
			request.setAttribute("payPwd", "has");
		}

		request.setAttribute("productBasicId", productBasicId);
		request.setAttribute("leftMoney", leftMoney);

		/**
		 * js api处理
		 */
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();

		if (null != queryString && !"".equals(queryString))
		{
			url = url + "?" + queryString;
		}

		String appId = WechatData.A_APP_ID;
		String appSecret = WechatData.A_APP_SECRET;
		WechatCache wechatCache = wechatCacheService.getByAppId(appId, "JsApi");
		String jsapi_ticket = new WechatJSSign().checkWechatCache(appId, appSecret, wechatCache);

		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket, url,appId, appSecret);

		request.setAttribute("appId", appId);
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());

		return "pay";
	}

	/**
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */
	@Action(value = "/nb/wechat/account/redPacketList")
	public void getRedPacket() throws IOException, ParseException
	{
		map = new HashMap<String, Object>();

		User user = getNBSessionUser();

		List<UserRedPacketModel> redPacketList = theUserRedPacketService.getNotRecommednByUserId(user.getUserId());
		List<UserRedPacketModel> recommendList = theUserRedPacketService.getRecommendByUserId(user.getUserId());

		map.put("notRecommendRedPacket", redPacketList);
		map.put("recommendRedPacket", recommendList);

		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 购买众筹
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/product/buy")
	public String crowdfundingBuy()
	{

		user = getNBSessionUser();

		String project_id = paramString("project_id");
		String invest_id = paramString("invest_id");

		request.setAttribute("project_id", project_id);

		if (null != invest_id && !"".equals(invest_id))
		{
			request.setAttribute("invest_id", invest_id);
		}
		else
		{
			request.setAttribute("invest_id", "");
		}

		if (user != null)
		{
			if (null != user.getRealName() && !"".equals(user.getRealName()))
			{
				request.setAttribute("userName", user.getRealName());
			} 
			else
			{
				request.setAttribute("userName", "");
			}

			request.setAttribute("tel", user.getMobilePhone());
			request.setAttribute("user_id", user.getUserId());
		}
		else
		{
			request.setAttribute("userName", "");
			request.setAttribute("tel", "");
			request.setAttribute("user_id", "");
		}

		return "buy";
	}

	/**
	 * 产品列表首页
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/product/product_list")
	public String wechatFirst() throws IOException
	{
		
		String flagName = new String(paramString("flagName").getBytes("iso-8859-1"),"UTF-8");
		request.setAttribute("productType", paramString("productType"));
		Long id = paramLong("id");
		request.setAttribute("id", id);
		request.setAttribute("flagName", flagName);
		return "product_list";
	}
	
	/**
	 * 美元专区列表页面
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/product/appointment_product_list")
	public String appointment_product_list() throws IOException
	{
		Long id = paramLong("id");
		request.setAttribute("id", id);
		String flagName = new String(paramString("flagName").getBytes("iso-8859-1"),"UTF-8");
		request.setAttribute("flagName", flagName);
		return "appointment_product_list";
	}
	
	/**
	 * 美元专区列表页面
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/product/appointment_product_detail")
	public String appointment_product_detail() throws IOException
	{

		return "appointment_product_detail";
	}
	
	
	
	/**
	 * 美元专区预约
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/product/appointment_product")
	public String appointment_product() throws IOException
	{
		user = getNBSessionUser();
		if(user!=null)
		{
			request.setAttribute("mobilePhone", user.getMobilePhone());
			request.setAttribute("realName",user.getRealName());
			int sex = user.getUserCache().getSex();
			if(sex==0){
				request.setAttribute("sex","女士");
			}else if(sex==1){
				request.setAttribute("sex","先生");
			}

		}

		return "appointment_product";
	}

	@Action(value = "/nb/wechat/product/product_menue")
	public String wechatproductMenue() throws IOException
	{
		request.setAttribute("productType", paramString("productType"));
		return "product_menue";
	}

	/**
	 * 普通产品详情
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Action(value = "/nb/wechat/product/productDetail")
	public String productDetail() throws ClientProtocolException, IOException
	{
		String code = paramString("code");
		request.setAttribute("type", paramString("type"));
		request.setAttribute("redirectURL", paramString("redirectURL"));
		request.setAttribute("product_id", paramString("product_id"));
		request.setAttribute("userCode", "");
		request.setAttribute("appId", "");
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");
		request.setAttribute("title","800bank投资产品年化收益7-11%，快来投");
		request.setAttribute("desc","优质投资产品，灵活投资方式，尽在800bank");
		request.setAttribute("promot",paramString("promot"));
		request.setAttribute("listSize","");
		request.setAttribute("url",getShareJumpPage(paramString("product_id"),paramString("promot")));
		
		List <ProductMaterials> productMaterialsList = productMaterialsService.getMaterialsByProductId(paramLong("product_id")); 
		
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

		if (null != code && !"".equals(code))
		{
			if (null != code && !"".equals(code))
			{
				String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,WechatData.A_APP_SECRET);

				User user = theUserService.getByAttribute("wechatOpenId",openid, "wechatId", WechatData.A_APP_ID);

				if (null != user)
				{
					List<User> list = new ArrayList<User>();
					list = theUserService.getByGroupId(user.getBindId());

					if (list.size() > 1)
					{
						for (int i = 0; i < list.size(); i++)
						{
							if (null != list.get(i).getUserName()&& !"".equals(list.get(i).getUserName()))
							{
								setAttr(ConstantUtil.SESSION_USER,list.get(i));
								setAttr(Constant.SESSION_USER,list.get(i));
								
								UserPromot userPromot = theUserPromotService.findUserPromotByUserId(list.get(i).getUserId());
								request.setAttribute("userCode", userPromot.getCouponCode());
							}
						}
					}
					
					request.setAttribute("listSize",list.size());
				} 
				else
				{
					user = new User();
					user.setWechatId(WechatData.A_APP_ID);
					user.setWechatOpenId(openid);
					user = theUserService.saveWechatUser(user);
					user.setBindId(user.getUserId());
					theUserService.updateUser(user);

					UserCache userCache = new UserCache();
					InetAddress ipAddress = InetAddress.getLocalHost();
					userCache.setUser(user);
					userCache.setAddIp(ipAddress.toString());
					theUserCacheService.saveWechatUserCache(userCache);

					Account account = new Account();
					account.setUser(user);
					theAccountService.save(account);

					UserIdentify userIdentify = new UserIdentify();
					userIdentify.setUser(user);
					theUserIdentifyService.saveWechatUserIdentify(userIdentify);

					UserPromot userPromot = new UserPromot();
					userPromot.setUser(user);
					userPromot = theUserPromotService.saveWechatUserPromot(userPromot);

					setAttr(ConstantUtil.SESSION_USER, user);
					setAttr(Constant.SESSION_USER, user);

				}

				//js api处理
				String url = request.getRequestURL().toString();
				String queryString = request.getQueryString();

				if (null != queryString && !"".equals(queryString))
				{
					url = url + "?" + queryString;
				}

				String appId = WechatData.A_APP_ID;
				String appSecret = WechatData.A_APP_SECRET;
				WechatCache wechatCache = wechatCacheService.getByAppId(appId,"JsApi");
				String jsapi_ticket = new WechatJSSign().checkWechatCache(appId, appSecret, wechatCache);


				Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,url, appId, appSecret);

				request.setAttribute("appId", appId);
				request.setAttribute("timestamp", ret.get("timestamp").toString());
				request.setAttribute("nonceStr", ret.get("nonceStr").toString());
				request.setAttribute("signature", ret.get("signature").toString());
			}
		}

		return "product_detail";
	}

	/**
	 * 组合产品详情
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Action(value = "/nb/wechat/product/mapProductDetail")
	public String mapProductDetail() throws ClientProtocolException,IOException
	{
		String code = paramString("code");

		request.setAttribute("appId","");
		request.setAttribute("redirectURL", paramString("redirectURL"));
		request.setAttribute("product_id", paramString("product_id"));
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");
		request.setAttribute("userCode", "");

		if (null != code && !"".equals(code))
		{
			if (null != code && !"".equals(code))
			{
				String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,WechatData.A_APP_SECRET);

				User user = theUserService.getByAttribute("wechatOpenId",openid, "wechatId", WechatData.A_APP_ID);

				if (null != user)
				{
					List<User> list = new ArrayList<User>();
					list = theUserService.getByGroupId(user.getBindId());

					if (list.size() > 1)
					{
						for (int i = 0; i < list.size(); i++)
						{
							if (null != list.get(i).getUserName()&& !"".equals(list.get(i).getUserName()))
							{
								setAttr(ConstantUtil.SESSION_USER,list.get(i));
								setAttr(ConstantUtil.SESSION_USER,list.get(i));
							}
						}
					}
				}
				else
				{
					user = new User();
					user.setWechatId(WechatData.A_APP_ID);
					user.setWechatOpenId(openid);
					user = theUserService.saveWechatUser(user);
					user.setBindId(user.getUserId());
					theUserService.updateUser(user);

					UserCache userCache = new UserCache();
					InetAddress ipAddress = InetAddress.getLocalHost();
					userCache.setUser(user);
					userCache.setAddIp(ipAddress.toString());
					theUserCacheService.saveWechatUserCache(userCache);

					Account account = new Account();
					account.setUser(user);
					theAccountService.save(account);

					UserIdentify userIdentify = new UserIdentify();
					userIdentify.setUser(user);
					theUserIdentifyService.saveWechatUserIdentify(userIdentify);

					UserPromot userPromot = new UserPromot();
					userPromot.setUser(user);
					userPromot = theUserPromotService.saveWechatUserPromot(userPromot);

					setAttr(ConstantUtil.SESSION_USER, user);
					setAttr(Constant.SESSION_USER, user);

					request.setAttribute("userCode", userPromot.getCouponCode());
				}
				
				//js api处理
				String appId = WechatData.A_APP_ID;
				String appSecret = WechatData.A_APP_SECRET;
				String url = request.getRequestURL().toString();
				String queryString = request.getQueryString();
				WechatCache wechatCache = wechatCacheService.getByAppId(appId,"JsApi");
				String jsapi_ticket = new WechatJSSign().checkWechatCache(appId, appSecret, wechatCache);

				if (null != queryString && !"".equals(queryString))
				{
					url = url + "?" + queryString;
				}

				Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,url, appId, appSecret);

				request.setAttribute("appId", appId);
				request.setAttribute("timestamp", ret.get("timestamp").toString());
				request.setAttribute("nonceStr", ret.get("nonceStr").toString());
				request.setAttribute("signature", ret.get("signature").toString());
			}
		}

		return "map_product_detail";
	}

	/**
	 * 实物众筹产品详情
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Action(value = "/nb/wechat/product/raiseCrowdfundingDetail")
	public String crowdfundingDetail() throws ClientProtocolException,IOException
	{
		String code = paramString("code");
		request.setAttribute("type", paramString("type"));
		request.setAttribute("crowdfunding_type",paramString("crowdfunding_type"));
		request.setAttribute("redirectURL", paramString("redirectURL"));
		request.setAttribute("product_id", paramString("product_id"));
		request.setAttribute("userCode", "");
		request.setAttribute("appId","");
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");

		if (null != code && !"".equals(code))
		{
			if (null != code && !"".equals(code))
			{
				String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,WechatData.A_APP_SECRET);

				User user = theUserService.getByAttribute("wechatOpenId",openid, "wechatId", WechatData.A_APP_ID);

				if (null != user)
				{
					List<User> list = new ArrayList<User>();
					list = theUserService.getByGroupId(user.getBindId());

					if (list.size() > 1)
					{
						for (int i = 0; i < list.size(); i++)
						{
							if (null != list.get(i).getUserName()&& !"".equals(list.get(i).getUserName()))
							{
								setAttr(ConstantUtil.SESSION_USER, list.get(i));
								setAttr(Constant.SESSION_USER, list.get(i));
							}
						}
					}
				} 
				else
				{
					user = new User();
					user.setWechatId(WechatData.A_APP_ID);
					user.setWechatOpenId(openid);
					user = theUserService.saveWechatUser(user);
					user.setBindId(user.getUserId());
					theUserService.updateUser(user);

					UserCache userCache = new UserCache();
					InetAddress ipAddress = InetAddress.getLocalHost();
					userCache.setUser(user);
					userCache.setAddIp(ipAddress.toString());
					theUserCacheService.saveWechatUserCache(userCache);

					Account account = new Account();
					account.setUser(user);
					theAccountService.save(account);

					UserIdentify userIdentify = new UserIdentify();
					userIdentify.setUser(user);
					theUserIdentifyService.saveWechatUserIdentify(userIdentify);

					UserPromot userPromot = new UserPromot();
					userPromot.setUser(user);
					userPromot = theUserPromotService.saveWechatUserPromot(userPromot);

					setAttr(ConstantUtil.SESSION_USER,user);
					setAttr(Constant.SESSION_USER,user);

					request.setAttribute("userCode", userPromot.getCouponCode());
				}
			
				//js api处理
				String url = request.getRequestURL().toString();
				String queryString = request.getQueryString();

				if (null != queryString && !"".equals(queryString))
				{
					url = url + "?" + queryString;
				}

				String appId = WechatData.A_APP_ID;
				String appSecret = WechatData.A_APP_SECRET;
				WechatCache wechatCache = wechatCacheService.getByAppId(appId,"JsApi");
				String jsapi_ticket = new WechatJSSign().checkWechatCache(appId, appSecret, wechatCache);

				Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,url, appId, appSecret);

				request.setAttribute("appId", appId);
				request.setAttribute("timestamp", ret.get("timestamp").toString());
				request.setAttribute("nonceStr", ret.get("nonceStr").toString());
				request.setAttribute("signature", ret.get("signature").toString());
			}
		}

		return "raise_crowdfunding_detail";
	}

	/**
	 * 权益众筹产品详情
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Action(value = "/nb/wechat/product/rightsCrowdfundingDetail")
	public String rightsCrowdfundingDetail() throws ClientProtocolException,
			IOException
	{
		String code = paramString("code");
		request.setAttribute("type", paramString("type"));
		request.setAttribute("crowdfunding_type",paramString("crowdfunding_type"));
		request.setAttribute("redirectURL", paramString("redirectURL"));
		request.setAttribute("product_id", paramString("product_id"));
		request.setAttribute("userCode", "");
		request.setAttribute("appId", "");
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");

		if (null != code && !"".equals(code))
		{
			if (null != code && !"".equals(code))
			{
				String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,WechatData.A_APP_SECRET);

				User user = theUserService.getByAttribute("wechatOpenId",openid, "wechatId", WechatData.A_APP_ID);

				if (null != user)
				{
					List<User> list = new ArrayList<User>();
					list = theUserService.getByGroupId(user.getBindId());

					if (list.size() > 1)
					{
						for (int i = 0; i < list.size(); i++)
						{
							if (null != list.get(i).getUserName()&& !"".equals(list.get(i).getUserName()))
							{
								setAttr(ConstantUtil.SESSION_USER,list.get(i));
								setAttr(Constant.SESSION_USER,list.get(i));
							}
						}
					}
				} 
				else
				{
					user = new User();
					user.setWechatId(WechatData.A_APP_ID);
					user.setWechatOpenId(openid);
					user = theUserService.saveWechatUser(user);
					user.setBindId(user.getUserId());
					theUserService.updateUser(user);

					UserCache userCache = new UserCache();
					InetAddress ipAddress = InetAddress.getLocalHost();
					userCache.setUser(user);
					userCache.setAddIp(ipAddress.toString());
					theUserCacheService.saveWechatUserCache(userCache);

					Account account = new Account();
					account.setUser(user);
					theAccountService.save(account);

					UserIdentify userIdentify = new UserIdentify();
					userIdentify.setUser(user);
					theUserIdentifyService.saveWechatUserIdentify(userIdentify);

					UserPromot userPromot = new UserPromot();
					userPromot.setUser(user);
					userPromot = theUserPromotService.saveWechatUserPromot(userPromot);

					setAttr(ConstantUtil.SESSION_USER,user);
					setAttr(Constant.SESSION_USER,user);

					request.setAttribute("userCode", userPromot.getCouponCode());
				}
				
				String appId = WechatData.A_APP_ID;
				String appSecret = WechatData.A_APP_SECRET;
				WechatCache wechatCache = wechatCacheService.getByAppId(appId,"JsApi");
				String url = request.getRequestURL().toString();
				String queryString = request.getQueryString();
				String jsapi_ticket = new WechatJSSign().checkWechatCache(appId, appSecret, wechatCache);

				if (null != queryString && !"".equals(queryString))
				{
					url = url + "?" + queryString;
				}

				Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,url, appId, appSecret);

				request.setAttribute("appId", appId);
				request.setAttribute("timestamp", ret.get("timestamp").toString());
				request.setAttribute("nonceStr", ret.get("nonceStr").toString());
				request.setAttribute("signature", ret.get("signature").toString());
			}
		}

		return "rights_crowdfunding_detail";
	}

	/**
	 * 实物众筹产品详情
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Action(value = "/nb/wechat/product/productMenueDetail")
	public String productMenueDetail() throws ClientProtocolException,
			IOException
	{
		String code = paramString("code");
		request.setAttribute("type", paramString("type"));
		request.setAttribute("crowdfunding_type",paramString("crowdfunding_type"));
		request.setAttribute("redirectURL", paramString("redirectURL"));
		request.setAttribute("product_id", paramString("product_id"));
		request.setAttribute("userCode", "");
		request.setAttribute("appId","");
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");

		if (null != code && !"".equals(code))
		{
			if (null != code && !"".equals(code))
			{
				String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,WechatData.A_APP_SECRET);

				User user = theUserService.getByAttribute("wechatOpenId",openid, "wechatId", WechatData.A_APP_ID);

				if (null != user)
				{
					List<User> list = new ArrayList<User>();
					list = theUserService.getByGroupId(user.getBindId());

					if (list.size() > 1)
					{
						for (int i = 0; i < list.size(); i++)
						{
							if (null != list.get(i).getUserName()&& !"".equals(list.get(i).getUserName()))
							{
								setAttr(ConstantUtil.SESSION_USER, list.get(i));
								setAttr(Constant.SESSION_USER, list.get(i));
							}
						}
					}
				} 
				else
				{
					user = new User();
					user.setWechatId(WechatData.A_APP_ID);
					user.setWechatOpenId(openid);
					user = theUserService.saveWechatUser(user);
					user.setBindId(user.getUserId());
					theUserService.updateUser(user);

					UserCache userCache = new UserCache();
					InetAddress ipAddress = InetAddress.getLocalHost();
					userCache.setUser(user);
					userCache.setAddIp(ipAddress.toString());
					theUserCacheService.saveWechatUserCache(userCache);

					Account account = new Account();
					account.setUser(user);
					theAccountService.save(account);

					UserIdentify userIdentify = new UserIdentify();
					userIdentify.setUser(user);
					theUserIdentifyService.saveWechatUserIdentify(userIdentify);

					UserPromot userPromot = new UserPromot();
					userPromot.setUser(user);
					userPromot = theUserPromotService.saveWechatUserPromot(userPromot);

					setAttr(ConstantUtil.SESSION_USER, user);
					setAttr(Constant.SESSION_USER, user);

					request.setAttribute("userCode", userPromot.getCouponCode());
				}
				
				//js api处理
				String url = request.getRequestURL().toString();
				String queryString = request.getQueryString();
				String appId = WechatData.A_APP_ID;
				String appSecret = WechatData.A_APP_SECRET;
				WechatCache wechatCache = wechatCacheService.getByAppId(appId,"JsApi");
				String jsapi_ticket = new WechatJSSign().checkWechatCache(appId, appSecret, wechatCache);

				if (null != queryString && !"".equals(queryString))
				{
					url = url + "?" + queryString;
				}

				Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,url, appId, appSecret);

				request.setAttribute("appId", appId);
				request.setAttribute("timestamp", ret.get("timestamp").toString());
				request.setAttribute("nonceStr", ret.get("nonceStr").toString());
				request.setAttribute("signature", ret.get("signature").toString());
			}
		}

		return "product_menue_detail";
	}

	/**
	 * 子详情页面
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/productChildDetail")
	public String productChildDetail() throws ClientProtocolException,IOException
	{
		String id = paramString("product_child_id");

		request.setAttribute("product_child_id", id);
		request.setAttribute("product_id", paramString("product_id"));

		return "product_childDetail";
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

	public Map<String, Object> getMap()
	{
		return map;
	}

	public void setMap(Map<String, Object> map)
	{
		this.map = map;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}
