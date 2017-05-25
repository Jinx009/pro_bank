package com.rongdu.p2psys.user.dao.jdbc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.user.dao.UserFreezeDao;
import com.rongdu.p2psys.user.domain.UserFreeze;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserFreezeModel;

@Repository("freezeDao")
public class UserFreezeDaoImpl extends BaseDaoImpl<UserFreeze> implements UserFreezeDao {

	@Override
	public PageDataList<UserFreezeModel> freezeList(int pageNumber, int pageSize, UserFreezeModel model) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if(!StringUtil.isBlank(model.getSearchName())){//模糊查询
			param.addParam("user.userName", Operators.LIKE, model.getSearchName());
		}else{//精确查询
			if (model.getStatus() != -1) {
				param.addParam("status", model.getStatus());
			}
			if (!StringUtil.isBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<UserFreeze> pageDataList = super.findPageList(param);
		PageDataList<UserFreezeModel> pageDataList_ = new PageDataList<UserFreezeModel>();
		List<UserFreezeModel> list = new ArrayList<UserFreezeModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				UserFreeze freeze = (UserFreeze) pageDataList.getList().get(i);
				UserFreezeModel freezeModel = UserFreezeModel.instance(freeze);
				freezeModel.setUserName(freeze.getUser().getUserName());
				list.add(freezeModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public boolean isExistsByUserName(String userName) {
		String sql = "select count(1) from rd_user_freeze f, rd_user u where f.user_id = u.user_id and u.user_name = :userName";
		Query query = em.createNativeQuery(sql);
		query.setParameter("userName", userName);
		BigInteger count = (BigInteger) query.getSingleResult();
		return count.intValue() == 0 ? false : true;
	}


//	@Override
//	public void freezeEdit(UserFreezeModel model) {
//		String sql = "update rd_user_freeze set status = :status,mark = :mark,remark = :remark where id = :id";
//		Query query = em.createNativeQuery(sql);
//		query.setParameter("status", model.getStatus());
//		query.setParameter("mark", model.getMark());
//		query.setParameter("remark", model.getRemark());
//		query.setParameter("id", model.getId());
//		int count = query.executeUpdate();
//		if (count != 1) {
//			throw new UserException("修改失败！", 1);
//		}
//	}


	@Override
	public void freezeDelete(long id, int status) {
		String jpql = "UPDATE Freeze SET status = :status WHERE id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("status", status);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result != 1) {
			throw new UserException("更新失败！");
		}
	}

	@Override
	public UserFreeze getByUserName(String userName) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userName", userName);
		return super.findByCriteriaForUnique(param);
	}

	@Override
	public UserFreeze getByUserId(long userId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		return super.findByCriteriaForUnique(param);
	}

}
