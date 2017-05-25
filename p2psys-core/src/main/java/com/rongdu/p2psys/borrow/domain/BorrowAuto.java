package com.rongdu.p2psys.borrow.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.user.domain.User;

/**
 * 自动投标
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_borrow_auto")
public class BorrowAuto implements Serializable {
	
	/**
	 * 余额全投
	 */
	public static final int BALANCE_FULL = 1;
	/**
	 * 固定金额
	 */
	public static final int FIXED_ACCOUNT = 2;
	/**
	 * 金额区间
	 */
	public static final int ACCOUNT_INTERVAL = 3;
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
	 * 用户ID
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 是否启用，0不启用，1启用
	 */
	private boolean enable;
	/**
	 * 固定投标金额
	 */
	private double money;
	/**
	 * 金额区间最小值
	 */
	private double min;
	/**
	 * 金额区间最大值
	 */
	private double max;
	/**
	 * 年利率下限
	 */
	private double aprDown;
	/**
	 * 月标借款期限上限
	 */
	private int timelimitUp;
	/**
	 * 月标借款期限下限
	 */
	private int timelimitDown;
	/**
	 * 还款方式
	 */
	private String style;
	/**
	 * 自动投资方式 1:余额全投 2:固定金额 3:金额区间
	 */
	private int tenderStyle;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加ip
	 */
	private String addIp;

	public BorrowAuto() {
		super();
	}

	public BorrowAuto(User user) {
		super();
		this.user = user;
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取是否启用，0不启用，1启用
	 * 
	 * @return 是否启用，0不启用，1启用
	 */
	public boolean getEnable() {
		return enable;
	}

	/**
	 * 设置是否启用，0不启用，1启用
	 * 
	 * @param enable 要设置的是否启用，0不启用，1启用
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * 获取投标金额
	 * 
	 * @return 投标金额
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * 设置投标金额
	 * 
	 * @param money 要设置的投标金额
	 */
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * 获取年利率下限
	 * 
	 * @return 年利率下限
	 */
	public double getAprDown() {
		return aprDown;
	}

	/**
	 * 设置年利率下限
	 * 
	 * @param aprDown 要设置的年利率下限
	 */
	public void setAprDown(double aprDown) {
		this.aprDown = aprDown;
	}

	/**
	 * 获取借款期限上限
	 * 
	 * @param timelimitDayUp 要设置的借款期限上限
	 */
	public int getTimelimitUp() {
		return timelimitUp;
	}
	/**
	 * 设置借款期限上限
	 * 
	 * @param timelimitDayUp 要设置的借款期限上限
	 */
	public void setTimelimitUp(int timelimitUp) {
		this.timelimitUp = timelimitUp;
	}

	/**
	 * 获取借款期限下限
	 * 
	 * @return 借款期限下限
	 */
	public int getTimelimitDown() {
		return timelimitDown;
	}

	/**
	 * 设置借款期限下限
	 * 
	 * @param timelimitDown 要设置的借款期限下限
	 */
	public void setTimelimitDown(int timelimitDown) {
		this.timelimitDown = timelimitDown;
	}

	/**
	 * 获取还款方式
	 * 
	 * @return 还款方式
	 */
	public String getStyle() {
		return style;
	}
	/**
	 * 设置还款方式
	 * 
	 * @param style 要设置的还款方式
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	/**
	 * 获取自动投标方式
	 * 
	 * @return 自动投标方式
	 */
	public int getTenderStyle() {
		return tenderStyle;
	}
	/**
	 * 设置自动投标方式
	 */
	public void setTenderStyle(int tenderStyle) {
		this.tenderStyle = tenderStyle;
	}


	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取添加ip
	 * 
	 * @return 添加ip
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加ip
	 * 
	 * @param addIp 要设置的添加ip
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}
}
