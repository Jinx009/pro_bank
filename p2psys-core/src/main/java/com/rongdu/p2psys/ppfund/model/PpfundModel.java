package com.rongdu.p2psys.ppfund.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.exception.PpfundException;

public class PpfundModel extends Ppfund {

	private static final long serialVersionUID = 2628525202611272023L;

	/**
	 * 当前页数
	 **/
	private int page = 1;

	/**
	 * 每页总数
	 **/
	private int rows = Page.ROWS;

	/**
	 * 0:等待审核
	 */
	public static final int STATUS_WAITING_FOR_APPROVE = 0;

	/**
	 * 1:审核通过
	 */
	public static final int STATUS_APPROVED = 1;

	/**
	 * 2:审核不通过
	 */
	public static final int STATUS_FAIL = 2;

	/**
	 * 3:截标
	 */
	public static final int STATUS_CLOSE = 3;

	/**
	 * 99:无关状态
	 */
	public static final int STATUS_UNRELATED = 99;

	/**
	 * 备注
	 **/
	private String remark;

	/**
	 * PPfund头像
	 **/
	private String ppfundImg;

	/**
	 * 查询条件
	 **/
	private String searchName;

	/**
	 * 对应产品ID
	 */
	private long productId;

	/**
	 * 微信素材 - 头图
	 */
	private String wechatHeadPic;

	/**
	 * 微信素材 - 产品亮点
	 */
	private String wechatLightspot;

	/**
	 * 微信素材 - 小贴士
	 */
	private String wechatTips;

	/**
	 * 微信素材 - 产品摘要
	 */
	private String wechatProductDigest;

	/**
	 * 微信素材 - 产品详情
	 */
	private String wechatProductDetail;

	/**
	 * 微信素材 - 收益时间概要
	 */
	private String wechatRefundDigest;

	/**
	 * 微信素材 - 收益时间详情
	 */
	private String wechatRefundDetail;

	/**
	 * 微信素材 - 安全保障概要
	 */
	private String wechatSafeguardDigest;

	/**
	 * 微信素材 - 安全保障详情
	 */
	private String wechatSafeguardDetail;

	/**
	 * 产品类型
	 */
	private Long typeId;

	/**
	 * 产品标签
	 */
	private Long flagId;

	/**
	 * 状态描述
	 */
	private String statusStr;

	/**
	 * 类型描述
	 */
	private String typeStr;

	/**
	 * 投资期限描述
	 */
	private String timeLimitStr;

	/**
	 * 周期天数描述
	 */
	private String cycleStr;

	/**
	 * 加息百分比
	 */
	private Double interestRateValue;

	public static PpfundModel instance(Ppfund ppfund) {
		PpfundModel model = new PpfundModel();
		BeanUtils.copyProperties(ppfund, model);
		return model;
	}

	public Ppfund prototype() {
		Ppfund ppfund = new Ppfund();
		BeanUtils.copyProperties(this, ppfund);
		return ppfund;
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

	public String getPpfundImg() {
		return ppfundImg;
	}

	public void setPpfundImg(String ppfundImg) {
		this.ppfundImg = ppfundImg;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getWechatHeadPic() {
		return wechatHeadPic;
	}

	public void setWechatHeadPic(String wechatHeadPic) {
		this.wechatHeadPic = wechatHeadPic;
	}

	public String getWechatLightspot() {
		return wechatLightspot;
	}

	public void setWechatLightspot(String wechatLightspot) {
		this.wechatLightspot = wechatLightspot;
	}

	public String getWechatTips() {
		return wechatTips;
	}

	public void setWechatTips(String wechatTips) {
		this.wechatTips = wechatTips;
	}

	public String getWechatProductDigest() {
		return wechatProductDigest;
	}

	public void setWechatProductDigest(String wechatProductDigest) {
		this.wechatProductDigest = wechatProductDigest;
	}

	public String getWechatProductDetail() {
		return wechatProductDetail;
	}

	public void setWechatProductDetail(String wechatProductDetail) {
		this.wechatProductDetail = wechatProductDetail;
	}

	public String getWechatRefundDigest() {
		return wechatRefundDigest;
	}

	public void setWechatRefundDigest(String wechatRefundDigest) {
		this.wechatRefundDigest = wechatRefundDigest;
	}

	public String getWechatRefundDetail() {
		return wechatRefundDetail;
	}

	public void setWechatRefundDetail(String wechatRefundDetail) {
		this.wechatRefundDetail = wechatRefundDetail;
	}

	public String getWechatSafeguardDigest() {
		return wechatSafeguardDigest;
	}

	public void setWechatSafeguardDigest(String wechatSafeguardDigest) {
		this.wechatSafeguardDigest = wechatSafeguardDigest;
	}

	public String getWechatSafeguardDetail() {
		return wechatSafeguardDetail;
	}

	public void setWechatSafeguardDetail(String wechatSafeguardDetail) {
		this.wechatSafeguardDetail = wechatSafeguardDetail;
	}

	public Long getFlagId() {
		return flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getStatusStr() {
		switch (getStatus()) {
		case PpfundModel.STATUS_WAITING_FOR_APPROVE:
			statusStr = "等待审核";
			break;
		case PpfundModel.STATUS_APPROVED:
			statusStr = "审核通过";
			break;
		case PpfundModel.STATUS_FAIL:
			statusStr = "审核不通过";
			break;
		case PpfundModel.STATUS_CLOSE:
			statusStr = "关闭";
			break;
		case PpfundModel.STATUS_UNRELATED:
			statusStr = "无关状态";
			break;
		default:
			statusStr = "状态异常";
			break;
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getTypeStr() {
		typeStr = getProductType().getTypeName();
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getTimeLimitStr() {
		if (getTimeLimit() > 0) {
			timeLimitStr = getTimeLimit() + "天";
		} else {
			timeLimitStr = "不限";
		}
		return timeLimitStr;
	}

	public void setTimeLimitStr(String timeLimitStr) {
		this.timeLimitStr = timeLimitStr;
	}

	public void checkModel() {
		if (this.getIsFixedTerm() == 1 && this.getTimeLimit() <= 0) {
			throw new PpfundException("请输入借款期限", PpfundException.TYPE_JSON);
		}
		if (this.getApr() <= 0) {
			throw new PpfundException("请输入收益率", PpfundException.TYPE_JSON);
		}
		if (StringUtil.isBlank(this.getStartTime())
				|| StringUtil.isBlank(this.getEndTime())) {
			throw new PpfundException("请选择每日购买起止时间", PpfundException.TYPE_JSON);
		}
		if ((this.getLowestAccount() != 0 && this.getMostAccount() != 0)
				&& (this.getLowestAccount() > this.getMostAccount())) {
			throw new PpfundException("最高投资金额不允许小于最低投资金额",
					PpfundException.TYPE_JSON);
		}
	}

	public String getCycleStr() {
		if (getCycle() > 0) {
			cycleStr = getCycle() + "天";
		} else {
			cycleStr = "到期结束";
		}
		return cycleStr;
	}

	public void setCycleStr(String cycleStr) {
		this.cycleStr = cycleStr;
	}

	public Double getInterestRateValue() {
		return interestRateValue;
	}

	public void setInterestRateValue(Double interestRateValue) {
		this.interestRateValue = interestRateValue;
	}

}
