package com.emcoo.ef.validator.impl;

import com.emcoo.ef.validator.constraints.Same;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of {@link Same} validator.
 *
 * @author mark
 */
public class SameValidator implements ConstraintValidator<Same, Object> {

	private String fieldName;
	private String dependFieldName;

	@Override
	public void initialize(Same constraintAnnotation) {
		fieldName = constraintAnnotation.fieldName();
		dependFieldName = constraintAnnotation.dependFieldName();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext ctx) {
		if (value == null) {
			return true;
		}

		try {
			final Object fieldNameValue = BeanUtils.getProperty(value, fieldName);
			final Object dependFieldValue = BeanUtils.getProperty(value, dependFieldName);

			return fieldNameValue == null && dependFieldValue == null || fieldNameValue != null && fieldNameValue.equals(dependFieldValue);

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
			throw new RuntimeException(e);
		}

	}

}