package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 规则表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "s_rule")
public class Rule implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 规则名
	 */
	private String name;
	/**
	 * 创建时间
	 */
	private Date addTime;
	/**
	 * 规则类型名
	 */
	private String nid;
	/**
	 * 规则备注
	 */
	private String remark;
	/**
	 * 规则约束JSON
	 */
	private String ruleCheck;

	/**
	 * 获取主键ID
	 * 
	 * @return 主键ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键ID
	 * 
	 * @param id 要设置的主键ID
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取规则名
	 * 
	 * @return 规则名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置规则名
	 * 
	 * @param name 要设置的规则名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * 设置创建时间
	 * 
	 * @param addtime 要设置的创建时间
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取规则类型名
	 * 
	 * @return 规则类型名
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置规则类型名
	 * 
	 * @param nid 要设置的规则类型名
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}

	/**
	 * 获取规则备注
	 * 
	 * @return 规则备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置规则备注
	 * 
	 * @param remark 要设置的规则备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取规则约束JSON
	 * 
	 * @return 规则约束JSON
	 */
	public String getRuleCheck() {
		return ruleCheck;
	}

	/**
	 * 设置规则约束JSON
	 * 
	 * @param ruleCheck 要设置的规则约束JSON
	 */
	public void setRuleCheck(String ruleCheck) {
		this.ruleCheck = ruleCheck;
	}

}
