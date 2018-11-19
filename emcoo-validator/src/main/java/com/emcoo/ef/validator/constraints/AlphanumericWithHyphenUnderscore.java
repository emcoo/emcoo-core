package com.emcoo.ef.validator.constraints;

import com.emcoo.ef.validator.impl.AlphanumericWithHyphenUnderscoreValidator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Documented
@Constraint(validatedBy = {
		AlphanumericWithHyphenUnderscoreValidator.class
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(AlphanumericWithHyphenUnderscore.List.class)
public @interface AlphanumericWithHyphenUnderscore {

	String message() default "{javax.validation.constraints.AlphanumericWithHyphenUnderscore.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return an additional regular expression the annotated element must match. The default
	 * is any string ('.*')
	 */
	String regexp() default "^[\\w-]*$";

	/**
	 * @return used in combination with {@link #regexp()} in order to specify a regular
	 * expression option
	 */
	Pattern.Flag[] flags() default {};

	/**
	 * Defines several {@code @AlphanumericWithHyphenUnderscore} constraints on the same element.
	 *
	 * @see AlphanumericWithHyphenUnderscore
	 */
	@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		AlphanumericWithHyphenUnderscore[] value();
	}
}