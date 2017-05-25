package com.rongdu.p2psys.nb.protocol.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 协议模板
 */
@Entity
@Table(name = ("nb_protocol_config"))
public class ProtocolConfig implements Serializable {

	private static final long serialVersionUID = 7220338226464895710L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 产品协议类型
	 */
	private Long protocolType;

	/**
	 * 产品协议名称
	 */
	private String protocolName;

	/**
	 * 协议对应实现类
	 */
	private String nid;

	/**
	 * 协议产品代码
	 */
	private Integer typeCode;

	/**
	 * 状态
	 * 
	 * <p>
	 * 0:启用
	 * </p>
	 * <p>
	 * 1:不启用
	 * </p>
	 */
	private Integer status;

	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * IP
	 */
	private String addIp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(Long protocolType) {
		this.protocolType = protocolType;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

}
