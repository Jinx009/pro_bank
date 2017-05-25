package com.rongdu.p2psys.score.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rd_goods_category")
public class GoodsCategory implements Serializable {

	private static final long serialVersionUID = -0L;
	
	//id 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//名称 
	private String name;
	
	//是否父级 
	private long parentId;
	
	//分类图标 
	private String iconCls;
	
	//备注 
	private String remark;
	
	//添加时间 
	private Date addTime;
	
	//添加者 
	private String addOparetor;
	
	// 排序
	private int sort;
	
	//是否删除:0否，1是
	private boolean isDelete; 

	public GoodsCategory() {
        super();
    }

	public GoodsCategory(long id) {
        super();
        this.id = id;
    }
	
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

    public String getAddOparetor() {
        return addOparetor;
    }

    public void setAddOparetor(String addOparetor) {
        this.addOparetor = addOparetor;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
