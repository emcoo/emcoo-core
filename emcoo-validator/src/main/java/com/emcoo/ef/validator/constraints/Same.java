package com.emcoo.ef.validator.constraints;

import com.emcoo.ef.validator.impl.NotNullIfValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validates that field {@code fieldName} is same as {@code dependFieldName}
 **/
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullIfValidator.class)
@Documented
public @interface Same {

	String message() default "{javax.validation.constraints.NotBlank.message}";

	String fieldName();

	String dependFieldName();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		Same[] value();
	}

}