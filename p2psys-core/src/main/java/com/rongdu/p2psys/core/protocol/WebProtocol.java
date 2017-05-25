package com.rongdu.p2psys.core.protocol;

import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.dao.OperatorDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 平台下载协议实现类
 * 
 * @author qj
 * @version 2.0
 * @since 2014-05-23
 */
public class WebProtocol extends AbstractProtocolBean {

	protected OperatorDao operatorDao;

	@Override
	public void prepare() {
		operatorDao = (OperatorDao) BeanUtil.getBean("operatorDao");
	}

	@Override
	public void validDownload() {
		// 先调用父类基础校验
		super.validDownload();
		Operator oper = operatorDao.find(userId);
		if (oper == null) {
			throw new BorrowException("操作异常！");
		}
	}

}
