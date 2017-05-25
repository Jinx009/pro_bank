package com.rongdu.p2psys.score.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "rd_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = -0L;
    /** 0未审核 */
    public static final byte WAIT_AUDIT = 0; 
    /** 1审核通过 */
    public static final byte PASS_AUDIT = 1;
    /** -1审核不通过 */
    public static final byte NOT_PASS_AUDIT = -1;
    
    /** 0待上架 */
    public static final byte WAIT_SHELVES = 0; 
    /** 1上架 */
    public static final byte UP_SHELVES = 1;
    /** -1下架 */
    public static final byte DOWN_SHELVES = -1;
    
	//商品id 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//商品名称 
	private String name;
	
	//状态：0待审核，1审核通过，-1审核未通过 
	private byte status;
	
	//类型：0待上架，1上架，-1下架
	private byte type;
	
	//所需积分 
	private int score;
	
	//商品库存
	private int store;
	
	//商品库存冻结 
	private int freezeStore;
	
	//销售数量
	private int sellAcount;
	
	//上下架时间 
	private Date shelvesTime;
	
	//添加时间 
	private Date addTime;
	
	//修改时间 
	private Date updateTime;
	
	//商品类别 
	@OneToOne
    @JoinColumn(name = "category_id")
	private GoodsCategory goodsCategory;
	
	//商品详情 
	private String description;
	
	//商城售价 
	private double cost;
	
	//市场价 
	private double marketPrice;
	
	//商品属性信息 
	private String attribute;
	
	//添加者 
	private long addOparetorId;
	
	//备注 
	private String remark;
	
	
	
	public Goods() {
        super();
        // TODO Auto-generated constructor stub
    }

	public Goods(long id) {
        super();
        // TODO Auto-generated constructor stub
        this.id = id;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getStore() {
		return store;
	}

	public void setStore(int store) {
		this.store = store;
	}

	public Date getShelvesTime() {
		return shelvesTime;
	}

	public void setShelvesTime(Date shelvesTime) {
		this.shelvesTime = shelvesTime;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public GoodsCategory getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(GoodsCategory goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public int getSellAcount() {
		return sellAcount;
	}

	public void setSellAcount(int sellAcount) {
		this.sellAcount = sellAcount;
	}

	public long getAddOparetorId() {
        return addOparetorId;
    }

    public void setAddOparetorId(long addOparetorId) {
        this.addOparetorId = addOparetorId;
    }

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getFreezeStore() {
		return freezeStore;
	}

	public void setFreezeStore(int freezeStore) {
		this.freezeStore = freezeStore;
	}

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
