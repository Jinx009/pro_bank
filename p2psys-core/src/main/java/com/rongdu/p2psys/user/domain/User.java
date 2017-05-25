package com.rongdu.p2psys.user.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfitRecord;

/**
 * rd_user 实体类
 * 
 * @author ZhuJunjie
 * @version 2.0
 * @since 2015-06-01
 */
@Entity
@Table(name = (Global.DB_PREFIX + "user"))
public class User
{

	/**
	 * 用户ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private long userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 用户密码
	 */
	private String pwd;

	/**
	 * 支付密码
	 */
	private String payPwd;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 电子邮件
	 */
	private String email;

	/**
	 * 手机号码
	 */
	private String mobilePhone;

	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * 证件号码
	 */
	private String cardId;

	/**
	 * 多账户绑定ID
	 */
	private Long bindId;

	/**
	 * 多账户绑定时间
	 */
	private Date bindTime;

	/**
	 * 微信平台ID
	 */
	private String wechatId;

	/**
	 * 微信open id
	 */
	private String wechatOpenId;

	/**
	 * 微信绑定时间
	 */
	private Date wechatBindTime;

	/**
	 * QQ平台ID
	 */
	private String qqId;

	/**
	 * QQ open id
	 */
	private String qqOpenId;

	/**
	 * QQ绑定时间
	 */
	private Date qqBindTime;

	/**
	 * 微博平台ID
	 */
	private String weiboId;

	/**
	 * 微博 open id
	 */
	private String weiboOpenId;

	/**
	 * 微博绑定时间
	 */
	private Date weiboBindTime;

	/**
	 * 绑定状态
	 */
	private Integer bindState;

	/** 关联 UserCache对象 */
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	private UserCache userCache;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<RecommendProfitRecord> recommendProfitRecordUserList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inviteUser")
	private List<RecommendProfitRecord> recommendProfitRecordInviteList;

	/**
	 * 获取(隐藏一定位数的)真实姓名
	 * 
	 * @return String
	 */

	public String getHideRealname()
	{
		if (StringUtil.isNotBlank(realName))
		{
			return realName.substring(0, 1) + "****";
		}
		return realName;
	}

	/**
	 * 获取证件号码
	 * 
	 * @return 证件号码
	 */
	public String getHideCardId()
	{
		if (StringUtil.isNotBlank(cardId) && cardId.length() == 18)
		{
			return cardId.substring(0, 6) + "********"
					+ cardId.substring(14, 18);
		}
		return cardId;
	}

	/**
	 * 获取(隐藏一定位数的)电子邮件
	 * 
	 * @return 电子邮件
	 */
	public String getHideEmail()
	{
		if (StringUtil.isNotBlank(email))
		{
			return email.substring(0, 2) + "***"
					+ email.substring(email.indexOf("@"), email.length());
		}
		return "";
	}

	/**
	 * 获取(隐藏一定位数的)手机号码
	 * 
	 * @return 手机号码
	 */
	public String getHideMobilePhone()
	{
		if (StringUtil.isNotBlank(mobilePhone) && mobilePhone.length() == 11)
		{
			return mobilePhone.substring(0, 4) + "****"
					+ mobilePhone.substring(7, 11);
		}
		return mobilePhone;
	}
	
	public Integer website;
	
	

	public Integer getWebsite() {
		return website;
	}

	public void setWebsite(Integer website) {
		this.website = website;
	}

	public User()
	{
		super();
	}

	public User(long userId)
	{
		super();
		this.userId = userId;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}

	public String getPayPwd()
	{
		return payPwd;
	}

	public void setPayPwd(String payPwd)
	{
		this.payPwd = payPwd;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public Date getAddTime()
	{
		return addTime;
	}

	public void setAddTime(Date addTime)
	{
		this.addTime = addTime;
	}

	public String getCardId()
	{
		return cardId;
	}

	public void setCardId(String cardId)
	{
		this.cardId = cardId;
	}

	public UserCache getUserCache()
	{
		return userCache;
	}

	public void setUserCache(UserCache userCache)
	{
		this.userCache = userCache;
	}

	public Long getBindId()
	{
		return bindId;
	}

	public void setBindId(Long bindId)
	{
		this.bindId = bindId;
	}

	public Date getBindTime()
	{
		return bindTime;
	}

	public void setBindTime(Date bindTime)
	{
		this.bindTime = bindTime;
	}

	public String getWechatId()
	{
		return wechatId;
	}

	public void setWechatId(String wechatId)
	{
		this.wechatId = wechatId;
	}

	public String getWechatOpenId()
	{
		return wechatOpenId;
	}

	public void setWechatOpenId(String wechatOpenId)
	{
		this.wechatOpenId = wechatOpenId;
	}

	public Date getWechatBindTime()
	{
		return wechatBindTime;
	}

	public void setWechatBindTime(Date wechatBindTime)
	{
		this.wechatBindTime = wechatBindTime;
	}

	public String getQqId()
	{
		return qqId;
	}

	public void setQqId(String qqId)
	{
		this.qqId = qqId;
	}

	public String getQqOpenId()
	{
		return qqOpenId;
	}

	public void setQqOpenId(String qqOpenId)
	{
		this.qqOpenId = qqOpenId;
	}

	public Date getQqBindTime()
	{
		return qqBindTime;
	}

	public void setQqBindTime(Date qqBindTime)
	{
		this.qqBindTime = qqBindTime;
	}

	public String getWeiboId()
	{
		return weiboId;
	}

	public void setWeiboId(String weiboId)
	{
		this.weiboId = weiboId;
	}

	public String getWeiboOpenId()
	{
		return weiboOpenId;
	}

	public void setWeiboOpenId(String weiboOpenId)
	{
		this.weiboOpenId = weiboOpenId;
	}

	public Date getWeiboBindTime()
	{
		return weiboBindTime;
	}

	public void setWeiboBindTime(Date weiboBindTime)
	{
		this.weiboBindTime = weiboBindTime;
	}

	public Integer getBindState()
	{
		return bindState;
	}

	public void setBindState(Integer bindState)
	{
		this.bindState = bindState;
	}

}
