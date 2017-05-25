package com.rongdu.p2psys.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.dao.MessageDao;
import com.rongdu.p2psys.core.dao.NoticeDao;
import com.rongdu.p2psys.core.domain.Message;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeModel;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.sms.ChinaSmsPortalImpl;
import com.rongdu.p2psys.core.sms.DaHanSanTongImpl;
import com.rongdu.p2psys.core.sms.ErongduSmsPortalImpl;
import com.rongdu.p2psys.core.sms.KuaiXianSmsPortalImpl;
import com.rongdu.p2psys.core.sms.SmsPortal;
import com.rongdu.p2psys.core.sms.YndSmsPortalImpl;
import com.rongdu.p2psys.core.sms.YwdSmsPortalImpl;
import com.rongdu.p2psys.core.sms.ZtSmsPortalImpl;
import com.rongdu.p2psys.core.util.mail.Mail;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserNoticeConfigDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserNoticeConfig;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

	private Logger logger = Logger.getLogger(NoticeServiceImpl.class);
	@Resource
	private NoticeDao noticeDao;
	@Resource
	private MessageDao messageDao;
	@Resource
	private UserNoticeConfigDao userNoticeConfigDao;
	@Resource
	private UserCacheDao userCacheDao;

	private SmsPortal smsPortal;
	private SmsPortal[] smsPortalBackups;

	private void sendSms(Notice s) {

		String mobile = s.getReceiveAddr();
		String content = StringUtil.isNull(s.getContent());

		logger.debug("contentString=========" + content);
		logger.debug("mobile=========" + mobile);

		// 如果没有为短信配置特殊的send_route则直接通过默认的短信通道发送
		NoticeType smsType = Global.getNoticeType(s.getNid(), NoticeConstant.NOTICE_SMS);
		String sendRoute = smsType.getSendRoute();

		List<SmsPortal> sps = new ArrayList<SmsPortal>();

		smsPortal = new ZtSmsPortalImpl();
		// 根据配置的发送路由生成通道列表
		if (null != sendRoute) {
			try {
				String[] spIdxs = sendRoute.split(":");
				for (String idx : spIdxs) {
					if (StringUtil.isNotBlank(idx)) {
						int spIdx = Integer.parseInt(idx);
						if (0 == spIdx) {
							sps.add(smsPortal);
						} else {
							smsPortalBackups = new SmsPortal[6];
							smsPortalBackups[0] = (SmsPortal) new ChinaSmsPortalImpl();
							smsPortalBackups[0] = (SmsPortal) new ErongduSmsPortalImpl();
							smsPortalBackups[0] = (SmsPortal) new KuaiXianSmsPortalImpl();
							smsPortalBackups[0] = (SmsPortal) new YndSmsPortalImpl();
							smsPortalBackups[0] = (SmsPortal) new YwdSmsPortalImpl();
							smsPortalBackups[0] = (SmsPortal) new DaHanSanTongImpl();
							smsPortalBackups[0] = (SmsPortal) new ZtSmsPortalImpl();
							if (0 < spIdx && smsPortalBackups.length >= spIdx) {
								sps.add(smsPortalBackups[spIdx - 1]);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (0 == sps.size()) {
			// 如果没有配置，就把默认的通道放入列表
			sps.add(smsPortal);
		}

		String result;
		StringBuilder sb = new StringBuilder();
		for (SmsPortal sp : sps) {

			result = sp.send(mobile, content);
			if (result.equalsIgnoreCase("ok")) {
				s.setStatus(1);
				sb.append("通过短信通道" + sp.getSPName() + "发送短信成功。");
				// 如果发送成功，就不再继续发送
				break;
			} else {
				s.setStatus(2);
				sb.append("通过短信通道" + sp.getSPName() + "发送短信失败：" + result + "。");
				// 失败后换下一个通道继续发送
			}
		}
		s.setResult(sb.toString());
		noticeDao.save(s);
	}

	private void sendMessage(Notice s) {
		Message m = new Message();
		m.setTitle(s.getTitle());
		m.setSentUser(s.getSentUser());
		m.setReceiveUser(s.getReceiveUser());
		m.setContent(s.getContent());
		m.setAddTime(new Date());
		m.setAddIp(Global.getIP());
		messageDao.save(m);

		s.setStatus(1);
		s.setResult("ok");
		noticeDao.save(s);
	}

	@Override
	public void sendNotice(String noticeTypeNid, Map<String, Object> sendData) {

		User user = (User) sendData.get("user");
		UserCache userCache = null;
		UserNoticeConfig unConfig = null;
		if(user != null){
			long userId = user.getUserId();
			userCache = userCacheDao.getUserCache(userId);
			unConfig = userNoticeConfigDao.getUNConfig(userId, noticeTypeNid);
		}
		String mobilePhone = (String)sendData.get("mobilePhone");
		String mail = (String)sendData.get("email");

		// 没有强制用户通知配置必须与用户对应，所以当取不到这个配置的时候，初始化一个配置对象，且发送标志为真
		if (unConfig == null) {
			unConfig = new UserNoticeConfig();
			unConfig.setSms(NoticeConstant.NOTICE_RECEIVE);
			unConfig.setEmail(NoticeConstant.NOTICE_RECEIVE);
			unConfig.setMessage(NoticeConstant.NOTICE_RECEIVE);
		}

		// 生成短信
		if (unConfig.getSms() == NoticeConstant.NOTICE_RECEIVE) {
			NoticeType smsType = Global.getNoticeType(noticeTypeNid, NoticeConstant.NOTICE_SMS);
			if (smsType.getSend() == NoticeConstant.NOTICE_SEND) {
				if (isSmssend(smsType, userCache, user)) {
					Notice sms = new Notice();

					sms.setNid(noticeTypeNid);
					sms.setType(String.valueOf(NoticeConstant.NOTICE_SMS));
					sms.setSentUser(new User(Constant.ADMIN_ID));
					sms.setReceiveUser(user);
					sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
					sms.setAddTime(new Date());
					if (mobilePhone == null || "".equals(mobilePhone)) {
						mobilePhone = user.getMobilePhone();
					}
					sms.setReceiveAddr(mobilePhone);
					sms.setTitle(StringUtil.fillTemplet(smsType.getTitleTemplet(), sendData));
					if (!"".equals(sms.getReceiveAddr()) && !"".equals(sms.getContent())) {
						JobQueue.getNoticeInstance().offer(sms);
					}
				}
			}
		}

		// 生成邮件
		if (unConfig.getEmail() == NoticeConstant.NOTICE_RECEIVE) {
			NoticeType emailType = Global.getNoticeType(noticeTypeNid, NoticeConstant.NOTICE_EMAIL);
			if (emailType.getSend() == NoticeConstant.NOTICE_SEND) {
				Notice email = new Notice();

				email.setNid(noticeTypeNid);
				email.setType(String.valueOf(NoticeConstant.NOTICE_EMAIL));
				email.setSentUser(new User(Constant.ADMIN_ID));
				email.setReceiveUser(user);
				email.setTitle(StringUtil.fillTemplet(emailType.getTitleTemplet(), sendData));
				email.setContent(StringUtil.fillTemplet(emailType.getTemplet(), sendData));
				email.setAddTime(new Date());
				if (mail == null || "".equals(mail)) {
					mail = user.getEmail();
				}
				email.setReceiveAddr(mail);

				if (!"".equals(email.getReceiveAddr()) && !"".equals(email.getTitle())
						&& !"".equals(email.getContent())) {
					JobQueue.getNoticeInstance().offer(email);
				}
			}
		}

		// 生成站内信
		if (unConfig.getMessage() == NoticeConstant.NOTICE_RECEIVE) {
			NoticeType messageType = Global.getNoticeType(noticeTypeNid, NoticeConstant.NOTICE_MESSAGE);
			if (messageType.getSend() == NoticeConstant.NOTICE_SEND) {
				Notice message = new Notice();

				message.setNid(noticeTypeNid);
				message.setType(String.valueOf(NoticeConstant.NOTICE_MESSAGE));
				message.setSentUser(new User(Constant.ADMIN_ID));
				message.setReceiveUser(user);
				message.setTitle(StringUtil.fillTemplet(messageType.getTitleTemplet(), sendData));
				message.setContent(StringUtil.fillTemplet(messageType.getTemplet(), sendData));
				message.setAddTime(new Date());
				message.setReceiveAddr(user.getMobilePhone());

				if (!"".equals(message.getReceiveAddr()) && !"".equals(message.getTitle())
						&& !"".equals(message.getContent())) {
					JobQueue.getNoticeInstance().offer(message);
				}
			}
		}
		
		sendData.remove("mobilePhone");
		sendData.remove("email");
		sendData.remove("user");
	}

	@Override
	public void sendNotice(Notice notice) {
		switch (Byte.parseByte(notice.getType())) {
			case NoticeConstant.NOTICE_SMS:
				this.sendSms(notice);
				break;
			case NoticeConstant.NOTICE_EMAIL:
				this.sendEmail(notice);
				break;
			case NoticeConstant.NOTICE_MESSAGE:
				this.sendMessage(notice);
				break;
		}
	}

	private void sendEmail(Notice notice) {
		Mail mail = Mail.getInstance();
		mail.setTo(notice.getReceiveAddr());
		mail.setSubject(notice.getTitle());
		mail.setBody(notice.getContent());
		try {
			if(StringUtil.isNotBlank(mail.getTo())) {
				mail.sendMail();
				notice.setStatus(1);
				notice.setResult("ok");
				logger.info("发送邮件成功！！");
			}
		} catch (Exception e) {
			notice.setStatus(2);
			notice.setResult("fail");
			logger.error("发送邮件失败", e);
		}

		noticeDao.save(notice);
	}

	// 在系统发送且用户接收的情况下再进行这个发送判断
	private boolean isSmssend(NoticeType noticeType, UserCache userCache, User user) {
		boolean smssend = false;

		// 手机激活验证码肯定发送
		if (noticeType.getNid().equalsIgnoreCase("phone_code")) {
			smssend = true;
			return smssend;
		}

		// 手机没认证不能发送
		/*
		 * if (1 != user.getPhone_status()){ return smssend; }
		 */

		// 系统短信也发送
		if (noticeType.getType() == Constant.SYSTEM_NOTICE) {
			smssend = true;
		} else {
			// 用户短信需要付费
			/*
			 * long smspayEndtime = userCache.getSmspay_endtime(); if (smspayEndtime > DateUtil.getTime(new Date())) {
			 * smssend = true; }
			 */
		}

		return smssend;
	}

	public SmsPortal getSmsPortal() {
		return smsPortal;
	}

	public void setSmsPortal(SmsPortal smsPortal) {
		this.smsPortal = smsPortal;
	}

	public SmsPortal[] getSmsPortalBackups() {
		return smsPortalBackups;
	}

	public void setSmsPortalBackups(SmsPortal[] smsPortalBackups) {
		this.smsPortalBackups = smsPortalBackups;
	}

	@Override
	public PageDataList<NoticeModel> noticeList(NoticeModel model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if(!StringUtil.isBlank(model.getSearchName())){//模糊查询条件
			SearchFilter orFilter1 = new SearchFilter("title", Operators.LIKE, model.getSearchName());
			SearchFilter orFilter2 = new SearchFilter("sentUser.userName", Operators.LIKE, model.getSearchName());
			SearchFilter orFilter3 = new SearchFilter("receiveUser.userName", Operators.LIKE, model.getSearchName());
			param.addOrFilter(orFilter1, orFilter2, orFilter3);
		}else{ //精确查询条件
			if (!StringUtil.isBlank(model.getTitle())) {
				param.addParam("title", Operators.EQ, model.getTitle());
			}
			if (!StringUtil.isBlank(model.getType())) {
				param.addParam("type", model.getType());
			}
			if (model.getStatus() != 0) {
				param.addParam("status", model.getStatus());
			}
		}	
		param.addOrder(OrderType.DESC, "id");
		PageDataList<Notice> plist = noticeDao.findPageList(param);

		PageDataList<NoticeModel> uList = new PageDataList<NoticeModel>();
		uList.setPage(plist.getPage());
		List<NoticeModel> list = new ArrayList<NoticeModel>();
		if (plist.getList().size() > 0) {
			for (int i = 0; i < plist.getList().size(); i++) {
				Notice notice = (Notice) plist.getList().get(i);
				NoticeModel noticeModel = NoticeModel.instance(notice);
				noticeModel.setSentName(notice.getSentUser().getUserName());
				noticeModel.setReceiveName(notice.getReceiveUser().getUserName());
				list.add(noticeModel);
			}
		}
		uList.setList(list);
		return uList;
	}

	@Override
	public Notice findById(long id) {

		return noticeDao.find(id);
	}

	public Notice findByOrderId(Integer id) {
		return (Notice) noticeDao.findObjByProperty("title",id);
	}

}
