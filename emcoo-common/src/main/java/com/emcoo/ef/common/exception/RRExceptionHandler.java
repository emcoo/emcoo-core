package com.emcoo.ef.common.exception;

import com.emcoo.ef.common.http.ErrorMessageBag;
import com.emcoo.ef.common.http.FieldErrorResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Global Exception Handler
 *
 * @author mark
 */
@Validated
@RestControllerAdvice
public class RRExceptionHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(RRExceptionHandler.class);

	@ExceptionHandler(RRException.class)
	public ResponseEntity<ErrorMessageBag> handleRRException(RRException e) {
		LOGGER.error("自订异常失败", e);

		ErrorMessageBag errorResponse = new ErrorMessageBag();
		errorResponse.setCode(e.getCode());
		errorResponse.setMessage(e.getMessage());

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorMessageBag> handleNullPointerException(NullPointerException e) {
		LOGGER.error("无效键值异常失败", e);

		ErrorMessageBag errorResponse = new ErrorMessageBag();
		errorResponse.setCode(400);
		errorResponse.setMessage("確保所有字段不為空!");

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorMessageBag> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		LOGGER.error("没有加载body参数 或 没有传入格式失败", e);
		ErrorMessageBag errorResponse = new ErrorMessageBag();
		errorResponse.setCode(400);
		errorResponse.setMessage("请正确传入参数!");

		return ResponseEntity.badRequest().body(errorResponse);
	}

	/**
	 * 全局异常
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessageBag> handleException(Exception e) {
		LOGGER.error("全局异常请求失败", e);
		ErrorMessageBag errorResponse = new ErrorMessageBag();
		errorResponse.setMessage(e.getMessage());

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessageBag> handleResourceNotFoundException(ResourceNotFoundException e) {

		ErrorMessageBag errorResponse = new ErrorMessageBag();
		errorResponse.setCode(400);
		errorResponse.setMessage(e.getMessage());

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity handleNotFoundException(NotFoundException e) {
		return new ResponseEntity(HttpStatus.NOT_FOUND);
	}

	/**
	 * Controller 裡標注 @RequestBody 的變數在 validate fail 時會丟出 MethodArgumentNotValidException。這個 method
	 * * 專門處理此類 exception
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handleBindException(MethodArgumentNotValidException e) {
		LOGGER.error("参数验证失败", e);
		List<FieldErrorResource> fieldErrorResources = new ArrayList<>();

		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			FieldErrorResource fieldErrorResource = new FieldErrorResource();
			fieldErrorResource.setResource(fieldError.getObjectName());
			fieldErrorResource.setField(fieldError.getField());
			fieldErrorResource.setCode(fieldError.getCode());
			fieldErrorResource.setMessage(fieldError.getDefaultMessage());
			fieldErrorResources.add(fieldErrorResource);
		}

		ErrorMessageBag error = new ErrorMessageBag("Validation failed", 500);
		error.setFieldErrors(fieldErrorResources);

		return ResponseEntity.badRequest().body(error);
	}

	/**
	 *
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorMessageBag> handleException(BindException e) {
		LOGGER.error("参数绑定失败", e);
		ErrorMessageBag errorResponse = new ErrorMessageBag();
		errorResponse.setMessage(e.getMessage());

		return ResponseEntity.badRequest().body(errorResponse);
	}

	/**
	 * ConversionFailedException
	 * 参数换转失败
	 */
	@ExceptionHandler(ConversionFailedException.class)
	public ResponseEntity<ErrorMessageBag> handleConversionFailedException(ConversionFailedException e) {
		LOGGER.error("参数转换失败(QueryDSL)", e);
		ErrorMessageBag errorResponse = new ErrorMessageBag();
		errorResponse.setCode(400);
		errorResponse.setMessage("请正确传入参数!");

		return ResponseEntity.badRequest().body(errorResponse);
	}


}
