package com.rongdu.manage.action.crowdfunding;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.model.MaterialsModel;
import com.rongdu.p2psys.nb.util.ConstantUtil;

/**
 * 上传控制中心
 * @author Jinx
 *
 */
public class UploadAction extends BaseAction<MaterialsModel> implements ModelDriven<MaterialsModel>{
	
	private Map<String,Object> data;

	/**
	 * 后台上传图片
	 * @throws IOException
	 */
	@Action(value = "/cf/upload/img")
	public void uploadFile() throws IOException{
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String path = imgUpload();
		if (path != null && !"".equals(path)){
			data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			data.put(ConstantUtil.ERRORMSG,path);
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
