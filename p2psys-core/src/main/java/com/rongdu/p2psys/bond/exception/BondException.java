package com.rongdu.p2psys.bond.exception;

import com.rongdu.common.exception.BussinessException;

/**
 * 债权转让异常
 * 
 * @author wzh
 * @version 2.0
 * @since 2014年12月15日
 */
public class BondException extends BussinessException {

	private static final long serialVersionUID = -7400559552805824955L;

	public BondException() {
		super();
	}

	public BondException(String message) {
		super(message);
	}

	public BondException(String message, String url) {
		super(message, url);
	}

	public BondException(String message, int type) {
		super(message, type);
	}

}
