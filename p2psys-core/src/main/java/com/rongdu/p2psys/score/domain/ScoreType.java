package com.rongdu.p2psys.score.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rd_score_type")
public class ScoreType implements Serializable {

	private static final long serialVersionUID = -0L;
	
	//主键 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//积分类型名称 
	private String name;
	
	//积分类型代码 
	private String nid;
	
	//状态:0停用,1启用 
	private byte status;
	
	//操作积分数值 
	private int value;
	
	//规则NID 
	private String ruleNid;
	
	//备注 
	private String remark;
	
	//添加时间 
	private Date addTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getRuleNid() {
		return ruleNid;
	}

	public void setRuleNid(String ruleNid) {
		this.ruleNid = ruleNid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
