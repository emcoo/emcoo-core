package com.emcoo.ef.validator.impl;

import com.emcoo.ef.validator.constraints.NotNullIf;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of {@link NotNullIf} validator.
 *
 * @author mark
 */
public class NotNullIfValidator implements ConstraintValidator<NotNullIf, Object> {

	String ARRAY_TYPE = "array";
	String STRING_TYPE = "array";

	private String fieldName;
	private String expectedFieldValue;
	private String dependFieldName;
	private String dependFieldValueType = STRING_TYPE;

	@Override
	public void initialize(NotNullIf constraintAnnotation) {
		fieldName = constraintAnnotation.fieldName();
		expectedFieldValue = constraintAnnotation.fieldValue();
		dependFieldName = constraintAnnotation.dependFieldName();
		dependFieldValueType = constraintAnnotation.dependFieldValueType();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext ctx) {
		if (value == null) {
			return true;
		}

		try {

			final Object fieldValue = BeanUtils.getProperty(value, fieldName);
			Object dependFieldValue = BeanUtils.getProperty(value, dependFieldName);
			if (ARRAY_TYPE.equals(dependFieldValueType)) {
				dependFieldValue = BeanUtils.getArrayProperty(value, dependFieldName);
			}

			if (expectedFieldValue.equals(fieldValue) && dependFieldValue == null) {
				ctx.disableDefaultConstraintViolation();
				ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
						.addPropertyNode(dependFieldName)
						.addConstraintViolation();
				return false;
			}

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
			throw new RuntimeException(e);
		}

		return true;
	}

}