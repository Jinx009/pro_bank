package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.model.MaterialsModel;

public interface MaterialsDao extends BaseDao<Materials>{
	
	public PageDataList<MaterialsModel> dataList(MaterialsModel model,int pageNumber, int pageSize);

	public List<Materials> getByHql(String hql);
}
