package com.rongdu.p2psys.nb.homepage.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nb_homepage_banner")
public class HomePageBanner implements Serializable {

	private static final long serialVersionUID = -4880397641854091017L;

	public HomePageBanner() {
		super();
	}

	public HomePageBanner(Long id) {
		super();
		this.id = id;
	}

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * banner名称
	 */
	private String bannerName;

	

	/**
	 * banner关键字
	 */
	private String bannerKeywords;


	/**
	 * banner标志背景图URL
	 */
	private String bannerPicUrl; 
    /**
     * banner连接URL 
     */
	private String bannerLinkUrl; 
	

	/**
	 * banner排序
	 */
	private Integer bannerOrder;
	
	/**
	 * 是否启用
	 * 
	 * <p>
	 * 0：未启用
	 * </p>
	 * <p>
	 * 1：启用
	 * </p>
	 */
	private Integer isEnable;

	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 0 OC 1微信
	 */
	private Integer useStatus;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public String getBannerKeywords() {
		return bannerKeywords;
	}

	public void setBannerKeywords(String bannerKeywords) {
		this.bannerKeywords = bannerKeywords;
	}

	public String getBannerPicUrl() {
		return bannerPicUrl;
	}

	public void setBannerPicUrl(String bannerPicUrl) {
		this.bannerPicUrl = bannerPicUrl;
	}

	public String getBannerLinkUrl() {
		return bannerLinkUrl;
	}

	public void setBannerLinkUrl(String bannerLinkUrl) {
		this.bannerLinkUrl = bannerLinkUrl;
	}

	public Integer getBannerOrder() {
		return bannerOrder;
	}

	public void setBannerOrder(Integer bannerOrder) {
		this.bannerOrder = bannerOrder;
	}
	

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getUseStatus()
	{
		return useStatus;
	}

	public void setUseStatus(Integer useStatus)
	{
		this.useStatus = useStatus;
	}
	
}
