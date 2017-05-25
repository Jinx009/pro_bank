package com.rongdu.p2psys.core.domain;

import org.springframework.beans.BeanUtils;

/**
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
public class NoticeModel extends Notice {

	private static final long serialVersionUID = 3936119664119258661L;
	private String sentName;
	private String receiveName;
	
	private String searchName;

	public String getSentName() {
		return sentName;
	}

	public void setSentName(String sentName) {
		this.sentName = sentName;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public static NoticeModel instance(Notice notice) {
		NoticeModel noticeModel = new NoticeModel();
		BeanUtils.copyProperties(notice, noticeModel);
		return noticeModel;
	}
}
