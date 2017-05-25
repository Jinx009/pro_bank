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
import com.rongdu.p2psys.user.model.UserCertificationApplyModel;
import com.rongdu.p2psys.user.service.UserCertificationApplyService;
import com.rongdu.p2psys.user.service.UserCertificationService;

/**
 * 用户审核申请
 * 
 * @author zf
 * @version 2.0
 */
public class ManageCertificationApplyAction extends BaseAction<UserCertificationApplyModel> implements ModelDriven<UserCertificationApplyModel> {

	@Resource
	private UserCertificationApplyService userCertificationApplyService;
	@Resource
	private UserCertificationService userCertificationService;

	private Operator operator;
	private Map<String, Object> data;

	/**
	 * 获得证明资料申请记录页面
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
	@Action(value = "/modules/user/user/certification/certificationApplyList")
	public void certificationApplyList() throws Exception {
		data = new HashMap<String, Object>();
		PageDataList<UserCertificationApplyModel> pagaDataList = userCertificationApplyService.applyList(model);
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
	@Action(value = "/modules/user/user/certification/certificationVerifyPage")
	public String certificationVerifyPage() throws Exception {
		long userId = paramLong("userId");
		long typeId = paramLong("typeId");
		List<String> list = userCertificationService.findByUserIdAndTypeId(userId, typeId);
		request.setAttribute("id", model.getId());
		request.setAttribute("list", list);
		return "certificationVerifyPage";
	}

	/**
	 * 审核证明资料
	 * 
	 * @return
	 * @throws Exception if has error
	 */
	@Action(value = "/modules/user/user/certification/certificationVerify")
	public void certificationVerify() throws Exception {
		operator = getOperator();
		data = new HashMap<String, Object>();
		model.setOperator(operator);
		userCertificationApplyService.certificationVerify(model);
		data.put("result", true);
		data.put("msg", "审核成功！");
		printJson(getStringOfJpaObj(data));
	}

}
