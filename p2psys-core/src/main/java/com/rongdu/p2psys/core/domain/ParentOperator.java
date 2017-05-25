package com.rongdu.p2psys.core.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 父级管理员--子级管理员关联中间表
 * @author yinliang
 * @version 2.0
 * @Date   2014年12月28日
 */
@Entity
@Table(name = "s_parent_operator")
public class ParentOperator {
	/**
	 * 序列号
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	/**
	 * 主键标示
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 父级管理员
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "p_operator_id")
	private Operator p_operator;
	
	/**
	 * 子级管理员
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id")
	private Operator c_operator;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Operator getP_operator() {
		return p_operator;
	}

	public void setP_operator(Operator p_operator) {
		this.p_operator = p_operator;
	}

	public Operator getC_operator() {
		return c_operator;
	}

	public void setC_operator(Operator c_operator) {
		this.c_operator = c_operator;
	}
	
}
