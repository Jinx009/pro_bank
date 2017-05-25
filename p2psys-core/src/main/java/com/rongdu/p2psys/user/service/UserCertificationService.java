package com.rongdu.p2psys.user.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.CertificationType;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCertification;
import com.rongdu.p2psys.user.model.UserCertificationModel;

/**
 * 证明材料Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月20日
 */
public interface UserCertificationService {

	/**
	 * 列表
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	public PageDataList<UserCertificationModel> list(long userId, int page);

	/**
	 * 新增
	 * 
	 * @param model
	 */
	public void add(UserCertification attestation);

	/**
	 * 获取有效证件列表
	 * 
	 * @param userId status
	 * @Auther:lijie
	 */
	public PageDataList<UserCertificationModel> findByUserId(long userId, int status, int page);

	/**
	 * 查询认证类型
	 * 
	 * @param typeId
	 * @return
	 */
	public CertificationType findByTypeId(long typeId);

	/**
	 * 获得证明资料信息列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	public PageDataList<UserCertificationModel> userCertificationList(int pageNumber, int pageSize,
			UserCertificationModel model);

	/**
	 * 审核证明资料
	 * 
	 * @param id
	 * @param verifyRemark
	 * @param status
	 */
	public void certificationVerify(long id, String verifyRemark, int status, Operator operator);

	/**
	 * 根据ID获得资料对象
	 * 
	 * @param status
	 * @return
	 */
	public UserCertification findById(long id);

	/**
	 * 根据用户和类型ID查找用户认证资料集合
	 * @param user
	 * @param typeId
	 * @return
	 */
	public List<UserCertification> findByUserAndTypeId(User user,
			long typeId);

	/**
	 * 删除认证材料
	 * @param id
	 */
	public void delete(long id);
	/**
	 * 获得证明资料信息列表
	 * @param model
	 * @return
	 */
	public PageDataList<UserCertificationModel> userCertificationList(
			UserCertificationModel model);

	/**
	 * 根据用户ID和类型ID查找图片路径类型集合
	 * @param userId
	 * @param typeId
	 * @return
	 */
	public List<String> findByUserIdAndTypeId(long userId, long typeId);

}
