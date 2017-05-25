package com.rongdu.p2psys.nb.recommend.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;

public class RecommendRecordModel extends RecommendRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3516265888456099409L;
	
	//推荐人
	private String inviteUserName;
	
	//被推荐人
	private String userName;
	
	
	
	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;
	
	
	public static RecommendRecordModel instance(RecommendRecord record)
	{
		RecommendRecordModel model = new RecommendRecordModel();
		BeanUtils.copyProperties(record, model);
		return model;
	}
	
	

	public String getInviteUserName() {
		return inviteUserName;
	}

	public void setInviteUserName(String inviteUserName) {
		this.inviteUserName = inviteUserName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
