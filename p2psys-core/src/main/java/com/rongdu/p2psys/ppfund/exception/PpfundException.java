package com.rongdu.p2psys.ppfund.exception;

import com.rongdu.common.exception.BussinessException;

public class PpfundException extends BussinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PpfundException() {
		super();
	}

	public PpfundException(String message) {
		super(message);
	}

	public PpfundException(String message, String url) {
		super(message, url);
	}

	public PpfundException(String message, int type) {
		super(message, type);
	}
}
