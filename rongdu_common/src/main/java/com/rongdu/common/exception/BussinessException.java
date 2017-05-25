package com.rongdu.common.exception;

public class BussinessException extends RuntimeException {
	private static final long serialVersionUID = 538922474277376456L;

	public static final int TYPE_JSON = 1;
	private String url;
	protected int type;

	public BussinessException(String msg, RuntimeException ex) {
		super(msg, ex);
	}

	public BussinessException() {
		super();
	}

	public BussinessException(String message) {
		super(message);
	}

	public BussinessException(String message, String url) {
		super(message);
		this.url = url;
	}

	public BussinessException(String message, int type) {
		super(message);
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
