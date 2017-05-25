package com.rongdu.p2psys.user.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.user.domain.UserRedPacket;

/**
 * 用户红包Model
 * 
 * @author zf
 * @version 2.0
 * @since 2014年10月24日
 */
public class UserRedPacketModel extends UserRedPacket
{

	/** 当前页码 */
	private int page = 1;

	/** 每页数据条数 */
	private int rows = Page.ROWS;
	/**
	 * 剩余天数
	 */
	private int lastDays;
	/**
	 * 状态：1已使用，2未使用，-1过期
	 */
	private int status;
	/**
	 * 红包类型名称
	 */
	private String name;
	/**
	 * 红包类型ID
	 */
	private long typeId;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 用户真实姓名
	 */
	private String realName;
	/**
	 * 用户红包总金额
	 */
	private double totalAmount;
	/**
	 * 领取开始时间
	 */
	private String receiveStartTime;
	/**
	 * 领取结束时间
	 */
	private String receiveEndTime;
	/**
	 * 过期开始时间
	 */
	private String expiredStartTime;
	/**
	 * 过期结束时间
	 */
	private String expiredEndTime;

	/**
	 * 条件查询
	 */
	private String searchName;

	/**
	 * 状态
	 */
	private String statusStr;
	/**
	 * 红包名称
	 */
	private String serviceName;
	/**
	 * 红包英文名称
	 */
	private String serviceType;
	/**
	 * 红包类型
	 */
	private String redMyPacketType;
	/**
	 * 投标名称
	 */
	private String tenderName;
	
	public static UserRedPacketModel instance(UserRedPacket urg)
	{
		UserRedPacketModel userModel = new UserRedPacketModel();
		BeanUtils.copyProperties(urg, userModel);
		return userModel;
	}

	public UserRedPacket prototype()
	{
		UserRedPacket urg = new UserRedPacket();
		BeanUtils.copyProperties(this, urg);
		return urg;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getTypeId()
	{
		return typeId;
	}

	public void setTypeId(long typeId)
	{
		this.typeId = typeId;
	}

	public int getLastDays()
	{
		return lastDays;
	}

	public void setLastDays(int lastDays)
	{
		this.lastDays = lastDays;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public double getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public String getReceiveStartTime()
	{
		return receiveStartTime;
	}

	public void setReceiveStartTime(String receiveStartTime)
	{
		this.receiveStartTime = receiveStartTime;
	}

	public String getReceiveEndTime()
	{
		return receiveEndTime;
	}

	public void setReceiveEndTime(String receiveEndTime)
	{
		this.receiveEndTime = receiveEndTime;
	}

	public String getExpiredStartTime()
	{
		return expiredStartTime;
	}

	public void setExpiredStartTime(String expiredStartTime)
	{
		this.expiredStartTime = expiredStartTime;
	}

	public String getExpiredEndTime()
	{
		return expiredEndTime;
	}

	public void setExpiredEndTime(String expiredEndTime)
	{
		this.expiredEndTime = expiredEndTime;
	}

	public String getSearchName()
	{
		return searchName;
	}

	public void setSearchName(String searchName)
	{
		this.searchName = searchName;
	}

	public String getStatusStr()
	{
		if (this.getStatus() == 1)
		{
			this.statusStr = "已兑换";
		} else if (this.getStatus() == -1)
		{
			this.statusStr = "已过期";
		} else
		{
			this.statusStr = "未兑换";
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr)
	{
		this.statusStr = statusStr;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public String getServiceType()
	{
		return serviceType;
	}

	public void setServiceType(String serviceType)
	{
		this.serviceType = serviceType;
	}

	public String getRedMyPacketType()
	{
		return redMyPacketType;
	}

	public void setRedMyPacketType(String redMyPacketType)
	{
		this.redMyPacketType = redMyPacketType;
	}

	public String getTenderName()
	{
		return tenderName;
	}

	public void setTenderName(String tenderName)
	{
		this.tenderName = tenderName;
	}
	
}
