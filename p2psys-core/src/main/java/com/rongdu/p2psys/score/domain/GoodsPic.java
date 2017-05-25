package com.rongdu.p2psys.score.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rd_goods_pic")
public class GoodsPic implements Serializable {
	
	private static final long serialVersionUID = -0L;
	
	//id 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//商品ID
	private long goodsId;
	
	//图片路径 
	private String picUrl;
	
	//添加时间 
	private Date addTime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
