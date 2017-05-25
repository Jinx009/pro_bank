package com.rongdu.p2psys.user.exception;

import com.rongdu.common.exception.BussinessException;

/**
 * 用户操作异常
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月4日
 */
public class UserException extends BussinessException {

	private static final long serialVersionUID = -7400559552805824955L;

	public UserException() {
		super();
	}

	public UserException(String message) {
		super(message);
	}

	public UserException(String message, int type) {
		super(message, type);
	}

	public UserException(String msg, RuntimeException ex) {
		super(msg, ex);
	}

	public UserException(String message, String url) {
		super(message, url);
	}

}
