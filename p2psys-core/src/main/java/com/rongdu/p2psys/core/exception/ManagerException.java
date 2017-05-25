package com.rongdu.p2psys.core.exception;

import com.rongdu.common.exception.BussinessException;

/**
 * 用户操作异常
 */
public class ManagerException extends BussinessException {

	private static final long serialVersionUID = 8381141496629624999L;

	public ManagerException() {
		super();
	}

	public ManagerException(String message) {
		super(message);
	}

	public ManagerException(String message, int type) {
		super(message, type);
	}

	public ManagerException(String msg, RuntimeException ex) {
		super(msg, ex);
	}

	public ManagerException(String message, String url) {
		super(message, url);
	}

}
