package com.rongdu.p2psys.ppfund.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PPfund资料图片上传
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月22日
 */
@Entity
@Table(name = "rd_ppfund_upload")
public class PpfundUpload implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * PPFUND资料图
	 */
	public static final int PPFUND_DATA = 0;
	/**
	 * PPFUND产品图标
	 */
	public static final int PPFUND_ICON = 1;

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 对应产品
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ppfund_id")
	private Ppfund ppfund;

	/**
	 * 图片类型
	 */
	private int type;

	/**
	 * 图片路径
	 */
	private String picPath;

	public PpfundUpload() {
		super();
	}

	public PpfundUpload(int type, String picPath) {
		super();
		this.type = type;
		this.picPath = picPath;
	}

	public PpfundUpload(Ppfund ppfund, int type, String picPath) {
		super();
		this.ppfund = ppfund;
		this.type = type;
		this.picPath = picPath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Ppfund getPpfund() {
		return ppfund;
	}

	public void setPpfund(Ppfund ppfund) {
		this.ppfund = ppfund;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
}
