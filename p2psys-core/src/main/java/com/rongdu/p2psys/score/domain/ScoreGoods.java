package com.rongdu.p2psys.score.domain;

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

@Entity
@Table(name = "rd_score_goods")
public class ScoreGoods implements Serializable {
	
	private static final long serialVersionUID = -0L;
	
	/** 0未审核 */
    public static final byte WAIT_AUDIT = 0; 
    /** 1审核通过 */
    public static final byte PASS_AUDIT = 1;
    /** -1审核不通过 */
    public static final byte NOT_PASS_AUDIT = -1;
    /** 2已发货 */
    public static final byte IS_DELIVERY = 2;
    /** 3已收货 */
    public static final byte IS_RECEIVE = 3;
    
	//主键ID 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//会员ID 
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	private User user;
	
	//商品ID  
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
	private Goods goods;
	
	//积分兑换份额 
	private int convertNum;
	
	//兑换的积分数值 
	private int score;
	
	//实际的兑换金额 
	private double money;
	
	//状态:0待审核,1审核通过,-1审核未通过,2已发货,3已收货
	private byte status;
	
	//发货时间 
	private Date deliverTime;
	
	//发货人 
	private String deliverUser;
	
	//收货人姓名
	private String receiveUserName;
	
	//收货地址 
	private String receiveAddress;
	
	//收货人电话
	private String receivePhone;
	
	//收货时间 
	private Date receiveTime;
	
	//快递名称 
	private String courierName;
	
	//快递单号 
	private String courierNum;
	
	//添加时间 
	private Date addTime;
	
	//审核时间 
	private Date verifyTime;
	
	//审核人 
	private String verifyUser;
	
	//审核人ID 
	private long verifyUserId;
	
	//审核备注 
	private String verifyRemark;
	
	//备注 
	private String remark;

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

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getConvertNum() {
		return convertNum;
	}

	public void setConvertNum(int convertNum) {
		this.convertNum = convertNum;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public String getDeliverUser() {
		return deliverUser;
	}

	public void setDeliverUser(String deliverUser) {
		this.deliverUser = deliverUser;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierNum() {
		return courierNum;
	}

	public void setCourierNum(String courierNum) {
		this.courierNum = courierNum;
	}

	public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public long getVerifyUserId() {
		return verifyUserId;
	}

	public void setVerifyUserId(long verifyUserId) {
		this.verifyUserId = verifyUserId;
	}

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
