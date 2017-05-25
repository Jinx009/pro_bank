package com.rongdu.p2psys.nb.recommend.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfitRecord;

public class RecommendProfitRecordModel extends RecommendProfitRecord
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7589983845830217542L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	/**
	 * 推荐人姓名
	 */
	private String inviteUserName;

	/**
	 * 被推荐人
	 */
	private String userName;

	/**
	 * 项目名称
	 */
	private String projectName;

	private String mobilePhone;

	public static RecommendProfitRecordModel instance(RecommendProfitRecord profit)
	{
		RecommendProfitRecordModel model = new RecommendProfitRecordModel();
		BeanUtils.copyProperties(profit, model);
		return model;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public String getInviteUserName()
	{
		return inviteUserName;
	}

	public void setInviteUserName(String inviteUserName)
	{
		this.inviteUserName = inviteUserName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public String getMobilePhone()
	{
		if (StringUtil.isNotBlank(mobilePhone) && mobilePhone.length() == 11)
		{
			return mobilePhone.substring(0, 4) + "****"
					+ mobilePhone.substring(7, 11);
		}
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}
	
}
