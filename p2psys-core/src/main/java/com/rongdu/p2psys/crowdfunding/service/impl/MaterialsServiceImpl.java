package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.crowdfunding.dao.MaterialsDao;
import com.rongdu.p2psys.crowdfunding.dao.ProjectBaseinfoDao;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.model.MaterialsModel;
import com.rongdu.p2psys.crowdfunding.service.MaterialsService;

@Service("materialsService")
public class MaterialsServiceImpl implements MaterialsService {
	@Resource
	private MaterialsDao materialsDao;

	@Resource
	private ProjectBaseinfoDao projectBaseinfoDao;

	public Materials find(Integer Id) {
		return materialsDao.find(Id);
	}

	public PageDataList<MaterialsModel> dataList(MaterialsModel model,int pageNumber, int pageSize) {
		return materialsDao.dataList(model, pageNumber, pageSize);
	}

	public void deleteProject(Integer id) {
		materialsDao.delete(id);
	}

	public void update(Materials materials) {
		materialsDao.update(materials);
	}

	public void saveObject(Materials materials) {
		materialsDao.save(materials);
	}

	public List<Materials> findByCriteria(QueryParam param) {
		return materialsDao.findByCriteria(param);
	}

	/**
	 * 根据产品id查找素材
	 * @param project_id
	 * @return
	 */
	public List<Materials> findByProjectId(Long project_id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM Materials m WHERE m.projectBaseinfo.id = ");
		buffer.append(project_id);
		List<Materials> list = materialsDao.getByHql(buffer.toString());
		return list;
	}

	
	/**
	 * 删除某个权益规则的素材
	 * @param profitRuleId
	 */
	public void deleteProfitMaterials(Integer profitRuleId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM Materials WHERE profitId  = ");
		buffer.append(profitRuleId);
		List<Materials> list = materialsDao.getByHql(buffer.toString());
		if(null!=list){
			for(int i = 0;i<list.size();i++){
				Materials materials = list.get(i);
				materialsDao.delete(materials .getId());
			}
		}
	}

	public void delete(Materials materials) {
		materialsDao.delete(materials.getId());
	}
}
