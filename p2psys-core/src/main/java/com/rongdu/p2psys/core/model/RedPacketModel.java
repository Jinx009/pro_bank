package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 红包model
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月10日
 */
public class RedPacketModel extends RedPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows;

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
	
	public static RedPacketModel instance(RedPacket redPacket) {
		RedPacketModel redPacketModel = new RedPacketModel();
		BeanUtils.copyProperties(redPacket, redPacketModel);
		return redPacketModel;
	}

	public RedPacket prototype() {
		RedPacket redPacket = new RedPacket();
		BeanUtils.copyProperties(this, redPacket);
		return redPacket;
	}
	
	public void checkModel() {
		if (StringUtil.isBlank(this.getName())) {
			throw new UserException("红包名称不能为空", 1);
		}
		if (this.getDay() == 0) {
			throw new UserException("请输入红包有效天数", 1);
		}
		if (this.getPaymentType() == 0) {
			throw new UserException("请选择发放方式", 1);
		}
		if (this.getType() == 0) {
			throw new UserException("请选择红包类型", 1);
		}
		if (this.getPaymentType() == 1 && this.getMoney() <= 0) {
			throw new UserException("请输入红包发放金额", 1);
		}
		if (this.getPaymentType() == 2 && this.getRate() == 0) {
			throw new UserException("请输入红包兑换比率", 1);
		}
		if (this.getPaymentType() == 3 && this.getFloatType() == 0) {
			throw new UserException("请选择浮动方式", 1);
		}
	}
}
