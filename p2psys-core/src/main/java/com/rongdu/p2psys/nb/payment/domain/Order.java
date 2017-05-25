package com.rongdu.p2psys.nb.payment.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.user.domain.User;

/**
 * 商户订单信息
 * 
 * @author cgw
 * @version 1.0
 * @since 2015-07-06
 */
@Entity
@Table(name = "nb_order")
public class Order implements Serializable {
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	/**
	 * 商品ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductBasic pb;

	private String no_order;            	// 商户唯一订单号
	private String oid_paybill;          	// 连连钱包支付单号
    private String dt_order;             	// 商户订单时间
    private String name_goods;         // 商品名称
    private String info_order;           	// 订单描述
    private String money_order;         // 交易金额 单位为RMB-元
    
//    private String code_order;			// 返回的状态码
//    private String desc_order;				// 返回的结果信息
    /**
	 * 0：待付款，1：成功，2：失败，3：处理中
	 */
	private int status;

	public Order() {
		super();
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

	public ProductBasic getPb() {
		return pb;
	}

	public void setPb(ProductBasic pb) {
		this.pb = pb;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getOid_paybill() {
		return oid_paybill;
	}

	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
