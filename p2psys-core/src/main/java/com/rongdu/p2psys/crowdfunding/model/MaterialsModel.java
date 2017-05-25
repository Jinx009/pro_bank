package com.rongdu.p2psys.crowdfunding.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.crowdfunding.domain.Materials;

/**
 * 素材
 * @author Jinx
 *
 */
public class MaterialsModel extends Materials {

	private static final long serialVersionUID = -7761298684234799191L;

	//当前页码
	private int page;
	//每页条数
	private int rows = Page.ROWS;
	//备注
	private String remark;

	public static MaterialsModel instance(Materials materials) {
		MaterialsModel model = new MaterialsModel();
		BeanUtils.copyProperties(materials, model);
		return model;
	}

	public Materials prototype() {
		Materials materials = new Materials();
		BeanUtils.copyProperties(this, materials);
		return materials;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
