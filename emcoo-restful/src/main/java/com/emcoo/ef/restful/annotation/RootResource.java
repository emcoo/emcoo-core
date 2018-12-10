package com.emcoo.ef.restful.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Root Resource
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface RootResource {
}