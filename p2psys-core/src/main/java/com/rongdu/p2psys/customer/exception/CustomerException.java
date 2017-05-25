package com.rongdu.p2psys.customer.exception;

import com.rongdu.common.exception.BussinessException;

public class CustomerException extends BussinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerException() {
		super();
	}

	public CustomerException(String message) {
		super(message);
	}

	public CustomerException(String message, String url) {
		super(message, url);
	}

	public CustomerException(String message, int type) {
		super(message, type);
	}
}
