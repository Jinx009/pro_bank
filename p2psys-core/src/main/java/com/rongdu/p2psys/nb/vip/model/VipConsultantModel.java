package com.rongdu.p2psys.nb.vip.model;



import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.vip.domain.VipConsultant;

public class VipConsultantModel extends VipConsultant
{
	/**
	 * 当前页数
	 */
	private int page;
	/**
	 * 每页页数
	 */
	private int rows = Page.ROWS;
	
	
	public static VipConsultantModel instance(VipConsultant vipConsultant)
	{
		VipConsultantModel model = new VipConsultantModel();
		BeanUtils.copyProperties(vipConsultant, model);
		return model;
	}

	public VipConsultant prototype()
	{
		VipConsultant vipConsultant = new VipConsultant();
		BeanUtils.copyProperties(this, vipConsultant);
		return vipConsultant;
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
	
	/**
	 * 申请开始时间
	 */
	private String startTime;
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * 申请结束时间
	 */
	private String endTime;
	
	
	private String expertName;


	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

}
