package com.rongdu.p2psys.account.model.payment.xspay;

public enum CharsetTypeEnum {
	UTF8(1, "UTF-8");

	private final int code;
	private final String description;

	private CharsetTypeEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public static CharsetTypeEnum getByCode(int code) {
		for (CharsetTypeEnum status : values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return null;
	}
}
