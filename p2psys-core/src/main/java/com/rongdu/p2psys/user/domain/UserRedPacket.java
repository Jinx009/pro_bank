package com.rongdu.p2psys.user.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;

/**
 * 红包实体类
 * @author zf
 * @version 2.0
 * @since 2014-10-20
 */
@Entity
@Table(name = "rd_user_red_packet")
public class UserRedPacket {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 投标ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tender_id")
	private BorrowTender tender;
	/**
	 * 债权投资ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bond_tender_id")
	private BondTender bondTender;
	
	/**
	 * ppfund投资ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ppfund_in_id")
	private PpfundIn ppfundIn;
	/**
	 * 赠送类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private RedPacket type;
	
	/**
	 * 红包类型，1为现金红包，2为虚拟红包
	 */
	private int redPacketType;
	/**
	 * 金额
	 */
	private double amount;
	/**
	 * 是否已使用
	 */
	private boolean isUsed;

	/**
	 * 是否已提醒
	 */
	private boolean isRemind;
	
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 过期时间
	 */
	private Date expiredTime;
	
	/**
	 * 兑换时间
	 */
	private Date usedTime;
	
	public UserRedPacket() {
		super();
	}
	
	public UserRedPacket(User user, BorrowTender tender,
			RedPacket type, double amount, Date expiredTime) {
		super();
		this.user = user;
		this.tender = tender;
		this.type = type;
		this.redPacketType = type.getType();
		this.amount = amount;
		this.expiredTime = expiredTime;
		this.addTime = new Date();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public int getRedPacketType() {
		return redPacketType;
	}

	public void setRedPacketType(int redPacketType) {
		this.redPacketType = redPacketType;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
	public boolean getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(boolean isRemind) {
		this.isRemind = isRemind;
	}

	public RedPacket getType() {
		return type;
	}

	public void setType(RedPacket type) {
		this.type = type;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}
	public BorrowTender getTender() {
		return tender;
	}
	public void setTender(BorrowTender tender) {
		this.tender = tender;
	}
	public BondTender getBondTender() {
		return bondTender;
	}
	public void setBondTender(BondTender bondTender) {
		this.bondTender = bondTender;
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public PpfundIn getPpfundIn() {
		return ppfundIn;
	}

	public void setPpfundIn(PpfundIn ppfundIn) {
		this.ppfundIn = ppfundIn;
	}
	
}
