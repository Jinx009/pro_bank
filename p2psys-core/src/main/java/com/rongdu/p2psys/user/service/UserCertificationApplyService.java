package com.rongdu.p2psys.user.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCertification;
import com.rongdu.p2psys.user.domain.UserCertificationApply;
import com.rongdu.p2psys.user.model.UserCertificationApplyModel;

/**
 * 证明材料申请Service
 * 
 * @author zf
 * @version 2.0
 * @since 2014年11月07日
 */
public interface UserCertificationApplyService {

	/**
	 * 添加申请记录
	 * @param userCertificationApply
	 */
	void add(UserCertificationApply userCertificationApply);

	/**
	 * 申请记录列表
	 * @param model
	 * @return
	 */
	PageDataList<UserCertificationApplyModel> applyList(
			UserCertificationApplyModel model);

	/**
	 * 审核
	 * @param model
	 * @param operator
	 */
	void certificationVerify(UserCertificationApplyModel model);

	/**
	 * 获取待审核总数
	 * @return
	 */
	int count();

	/**
	 * 获取审核状态
	 * @param user
	 * @param typeId
	 * @return
	 */
	int getStatusByUserAndTypeId(User user, long typeId);

	/**
	 * 获取认证审核列表
	 * @param user
	 * @return
	 */
	List<UserCertificationApplyModel> findByUser(User user);

	/**
	 * 上传认证材料
	 * @param userCertificationApply
	 * @param list
	 */
	void add(UserCertificationApply userCertificationApply,
			List<UserCertification> list);

}
