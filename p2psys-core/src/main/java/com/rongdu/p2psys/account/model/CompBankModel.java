package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.account.domain.CompBank;

/**
 * 对公付款银行卡 Model
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月3日
 */
public class CompBankModel extends CompBank {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;

	/** 当前页数 **/
	private int page;

	/** 每页总数 **/
	private int rows;

	/**
	 * 操作员姓名
	 */
	private String opName;

	public static CompBankModel instance(CompBank bank) {
		CompBankModel model = new CompBankModel();
		BeanUtils.copyProperties(bank, model);
		return model;
	}
	
	public CompBank prototype() {
		CompBank bank = new CompBank();
		BeanUtils.copyProperties(this, bank);
		return bank;
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

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

}
