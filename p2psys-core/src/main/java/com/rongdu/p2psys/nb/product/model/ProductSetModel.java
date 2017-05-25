package com.rongdu.p2psys.nb.product.model;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.p2psys.nb.product.domain.ProductSet;

public class ProductSetModel extends ProductSet
{
	private static final long serialVersionUID = 4304096022201509088L;

	public static List<ProductSet> transSetList(ProductBasicModel model)
	{
		List<ProductSet> list = new ArrayList<ProductSet>();
		ProductSet obj = null;
		if (null != model)
		{
			for (String prod : model.getProductsSelected().split(","))
			{
				obj = new ProductSet();
				// 组合产品ID
				obj.setProductId(model.getId());

				// 子产品ID
				obj.setSubProductId(Long.parseLong(prod.substring(0,
						prod.indexOf("-"))));

				// 百分比
				obj.setRate(Double.parseDouble(prod.substring(prod.indexOf("-") + 1)));

				list.add(obj);
			}
		}
		return list;
	}
}
