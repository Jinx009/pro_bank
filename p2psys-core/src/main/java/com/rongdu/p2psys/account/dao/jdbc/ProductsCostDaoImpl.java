package com.rongdu.p2psys.account.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.ProductsCostDao;
import com.rongdu.p2psys.account.domain.ProductsCost;
import com.rongdu.p2psys.account.model.ProductsCostModel;

/**
 * 产品费用
 * 
 * @author yl
 *
 */
@Repository("productsCostDao")
public class ProductsCostDaoImpl extends BaseDaoImpl<ProductsCost>
		implements ProductsCostDao {

	@Override
	public PageDataList<ProductsCostModel> list(ProductsCostModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(), model.getRows());
		if (StringUtil.isNotBlank(model.getSearchName())) {
			param.addParam("name", model.getSearchName());
		} else {
			if (StringUtil.isNotBlank(model.getName())) {
				param.addParam("name", model.getName());
			}
			if (StringUtil.isNotBlank(model.getCode())) {
				param.addParam("code", model.getCode());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
	            Date start = DateUtil.valueOf(model.getStartTime());
	            param.addParam("addTime", Operators.GTE, start);
	        }
	        if (StringUtil.isNotBlank(model.getEndTime())) {
	            Date end = DateUtil.valueOf(model.getEndTime());
	            param.addParam("addTime", Operators.LTE, end);
	        }
		}
		PageDataList<ProductsCost> pageDataList = this.findPageList(param);
		PageDataList<ProductsCostModel> pageDataList_ = new PageDataList<ProductsCostModel>();
		List<ProductsCostModel> list = new ArrayList<ProductsCostModel>();
		if(pageDataList.getList() != null && pageDataList.getList().size() > 0) {
			for (ProductsCost cost : pageDataList.getList()) {
				ProductsCostModel costModel = ProductsCostModel.instance(cost);
				list.add(costModel);
			}
		}
		pageDataList_.setPage(pageDataList.getPage());
		pageDataList_.setList(list);
		return pageDataList_;
	}
	
}
