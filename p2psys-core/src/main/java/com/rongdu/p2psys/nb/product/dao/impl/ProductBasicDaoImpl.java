package com.rongdu.p2psys.nb.product.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.nb.product.dao.ProductBasicDao;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.util.ConstantUtil;

@Repository("productBasicDao")
public class ProductBasicDaoImpl extends BaseDaoImpl<ProductBasic> implements
		ProductBasicDao {

	@Override
	public List<ProductBasic> getRecommendProduct(String platform,
			Boolean isExperienceProduct) {
		QueryParam param = QueryParam.getInstance();

		// 推荐的
		param.addParam("hotProduct", ConstantUtil.FLAG_TRUE);

		if (isExperienceProduct) {
			// 体验标
			param.addParam("productType.typeCategory",
					ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE);
		} else {
			// 排除体验标
			param.addParam("productType.typeCategory", Operators.NOTEQ,
					ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE);
			param.addParam("hotProduct", ConstantUtil.FLAG_TRUE);
		}

		// 区分PC端、微信端
		if (Constant.COOPERATE_TYPE__PC.equals(platform)) {
			param.addParam("showForPc", ConstantUtil.FLAG_TRUE);
		} else if (Constant.COOPERATE_TYPE__WECHAT.equals(platform)) {
			param.addParam("showForWechat", ConstantUtil.FLAG_TRUE);
		}
		param.addOrder("showOrder");
		param.addOrder(OrderType.DESC, "recommendTime");
		param.addOrder(OrderType.DESC, "id");
		return findByCriteria(param);
	}

	@Override
	public List<ProductBasic> getProductByFlag(Long flagId, String orderField) {
		QueryParam param = QueryParam.getInstance();
		// PC端
		param.addParam("showForPc", ConstantUtil.FLAG_TRUE);
		// 产品标签
		param.addParam("productTypeFlag.id", flagId);
		// 排序
		if (StringUtil.isNotBlank(orderField)) {
			if ("lowestRefundRate".equalsIgnoreCase(orderField)) {
				param.addOrder(OrderType.DESC, orderField);
			} else {
				param.addOrder(OrderType.ASC, orderField);
			}
		}
		param.addOrder("showOrder");
		param.addOrder(OrderType.DESC, "recommendTime");
		param.addOrder(OrderType.DESC, "id");

		return findByCriteria(param);
	}

	@Override
	public void changeRecommend(Long id, String reason) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("isRecommend", Operators.NOTEQ, ConstantUtil.FLAG_FALSE);
		List<ProductBasic> pojoList = findByCriteria(param);

		// 重置现有首页推荐标
		List<ProductBasic> pojoListTemp = new ArrayList<ProductBasic>();
		for (ProductBasic pojoTemp : pojoList) {
			pojoTemp.setIsRecommend(ConstantUtil.FLAG_FALSE);
			pojoListTemp.add(pojoTemp);
		}
		update(pojoListTemp);

		// 将指定标设置为新的首页推荐标
		ProductBasic pojo = find(id);
		pojo.setIsRecommend(ConstantUtil.FLAG_TRUE);
		pojo.setRecommendReason(reason);
		update(pojo);
	}

}
