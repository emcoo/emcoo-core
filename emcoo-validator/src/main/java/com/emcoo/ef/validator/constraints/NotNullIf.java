package com.emcoo.ef.validator.constraints;

import com.emcoo.ef.validator.impl.NotNullIfValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validates that field {@code dependFieldName} is not null if
 * field {@code fieldName} has value {@code fieldValue}.
 **/
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullIfValidator.class)
@Documented
public @interface NotNullIf {

	String message() default "{javax.validation.constraints.NotBlank.message}";

	String fieldName();

	String fieldValue();

	String dependFieldName();

	String dependFieldValueType() default "string";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		NotNullIf[] value();
	}

}