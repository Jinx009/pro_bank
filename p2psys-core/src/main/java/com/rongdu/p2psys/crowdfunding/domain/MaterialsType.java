package com.rongdu.p2psys.crowdfunding.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 众筹产品素材
 * @author Jinx
 *
 */
@Entity
@Table(name = ("cf_material_code_list"))
public class MaterialsType implements Serializable
{
	private static final long serialVersionUID = 442399593546114498L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	//素材名称
	private String materialName;
	//素材编码
	private String materialCode;
	//素材类型 图片/文字
	private Integer materialType;
	//是否显示
	private Integer viewPrivilege;

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public Integer getMaterialType() {
		return materialType;
	}
	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}
	public Integer getViewPrivilege() {
		return viewPrivilege;
	}
	public void setViewPrivilege(Integer viewPrivilege) {
		this.viewPrivilege = viewPrivilege;
	}

	
	
}
