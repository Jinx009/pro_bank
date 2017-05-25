package com.rongdu.manage.action.crowdfunding;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.domain.MaterialsType;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.MaterialsModel;
import com.rongdu.p2psys.crowdfunding.service.MaterialsService;
import com.rongdu.p2psys.crowdfunding.service.MaterialsTypeService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;

/**
 * 素材相關
 * 
 * @author Jinx
 *
 */
public class MaterialsAction extends BaseAction<MaterialsModel> implements
		ModelDriven<MaterialsModel> {
	@Resource
	private MaterialsService materialsService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private MaterialsTypeService materialsTypeService;

	private Map<String, Object> data;

	/**
	 * 素材类型列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/materials/materialsCodeListJson")
	public void dataAll() throws Exception {
		List<MaterialsType> list = materialsTypeService.getList();
		data = new HashMap<String, Object>();
		data.put("list", list);
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 素材页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/materials/materialsCommon")
	public String dataCommonIndex() throws Exception {
		long id = paramLong("id");
		request.setAttribute("project_id", id);

		return "materialsCommon";
	}

	/**
	 * 产品素材列表
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/materials/materialJson")
	public void dataCommonJson() throws Exception {
		Long project_id = paramLong("id");
		List<Materials> list = materialsService.findByProjectId(project_id);
		data = new HashMap<String, Object>();
		data.put("list", list);
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/materials/materialsAdd")
	public void dataAdd() throws Exception {
		long project_id = paramLong("project_id");
		int materialType = paramInt("materialType");
		Materials materials = model.prototype();
		if (!"none".equals(materials.getMaterialName())) {
			if (1 == materialType) {
				String path = imgUpload();
				if (path != null && !"".equals(path)) {
					materials.setMaterialContent(path);
				}
			} else if (3 == materialType) {
				String path = singleFileUpload();
				if (path != null && !"".equals(path)) {
					materials.setMaterialContent(path);
				}
			}

			ProjectBaseinfo projectBaseinfo = projectBaseinfoService
					.find(project_id);
			materials.setProjectBaseinfo(projectBaseinfo);
			materials.setMaterialUploadTime(new Date());
			materialsService.saveObject(materials);
		}
		data = new HashMap<String, Object>();
		data.put("result", "success");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/materials/materialsDelete")
	public void dataDelete() throws Exception {
		Integer id = paramInt("id");
		materialsService.deleteProject(id);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 更新
	 * 
	 * @throws Exception
	 */
	@Action("/modules/crowdfunding/materials/materialsUpdate")
	public void dataEdit() throws Exception {
		Materials materials = model.prototype();
		materialsService.update(materials);

		data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}
}
