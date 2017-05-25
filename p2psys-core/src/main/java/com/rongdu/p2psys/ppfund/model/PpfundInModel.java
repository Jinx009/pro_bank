package com.rongdu.p2psys.ppfund.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.Page;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.exception.PpfundException;

/**
 * PPfund资金管理产品 Model
 */
public class PpfundInModel extends PpfundIn {

	private static final long serialVersionUID = -2269677053968719038L;
	/**
	 * 当前页数
	 **/
	private int page;
	/**
	 * 每页总数
	 **/
	private int rows = Page.ROWS;

	/**
	 * 产品基本ID
	 **/
	private Long productBasicId;

	/**
	 * 昨日收益
	 **/
	private Double lastInterest;

	/**
	 * 用户名
	 **/
	private String userName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 产品ID
	 **/
	private Long ppfundId;

	/**
	 * 产品名称
	 **/
	private String ppfundName;

	/**
	 * 产品年化
	 **/
	private Double ppfundApr;

	/**
	 * 固定期限
	 **/
	private Integer isFixedTerm;

	/**
	 * 搜索条件（用户名/真实姓名）
	 */
	private String searchName;

	/**
	 * 产品编码
	 */
	private String pidNo;

	/**
	 * 红包id
	 */
	private Long[] ids;
	
	/**
	 * 加息券id
	 */
	private Long[] cids;

	/**
	 * 开始时间
	 */
	private String startTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	private String outStr;
	
	/**
	 * 体验标
	 */
	private String typeCode;
	
	/**
	 * 计息金额（体验标）
	 */
	private Double interestMoney;
	

	/**
	 * 开始时间
	 */
	private String startOutTime;

	/**
	 * 结束时间
	 */
	private String endOutTime;
	
	public String getStartOutTime() {
		return startOutTime;
	}

	public void setStartOutTime(String startOutTime) {
		this.startOutTime = startOutTime;
	}

	public String getEndOutTime() {
		return endOutTime;
	}

	public void setEndOutTime(String endOutTime) {
		this.endOutTime = endOutTime;
	}

	public static PpfundInModel instance(PpfundIn in) {
		PpfundInModel model = new PpfundInModel();
		BeanUtils.copyProperties(in, model);
		return model;
	}

	/**
	 * 检测数据
	 * 
	 * @param ppfund
	 */
	public void checkInModel(Ppfund ppfund,String typeName) {
		if (ppfund.getStatus() != 1) {	
			throw new PpfundException("请求非法", 2);
		}
		if(!ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE
				.equals(typeName)){			
			if (this.getMoney() <= 0) {
				throw new PpfundException("购买金额有误，请检查后再投资", 2);
			}
			if (this.getMoney() < ppfund.getLowestAccount()
					&& BigDecimalUtil.sub(ppfund.getAccount(),
							ppfund.getAccountYes()) > ppfund.getLowestAccount()) {
				throw new PpfundException("购买金额不能低于最低限额", 2);
			}
			if (ppfund.getMostAccount() > 0
					&& this.getMoney() > ppfund.getMostAccount()) {
				throw new PpfundException("购买金额不能高于最高限额", 2);
			}
		}
		if (this.getUser().getUserCache().getStatus() == 1) {
			throw new PpfundException("对不起您的帐号已被锁定，无法进行投资", 2);
		}
		if (ppfund.getAccount() != 0
				&& (ppfund.getAccountYes() == ppfund.getAccount())) {
			throw new PpfundException("对不起，该产品已满", 2);
		}
		if (this.getOutTime() != null) {
			// 计算间隔天数
			int days = DateUtil.daysBetween(new Date(), this.getOutTime());
			if (ppfund.getCycle() != 0
					&& (days == 0 || days % ppfund.getCycle() != 0)) {
				throw new BussinessException("转出时间选择不正确", 2);
			}
		}
		String startTimeStr = DateUtil.dateStr2(new Date()) + " "
				+ ppfund.getStartTime() + ":00";
		String endTimeStr = DateUtil.dateStr2(new Date()) + " "
				+ ppfund.getEndTime() + ":00";
		long startTime = DateUtil.getTime(startTimeStr);
		long endTime = DateUtil.getTime(endTimeStr);
		long nowTime = DateUtil.getNowTime();
		if ((nowTime < startTime) || (nowTime > endTime)) {
			throw new PpfundException("该时间段不允许购买", 2);
		}
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

	public Long getProductBasicId() {
		return productBasicId;
	}

	public void setProductBasicId(Long productBasicId) {
		this.productBasicId = productBasicId;
	}

	public Double getLastInterest() {
		return lastInterest;
	}

	public void setLastInterest(Double lastInterest) {
		this.lastInterest = lastInterest;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getPpfundId() {
		return ppfundId;
	}

	public void setPpfundId(Long ppfundId) {
		this.ppfundId = ppfundId;
	}

	public String getPpfundName() {
		return ppfundName;
	}

	public void setPpfundName(String ppfundName) {
		this.ppfundName = ppfundName;
	}

	public Double getPpfundApr() {
		return ppfundApr;
	}

	public void setPpfundApr(Double ppfundApr) {
		this.ppfundApr = ppfundApr;
	}

	public Integer getIsFixedTerm() {
		return isFixedTerm;
	}

	public void setIsFixedTerm(Integer isFixedTerm) {
		this.isFixedTerm = isFixedTerm;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getPidNo() {
		return pidNo;
	}

	public void setPidNo(String pidNo) {
		this.pidNo = pidNo;
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public Long[] getCids() {
		return cids;
	}

	public void setCids(Long[] cids) {
		this.cids = cids;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOutStr() {
		if (this.getIsOut() == 1) {
			this.outStr = "已转出";
		} else {
			this.outStr = "未转出";
		}
		return outStr;
	}

	public void setOutStr(String outStr) {
		this.outStr = outStr;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Double getInterestMoney() {
		return interestMoney;
	}

	public void setInterestMoney(Double interestMoney) {
		this.interestMoney = interestMoney;
	}
}
