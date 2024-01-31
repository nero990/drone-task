package com.nero.dronetask.utils.customvalidations.constraints;

import com.nero.dronetask.utils.customvalidations.validators.InValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface In {

	String message() default "is not valid. Valid values are: {value}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	String[] value();
}
