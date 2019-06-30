package com.tz.redismanager.util;

public class RsaException extends Exception {

	private static final long serialVersionUID = -238091758285157331L;

	private String errCode;
	private String errMsg;

	public RsaException() {
		super();
	}

	public RsaException(String message, Throwable cause) {
		super(message, cause);
	}

	public RsaException(String message) {
		super(message);
	}

	public RsaException(Throwable cause) {
		super(cause);
	}

	public RsaException(String errCode, String errMsg) {
		super(errCode + ":" + errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public String getErrCode() {
		return this.errCode;
	}

	public String getErrMsg() {
		return this.errMsg;
	}

}