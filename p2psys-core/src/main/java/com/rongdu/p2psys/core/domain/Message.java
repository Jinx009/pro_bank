package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.model.MessageModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 发送邮件接收邮件信息表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_message")
public class Message implements Serializable {
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
	 * 发送用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sent_user")
	private User sentUser;
	/**
	 * 接收用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receive_user")
	private User receiveUser;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 状态 0未读，1已读
	 */
	private int status;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 1保存到发件箱 0不保存到发件箱
	 */
	private int sented;
	/**
	 * 删除类型 1 收件人删除邮件 0 收件人未删除邮件
	 */
	private int delType;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;

	public Message() {
		super();
	}

	public Message(User sentUser, User receiveUser) {
		super();
		this.sentUser = sentUser;
		this.receiveUser = receiveUser;
		this.status = 0;
		this.type = Constant.SYSTEM;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}

	public Message(MessageModel msg, String content) {
		super();
		this.sentUser = msg.getSentUser();
		this.receiveUser = msg.getReceiveUser();
		this.status = 0;
		this.type = Constant.SYSTEM;
		this.addTime = new Date();
		this.addIp = Global.getIP();
		this.title = "[回复]:" + msg.getTitle();
		this.content = msg.getContent() + "</br>------------------ 原始信息 ------------------</br>" + content;
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

	public User getSentUser() {
		return sentUser;
	}

	public void setSentUser(User sentUser) {
		this.sentUser = sentUser;
	}

	public User getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}

	/**
	 * 获取标题
	 * 
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * 
	 * @param title 要设置的标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取状态 0未读，1已读
	 * 
	 * @return 状态 0未读，1已读
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态 0未读，1已读
	 * 
	 * @param status 要设置的状态 0未读，1已读
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type 要设置的类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取1保存到发件箱 0不保存到发件箱
	 * 
	 * @return 1保存到发件箱 0不保存到发件箱
	 */
	public int getSented() {
		return sented;
	}

	/**
	 * 设置1保存到发件箱 0不保存到发件箱
	 * 
	 * @param sented 要设置的1保存到发件箱 0不保存到发件箱
	 */
	public void setSented(int sented) {
		this.sented = sented;
	}

	/**
	 * 获取删除类型 1 收件人删除邮件 0 收件人未删除邮件
	 * 
	 * @return 删除类型 1 收件人删除邮件 0 收件人未删除邮件
	 */
	public int getDelType() {
		return delType;
	}

	/**
	 * 设置删除类型 1 收件人删除邮件 0 收件人未删除邮件
	 * 
	 * @param delType 要设置的删除类型 1 收件人删除邮件 0 收件人未删除邮件
	 */
	public void setDelType(int delType) {
		this.delType = delType;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content 要设置的内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取添加IP
	 * 
	 * @return 添加IP
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加IP
	 * 
	 * @param addIp 要设置的添加IP
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
}
