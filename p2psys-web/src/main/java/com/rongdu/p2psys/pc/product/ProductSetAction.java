package com.rongdu.p2psys.pc.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.product.domain.ProductMaterials;
import com.rongdu.p2psys.nb.product.model.ProductBasicModel;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductMaterialsService;

public class ProductSetAction extends BaseAction<ProductBasicModel> implements
		ModelDriven<ProductBasicModel>
{
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private ProductMaterialsService productMaterialsService;

	private Map<String, Object> data;

	/**
	 * 显示组合产品列表
	 * 
	 * @throws Exception
	 */
	@Action("/product/showProductSetList")
	public void showProductSetList() throws Exception
	{
		List<ProductBasicModel> dataList = productBasicService
				.getModelSetByStatus(ProductStatusConstant.STATUS_APPROVED,
						Constant.COOPERATE_TYPE__WECHAT);

		data = new HashMap<String, Object>();
		data.put("data", dataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 显示组合产品详情
	 * 
	 * @throws Exception
	 */
	@Action("/product/showProductSetDetail")
	public void showProductSetDetail() throws Exception
	{
		Long id = paramLong("prodId");
		ProductBasicModel model = productBasicService.getModelSetById(id);

		// 素材
		List<ProductMaterials> list = productMaterialsService
				.getMaterialsByProductId(id);

		data = new HashMap<String, Object>();
		data.put("data", model);
		data.put("matList", list);
		printWebJson(getStringOfJpaObj(data));
	}

}
