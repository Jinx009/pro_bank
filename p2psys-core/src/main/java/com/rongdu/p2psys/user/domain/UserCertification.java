package com.rongdu.p2psys.user.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 上传信息表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_user_certification")
public class UserCertification implements Serializable {
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
	 * 用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 上传类型
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", referencedColumnName = "typeId")
	private CertificationType certificationType;
	/**
	 * 认证图片
	 */
	private String picPath;
	/**
	 * 积分
	 */
	private int credit;

	public UserCertification() {
		super();
	}

	public UserCertification(User user, String picPath, CertificationType certificationType) {
		super();
		this.user = user;
		this.certificationType = certificationType;
		this.picPath = picPath;
	}

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
	 * 获取用户ID
	 * 
	 * @return 用户ID
	 */

	public User getUser() {
		return user;
	}

	/**
	 * 设置用户ID
	 * 
	 * @param user 要设置的用户ID
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取上传类型
	 * 
	 * @return 上传类型
	 */
	public CertificationType getCertificationType() {
		return certificationType;
	}

	/**
	 * 设置上传类型
	 * 
	 * @param typeId 要设置的上传类型
	 */
	public void setCertificationType(CertificationType certificationType) {
		this.certificationType = certificationType;
	}

	/**
	 * 获取认证图片
	 * @return 认证图片路径
	 */
	public String getPicPath() {
		return picPath;
	}
	/**
	 * 设置认证图片路径
	 * @param picPath  认证图片路径
	 */
	public void setPicPath(String picPath) {
		this.picPath = picPath;
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
