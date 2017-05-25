package com.rongdu.p2psys.core.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.user.domain.User;

/**
 * 管理员--用户关联表
 * @author yinliang
 * @version 2.0
 * @Date   2014年12月28日
 */
@Entity
@Table(name = "rd_operator_user")
public class OperatorUser {
	/**
	 * 序列号
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	/**
	 * 主键标示
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 管理员
	 */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_id")
	private Operator operator;
	
	/**
	 * 用户
	 */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	
}
