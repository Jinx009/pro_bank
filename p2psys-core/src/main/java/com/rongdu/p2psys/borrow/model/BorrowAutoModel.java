package com.rongdu.p2psys.borrow.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.borrow.domain.BorrowAuto;

/**
 * 自动投标Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月21日
 */
public class BorrowAutoModel extends BorrowAuto {

	private static final long serialVersionUID = 1L;

	public static BorrowAutoModel instance(BorrowAuto auto) {
		BorrowAutoModel borrowAutoModel = new BorrowAutoModel();
		BeanUtils.copyProperties(auto, borrowAutoModel);
		return borrowAutoModel;
	}

	public BorrowAuto prototype() {
		BorrowAuto auto = new BorrowAuto();
		BeanUtils.copyProperties(this, auto);
		return auto;
	}

}
