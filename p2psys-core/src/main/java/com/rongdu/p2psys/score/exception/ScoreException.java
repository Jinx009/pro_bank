package com.rongdu.p2psys.score.exception;

import com.rongdu.common.exception.BussinessException;

/**
 * 积分自定义异常
 */
public class ScoreException extends BussinessException {

	private static final long serialVersionUID = -7400559552805824955L;

	public ScoreException() {
		super();
	}

	public ScoreException(String message) {
		super(message);
	}

	public ScoreException(String message, int type) {
		super(message, type);
	}

	public ScoreException(String msg, RuntimeException ex) {
		super(msg, ex);
	}

	public ScoreException(String message, String url) {
		super(message, url);
	}

}
