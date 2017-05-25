package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.crowdfunding.dao.MaterialsDao;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.model.MaterialsModel;

@Repository("materialsDao")
public class MaterialsDaoImpl extends BaseDaoImpl<Materials> implements
		MaterialsDao {

	@Override
	public PageDataList<MaterialsModel> dataList(MaterialsModel model,
			int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);

		PageDataList<Materials> pageDataList = super.findPageList(param);
		PageDataList<MaterialsModel> pageDataList_ = new PageDataList<MaterialsModel>();
		List<MaterialsModel> list = new ArrayList<MaterialsModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				Materials materials = (Materials) pageDataList.getList().get(i);
				MaterialsModel materialsModel = MaterialsModel
						.instance(materials);
				list.add(materialsModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@SuppressWarnings("unchecked")
	public List<Materials> getByHql(String hql) {
		Query query = (Query) em.createQuery(hql);
		List<Materials> list = query.getResultList();

		if (list != null && !list.isEmpty()) {
			return list;
		}
		return null;
	}

}
