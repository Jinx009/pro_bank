package com.rongdu.p2psys.nb.product.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.domain.ProductTypeFlag;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundModel;

public class ProductBasicModel extends ProductBasic {

	private static final long serialVersionUID = 3646503974430687614L;

	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页数据条数
	 */
	private int size = Page.ROWS;

	/**
	 * 产品组合
	 * 
	 * <p>
	 * 每组用“,”隔开，子产品ID和子产品所占比用"-"隔开
	 * </p>
	 */
	private String productsSelected;

	/**
	 * 体验标产品
	 */
	private PpfundModel experienceModel;

	/**
	 * 现金类产品
	 */
	private PpfundModel ppfundModel;

	/**
	 * 非现金类产品
	 */
	private BorrowModel borrowModel;

	/**
	 * 众筹类产品
	 */
	private ProjectBaseinfoModel projectBaseinfoModel;

	/**
	 * 组合产品下所包含的子产品集合
	 */
	private Collection<ProductBasicModel> subProductList;

	/**
	 * 分配比例
	 */
	private Double rate;

	/**
	 * 产品类型的文字描述
	 */
	private String typeDesc;

	/**
	 * 产品状态的文字描述
	 */
	private String statusDesc;

	/**
	 * 组合产品的亮点
	 */
	private String lightspot;

	/**
	 * 组合产品的详情
	 */
	private String productDetail;

	/**
	 * 产品标签
	 */
	private Long flagId;

	/**
	 * 产品标签的文字描述
	 */
	private String flagDesc;

	/**
	 * 产品类型
	 */
	private Long typeId;

	/**
	 * 投资进度
	 */
	private Double scales;

	/**
	 * 投资总额
	 */
	private Double totalInvestMoney;

	/**
	 * 投资总收益
	 */
	private Double totalProfitMoney;

	public static ProductBasicModel instance(ProductBasic basic) {
		ProductBasicModel model = new ProductBasicModel();
		BeanUtils.copyProperties(basic, model);
		return model;
	}

	public ProductBasic protoType() {
		ProductBasic basic = new ProductBasic();
		BeanUtils.copyProperties(this, basic);
		return basic;
	}

	public static ProductBasic transBorrow(Borrow borrow) {
		ProductBasic prod = new ProductBasic();
		if (null != borrow) {
			setProductBasicDefaultValue(prod);

			prod.setProductName(borrow.getName());
			prod.setRelatedId(borrow.getId());
			prod.setStatus(borrow.getStatus());

			if (ProductTypeConstant.PRODUCT_TYPE__FLOAT == borrow.getType()) {
				prod.setLowestRefundRate(borrow.getExpectedLow());
				prod.setHighestRefundRate(borrow.getExpectedUp());
			} else {
				prod.setLowestRefundRate(borrow.getApr());
				prod.setHighestRefundRate(borrow.getApr());
			}
			// 投资期限
			prod.setTimeLimit(borrow.getBorrowTimeType() == 1 ? borrow
					.getTimeLimit() : borrow.getTimeLimit() * 30);
			// 起投金额
			prod.setLowestAccount(borrow.getLowestAccount());
		}
		return prod;
	}

	public static ProductBasic transPpfund(Ppfund ppfund) {
		ProductBasic prod = new ProductBasic();
		if (null != ppfund) {
			setProductBasicDefaultValue(prod);

			prod.setProductName(ppfund.getName());
			prod.setRelatedId(ppfund.getId());
			prod.setStatus(ppfund.getStatus());

			// 收益率
			prod.setLowestRefundRate(ppfund.getApr());
			prod.setHighestRefundRate(ppfund.getApr());
			// 投资期限
			prod.setTimeLimit(ppfund.getTimeLimit());
			// 起投金额
			prod.setLowestAccount(ppfund.getLowestAccount());
		}
		return prod;
	}

	public static ProductBasic transCrowdfunding(ProjectBaseinfo baseinfo) {
		ProductBasic prod = new ProductBasic();
		if (null != baseinfo) {
			setProductBasicDefaultValue(prod);

			prod.setProductName(baseinfo.getProjectName());
			prod.setRelatedId(Long.valueOf(baseinfo.getId()));
			prod.setProductType(new ProductType(
					Long.valueOf(baseinfo.getType())));
			prod.setStatus(baseinfo.getStatus());

			// 收益率
			prod.setLowestRefundRate(0.0);
			prod.setHighestRefundRate(0.0);
		}
		return prod;
	}

	public static ProductBasic transSetModel(ProductBasicModel setModel) {
		ProductBasic prod = new ProductBasic();
		if (null != setModel) {
			setProductBasicDefaultValue(prod);

			prod.setProductName(setModel.getProductName());
			prod.setProductTypeFlag(new ProductTypeFlag(setModel.getFlagId()));
			prod.setProductType(new ProductType(setModel.getTypeId()));
			prod.setStatus(setModel.getStatus());

			// 收益率
			prod.setLowestRefundRate(setModel.getLowestRefundRate());
			prod.setHighestRefundRate(setModel.getHighestRefundRate());
		}
		return prod;
	}

	public static void transSetModel(ProductBasic prod, ProductBasicModel model) {
		prod.setProductName(model.getProductName());
		prod.setProductTypeFlag(new ProductTypeFlag(model.getFlagId()));
		prod.setProductType(new ProductType(model.getTypeId()));
		// 收益率
		prod.setLowestRefundRate(model.getLowestRefundRate());
		prod.setHighestRefundRate(model.getHighestRefundRate());
	}

	private static void setProductBasicDefaultValue(ProductBasic prod) {
		// 默认不显示
		prod.setShowForPc(0);
		prod.setShowForWechat(0);
		prod.setShowForAndroid(0);
		prod.setShowForIos(0);
		prod.setShowForWinphone(0);
		prod.setShowForMobile(0);
		// 默认不推荐微信首页
		prod.setIsRecommend(0);
		// 默认不推荐热门产品
		prod.setHotProduct(0);
		// 投资期限
		prod.setTimeLimit(0);
		// 起投金额
		prod.setLowestAccount(0.0);
		// 重置推荐时间
		prod.setShowOrder(1);
		prod.setRecommendTime(new Date());
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public PpfundModel getExperienceModel() {
		return experienceModel;
	}

	public void setExperienceModel(PpfundModel experienceModel) {
		this.experienceModel = experienceModel;
	}

	public PpfundModel getPpfundModel() {
		return ppfundModel;
	}

	public void setPpfundModel(PpfundModel ppfundModel) {
		this.ppfundModel = ppfundModel;
	}

	public BorrowModel getBorrowModel() {
		return borrowModel;
	}

	public void setBorrowModel(BorrowModel borrowModel) {
		this.borrowModel = borrowModel;
	}

	public ProjectBaseinfoModel getProjectBaseinfoModel() {
		return projectBaseinfoModel;
	}

	public void setProjectBaseinfoModel(
			ProjectBaseinfoModel projectBaseinfoModel) {
		this.projectBaseinfoModel = projectBaseinfoModel;
	}

	public Collection<ProductBasicModel> getSubProductList() {
		return subProductList;
	}

	public void setSubProductList(Collection<ProductBasicModel> subProductList) {
		this.subProductList = subProductList;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getProductsSelected() {
		return productsSelected;
	}

	public void setProductsSelected(String productsSelected) {
		this.productsSelected = productsSelected;
	}

	public String getLightspot() {
		return lightspot;
	}

	public void setLightspot(String lightspot) {
		this.lightspot = lightspot;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	public Long getFlagId() {
		return flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	public String getFlagDesc() {
		return flagDesc;
	}

	public void setFlagDesc(String flagDesc) {
		this.flagDesc = flagDesc;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Double getScales() {
		return scales;
	}

	public void setScales(Double scales) {
		this.scales = scales;
	}

	public Double getTotalInvestMoney() {
		return totalInvestMoney;
	}

	public void setTotalInvestMoney(Double totalInvestMoney) {
		this.totalInvestMoney = totalInvestMoney;
	}

	public Double getTotalProfitMoney() {
		return totalProfitMoney;
	}

	public void setTotalProfitMoney(Double totalProfitMoney) {
		this.totalProfitMoney = totalProfitMoney;
	}

}
