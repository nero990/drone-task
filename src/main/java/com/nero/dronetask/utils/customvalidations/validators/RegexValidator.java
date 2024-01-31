package com.nero.dronetask.utils.customvalidations.validators;

import com.nero.dronetask.utils.customvalidations.constraints.Regex;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class RegexValidator implements ConstraintValidator<Regex, String> {
	private String pattern;
	@Override
	public void initialize(Regex constraintAnnotation) {
		pattern = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value == null || value.length() == 0) return false;

		return Pattern.compile(pattern).matcher(value).matches();
	}
}
