package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 管理员表
 */
@Entity
@Table(name = "s_operator")
public class Operator implements Serializable {

	private static final long serialVersionUID = 8504233433934034554L;

	public Operator() {
		super();
	}

	public Operator(Long id) {
		super();
		this.id = id;
	}

	/**
	 * 主键标示
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 用户登录名
	 */
	private String userName;

	/**
	 * 登录密码
	 */
	private String pwd;

	/**
	 * 工号
	 */
	private String no;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 手机
	 */
	private String mobile;

	/**
	 * 状态
	 * 
	 * <p>
	 * 0:正常
	 * </p>
	 */
	private Integer status;

	/**
	 * 是否删除
	 * 
	 * <p>
	 * 0:不删除
	 * </p>
	 * <p>
	 * 1:删除
	 * </p>
	 */
	private Boolean isDelete;

	/**
	 * 最后登录IP
	 */
	private String loginIp;

	/**
	 * 最后登录时间
	 */
	private Date loginTime;

	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * 添加者
	 */
	private String addManager;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 修改者
	 */
	private String updateManager;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 头像保存路径
	 */
	private String path;

	/**
	 * QQ号
	 */
	private String qq;

	/**
	 * 所属部门
	 */
	private String department;

	/**
	 * 动态口令卡的序列号
	 */
	private String serialId;

	/**
	 * 管理员角色
	 */
	@OneToMany(mappedBy = "operator", fetch = FetchType.LAZY)
	private List<OperatorRole> operatorRole;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddManager() {
		return addManager;
	}

	public void setAddManager(String addManager) {
		this.addManager = addManager;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateManager() {
		return updateManager;
	}

	public void setUpdateManager(String updateManager) {
		this.updateManager = updateManager;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public List<OperatorRole> getOperatorRole() {
		return operatorRole;
	}

	public void setOperatorRole(List<OperatorRole> operatorRole) {
		this.operatorRole = operatorRole;
	}

}
