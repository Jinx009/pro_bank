package com.rongdu.p2psys.user.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.user.dao.UserVipApplyDao;
import com.rongdu.p2psys.user.domain.UserVipApply;
import com.rongdu.p2psys.user.model.UserIdentifyModel;
import com.rongdu.p2psys.user.model.UserVipApplyModel;

@Repository(value = "userVipApplyDao")
public class UserVipApplyDaoImpl extends BaseDaoImpl<UserVipApply> implements UserVipApplyDao {

	/**
	 * 获取vip列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@Override
	public PageDataList<UserVipApplyModel> vipManagerList(int pageNumber, int pageSize, UserIdentifyModel model) {

		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if (model != null) {
			if (!StringUtil.isBlank(model.getUserName())) {
				param.addParam("user.userName", model.getUserName());
			}
			if (!StringUtil.isBlank(model.getStartTime())) {
				String startTime = model.getStartTime() + " 00:00:00";// 起始时间
				param.addParam("addTime", Operators.GT, DateUtil.getDateByFullDateStr(startTime));
			}
			if (!StringUtil.isBlank(model.getEndTime())) {
				String endTime = model.getEndTime() + " 23:59:59";// 起始时间
				param.addParam("addTime", Operators.LT, DateUtil.getDateByFullDateStr(endTime));
			}
			if (model.getStatus() < 3) { // 代表全部
				param.addParam("status", Operators.EQ, model.getStatus());
			}
		}
		PageDataList<UserVipApply> pageList = findPageList(param);

		PageDataList<UserVipApplyModel> pageDataList_ = new PageDataList<UserVipApplyModel>();
		List<UserVipApplyModel> list_ = new ArrayList<UserVipApplyModel>();

		pageDataList_.setPage(pageList.getPage());
		List<UserVipApply> list = pageList.getList();
		for (UserVipApply userVipApply : list) {
			UserVipApplyModel userVipApplyModel = UserVipApplyModel.instance(userVipApply);
			userVipApplyModel.setUserName(userVipApply.getUser().getUserName());
			list_.add(userVipApplyModel);
		}
		pageDataList_.setList(list_);

		return pageDataList_;
	}
}
