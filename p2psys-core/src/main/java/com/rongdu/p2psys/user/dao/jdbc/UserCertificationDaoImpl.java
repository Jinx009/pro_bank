package com.rongdu.p2psys.user.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.Page;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.dao.UserCertificationDao;
import com.rongdu.p2psys.user.domain.CertificationType;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCertification;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserCertificationModel;

@Repository("userCertificationDao")
public class UserCertificationDaoImpl extends BaseDaoImpl<UserCertification> implements UserCertificationDao {

	@Override
	public int count(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		return countByCriteria(param);
	}

	@Override
	public PageDataList<UserCertificationModel> list(long userId, int page) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addOrder(OrderType.DESC, "addTime");
		param.addPage(page);
		PageDataList<UserCertification> pageDataList = findPageList(param);
		PageDataList<UserCertificationModel> pageDataList_ = new PageDataList<UserCertificationModel>();
		List<UserCertificationModel> list = new ArrayList<UserCertificationModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				UserCertification attestation = (UserCertification) pageDataList.getList().get(i);
				UserCertificationModel model = UserCertificationModel.instance(attestation);
				model.setTypeName(attestation.getCertificationType().getName());
				model.setJifenVal(attestation.getCertificationType().getCredit() + "");
				list.add(model);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public List<UserCertificationModel> list(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addOrder(OrderType.DESC, "addTime");
		List<UserCertification> list = findByCriteria(param);
		List<UserCertificationModel> modelList = new ArrayList<UserCertificationModel>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				UserCertification attestation = list.get(i);
				UserCertificationModel model = UserCertificationModel.instance(attestation);
				model.setTypeName(attestation.getCertificationType().getName());
				model.setJifenVal(attestation.getCertificationType().getCredit() + "");
				modelList.add(model);
			}
		}
		return modelList;
	}

	@Override
	public PageDataList<UserCertificationModel> findByUserId(long userId, int status, int page) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addParam("status", status);
		param.addPage(page);
		PageDataList<UserCertification> pageDataList = findPageList(param);
		PageDataList<UserCertificationModel> pageDataList_ = new PageDataList<UserCertificationModel>();
		List<UserCertificationModel> list = new ArrayList<UserCertificationModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				UserCertification attestation = (UserCertification) pageDataList.getList().get(i);
				UserCertificationModel model = UserCertificationModel.instance(attestation);
				model.setTypeName(attestation.getCertificationType().getName());
				list.add(model);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CertificationType> getAllList() {
		Query query = em.createQuery(" from CertificationType order by typeId ");
		return query.getResultList();
	}

	public CertificationType findByTypeId(long typeId) {
		Query query = em.createQuery("from CertificationType where typeId=:typeId").setParameter("typeId", typeId);
		return (CertificationType) query.getSingleResult();
	}

	@Override
	public void attestationEdit(long id, String verifyRemark, int status, Operator operator) {
		String jpql = "UPDATE UserCertification SET verifyRemark = :verifyRemark, status = :status, "
				+ "verifyUser = :verifyUser, verifyTime= :verifyTime WHERE id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("verifyRemark", verifyRemark);
		query.setParameter("status", status);
		query.setParameter("verifyUser", operator.getId());
		query.setParameter("verifyTime", new Date());
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result != 1) {
			throw new UserException("审核证明材料失败！");
		}
	}

	@Override
	public int count(int status) {
		QueryParam param = QueryParam.getInstance();
		return countByCriteria(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageDataList<UserCertificationModel> userCertificationList(
			UserCertificationModel model) {
		String sql = "from UserCertification group by user_id, type_id";
		Query query = em.createQuery(sql, UserCertification.class);
		int size = query.getResultList().size();
		query.setFirstResult((model.getPage() - 1) * model.getSize());
		query.setMaxResults(model.getSize());
		PageDataList<UserCertificationModel> pageDataList = new PageDataList<UserCertificationModel>();
		List<UserCertification> list = query.getResultList();
		List<UserCertificationModel> models = new ArrayList<UserCertificationModel>();
		for (UserCertification uc : list) {
			UserCertificationModel uModel = UserCertificationModel.instance(uc);
			User user = uc.getUser();
			uModel.setUserName(user.getUserName());
			uModel.setRealName(user.getRealName());
			uModel.setTypeName(uc.getCertificationType().getName());
			uModel.setUserId(user.getUserId());
			uModel.setTypeId(uc.getCertificationType().getId());
			uModel.setCertificationType(null);
			uModel.setUser(null);
			models.add(uModel);
		}
		pageDataList.setList(models);
		pageDataList.setPage(new Page(size, model.getPage(), model.getSize()));
		return pageDataList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findByUserIdAndTypeId(long userId, long typeId) {
		String sql = "select picPath from UserCertification where user_id=? and type_id=?";
		Query query = em.createQuery(sql);
		query.setParameter(1, userId);
		query.setParameter(2, typeId);
		return query.getResultList();
	}
}
