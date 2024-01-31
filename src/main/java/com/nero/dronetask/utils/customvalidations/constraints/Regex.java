package com.nero.dronetask.utils.customvalidations.constraints;


import com.nero.dronetask.utils.customvalidations.validators.RegexValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegexValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Regex {

	String message() default "pattern is not invalid";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	String value();
}
