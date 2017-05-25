package com.rongdu.p2psys.nb.payment.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;

/**
 * 支持的银行信息model
 */
public class SupportBankModel extends NbSupportBank {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows = Page.ROWS;
	
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

	public static SupportBankModel instance(NbSupportBank sb) {
		SupportBankModel sbModel = new SupportBankModel();
		BeanUtils.copyProperties(sb, sbModel);
		return sbModel;
	}
	
	public static NbSupportBank instance(SupportBankModel sbModel) {
		NbSupportBank sb = new NbSupportBank();
		BeanUtils.copyProperties(sbModel, sb);
		return sb;
	}
}
