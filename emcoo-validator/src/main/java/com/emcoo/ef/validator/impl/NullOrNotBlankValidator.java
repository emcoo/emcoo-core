package com.emcoo.ef.validator.impl;

import com.emcoo.ef.validator.constraints.NullOrNotBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Implementation of {@link NullOrNotBlank} validator.
 *
 * @author mark
 */
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

	@Override
	public void initialize(NullOrNotBlank constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || !value.trim().isEmpty();
	}

}