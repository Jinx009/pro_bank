package com.rongdu.p2psys.nb.protocol.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;

/**
 * 协议模板
 * 
 * @author qj
 * @version 2.0
 * @since 2014-05-22
 */
@Entity
@Table(name = (Global.DB_PREFIX + "protocol"))
public class Protocol implements Serializable {
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
	 * 模板对应实现类
	 */
	private String nid;
	/**
	 * 模板内容
	 */
	private String content;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
