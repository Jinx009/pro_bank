package com.rongdu.p2psys.pro;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.MaterialsModel;
import com.rongdu.p2psys.crowdfunding.service.MaterialsService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;

/**
 * 后台素材 
 * @author Jinx
 *
 */
public class MaterialsAction extends BaseAction<MaterialsModel> implements ModelDriven<MaterialsModel>{

	@Resource
	private MaterialsService materialsService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	
	private Map<String,Object> data;
	
	/**
	 * 前台向后台上传图片
	 * @throws IOException
	 */
	@Action(value="/code/imgUpload")
	public void backImgUpload() throws IOException{
		String path = manageImgUpload();
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.MSG,"前台向后台上传图片");
		data.put(ConstantUtil.DATA,"");
		if(StringUtil.isNotBlank(path)){
			data.put(ConstantUtil.DATA,path);
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	@Action(value = "/manage/code/materials",results = {@Result(name = "materials", type = "ftl", location = "/nb/pro/materials.html")})
	public String materials(){
		request.setAttribute("id",paramLong("id"));
		return "materials";
	}
	
	/**
	 * 保存或者修改素材
	 * @throws IOException 
	 */
	@Action(value="/manage/code/saveOrUpdateMaterials")
	public void saveAndUpdateMaterials() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.MSG,"添加或者更新素材");
		
		String json = paramString("data");
		JSONObject jsonObject = new JSONObject(json);
		Long projectId = jsonObject.getLong("projectId");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		if(null==projectBaseinfo){
			data.put(ConstantUtil.CODE,400);
			data.put(ConstantUtil.DATA,"项目不存在");
		}else{
			//将原来的素材设置为为不可用
			List<Materials> list = materialsService.findByProjectId(projectId);
			if(null!=list&&!list.isEmpty()){
				for(Materials materials:list){
					materials.setMaterialFather(0);
					materialsService.delete(materials);
				}
			}
			//解析新建的素材
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for(int i = 0;i<jsonArray.length();i++){
				Materials materials = new Materials();
				JSONObject jsObject = jsonArray.getJSONObject(i);
				materials.setMaterialCode(jsObject.getString("materials_code"));
				materials.setProjectBaseinfo(projectBaseinfo);
				materials.setMaterialName(jsObject.getString("materials_name"));
				materials.setMaterialType(jsObject.getInt("materials_type"));
				materials.setMaterialUploadTime(new Date());
				materials.setMaterialFather(1);
				materials.setMaterialContent(jsObject.getString("materials_content"));
				materialsService.saveObject(materials);
			}
			data.put(ConstantUtil.CODE,200);
			data.put(ConstantUtil.DATA,"操作成功！");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 获取项目素材
	 * @throws IOException
	 */
	@Action(value="/manage/materials")
	public void materialsList() throws IOException{
		Long id = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(id);
		List<Materials> list = materialsService.findByProjectId(id);
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,200);
		data.put(ConstantUtil.MSG,"获取产品素材列表");
		data.put(ConstantUtil.DATA,list);
		data.put("projectName",projectBaseinfo.getProjectName());
		data.put("id",id);
		printWebJson(getStringOfJpaObj(data));
	}
	
	  
}
