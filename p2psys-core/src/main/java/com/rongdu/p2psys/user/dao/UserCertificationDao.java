package com.rongdu.p2psys.user.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.CertificationType;
import com.rongdu.p2psys.user.domain.UserCertification;
import com.rongdu.p2psys.user.model.UserCertificationModel;

/**
 * 证明材料Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月20日
 */
public interface UserCertificationDao extends BaseDao<UserCertification> {

	/**
	 * 获取充值记录的总数
	 * 
	 * @param userId
	 * @return
	 */
	int count(long userId);

	/**
	 * 获取充值记录，有分页
	 * 
	 * @param userId
	 * @param start
	 * @param pernum
	 * @return
	 */
	PageDataList<UserCertificationModel> list(long userId, int page);

	/**
	 * 列表
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	List<UserCertificationModel> list(long userId);

	/**
	 * @param userId
	 * @param status 用户iD,证件有效状态
	 * @return 指定用户证件列表
	 */
	PageDataList<UserCertificationModel> findByUserId(long userId, int status, int page);

	/**
	 * 
	 * @return
	 */
	List<CertificationType> getAllList();

	/**
	 * 查询认证类型
	 * 
	 * @param typeId
	 * @return
	 */
	CertificationType findByTypeId(long typeId);

	/**
	 * 审核证明资料
	 * 
	 * @param id
	 * @param verifyRemark
	 * @param status
	 */
	void attestationEdit(long id, String verifyRemark, int status, Operator operator);

	/**
	 * 统计资料待审核总数
	 * 
	 * @param status
	 * @return
	 */
	int count(int status);

	/**
	 * 返回用户认证列表
	 * @param model
	 * @return
	 */
	PageDataList<UserCertificationModel> userCertificationList(
			UserCertificationModel model);

	List<String> findByUserIdAndTypeId(long userId, long typeId);

}
