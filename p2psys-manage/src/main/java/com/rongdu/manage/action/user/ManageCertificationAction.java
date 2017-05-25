package com.rongdu.manage.action.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.model.UserCertificationModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserCertificationService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 用户审核
 * 
 * @author zxc
 * @version 2.0
 */
public class ManageCertificationAction extends BaseAction<UserCertificationModel> implements ModelDriven<UserCertificationModel> {

	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserCertificationService certificationService;
	@Resource
	private UserCacheService userCacheService;

	private Operator operator;
	private Map<String, Object> data;

	/**
	 * 获得证明资料页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/certification/certificationManager")
	public String certificationManager() throws Exception {
		return "certificationManager";
	}

	/**
	 * 获得证明资料信息列表
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/certification/certificationList")
	public void certificationList() throws Exception {
		data = new HashMap<String, Object>();
		PageDataList<UserCertificationModel> pagaDataList = certificationService.userCertificationList(model);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 审核证明资料页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/user/certification/certificationEditPage")
	public String certificationEditPage() throws Exception {
		long userId = paramLong("userId");
		long typeId = paramLong("typeId");
		List<String> list = certificationService.findByUserIdAndTypeId(userId, typeId);
		request.setAttribute("userId", userId);
		request.setAttribute("typeId", typeId);
		request.setAttribute("list", list);
		return "certificationEditPage";
	}

	/**
	 * 审核证明资料
	 * 
	 * @return
	 * @throws Exception if has error
	 */
	@Action(value = "/modules/user/user/certification/certificationEdit")
	public void certificationEdit() throws Exception {
		operator = getOperator();
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		String verifyRemark = paramString("verifyRemark");
		int status = paramInt("status");
		certificationService.certificationVerify(id, verifyRemark, status, operator);
		data.put("result", true);
		data.put("msg", "审核成功！");
		printJson(getStringOfJpaObj(data));
	}
}
