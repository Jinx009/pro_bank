package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_notice_type")
public class NoticeType implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 编码，与notice_type组合起来唯一
	 */
	private String nid;
	/**
	 * 通知类型:1-sms,2-email,3-message
	 */
	private int noticeType;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 发送类型：1-系统通知，2-用户通知
	 */
	private int type;
	/**
	 * 是否可以切换发送类型，0-不可以切换，1可以切换
	 */
	private int canSwitch;
	/**
	 * 是否发送：0-不发送，1-发送
	 */
	private int send;
	/**
	 * 标题的freemarker模板
	 */
	private String titleTemplet;
	/**
	 * 内容的freemarker模板
	 */
	private String templet;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 
	 */
	private Date addTime;
	/**
	 * 
	 */
	private String addIp;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private String updateIp;
	/**
	 * 短信通道
	 */
	private String sendRoute;

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
	 * 获取编码，与notice_type组合起来唯一
	 * 
	 * @return 编码，与notice_type组合起来唯一
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置编码，与notice_type组合起来唯一
	 * 
	 * @param nid 要设置的编码，与notice_type组合起来唯一
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}

	/**
	 * 获取通知类型:1-sms,2-email,3-message
	 * 
	 * @return 通知类型:1-sms,2-email,3-message
	 */
	public int getNoticeType() {
		return noticeType;
	}

	/**
	 * 设置通知类型:1-sms,2-email,3-message
	 * 
	 * @param noticeType 要设置的通知类型:1-sms,2-email,3-message
	 */
	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
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
	 * 获取发送类型：1-系统通知，2-用户通知
	 * 
	 * @return 发送类型：1-系统通知，2-用户通知
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置发送类型：1-系统通知，2-用户通知
	 * 
	 * @param type 要设置的发送类型：1-系统通知，2-用户通知
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取是否可以切换发送类型，0-不可以切换，1可以切换
	 * 
	 * @return 是否可以切换发送类型，0-不可以切换，1可以切换
	 */
	public int getCanSwitch() {
		return canSwitch;
	}

	/**
	 * 设置是否可以切换发送类型，0-不可以切换，1可以切换
	 * 
	 * @param canswitch 要设置的是否可以切换发送类型，0-不可以切换，1可以切换
	 */
	public void setCanSwitch(int canSwitch) {
		this.canSwitch = canSwitch;
	}

	/**
	 * 获取是否发送：0-不发送，1-发送
	 * 
	 * @return 是否发送：0-不发送，1-发送
	 */
	public int getSend() {
		return send;
	}

	/**
	 * 设置是否发送：0-不发送，1-发送
	 * 
	 * @param send 要设置的是否发送：0-不发送，1-发送
	 */
	public void setSend(int send) {
		this.send = send;
	}

	/**
	 * 获取标题的freemarker模板
	 * 
	 * @return 标题的freemarker模板
	 */
	public String getTitleTemplet() {
		return titleTemplet;
	}

	/**
	 * 设置标题的freemarker模板
	 * 
	 * @param titleTemplet 要设置的标题的freemarker模板
	 */
	public void setTitleTemplet(String titleTemplet) {
		this.titleTemplet = titleTemplet;
	}

	/**
	 * 获取内容的freemarker模板
	 * 
	 * @return 内容的freemarker模板
	 */
	public String getTemplet() {
		return templet;
	}

	/**
	 * 设置内容的freemarker模板
	 * 
	 * @param templet 要设置的内容的freemarker模板
	 */
	public void setTemplet(String templet) {
		this.templet = templet;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark 要设置的备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置
	 * 
	 * @param addip 要设置的
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置
	 * 
	 * @param updatetime 要设置的
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getUpdateIp() {
		return updateIp;
	}

	/**
	 * 设置
	 * 
	 * @param updateip 要设置的
	 */
	public void setUpdateIp(String updateIp) {
		this.updateIp = updateIp;
	}

	/**
	 * 获取短信通道
	 * 
	 * @return 短信通道
	 */
	public String getSendRoute() {
		return sendRoute;
	}

	/**
	 * 设置短信通道
	 * 
	 * @param sendRoute 要设置的短信通道
	 */
	public void setSendRoute(String sendRoute) {
		this.sendRoute = sendRoute;
	}
}
