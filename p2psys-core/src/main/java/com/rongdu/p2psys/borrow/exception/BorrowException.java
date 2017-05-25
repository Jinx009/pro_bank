package com.rongdu.p2psys.borrow.exception;

import com.rongdu.common.exception.BussinessException;

/**
 * 借贷异常
 */
public class BorrowException extends BussinessException {

	private static final long serialVersionUID = -7547893929820680608L;

	public BorrowException() {
		super();
	}

	public BorrowException(String message) {
		super(message);
	}

	public BorrowException(String message, String url) {
		super(message, url);
	}

	public BorrowException(String message, int type) {
		super(message, type);
	}

}
