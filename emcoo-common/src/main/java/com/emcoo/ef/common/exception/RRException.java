package com.emcoo.ef.common.exception;

/**
 * Custom Exception
 *
 * @author mark
 */
public class RRException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误Key，用于唯一标识错误类型
	 */
	private Integer code = 500;

	public RRException() {
	}

	public RRException(String message) {
		super(message);
	}

	public RRException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public RRException(Throwable cause) {
		super(cause);
	}

	public RRException(String message, Throwable cause) {
		super(message, cause);
	}

	public RRException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
