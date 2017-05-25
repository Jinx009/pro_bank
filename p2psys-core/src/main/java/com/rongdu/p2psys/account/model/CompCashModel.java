package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.account.domain.CompCash;

/**
 * 对公付款 Model
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月30日
 */
public class CompCashModel extends CompCash {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 第三方处理成功
	 */
	public static final int TPP_SUCCESS = 1;
	/**
	 * 第三方处理失败
	 */
	public static final int TPP_FAIL = 2;
	/**
	 * 第三方处理中
	 */
	public static final int TPP_PROCESSING = 0;
	/**
	 * 平台审核通过
	 */
	public static final int WEB_SUCCESS = 1;
	/**
	 * 平台审核失败
	 */
	public static final int WEB_FAIL = 2;
	/**
	 * 平台待审核
	 */
	public static final int WEB_PROCESSING = 0;

	/** 当前页数 **/
	private int page;

	/** 每页总数 **/
	private int rows;

	/**
	 * 操作员姓名
	 */
	private String addOpName;

	/**
	 * 审核员姓名
	 */
	private String verifyOpName;
	
	/**
	 * 网站处理状态描述
	 */
	private String webStatusStr;
	/**
	 * 银联处理状态描述
	 */
	private String tppStatusStr;

	
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

	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	public static CompCashModel instance(CompCash cash) {
		CompCashModel model = new CompCashModel();
		BeanUtils.copyProperties(cash, model);
		return model;
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

	public String getAddOpName() {
		return addOpName;
	}

	public void setAddOpName(String addOpName) {
		this.addOpName = addOpName;
	}

	public String getVerifyOpName() {
		return verifyOpName;
	}

	public void setVerifyOpName(String verifyOpName) {
		this.verifyOpName = verifyOpName;
	}

	public String getWebStatusStr() {
		switch (getWebStatus()) {
		case WEB_SUCCESS:
			webStatusStr = "审核成功";
			break;
		case WEB_FAIL:
			webStatusStr = "审核失败";
			break;
		case WEB_PROCESSING:
			webStatusStr = "待审核";
		default:
			break;
		}
		return webStatusStr;
	}

	public void setWebStatusStr(String webStatusStr) {
		this.webStatusStr = webStatusStr;
	}

	public String getTppStatusStr() {
		switch (getTppStatus()) {
		case TPP_SUCCESS:
			tppStatusStr = "成功";
			break;
		case TPP_FAIL:
			tppStatusStr = "失败";
			break;
		case TPP_PROCESSING:
			tppStatusStr = "处理中";
		default:
			break;
		}
		return tppStatusStr;
	}

	public void setTppStatusStr(String tppStatusStr) {
		this.tppStatusStr = tppStatusStr;
	}

}
