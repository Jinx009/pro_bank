package com.rongdu.p2psys.account.exception;

import com.rongdu.common.exception.BussinessException;

public class AccountException extends BussinessException {

	private static final long serialVersionUID = -7400559552805824955L;

	public AccountException() {
		super();
	}

	public AccountException(String message) {
		super(message);
	}

	public AccountException(String message, int type) {
		super(message, type);
	}

	public AccountException(String msg, RuntimeException ex) {
		super(msg, ex);
	}

	public AccountException(String message, String url) {
		super(message, url);
	}

}
