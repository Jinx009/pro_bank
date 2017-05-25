package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.card.domain.ActivityRecord;
import com.rongdu.p2psys.nb.card.domain.CardFactory;
import com.rongdu.p2psys.nb.card.model.CardFactoryModel;
import com.rongdu.p2psys.nb.card.service.ActivityRecordService;
import com.rongdu.p2psys.nb.card.service.CardFactoryService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserPromotService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;
import com.rongdu.p2psys.util.WechatUtil;

public class WechatCardAction extends BaseAction<CardFactoryModel> implements ModelDriven<CardFactoryModel>
{
	@Resource
	private CardFactoryService cardFactoryService;
	@Resource
	private ActivityRecordService activityRecordService;
	@Resource
	private WechatCacheService wechatCacheService;
	@Resource
	private UserService theUserService;
	@Resource
	private UserPromotService theUserPromotService;
	@Resource
	private UserCacheService theUserCacheService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private NoticeService noticeService;
	  
	private Map<String,Object> data;
	
	/**
	 * 抽奖
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/card/qcard" )
	public String wechatQCard() throws IOException
	{
		return "qcard";
	}

	
	/**
	 * 抽奖
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/card/card" )
	public String wechatCard() throws IOException
	{
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");
		request.setAttribute("userId",0);
		User user = null;
		
		String code = paramString("code");
		request.setAttribute("status",0);
		if(StringUtil.isNotBlank(code))
		{
			String openid = WechatUtil.getOpenid(code,WechatData.A_APP_ID,WechatData.A_APP_SECRET);
			if(openid.indexOf("errcode")<0)
			{
				user = theUserService.getByAttribute("wechatOpenId", openid,"wechatId", WechatData.A_APP_ID);
				if(null==user)
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

				}
				ActivityRecord activityRecord = activityRecordService.getByUserId(user.getUserId());
				if(null==activityRecord)
				{
					activityRecord = new ActivityRecord();
					activityRecord.setAddTime(new Date());
					activityRecord.setTimes(0);
					activityRecord.setUpdateTime(new Date());
					activityRecord.setUser(user);
					
					activityRecordService.saveActivityRecord(activityRecord);
				}
				request.setAttribute("status",1);
				request.setAttribute("userId",user.getUserId());
			}
		}
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		String appSecret = WechatData.A_APP_SECRET;
		
		if (null != queryString && !"".equals(queryString))
		{
			url = url + "?" + queryString;
		}
	
		WechatCache wechatCache = wechatCacheService.getByAppId(WechatData.A_APP_ID,"JsApi");
		WechatJSSign wechatJSSign = new WechatJSSign();
		String jsapi_ticket = wechatJSSign.checkWechatCache(WechatData.A_APP_ID,appSecret, wechatCache) ;
		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,url,WechatData.A_APP_ID, appSecret);

		request.setAttribute("url",getOauthUrl());
		request.setAttribute("appId",WechatData.A_APP_ID);
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());
		
		return "card";
	}

	/**
	 * 抽奖
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/card/activity" )
	public String wechatIndex() throws IOException
	{
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");
		request.setAttribute("userId",0);
		User user = null;
		
		String code = paramString("code");
		request.setAttribute("status",0);
		if(StringUtil.isNotBlank(code))
		{
			String openid = WechatUtil.getOpenid(code,WechatData.A_APP_ID,WechatData.A_APP_SECRET);
			if(openid.indexOf("errcode")<0)
			{
				user = theUserService.getByAttribute("wechatOpenId", openid,"wechatId", WechatData.A_APP_ID);
				if(null==user)
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

				}
				ActivityRecord activityRecord = activityRecordService.getByUserId(user.getUserId());
				if(null==activityRecord)
				{
					activityRecord = new ActivityRecord();
					activityRecord.setAddTime(new Date());
					activityRecord.setTimes(0);
					activityRecord.setUpdateTime(new Date());
					activityRecord.setUser(user);
					
					activityRecordService.saveActivityRecord(activityRecord);
				}
				request.setAttribute("status",1);
				request.setAttribute("userId",user.getUserId());
			}
		}
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		String appSecret = WechatData.A_APP_SECRET;
		
		if (null != queryString && !"".equals(queryString))
		{
			url = url + "?" + queryString;
		}
	
		WechatCache wechatCache = wechatCacheService.getByAppId(WechatData.A_APP_ID,"JsApi");
		WechatJSSign wechatJSSign = new WechatJSSign();
		String jsapi_ticket = wechatJSSign.checkWechatCache(WechatData.A_APP_ID,appSecret, wechatCache) ;
		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,url,WechatData.A_APP_ID, appSecret);

		request.setAttribute("url",getOauthUrl());
		request.setAttribute("url2",getOauthUrl2());
		request.setAttribute("appId",WechatData.A_APP_ID);
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());
		
		return "activity";
	}
	
	/**
	 * 抽奖记录
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/card/activityRecord")
	public void getActivityRecord() throws IOException
	{
		Long userId = paramLong("userId");
		ActivityRecord activityRecord = activityRecordService.getByUserId(userId);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,activityRecord);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	/**
	 * 抽奖奖励
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/card/activityResult")
	public void getActivityResult() throws IOException
	{
		Long userId = paramLong("userId");
		ActivityRecord activityRecord = activityRecordService.getByUserId(userId);
		
		activityRecord.setUpdateTime(new Date());
		activityRecord.setTimes(activityRecord.getTimes()+1);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		int index = (int)(Math.random()*100); 
		System.out.println(index+"--index");
		if(-1<index)
		{
			List<CardFactory> list = cardFactoryService.getUnusedCard();
			
			if(null!=list&&!list.isEmpty())
			{
				int number = new Random().nextInt(list.size()-1);
				
				activityRecord.setCardFactory(list.get(number));
				
				CardFactory cardFactory = cardFactoryService.getCardFactory(list.get(number).getId());
				cardFactory.setStatus(1);
				cardFactoryService.update(cardFactory);
				
				data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
				data.put(ConstantUtil.ERRORMSG,list.get(number).getCardDesc());
				data.put(ConstantUtil.CARD_NO,list.get(number).getCardNo());
				data.put(ConstantUtil.CARD_PASSWORD,list.get(number).getCardPassword());
				data.put(ConstantUtil.MSG,activityRecord.getId());
			}
			else
			{
				data.put(ConstantUtil.ERRORMSG,"很遗憾您没有中奖!");
			}
		}
		else
		{
			data.put(ConstantUtil.ERRORMSG,"很遗憾您没有中奖!");
		}
		activityRecordService.updateActivityRecord(activityRecord);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 发送注册手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/sendCardCode")
	public void sendActivityCode() throws IOException
	{
		data = new HashMap<String, Object>();
		Long activityId = paramLong("activityId");
		String tel = paramString("tel");
		String cardNo = paramString("cardNo");
		String cardPassword = paramString("cardPassword");
		String cardDesc = paramString("cardDesc");
		
		ActivityRecord activityRecord = activityRecordService.getById(activityId);
		activityRecord.setMobilePhone(tel);
		activityRecordService.updateActivityRecord(activityRecord);
		
		Global.setTransfer("cardNo",cardNo);
		Global.setTransfer("cardPassword",cardPassword);
		Global.setTransfer("cardDesc",cardDesc);
		NoticeType smsType = Global.getNoticeType(NoticeConstant.CARD_CODE, NoticeConstant.NOTICE_SMS);
		if(smsType.getSend() == 1 && StringUtil.isNotBlank(tel)) {
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("addTime", new Date());
			sendData.put("cardNo",cardNo);
			sendData.put("cardPassword",cardPassword);
			sendData.put("cardDesc",cardDesc);
			Notice sms = new Notice();
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(tel);
			sms.setNid(NoticeConstant.CASH_SUPERVISION_NOTICE);
			sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
			noticeService.sendNotice(sms);
		}
		
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 再次发送短信
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/reSendCode")
	public void sendActicityPasswordCode() throws IOException
	{
		data = new HashMap<String, Object>();
		Long activityId = paramLong("activityId");
		String tel = paramString("mobilePhone");
		
		ActivityRecord activityRecord = activityRecordService.getById(activityId);
		activityRecord.setMobilePhone(tel);
		activityRecordService.updateActivityRecord(activityRecord);
		
		Global.setTransfer("cardNo",activityRecord.getCardFactory().getCardNo());
		Global.setTransfer("cardPassword",activityRecord.getCardFactory().getCardPassword());
		Global.setTransfer("cardDesc",activityRecord.getCardFactory().getCardDesc());
		NoticeType smsType = Global.getNoticeType(NoticeConstant.CARD_CODE, NoticeConstant.NOTICE_SMS);
		if(smsType.getSend() == 1 && StringUtil.isNotBlank(tel)) {
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("addTime",new Date());
			sendData.put("cardNo",activityRecord.getCardFactory().getCardNo());
			sendData.put("cardPassword",activityRecord.getCardFactory().getCardPassword());
			sendData.put("cardDesc",activityRecord.getCardFactory().getCardDesc());
			Notice sms = new Notice();
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(tel);
			sms.setNid(NoticeConstant.CASH_SUPERVISION_NOTICE);
			sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
			noticeService.sendNotice(sms);
		}
		
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	public String getOauthUrl()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(WechatData.OAUTH_URL_ONE);
		buffer.append(WechatData.OAUTH_URL_TWO);
		buffer.append("/nb/wechat/card/activity.action");
		buffer.append(WechatData.OAUTH_URL_FOUR);
		
		return buffer.toString();
	}
	
	public String getOauthUrl2()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(WechatData.OAUTH_URL_ONE);
		buffer.append(WechatData.OAUTH_URL_TWO);
		buffer.append("/nb/wechat/card/card.action");
		buffer.append(WechatData.OAUTH_URL_FOUR);
		
		return buffer.toString();
	}
	
	
	
	
	
	public Map<String, Object> getData()
	{
		return data;
	}

	public void setData(Map<String, Object> data)
	{
		this.data = data;
	}
}
