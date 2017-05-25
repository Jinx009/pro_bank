package com.rongdu.p2psys.borrow.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 借款抵押物表
 * @author sj
 * @version 2.0
 * @since 2014-08-21
 */
@Entity
@Table(name = "rd_borrow_mortgage")
public class BorrowMortgage implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 借款标ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borrow_id")
	private Borrow borrow;
	
	/**
     * 状态 1入库 2出库 
     * 3入库后出库 ：用于更新记录查询
     */
    private int status;
    /**
     * 状态 0初始入库 其它代表第几次更新
     */
    private int num;
	
	/**
	 * 车型
	 */
	private String carType;
	/**
	 * 车驾号
	 */
	private String carNo;
	
	/**
	 * 评估价
	 */
	private double assessPrice;
	
	/**
	 * 抵押价
	 */
	private double mortgagePrice;
	
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	/**
	 * 添加IP
	 */
	private String addIp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public double getAssessPrice() {
		return assessPrice;
	}

	public void setAssessPrice(double assessPrice) {
		this.assessPrice = assessPrice;
	}

	public double getMortgagePrice() {
		return mortgagePrice;
	}

	public void setMortgagePrice(double mortgagePrice) {
		this.mortgagePrice = mortgagePrice;
	}

	public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
}
