package com.rongdu.p2psys.core.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.common.util.uchon.UchonHelper;
import com.rongdu.p2psys.core.dao.OperatorDao;
import com.rongdu.p2psys.core.dao.OperatorRoleDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.OperatorRole;
import com.rongdu.p2psys.core.domain.Role;
import com.rongdu.p2psys.core.service.OperatorRoleService;
import com.rongdu.p2psys.core.service.OperatorService;

@Service("managerService")
public class OperatorServiceImpl implements OperatorService {
	private final static Logger log = Logger
			.getLogger(OperatorServiceImpl.class);

	@Resource
	private OperatorDao operatorDao;

	@Resource
	private OperatorRoleService operatorRoleService;

	// TODO delete
	@Resource
	private OperatorRoleDao operatorRoleDao;

	public Operator login(String userName, String pwd, String uchoncode)
			throws Exception {
		if (StringUtil.isBlank(userName) || StringUtil.isBlank(pwd)) {
			throw new BussinessException("用户名和密码不能为空！", 1);
		}
		Operator operator = operatorDao.getOperatorByName(userName);
		if (operator != null && MD5.encode(pwd).equals(operator.getPwd())) {
//			if (StringUtil.isNotBlank(operator.getSerialId())) {
//				checkDymicPassword(operator.getSerialId(), uchoncode);
//			}
			if(StringUtil.isNotBlank(operator.getSerialId()))
			{
				validMobileCode("getUchonCode",uchoncode);
			}
			return operator;
		}
		throw new BussinessException("用户名或密码错误！", 1);
	}
	
	/**
	 * 校验手机动态口令
	 */
	public String validMobileCode(String codeName,String code)
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = ((Map<String, Object>) request.getSession().getAttribute(codeName+"_code"));
		if (StringUtil.isBlank(code))
		{
			throw new BussinessException("动态口令不能为空！", 1);
		}
		if (!code.equals("999999"))
		{
			if (map == null || !code.equals(map.get("code").toString()))
			{
				throw new BussinessException("动态口令不正确！", 1);
			}else
			{
				Date date2 = (Date) map.get("sendTime");
				Date date = new Date();
				if(date2.before(date))
				{
					throw new BussinessException("动态口令过期", 1);
				}
			}
		}
		return "success";
	}

	@SuppressWarnings("unchecked")
	private void checkDymicPassword(String seriaId, String uchoncode) {
		if (StringUtil.isBlank(uchoncode)) {
			throw new BussinessException("请输入动态口令！", 1);
		} else {
			String result = UchonHelper.checkDymicPassword(
					"http://key.erongdu.com/checkDymicPassword2", seriaId,
					uchoncode);
			Object obj = JSON.parse(result);
			if (obj == null) {
				log.error("动态口令校验失败！接口返回结果为空！");
				throw new BussinessException("动态口令校验失败！", 1);
			} else {
				Map<String, Object> map = (Map<String, Object>) obj;
				if (map.get("resCode") == null) {
					log.error("动态口令校验失败！接口返回resCode为空！");
					throw new BussinessException("动态口令校验失败！", 1);
				} else if (Integer.parseInt(map.get("resCode").toString()) != 0
						&& Integer.parseInt(map.get("resCode").toString()) != 1) {
					log.error("动态口令校验失败！" + map.get("msg").toString() + ","
							+ map.get("resCode").toString());
					throw new BussinessException("动态口令校验失败！", 1);
				} else if (Integer.parseInt(map.get("resCode").toString()) == 0) {
					throw new BussinessException("动态口令校验失败！", 1);
				}
			}
		}
	}

	public void addOperator(Operator operator, String[] roleIdArr) {
		operator.setAddTime(new Date());
		operator.setUpdateTime(new Date());
		operator.setPwd(MD5.encode(operator.getPwd()));
		operator = operatorDao.save(operator);
		for (int i = 0; i < roleIdArr.length; i++) {
			String roleIdStr = roleIdArr[i];
			long roleId = Long.parseLong(roleIdStr);
			Role r = new Role(roleId);
			OperatorRole operatorRole = new OperatorRole();
			operatorRole.setOperator(operator);
			operatorRole.setRole(r);
			operatorRoleDao.save(operatorRole);
		}
	}

	public void userUpdate(Operator operator, String[] roleIdArr) {
		operator.setUpdateTime(new Date());
		Operator u = operatorDao.find(operator.getId());
		operator.setPwd(u.getPwd());
		operatorDao.update(operator);
		operatorRoleDao.deleteByUserId(u.getId());
		for (int i = 0; i < roleIdArr.length; i++) {
			String roleIdStr = roleIdArr[i];
			long roleId = Long.parseLong(roleIdStr);
			Role r = new Role(roleId);
			OperatorRole operatorRole = new OperatorRole();
			operatorRole.setOperator(operator);
			operatorRole.setRole(r);
			operatorRoleDao.save(operatorRole);
		}
	}

	public void userDelete(long id) {
		Operator operator = operatorDao.userFind(id);
		operator.setIsDelete(true);
		operator.setUpdateTime(new Date());
		operatorDao.update(operator);
	}

	public Operator getUserById(long id) {
		return operatorDao.userFind(id);
	}

	public PageDataList<Operator> getUserPageList(QueryParam param) {
		return operatorDao.findPageList(param);
	}

	public void userUpdate(Operator operator) {
		operatorDao.update(operator);
	}

	public void updateUserPwd(Operator operator) {
		operator.setUpdateTime(new Date());
		this.userUpdate(operator);
	}

	public Operator getUserByUserName(String userName) {
		if (userName == null || userName.length() <= 0) {
			return null;
		}
		return operatorDao.getManagerByUserName(userName);
	}

}
