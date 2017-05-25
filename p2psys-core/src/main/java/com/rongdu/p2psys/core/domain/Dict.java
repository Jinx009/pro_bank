package com.rongdu.p2psys.core.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 数据字典
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "s_dict")
public class Dict implements Serializable {
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
	 * 状态，0：禁用，1：启用
	 */
	private int status;
	/**
	 * 排序
	 */
	private int sort;
	/**
	 * 标识
	 */
	private String nid;
	
	/**
	 * 标识名
	 */
	private String nidName;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 值
	 */
	private String value;

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
	 * 获取状态，0：禁用，1：启用
	 * 
	 * @return 状态，0：禁用，1：启用
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态，0：禁用，1：启用
	 * 
	 * @param status 要设置的状态，0：禁用，1：启用
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取标识ID
	 * 
	 * @return 标识ID
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置标识ID
	 * 
	 * @param nid 要设置的标识ID
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}
	
	/**
	 * 获取标识名
	 * 
	 * @return 标识名
	 */
	public String getNidName() {
		return nidName;
	}

	/**
	 * 设置标识名
	 * 
	 * @param nidName 要设置的标识名
	 */
	public void setNidName(String nidName) {
		this.nidName = nidName;
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
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name 要设置的名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取值
	 * 
	 * @return 值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value 要设置的值
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
