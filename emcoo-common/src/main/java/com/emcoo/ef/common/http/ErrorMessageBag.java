package com.emcoo.ef.common.http;

import java.util.List;

/**
 * Message Error Bag
 *
 * @author mark
 */
public class ErrorMessageBag {

	private static final long serialVersionUID = 1L;

	private Integer code;
	private String message;
	private List<FieldErrorResource> fieldErrors;

	public ErrorMessageBag() {
	}

	public ErrorMessageBag(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<FieldErrorResource> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}