package com.rongdu.p2psys.core.domain;

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
 * 红包详情 红包发放方式为浮动金额时详情
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月10日
 */
@Entity
@Table(name = ("s_red_packet_detail"))
public class RedPacketDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 红包
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "red_packet_id")
	private RedPacket redPacket;

	/**
	 * 浮动方式，1固定金额，2金额比例
	 */
	private int type;

	/**
	 * 最小值/下限
	 */
	private double min;

	/**
	 * 最大值/上限
	 */
	private double max;

	/**
	 * 固定区间金额
	 */
	private double money;

	/**
	 * 区间计算比率
	 */
	private double rate;
	
	/**
	 * 添加时间
	 */
	private Date addTime = new Date();
	
	/**
	 * 是否删除
	 */
	private int isDelete;

	public RedPacketDetail() {
		super();
	}

	public RedPacketDetail(RedPacket redPacket, int type, double min,
			double max, double money, double rate) {
		super();
		this.redPacket = redPacket;
		this.type = type;
		this.min = min;
		this.max = max;
		this.money = money;
		this.rate = rate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RedPacket getRedPacket() {
		return redPacket;
	}

	public void setRedPacket(RedPacket redPacket) {
		this.redPacket = redPacket;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

}
