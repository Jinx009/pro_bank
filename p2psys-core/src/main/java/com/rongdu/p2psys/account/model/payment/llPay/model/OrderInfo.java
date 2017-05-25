package com.rongdu.p2psys.account.model.payment.llPay.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.payment.domain.Order;

/**
* 商户订单信息
* @author cgw
* @date:2015-06-20
* @version :1.0
*
*/
public class OrderInfo  extends  Order{
    private static final long serialVersionUID = 1L;
    
    /** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows = Page.ROWS;
    
    private String            no_order;             // 商户唯一订单号
    private String 			  oid_paybill;          // 连连钱包支付单号
    private String            dt_order;             // 商户订单时间
    private String            name_goods;           // 商品名称
    private String            info_order;           // 订单描述
    private String            money_order;          // 交易金额 单位为RMB-元
    
    /**
	 * 用户真实姓名
	 */
	private String realName;
	
	/**
	 * 0：待付款，1：成功，2：失败，3：处理中
	 */
	private int status;


    public String getNo_order()
    {
        return no_order;
    }

    public void setNo_order(String no_order)
    {
        this.no_order = no_order;
    }

    public String getOid_paybill() {
		return oid_paybill;
	}

	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}

	public String getDt_order()
    {
        return dt_order;
    }

    public void setDt_order(String dt_order)
    {
        this.dt_order = dt_order;
    }

    public String getName_goods()
    {
        return name_goods;
    }

    public void setName_goods(String name_goods)
    {
        this.name_goods = name_goods;
    }

    public String getInfo_order()
    {
        return info_order;
    }

    public void setInfo_order(String info_order)
    {
        this.info_order = info_order;
    }

    public String getMoney_order()
    {
        return money_order;
    }

    public void setMoney_order(String money_order)
    {
        this.money_order = money_order;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public static OrderInfo instance(Order order) {
		OrderInfo oi = new OrderInfo();
		BeanUtils.copyProperties(order, oi);
		return oi;
	}
	public static Order instance(OrderInfo oi) {
		Order order = new Order();
		BeanUtils.copyProperties(oi, order);
		return order;
	}
}
