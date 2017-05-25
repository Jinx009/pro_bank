package com.rongdu.p2psys.core.exception;

import com.rongdu.common.exception.BussinessException;

/**
 * 信息处理异常
 */
public class MessageException extends BussinessException {

	private static final long serialVersionUID = 1307401022764603615L;

	public MessageException() {
		super();
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(String message, int type) {
		super(message, type);
	}

	public MessageException(String msg, RuntimeException ex) {
		super(msg, ex);
	}

	public MessageException(String message, String url) {
		super(message, url);
	}

}
