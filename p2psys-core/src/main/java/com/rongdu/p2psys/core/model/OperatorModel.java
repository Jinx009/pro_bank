package com.rongdu.p2psys.core.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.exception.ManagerException;
import com.rongdu.p2psys.core.util.ValidateUtil;

/**
 * 管理员Model
 */
public class OperatorModel extends Operator {

	private static final long serialVersionUID = 7196231459291916358L;

	/**
	 * 原密码
	 **/
	private String oldPassword;

	/**
	 * 确认新修改密码
	 **/
	private String confirmPassword;

	/**
	 * 角色名称
	 **/
	private List<String> roleName;

	/**
	 * 角色ID
	 **/
	private List<Long> roleId;

	/**
	 * 实体转换Model
	 * 
	 * @param manager
	 * @return Model
	 */
	public static OperatorModel instance(Operator manager) {
		OperatorModel userModel = new OperatorModel();
		BeanUtils.copyProperties(manager, userModel);
		return userModel;
	}

	/**
	 * Model转换实体
	 * 
	 * @return 实体
	 */
	public Operator prototype() {
		Operator manager = new Operator();
		BeanUtils.copyProperties(this, manager);
		return manager;
	}

	/**
	 * 验证新增的数据
	 */
	public void validLoginModel() {
		if (StringUtil.isBlank(getUserName())) {
			throw new ManagerException("用户名不能为空！", 1);
		}
		if (StringUtil.isBlank(getPwd())) {
			throw new ManagerException("密码不能为空！", 1);
		}
		if (StringUtil.isBlank(getConfirmPassword())) {
			throw new ManagerException("确认密码不能为空！");
		}
		if (!getPwd().equals(getConfirmPassword())) {
			throw new ManagerException("两次输入的密码不一致！");
		}
		if (!ValidateUtil.isUserName(getUserName())) {
			throw new ManagerException("用户名格式错误！", 1);
		}
	}

	/**
	 * 验证修改的数据
	 * 
	 * @param manager
	 */
	public void validModifyPwdModel(Operator manager) {
		if (getOldPassword() == null) {
			throw new ManagerException("请输入您的原密码！", 1);
		} else if (!MD5.encode(getOldPassword()).equals(manager.getPwd())) {
			throw new ManagerException("原密码错误！", 1);
		} else if (getPwd().equals(getOldPassword())) {
			throw new ManagerException("新密码不能和原密码相同！", 1);
		} else if (!getPwd().equals(getConfirmPassword())) {
			throw new ManagerException("新密码和确认密码不相同！", 1);
		}
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public List<String> getRoleName() {
		return roleName;
	}

	public void setRoleName(List<String> roleName) {
		this.roleName = roleName;
	}

	public List<Long> getRoleId() {
		return roleId;
	}

	public void setRoleId(List<Long> roleId) {
		this.roleId = roleId;
	}

}
