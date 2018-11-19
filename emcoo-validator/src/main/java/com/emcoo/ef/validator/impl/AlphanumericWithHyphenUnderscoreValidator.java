package com.emcoo.ef.validator.impl;

import com.emcoo.ef.validator.constraints.AlphanumericWithHyphenUnderscore;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Implementation of {@link AlphanumericWithHyphenUnderscore} validator.
 *
 * @author mark
 */
public class AlphanumericWithHyphenUnderscoreValidator implements ConstraintValidator<AlphanumericWithHyphenUnderscore, String> {

	private String regexp;

	@Override
	public void initialize(AlphanumericWithHyphenUnderscore constraintAnnotation) {
		this.regexp = constraintAnnotation.regexp();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		if (value.matches(regexp)) {
			return true;
		}
		return false;
	}

}