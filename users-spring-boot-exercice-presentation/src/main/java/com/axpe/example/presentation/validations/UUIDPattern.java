/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.presentation.validations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * Validation constraint for a UUID v4 in string format.
 *
 * e.g. 6cfb0496-fa35-4668-a970-78a873d7970e
 *
 * @author autor@axpe.com
 */
@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
@Retention(RUNTIME)
@Target({ METHOD, FIELD, PARAMETER, ANNOTATION_TYPE })
@Constraint(validatedBy = {})
@Documented
public @interface UUIDPattern {
	String message() default "UUID has wrong format";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
