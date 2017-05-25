package com.rongdu.p2psys.tpp.ips.model;


import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.user.exception.UserException;

public class TppIpsPayModel extends TppIpsPay {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page = 1;

	/** 每页数据条数 */
	private int size = Page.ROWS;

	
	/** 开始时间 **/
	private String searchStartTime;
	/** 结束时间 **/
	private String searchEndTime;
	/**日期范围：0：全部，1：最近七天 2：最近一个月  3：最近两个月，4 最近三个月**/
	private int time;
	
	

	public static TppIpsPayModel instance(TppIpsPay tppIpsPay) {
		TppIpsPayModel tppIpsPayModel = new TppIpsPayModel();
		BeanUtils.copyProperties(tppIpsPay, tppIpsPayModel);
		return tppIpsPayModel;
	}

	public Borrow prototype() {
		Borrow borrow = new Borrow();
		BeanUtils.copyProperties(this, borrow);
		return borrow;
	}

	public static TppIpsPayModel instanceCurr(Borrow borrow, TppIpsPayModel borrowModel) {
		BeanUtils.copyProperties(borrow, borrowModel);
		return borrowModel;
	}

	/**
	 * 发标校验验证码
	 * 
	 * @param imgCode
	 */
	public void validBorrowCode(String imgCode) {
		if (!ValidateUtil.checkValidCode(imgCode)) {
			throw new UserException("验证码不正确！", 1);
		}
	}
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size > 0 ? size : Page.ROWS;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public String getSearchStartTime() {
        return searchStartTime;
    }

    public void setSearchStartTime(String searchStartTime) {
        this.searchStartTime = searchStartTime;
    }

    public String getSearchEndTime() {
        return searchEndTime;
    }

    public void setSearchEndTime(String searchEndTime) {
        this.searchEndTime = searchEndTime;
    }

    public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

	

}
