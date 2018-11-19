package com.emcoo.ef.common.http;

/**
 * Field Error Resource
 *
 * @author mark
 */
public class FieldErrorResource {
	private String resource;
	private String field;
	private String code;
	private String message;

	public FieldErrorResource() {

	}

	public FieldErrorResource(String resource, String field, String code, String message) {
		this.resource = resource;
		this.field = field;
		this.code = code;
		this.message = message;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
