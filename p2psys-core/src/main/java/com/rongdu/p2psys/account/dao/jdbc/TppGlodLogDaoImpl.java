package com.rongdu.p2psys.account.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.TppGlodLogDao;
import com.rongdu.p2psys.account.domain.TppGlodLog;
import com.rongdu.p2psys.account.model.TppGlodLogModel;

/**
 * 平台账户操作记录
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年2月4日
 */
@Repository("tppGlodLogDao")
public class TppGlodLogDaoImpl extends BaseDaoImpl<TppGlodLog> implements
		TppGlodLogDao {

	@Override
	public PageDataList<TppGlodLogModel> list(TppGlodLogModel glodLogModel) {
		QueryParam param = QueryParam.getInstance();
		param.addPage(glodLogModel.getPage(), glodLogModel.getRows());
		if(!StringUtil.isBlank(glodLogModel.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("operator.userName", Operators.LIKE, glodLogModel.getSearchName());
    		param.addOrFilter(orFilter1, orFilter1);
		}else{
			if (StringUtil.isNotBlank(glodLogModel.getStartTime())) {
				Date start = DateUtil.valueOf(glodLogModel.getStartTime() + " 00:00:00");
				param.addParam("addtime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(glodLogModel.getEndTime())) {
				Date end = DateUtil.valueOf(glodLogModel.getEndTime() + " 23:59:59");
				param.addParam("addtime", Operators.LTE, end);
			}
			if (StringUtil.isNotBlank(glodLogModel.getAccount())) {
				param.addParam("account", Operators.EQ, glodLogModel.getAccount());
			}
			if (StringUtil.isNotBlank(glodLogModel.getPayAccount())) {
				param.addParam("payAccount", Operators.EQ, glodLogModel.getPayAccount());
			}
			if (glodLogModel.getStatus() != 99){
				param.addParam("status", Operators.EQ, glodLogModel.getStatus());
			}
			if(StringUtil.isNotBlank(glodLogModel.getType())){
				param.addParam("type", Operators.EQ, glodLogModel.getType());
			}
			if(StringUtil.isNotBlank(glodLogModel.getOrdId())){
				param.addParam("ordId", Operators.EQ, glodLogModel.getOrdId());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<TppGlodLog> pageDataList = this.findPageList(param);
		PageDataList<TppGlodLogModel> pageDataList_ = new PageDataList<TppGlodLogModel>();
		List<TppGlodLogModel> list = new ArrayList<TppGlodLogModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				TppGlodLog glodLog = (TppGlodLog) pageDataList.getList().get(i);
				TppGlodLogModel model = TppGlodLogModel.instance(glodLog);
				model.setUserName(glodLog.getOperator().getUserName());
				list.add(model);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

}
