package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.model.MaterialsModel;

public interface MaterialsService
{
	public Materials find(Integer Id);

	public PageDataList<MaterialsModel> dataList(MaterialsModel model,int pageNumber, int pageSize);

	public void deleteProject(Integer id);

	public void update(Materials materials);

	public void saveObject(Materials materials);

	public List<Materials> findByCriteria(QueryParam param);

	public List<Materials> findByProjectId(Long project_id);

	public void deleteProfitMaterials(Integer profitRuleId);
	
	public void delete(Materials materials);
}
