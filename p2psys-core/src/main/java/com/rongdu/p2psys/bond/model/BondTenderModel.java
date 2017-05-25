package com.rongdu.p2psys.bond.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.bond.domain.BondTender;

/**
 * 债权投资Model
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
public class BondTenderModel extends BondTender{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public static BondTenderModel instance(BondTender item) {
        BondTenderModel model = new BondTenderModel();
        BeanUtils.copyProperties(item, model);
        return model;
    }

    public BondTender prototype() {
        BondTender item = new BondTender();
        BeanUtils.copyProperties(this, item);
        return item;
    }
    
    /** 债权ID **/
    private long bondId;
    
    /**
     * 转出折扣率
     */
    private double bondApr;
    
    /** 用户名 **/
    private String userName;
    /**
     * 标名称
     */
    private String borrowName;
    
    /** 开始时间 **/
    private String startTime;
    
    /** 结束时间 **/
    private String endTime;
    
    /**
     * 标ID
     */
    private long borrowId;
    
    /** 当前页数 **/
    private int page = 1;
    
    /** 分页数 **/
    private int rows = Page.ROWS;
    
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

	public long getBondId() {
		return bondId;
	}

	public void setBondId(long bondId) {
		this.bondId = bondId;
	}

	public double getBondApr() {
		return bondApr;
	}

	public void setBondApr(double bondApr) {
		this.bondApr = bondApr;
	}

	public long getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(long borrowId) {
		this.borrowId = borrowId;
	}

}


