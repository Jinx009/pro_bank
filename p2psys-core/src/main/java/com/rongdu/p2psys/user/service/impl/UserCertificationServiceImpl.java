package com.rongdu.p2psys.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderScoreCertificateLog;
import com.rongdu.p2psys.user.dao.UserCertificationDao;
import com.rongdu.p2psys.user.domain.CertificationType;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCertification;
import com.rongdu.p2psys.user.model.UserCertificationModel;
import com.rongdu.p2psys.user.service.UserCertificationService;

@Service("userCertificationService")
public class UserCertificationServiceImpl implements UserCertificationService {

	@Resource
	private UserCertificationDao userCertificationDao;
	@Resource
	private VerifyLogService verifyLogService;

	@Override
	public PageDataList<UserCertificationModel> list(long userId, int page) {
		return userCertificationDao.list(userId, page);
	}

	@Override
	public void add(UserCertification certification) {
		userCertificationDao.save(certification);
	}

	/** 通过userId 获得资料审核列表 **/
	@Override
	public PageDataList<UserCertificationModel> findByUserId(long userId, int status, int page) {
		return userCertificationDao.findByUserId(userId, status, page);
	}

	public CertificationType findByTypeId(long typeId) {
		return userCertificationDao.findByTypeId(typeId);
	}

	/**
	 * 获得证明资料信息列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@Override
	public PageDataList<UserCertificationModel> userCertificationList(int pageNumber, int pageSize,
			UserCertificationModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if (!StringUtil.isBlank(model.getUserName())) {
			param.addParam("user.userName", Operators.EQ, model.getUserName());
		}
		/*if (model.getStatus() != -1) {
			param.addParam("status", model.getStatus());
		}*/
		param.addOrder(OrderType.DESC, "id");
		PageDataList<UserCertification> pageDataList = userCertificationDao.findPageList(param);
		PageDataList<UserCertificationModel> pageDataList_ = new PageDataList<UserCertificationModel>();
		List<UserCertificationModel> list = new ArrayList<UserCertificationModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				UserCertification certification = (UserCertification) pageDataList.getList().get(i);
				UserCertificationModel attestationModel = UserCertificationModel.instance(certification);
				attestationModel.setTypeName(certification.getCertificationType().getName());
				attestationModel.setUserName(certification.getUser().getUserName());
				attestationModel.setRealName(certification.getUser().getRealName());
				list.add(attestationModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public void certificationVerify(long id, String verifyRemark, int status, Operator operator) {
		// 审核日志表中插入一条数据
		VerifyLog verifyLog = new VerifyLog();
		verifyLog.setVerifyUser(operator);
		verifyLog.setType("atteatation");
		verifyLog.setFid(id);
		verifyLog.setVerifyType(10);// 审核类型
		if (status == 1) {
			UserCertification attestation = userCertificationDao.find(id);
			BaseScoreLog bLog = new TenderScoreCertificateLog(attestation.getUser().getUserId());
			bLog.doEvent();
			verifyLog.setResult(1);
		} else if (status == 2) {
			verifyLog.setResult(-1);
		}
		verifyLog.setRemark(verifyRemark);
		verifyLog.setTime(new Date());
		verifyLog.setIp(Global.getIP());
		verifyLogService.save(verifyLog);
		userCertificationDao.attestationEdit(id, verifyRemark, status, operator);
	}

	@Override
	public UserCertification findById(long id) {
		return userCertificationDao.find(id);
	}

	@Override
	public List<UserCertification> findByUserAndTypeId(User user, long typeId) {
		QueryParam param = QueryParam.getInstance()
				.addParam("certificationType.id", typeId)
				.addParam("user", user);
		return userCertificationDao.findByCriteria(param);
	}

	@Override
	public void delete(long id) {
		userCertificationDao.delete(id);
	}

	@Override
	public PageDataList<UserCertificationModel> userCertificationList(
			UserCertificationModel model) {
		
		return userCertificationDao.userCertificationList(model);
	}

	@Override
	public List<String> findByUserIdAndTypeId(long userId, long typeId) {
		
		return userCertificationDao.findByUserIdAndTypeId(userId, typeId);
	}
}
