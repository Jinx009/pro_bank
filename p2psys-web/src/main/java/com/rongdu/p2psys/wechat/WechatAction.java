package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.borrow.service.BorrowService;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;
import com.rongdu.p2psys.nb.homepage.service.HomePageBannerService;
import com.rongdu.p2psys.nb.ppfund.service.PpfundService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserPromotService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.model.WechatCacheModel;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;
import com.rongdu.p2psys.util.WechatUtil;

public class WechatAction extends BaseAction<WechatCacheModel> implements
		ModelDriven<WechatCacheModel> {
	@Resource
	private WechatCacheService wechatCacheService;
	@Resource
	private UserService theUserService;
	@Resource
	private UserCacheService theUserCacheService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private UserPromotService theUserPromotService;
	@Resource
	private SystemConfigService systemConfigService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private HomePageBannerService homePageBannerService;
	@Resource
	private BorrowService theBorrowService;
	@Resource
	private PpfundService thePpfundService;

	private Map<String, Object> map;

	/**
	 * 获取微信首页图片
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/banner")
	public void getIndexBanner() throws IOException {
		List<HomePageBanner> list = homePageBannerService.findAllWechat();

		map = new HashMap<String, Object>();
		map.put("data", list);

		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 关注页面
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/attention")
	public String wechatAttention() {
		return "attention";
	}

	/**
	 * 新800公告
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/newRule")
	public String wechatNewAttention() {
		return "newRule";
	}

	/**
	 * 800公告
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/rule")
	public String wechatRule() {
		return "rule";
	}

	/**
	 * 10月1日活动
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Action(value = "/nb/wechat/doubleGift")
	public String wechatDoubleGift() throws ClientProtocolException,
			IOException {
		request.setAttribute("appId", "");
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");

		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		String appId = WechatData.A_APP_ID;
		String appSecret = WechatData.A_APP_SECRET;

		if (null != queryString && !"".equals(queryString)) {
			url = url + "?" + queryString;
		}

		WechatCache wechatCache = wechatCacheService.getByAppId(appId, "JsApi");
		WechatJSSign wechatJSSign = new WechatJSSign();
		String jsapi_ticket = wechatJSSign.checkWechatCache(appId, appSecret,
				wechatCache);
		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket, url,
				appId, appSecret);

		request.setAttribute("appId", appId);
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());

		return "doubleGift";
	}

	/**
	 * 五星专区
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/5star")
	public String wechatFiveStar() {
		return "5star";
	}

	/**
	 * 一起领赏金
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/share")
	public String wechatShare() throws IOException {
		String code = paramString("code");

		request.setAttribute("size", 0);
		request.setAttribute("wechatUrl", getWechatUrl());

		// 微信进入
		if (null != code && !"".equals(code)) {
			System.out.println("nb share wechat code:" + code);

			String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,
					WechatData.A_APP_SECRET);

			User user = theUserService.getByAttribute("wechatOpenId", openid,
					"wechatId", WechatData.A_APP_ID);

			// 判断是否已经绑定
			if (null != user) {
				List<User> list = new ArrayList<User>();
				list = theUserService.getByGroupId(user.getBindId());

				if (list.size() > 1) {
					for (int i = 0; i < list.size(); i++) {
						if (null != list.get(i).getUserName()
								&& !"".equals(list.get(i).getUserName())) {
							setAttr(ConstantUtil.SESSION_USER, list.get(i));
							setAttr(Constant.SESSION_USER, list.get(i));
						}
					}
				}

				request.setAttribute("size", list.size());
			}
		}

		return "share";
	}

	/**
	 * 开发者校验
	 * 
	 * @throws IOException
	 */
	@Action(value = "/wechat/index")
	public void wechatIndex() throws IOException {
		if ("get".equalsIgnoreCase(request.getMethod())) {

			String echostr = request.getParameter("echostr");

			response.getWriter().print(echostr);
		}
	}

	/**
	 * 被邀请绑定
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/beinvited")
	public String wechatBeInvited() throws IOException {
		request.setAttribute("appId", "");
		request.setAttribute("promotCode", paramString("promotCode"));
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");

		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		String appId = WechatData.A_APP_ID;
		String appSecret = WechatData.A_APP_SECRET;

		WechatCache wechatCache = wechatCacheService.getByAppId(appId, "JsApi");
		String jsapi_ticket = new WechatJSSign().checkWechatCache(appId,
				appSecret, wechatCache);

		if (null != queryString && !"".equals(queryString)) {
			url = url + "?" + queryString;
		}

		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket, url,
				appId, appSecret);

		request.setAttribute("appId", appId);
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());

		return "beinvited";
	}

	/**
	 * 被邀请人点开链接
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Action(value = "/nb/wechat/invited")
	public String wechatInvited() throws ClientProtocolException, IOException {

		String code = paramString("code");
		String imgUrl = paramString("imgUrl");
		String nickName = paramString("nickName");
		String promotCode = paramString("promotCode");

		request.setAttribute("appId", "");
		request.setAttribute("isSelf", "not");
		request.setAttribute("promotCode", promotCode);
		request.setAttribute("imgUrl", imgUrl);
		request.setAttribute("nickName", nickName);
		request.setAttribute("appId", "");
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");
		request.setAttribute("listSize", 1);

		// 微信转发的url
		if (null != code && !"".equals(code)) {
			System.out.println("wechat code:" + code);

			String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,
					WechatData.A_APP_SECRET);

			User user = theUserService.getByAttribute("wechatOpenId", openid,
					"wechatId", WechatData.A_APP_ID);

			// 该微信号码已经注册过了
			if (null != user) {
				setAttr(ConstantUtil.SESSION_USER, user);

				List<User> list = theUserService.getByGroupId(user.getBindId());

				if (list.size() > 1) {
					/**
					 * 把有手机号的账号放到session中
					 */
					for (int i = 0; i < list.size(); i++) {
						if (null != list.get(i).getUserName()
								&& !"".equals(list.get(i).getUserName())) {
							UserCache userCache = list.get(i).getUserCache();
							if (1 == userCache.getStatus()) {
								setAttr(ConstantUtil.SESSION_USER, user);

								UserPromot userPromot = theUserPromotService
										.findUserPromotByUserId(list.get(i)
												.getUserId());
								request.setAttribute("listSize", 1);

								if (promotCode.equals(userPromot
										.getCouponCode())) {
									request.setAttribute("isSelf", "is");
								}
							} else {
								setAttr(ConstantUtil.SESSION_USER, list.get(i));

								UserPromot userPromot = theUserPromotService
										.findUserPromotByUserId(list.get(i)
												.getUserId());
								request.setAttribute("listSize", list.size());

								if (promotCode.equals(userPromot
										.getCouponCode())) {
									request.setAttribute("isSelf", "is");
								}
							}
						}
					}
				}
			} else {
				/**
				 * 创建新微信用户
				 */
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
				userPromot = theUserPromotService
						.saveWechatUserPromot(userPromot);

				setAttr(ConstantUtil.SESSION_USER, user);
				request.setAttribute("listSize", 1);
			}
		} else {
			request.getSession().setAttribute(ConstantUtil.SESSION_USER, null);
		}

		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		String appId = WechatData.A_APP_ID;
		String appSecret = WechatData.A_APP_SECRET;
		WechatCache wechatCache = wechatCacheService.getByAppId(appId, "JsApi");
		String jsapi_ticket = new WechatJSSign().checkWechatCache(appId,
				appSecret, wechatCache);

		if (null != queryString && !"".equals(queryString)) {
			url = url + "?" + queryString;
		}

		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket, url,
				appId, appSecret);
		session.put(Constant.SYSTEM, Constant.COOPERATE_TYPE__WECHAT);//从微信登陆
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());

		return "invited";
	}

	/**
	 * 获取用户信息
	 * 
	 * @throws IOException
	 *//*
	@Action(value = "/nb/wechat/invite")
	public String wechatInvite() throws IOException {
		String code = paramString("code");

		request.setAttribute("appId", "");
		request.setAttribute("json", "");
		request.setAttribute("isAllow", "is");
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");
		request.setAttribute("urlOne", getUrlOne());
		request.setAttribute("urlTwo", WechatData.OAUTH_URL_FOUR);
		request.setAttribute("promotCode", "");
		request.setAttribute("json", "");

		if (null != code && !"".equals(code)) {
			String result = WechatUtil.getUserInfoFirst(code,
					WechatData.A_APP_ID, WechatData.A_APP_SECRET);

			System.out.println(result);

			String accessToken = result.split("#")[1];
			String openId = result.split("#")[0];

			String json = WechatUtil.getRealUserInfo(accessToken, openId);

			System.out.println("userInfo:" + json);

			User user = getNBSessionUser();

			UserPromot userPromot = theUserPromotService
					.findUserPromotByUserId(user.getUserId());

			request.setAttribute("promotCode", userPromot.getCouponCode());
			request.setAttribute("json", json);

			String appId = WechatData.A_APP_ID;
			String appSecret = WechatData.A_APP_SECRET;
			WechatCache wechatCache = wechatCacheService.getByAppId(appId,
					"JsApi");
			String url = request.getRequestURL().toString();
			String queryString = request.getQueryString();
			String jsapi_ticket = new WechatJSSign().checkWechatCache(appId,
					appSecret, wechatCache);

			if (null != queryString && !"".equals(queryString)) {
				url = url + "?" + queryString;
			}

			Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,
					url, appId, appSecret);

			request.setAttribute("appId", appId);
			request.setAttribute("timestamp", ret.get("timestamp").toString());
			request.setAttribute("nonceStr", ret.get("nonceStr").toString());
			request.setAttribute("signature", ret.get("signature").toString());
		} else {
			request.setAttribute("isAllow", "isNot");
		}

		return "invite";
	}*/

	/**
	 * 首页
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/800bank")
	public String wechatFirst() throws IOException {
		String code = paramString("code");

		request.setAttribute("appId", "");
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");
		request.setAttribute("userCode", "");

		/**
		 * 微信进入
		 */
		if (null != code && !"".equals(code)) {
			System.out.println("wechat code:" + code);

			String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,
					WechatData.A_APP_SECRET);

			User user = theUserService.getByAttribute("wechatOpenId", openid,
					"wechatId", WechatData.A_APP_ID);

			if (null != user) {
				List<User> list = new ArrayList<User>();
				list = theUserService.getByGroupId(user.getBindId());

				if (list.size() > 1) {
					for (int i = 0; i < list.size(); i++) {
						if (null != list.get(i).getUserName()
								&& !"".equals(list.get(i).getUserName())) {
							setAttr(ConstantUtil.SESSION_USER, list.get(i));
							setAttr(Constant.SESSION_USER, list.get(i));
						}
					}
				}
			} else {
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
				userPromot = theUserPromotService
						.saveWechatUserPromot(userPromot);

				setAttr(ConstantUtil.SESSION_USER, user);
				setAttr(Constant.SESSION_USER, user);
				session.put(Constant.SESSION_USER, user);

				request.setAttribute("userCode", userPromot.getCouponCode());

			}

			// js api处理
			String url = request.getRequestURL().toString();
			String queryString = request.getQueryString();

			if (null != queryString && !"".equals(queryString)) {
				url = url + "?" + queryString;
			}

			String appId = WechatData.A_APP_ID;
			String appSecret = WechatData.A_APP_SECRET;
			WechatCache wechatCache = wechatCacheService.getByAppId(appId,
					"JsApi");
			String jsapi_ticket = new WechatJSSign().checkWechatCache(appId,
					appSecret, wechatCache);

			Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,
					url, appId, appSecret);

			request.setAttribute("appId", appId);
			request.setAttribute("timestamp", ret.get("timestamp").toString());
			request.setAttribute("nonceStr", ret.get("nonceStr").toString());
			request.setAttribute("signature", ret.get("signature").toString());
		}

		// 推荐页产品信息
		SystemConfig systemConfig = systemConfigService
				.findByNid("recommendId");

		ProductBasic productBasic = productBasicService.findById(Long
				.valueOf(systemConfig.getValue()));

		// 判断是组合产品还是子产品
		if (null != productBasic.getRelatedId()) {
			StringBuffer buffer = new StringBuffer();

			buffer.append("/nb/wechat/product/productDetail.action?product_id=");
			buffer.append(productBasic.getId());
			buffer.append("%26redirectURL=/nb/wechat/account/800bank.html");

			request.setAttribute("url", buffer.toString());
		} else {
			StringBuffer buffer = new StringBuffer();

			buffer.append("/nb/wechat/product/mapProductDetail.action?product_id=");
			buffer.append(productBasic.getId());
			buffer.append("%26redirectURL=/nb/wechat/account/800bank.html");

			request.setAttribute("url", buffer.toString());
		}

		ProductType productType = productBasic.getProductType();
		if (ProductTypeConstant.PRODUCT_CATEGORY__BORROW.equals(productType
				.getTypeCategory())
				|| ProductTypeConstant.PRODUCT_CATEGORY__VIP.equals(productType
						.getTypeCategory())
				|| ProductTypeConstant.PRODUCT_CATEGORY__FIVESTAR
						.equals(productType.getTypeCategory())) {
			Borrow borrow = theBorrowService.getById(productBasic
					.getRelatedId());
			if (null != borrow.getInterestRate()) {
				request.setAttribute("interestRateValue", borrow
						.getInterestRate().getRate());
			} else {
				request.setAttribute("interestRateValue", null);
			}

		}
		if (ProductTypeConstant.PRODUCT_CATEGORY__PPFUND.equals(productType
				.getTypeCategory())
				|| ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE
						.equals(productType.getTypeCategory())) {
			Ppfund ppfund = thePpfundService.getById(productBasic
					.getRelatedId());
			if (null != ppfund.getInterestRate()) {
				request.setAttribute("interestRateValue", ppfund
						.getInterestRate().getRate());
			} else {
				request.setAttribute("interestRateValue", null);
			}
		}
		request.setAttribute("product", productBasic);
		request.setAttribute("productType", productType);

		request.setAttribute("recommendProp1",
				systemConfigService.findByNid("recommendProp1").getValue());
		request.setAttribute("recommendProp2",
				systemConfigService.findByNid("recommendProp2").getValue());
		request.setAttribute("recommendProp3",
				systemConfigService.findByNid("recommendProp3").getValue());

		return "800bank";
	}

	/**
	 * 非微信绑定
	 * 
	 * @return
	 */
	@Action(value = "/nb/wechat/account/bind")
	public String jump() {
		String redirectURL = paramString("redirectURL");

		request.setAttribute("redirectURL", redirectURL);
		request.setAttribute("listSize", 1);

		return "bind";
	}

	/**
	 * 获取opendi中转页面
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/wechat_bind")
	public String wechatJump() throws IOException {
		String code = paramString("code");
		String ui = paramString("ui");
		String redirectURL = paramString("redirectURL");
		int size = 0;

		request.setAttribute("ui", ui);
		request.setAttribute("redirectURL", redirectURL);

		String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,
				WechatData.A_APP_SECRET);

		List<User> list = new ArrayList<User>();

		User user = theUserService.getByAttribute("wechatOpenId", openid,
				"wechatId", WechatData.A_APP_ID);

		if (null != user) {
			list = theUserService.getByGroupId(user.getBindId());

			if (list.size() > 1) {
				size = list.size();
				for (int i = 0; i < list.size(); i++) {
					if (null != list.get(i).getUserName()
							&& !"".equals(list.get(i).getUserName())) {
						UserCache userCache = list.get(i).getUserCache();
						if (1 == userCache.getStatus()) {
							setAttr(ConstantUtil.SESSION_USER, null);
							setAttr(Constant.SESSION_USER, null);
							session.put(Constant.SESSION_USER, null);
							size = 0;
						} else {
							setAttr(ConstantUtil.SESSION_USER, list.get(i));
							setAttr(Constant.SESSION_USER, list.get(i));
							session.put(Constant.SESSION_USER, list.get(i));
						}
					}
				}
			} else {
				setAttr(ConstantUtil.SESSION_USER, user);
				setAttr(Constant.SESSION_USER, user);
				session.put(Constant.SESSION_USER, user);
			}
		} else {
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
			theUserPromotService.saveWechatUserPromot(userPromot);

			setAttr(ConstantUtil.SESSION_USER, user);
			setAttr(Constant.SESSION_USER, user);
			session.put(Constant.SESSION_USER, user);

		}
		session.put(Constant.SYSTEM, Constant.COOPERATE_TYPE__WECHAT);//从微信登陆
		request.setAttribute("listSize", size);

		return "wechat_bind";
	}

	public static String getUrlOne() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(WechatData.OAUTH_URL_ONE);
		buffer.append(WechatData.OAUTH_URL_TWO);
		buffer.append("/nb/wechat/invited.action?");

		return buffer.toString();
	}

	public static String getWechatUrl() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(WechatData.OAUTH_URL_ONE);
		buffer.append(WechatData.OAUTH_URL_TWO);
		buffer.append("/nb/wechat/invite.action");
		buffer.append(WechatData.OAUTH_URL_FIVE);

		return buffer.toString();
	}

	/**
	 * getter&setter
	 * 
	 * @return
	 */
	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public WechatCacheService getWechatCacheService() {
		return wechatCacheService;
	}

	public void setWechatCacheService(WechatCacheService wechatCacheService) {
		this.wechatCacheService = wechatCacheService;
	}

}
