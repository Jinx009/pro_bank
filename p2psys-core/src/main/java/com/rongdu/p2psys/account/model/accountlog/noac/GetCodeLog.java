package com.rongdu.p2psys.account.model.accountlog.noac;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.RandomUtil;
import com.rongdu.p2psys.account.model.accountlog.BaseSimpleNoticeLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

/**
 * 获取校验码
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月19日
 */
public class GetCodeLog extends BaseSimpleNoticeLog {
	private static final long serialVersionUID = 1L;

	private String noticeTypeNid = null;
	private String userName;
	private String email;
	private String mobilePhone;

	public GetCodeLog() {
		super();
	}

	public GetCodeLog(double money, User user, User toUser) {
		super(money, user, toUser);
	}

	public GetCodeLog(double money, User user) {
		super(money, user);
	}

	public GetCodeLog(User user, String userName, String noticeTypeNid) {
		super();
		this.setUser(user);
		this.userName = userName;
		this.noticeTypeNid = noticeTypeNid;
	}

	public GetCodeLog(User user, String userName, String email, String mobilePhone, String noticeTypeNid) {
		super();
		this.setUser(user);
		this.userName = userName;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.noticeTypeNid = noticeTypeNid;
	}

	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	}

	@Override
	public void initCode(String todo) {
		String code = RandomUtil.code();
		Global.setTransfer("code", code);
		System.out.println("验证码：" + code);
		Global.setTransfer("vtime", Global.getString("verify_code_time"));
		Global.setTransfer("mobilePhone", mobilePhone);
		Global.setTransfer("email", email);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", this.userName);
		map.put("todo", todo);
		map.put("code", code);
		Date date=new Date();
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(date); 
		calendar.add(calendar.SECOND,190);
		date=calendar.getTime(); 
		map.put("sendTime", date);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.getSession().setAttribute(todo + "_code", map);
	}

	@Override
	public void sendNotice() {
		// 能取到noticeTypeNid就可以发送
		if (!"".equals(getNoticeTypeNid())) {
			Map<String, Object> transferMap = transfer();
			
			if(this.getUser() != null){
				long userid = this.getUser().getUserId();
				User user = userDao.findObjByProperty("userId", userid);
				transferMap.put("user", user);
			}

			transferMap.put("host", Global.getValue("weburl"));
			transferMap.put("webname", Global.getValue("webname"));
			transferMap.put("noticeTime", DateUtil.dateStr(new Date(), "MM月dd日 HH时mm分ss秒"));
			noticeService.sendNotice(getNoticeTypeNid(), transferMap);
		}
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setNoticeTypeNid(String noticeTypeNid) {
		this.noticeTypeNid = noticeTypeNid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

}
