package com.rongdu.p2psys.core.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 日志模板表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "s_log_template")
public class LogTemplate implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 资金日志
	 */
	public static final byte ACCOUNT_LOG = 1;
	
	/**
	 * 合计日志
	 */
	public static final byte SUM_LOG = 2;
	
	/**
	 * 操作日志
	 */
	public static final byte OPERATION_LOG = 3;

	/**
	 * 站内信
	 */
	public static final byte MESSAGE_LOG = 4;
	
	/**
	 * 积分日志模板
	 */
	public static final byte SCORE_LOG = 5;
	
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 信息类型:1资金日志，2合计日志，3会员日志
	 */
	private int type;
	/**
	 * 日志类型
	 */
	private String logType;
	/**
	 * 模板信息
	 */
	private String value;
	/**
	 * 模板备注
	 */
	private String remark;
	/**
	 * 模板类型
	 */
	private String nid;

	/**
	 * 获取
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置
	 * 
	 * @param id 要设置的
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取信息类型:1资金日志，2合计日志，3会员日志
	 * 
	 * @return 信息类型:1资金日志，2合计日志，3会员日志
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置信息类型:1资金日志，2合计日志，3会员日志
	 * 
	 * @param type 要设置的信息类型:1资金日志，2合计日志，3会员日志
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取日志类型
	 * 
	 * @return 日志类型
	 */
	public String getLogType() {
		return logType;
	}

	/**
	 * 设置日志类型
	 * 
	 * @param logType 要设置的日志类型
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}

	/**
	 * 获取模板信息
	 * 
	 * @return 模板信息
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置模板信息
	 * 
	 * @param value 要设置的模板信息
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取模板备注
	 * 
	 * @return 模板备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置模板备注
	 * 
	 * @param remark 要设置的模板备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取模板类型
	 * 
	 * @return 模板类型
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置模板类型
	 * 
	 * @param nid 要设置的模板类型
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}
}
