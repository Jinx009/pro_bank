package com.rongdu.manage.action.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.UserVipApply;
import com.rongdu.p2psys.user.model.UserVipApplyModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserCertificationService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * vip审核
 * 
 * @author zf
 * @version 2.0
 */
public class ManageVipVerifyAction extends BaseAction implements ModelDriven<UserVipApplyModel> {

	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserCertificationService attestationService;
	@Resource
	private UserCacheService userCacheService;

	private Map<String, Object> data = new HashMap<String, Object>();
	private UserVipApplyModel model = new UserVipApplyModel();

	public UserVipApplyModel getModel() {
		return model;
	}

	/**
	 * vip申请管理
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/identification/vipApplyManager")
	public String vipApplyManager() throws Exception {
		return "vipApplyManager";
	}

	/**
	 * vip申请列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/identification/vipApplyList")
	public void vipList() throws Exception {
		int pageNumber = paramInt("page");// 当前页数
		int pageSize = paramInt("rows");// 每页总数
		PageDataList<UserVipApplyModel> pagaDataList = userIdentifyService.vipApplyList(pageNumber, pageSize, model);
		int totalPage = pagaDataList.getPage().getTotal();// 总页数
		data.put("total", totalPage);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * vip审核详情页面
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/identification/vipVerifyAddPage")
	public String vipVerifyAddPage() throws Exception {
		UserVipApply userVipApply = userIdentifyService.getUserVipApplyById(model.getId());
		request.setAttribute("userVipApply", userVipApply);
		return "vipVerifyAddPage";
	}

	/**
	 * vip审核页面
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/user/identification/vipVerify")
	public void vipVerify() throws Exception {
		Operator operator = getOperator();

		UserVipApply userVipApply = userIdentifyService.getUserVipApplyById(model.getId());
		if (userVipApply.getStatus() == 0) {
			userVipApply.setStatus(model.getStatus());
			userIdentifyService.verifyVip(operator, userVipApply);
		} else {
			throw new BussinessException("用户vip状态不对，请查看", 1);
		}
		printResult("vip审核成功", true);
	}

}
