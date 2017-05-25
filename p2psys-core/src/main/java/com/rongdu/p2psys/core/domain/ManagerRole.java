package com.rongdu.p2psys.core.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 管理员角色关联表
 * 
 * @author lhm
 * @version 1.0
 * @since 2014-03-17
 */
@Entity
@Table(name = "s_manager_role")
public class ManagerRole implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 角色主键
	 */

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Role role;
	/**
	 * 用户主键
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private Operator manager;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Operator getManager() {
		return manager;
	}

	public void setManager(Operator manager) {
		this.manager = manager;
	}

}
