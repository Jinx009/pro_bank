package com.rongdu.p2psys.borrow.protocol;

import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;

/**
 * 借款人下载协议实现类
 * 
 * @author qj
 * @version 2.0
 * @since 2014-05-22
 */
public class BorrowerProtocol extends AbstractProtocolBean {

	@Override
	public void validDownload() {
		// 先调用父类基础校验
		super.validDownload();
		boolean isDown = borrowService.isBorrowUser(borrowId, userId);
		if (!isDown) {
			if(borrow.getVouchFirm()!=null && borrow.getVouchFirm().getUserId()== userId){
				return;
			}
			throw new BorrowException("你不是借款人不能下载该协议！");
		}
	}

	@Override
	public void prepare() {
	}

}
