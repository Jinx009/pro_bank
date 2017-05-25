package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统操作日志实体
 * 
 * @author wujing
 * @version 1.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "s_log")
public class Log implements Serializable {

	/** 日志类型（1：接入日志） */
	public static final String TYPE_ACCESS = "1";
	/** 日志类型（2：错误日志） */
	public static final String TYPE_EXCEPTION = "2";
	/** 日志类型（3：操作日志） */
	public static final String TYPE_OPERATOR = "3";

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 日志类型
	 */
	private String type;
	/**
	 * 创建者
	 */
	private String addUser;
	/**
	 * 创建时间
	 */
	private Date addTime;
	/**
	 * 操作IP地址
	 */
	private String remoteAddr;
	/**
	 * 请求URI
	 */
	private String requestUri;
	/**
	 * 操作方式
	 */
	private String method;
	/**
	 * 操作提交的数据
	 */
	private String params;
	/**
	 * 异常信息
	 */
	private String exception;

	/**
	 * 获取编号
	 * 
	 * @return 编号
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置编号
	 * 
	 * @param id 要设置的编号
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取日志类型
	 * 
	 * @return 日志类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置日志类型
	 * 
	 * @param type 要设置的日志类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取创建者
	 * 
	 * @return 创建者
	 */
	public String getAddUser() {
		return addUser;
	}

	/**
	 * 设置创建者
	 * 
	 * @param addUser 要设置的创建者
	 */
	public void setAddUser(String addUser) {
		this.addUser = addUser;
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
	 * @param addTime 要设置的创建时间
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取操作IP地址
	 * 
	 * @return 操作IP地址
	 */
	public String getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * 设置操作IP地址
	 * 
	 * @param remoteAddr 要设置的操作IP地址
	 */
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * 获取请求URI
	 * 
	 * @return 请求URI
	 */
	public String getRequestUri() {
		return requestUri;
	}

	/**
	 * 设置请求URI
	 * 
	 * @param requestUri 要设置的请求URI
	 */
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	/**
	 * 获取操作方式
	 * 
	 * @return 操作方式
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 设置操作方式
	 * 
	 * @param method 要设置的操作方式
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 获取操作提交的数据
	 * 
	 * @return 操作提交的数据
	 */
	public String getParams() {
		return params;
	}

	/**
	 * 设置操作提交的数据
	 * 
	 * @param params 要设置的操作提交的数据
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * 获取异常信息
	 * 
	 * @return 异常信息
	 */
	public String getException() {
		return exception;
	}

	/**
	 * 设置异常信息
	 * 
	 * @param exception 要设置的异常信息
	 */
	public void setException(String exception) {
		this.exception = exception;
	}
}
