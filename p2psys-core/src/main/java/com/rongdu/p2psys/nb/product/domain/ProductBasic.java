package com.rongdu.p2psys.nb.product.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "nb_product_basic")
public class ProductBasic implements Serializable {

	private static final long serialVersionUID = 6433599127505927860L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 产品名称
	 */
	private String productName;

	/**
	 * 产品标签
	 */
	@ManyToOne
	@JoinColumn(name = "flag_id")
	private ProductTypeFlag productTypeFlag;

	/**
	 * 产品类型
	 */
	@ManyToOne
	@JoinColumn(name = "type_id")
	private ProductType productType;

	/**
	 * 产品关联的真实ID
	 */
	private Long relatedId;

	/**
	 * 状态
	 * 
	 * <p>
	 * 0：待初审
	 * </p>
	 * <p>
	 * 1：初审通过
	 * </p>
	 * <p>
	 * 2：初审不通过
	 * </p>
	 */
	private Integer status;

	/**
	 * 是否在PC端呈现
	 */
	private Integer showForPc;

	/**
	 * 是否在微信端呈现
	 */
	private Integer showForWechat;

	/**
	 * 是否在移动浏览器端呈现
	 */
	private Integer showForMobile;

	/**
	 * 是否在IOS端呈现
	 */
	private Integer showForIos;

	/**
	 * 是否在Android端呈现
	 */
	private Integer showForAndroid;

	/**
	 * 是否在Windows Phone端呈现
	 */
	private Integer showForWinphone;

	/**
	 * 显示顺序
	 */
	private Integer showOrder;

	/**
	 * 最低预期收益
	 */
	private Double lowestRefundRate;

	/**
	 * 最高预期收益
	 */
	private Double highestRefundRate;

	/**
	 * 是否为微信首页推荐产品
	 */
	private Integer isRecommend;

	/**
	 * 微信首页推荐理由
	 */
	private String recommendReason;

	/**
	 * 是否为热销推荐产品
	 */
	private Integer hotProduct;

	/**
	 * 产品推荐时间
	 */
	private Date recommendTime;

	/**
	 * 审核备注
	 */
	private String verifyRemark;

	/**
	 * 借款期限
	 */
	private Integer timeLimit;

	/**
	 * 最低投标金额
	 */
	private Double lowestAccount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ProductTypeFlag getProductTypeFlag() {
		return productTypeFlag;
	}

	public void setProductTypeFlag(ProductTypeFlag productTypeFlag) {
		this.productTypeFlag = productTypeFlag;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getShowForPc() {
		return showForPc;
	}

	public void setShowForPc(Integer showForPc) {
		this.showForPc = showForPc;
	}

	public Integer getShowForWechat() {
		return showForWechat;
	}

	public void setShowForWechat(Integer showForWechat) {
		this.showForWechat = showForWechat;
	}

	public Integer getShowForMobile() {
		return showForMobile;
	}

	public void setShowForMobile(Integer showForMobile) {
		this.showForMobile = showForMobile;
	}

	public Integer getShowForIos() {
		return showForIos;
	}

	public void setShowForIos(Integer showForIos) {
		this.showForIos = showForIos;
	}

	public Integer getShowForAndroid() {
		return showForAndroid;
	}

	public void setShowForAndroid(Integer showForAndroid) {
		this.showForAndroid = showForAndroid;
	}

	public Integer getShowForWinphone() {
		return showForWinphone;
	}

	public void setShowForWinphone(Integer showForWinphone) {
		this.showForWinphone = showForWinphone;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public Double getLowestRefundRate() {
		return lowestRefundRate;
	}

	public void setLowestRefundRate(Double lowestRefundRate) {
		this.lowestRefundRate = lowestRefundRate;
	}

	public Double getHighestRefundRate() {
		return highestRefundRate;
	}

	public void setHighestRefundRate(Double highestRefundRate) {
		this.highestRefundRate = highestRefundRate;
	}

	public Integer getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getRecommendReason() {
		return recommendReason;
	}

	public void setRecommendReason(String recommendReason) {
		this.recommendReason = recommendReason;
	}

	public Integer getHotProduct() {
		return hotProduct;
	}

	public void setHotProduct(Integer hotProduct) {
		this.hotProduct = hotProduct;
	}

	public Date getRecommendTime() {
		return recommendTime;
	}

	public void setRecommendTime(Date recommendTime) {
		this.recommendTime = recommendTime;
	}

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Double getLowestAccount() {
		return lowestAccount;
	}

	public void setLowestAccount(Double lowestAccount) {
		this.lowestAccount = lowestAccount;
	}

}
