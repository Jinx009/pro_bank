package com.rongdu.p2psys.tpp.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 易极付银行卡地区表
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月31日
 */
@Entity
@Table(name = "tpp_yjf_area_bank")
public class YjfAreaBank implements Serializable {
	private static final long serialVersionUID = 17984496L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	/**
	 * 标示
	 */
	private String nid;
	/**
	 * 父标示
	 */
	private String pid;
	/**
	 * 地区名称
	 */
	private String name;

	public YjfAreaBank() {
		super();
	}

	public YjfAreaBank(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}