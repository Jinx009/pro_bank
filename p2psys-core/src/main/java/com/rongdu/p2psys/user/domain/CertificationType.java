package com.rongdu.p2psys.user.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 上传类型表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "s_certification_type")
public class CertificationType implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 上传类型
	 */
	private int typeId;

	/**
	 * 说明信息
	 */
	private String name;
	/**
	 * 排序
	 */
	private int sort;
	/**
	 * 状态
	 */
	private int status;
	/**
	 * 积分
	 */
	private int credit;

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取上传类型
	 * 
	 * @return 上传类型
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * 设置上传类型
	 * 
	 * @param typeId 要设置的上传类型
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * 获取说明信息
	 * 
	 * @return 说明信息
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置说明信息
	 * 
	 * @param name 要设置的说明信息
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * 设置排序
	 * 
	 * @param sort 要设置的排序
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status 要设置的状态
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取积分
	 * 
	 * @return 积分
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * 设置积分
	 * 
	 * @param credit 要设置的积分
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}
}
