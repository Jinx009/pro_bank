package com.rongdu.p2psys.crowdfunding.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 收益列表
 * @author Jinx
 *
 */
@Entity
@Table(name = ("cf_profit_rule"))
public class ProfitRule implements Serializable
{
	private static final long serialVersionUID = -4779800456843797313L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	//对应产品id
	private Long projectId;
	//投资金额
	private Double money;
	//文字描述
	private String content;
	//图片信息
	private String picPath;
	//权益名称
	private String name;
	//最大接受人数
	private Integer maxInvestor;
	//是否可以超人数
	private Integer isAccept;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMaxInvestor() {
		return maxInvestor;
	}
	public void setMaxInvestor(Integer maxInvestor) {
		this.maxInvestor = maxInvestor;
	}
	public Integer getIsAccept() {
		return isAccept;
	}
	public void setIsAccept(Integer isAccept) {
		this.isAccept = isAccept;
	}
	
}
