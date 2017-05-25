package com.rongdu.p2psys.cooperation.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;



/**
 * 联合登陆实体
 * 
 * @author sj
 * @since 2014年5月28日15:12:03
 */

@Entity
@Table(name = (Global.DB_PREFIX + "cooperation_login"))
public class CooperationLogin implements Serializable {
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/** 合作登陆类型: QQ */
	public static final String TYPE_QQ = "1";
	/** 合作登陆类型: 新浪微博 */
	public static final String TYPE_SINA = "2";

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/** 合作登陆类型 */
	private int type;

	/** 用户 ID */
	private long userId;

	/** 外部ID */
	private String openId;

	/** 外部Key */
	private String openKey;

	/** 创建时间 */
	private Date addTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOpenKey() {
		return openKey;
	}

	public void setOpenKey(String openKey) {
		this.openKey = openKey;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
