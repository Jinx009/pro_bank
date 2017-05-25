package com.rongdu.p2psys.crowdfunding.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 素材仓库
 * @author Jinx
 *
 */
@Entity
@Table(name = ("cf_materials"))
public class Materials implements Serializable
{
	private static final long serialVersionUID = 442399593546114498L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	//产品
	@ManyToOne
	@JoinColumn(name = "project_id")
	private ProjectBaseinfo projectBaseinfo;
	//素材名称
	private String materialName;
	//素材编码
	private String materialCode;
	//素材类型
	private Integer materialType;
	//素材内容
	private String materialContent;
	//上传时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "material_upload_time")
	private Date materialUploadTime;
	//素材从属 0产品 1 权益(目前全部都是产品)
	private Integer  materialFather;


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ProjectBaseinfo getProjectBaseinfo() {
		return projectBaseinfo;
	}
	public void setProjectBaseinfo(ProjectBaseinfo projectBaseinfo) {
		this.projectBaseinfo = projectBaseinfo;
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
	public String getMaterialContent() {
		return materialContent;
	}
	public void setMaterialContent(String materialContent) {
		this.materialContent = materialContent;
	}
	public Date getMaterialUploadTime() {
		return materialUploadTime;
	}
	public void setMaterialUploadTime(Date materialUploadTime) {
		this.materialUploadTime = materialUploadTime;
	}
	public Integer getMaterialFather() {
		return materialFather;
	}
	public void setMaterialFather(Integer materialFather) {
		this.materialFather = materialFather;
	}
}
