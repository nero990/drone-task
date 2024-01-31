package com.nero.dronetask.utils.customvalidations.validators;


import com.nero.dronetask.utils.customvalidations.constraints.In;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InValidator implements ConstraintValidator<In, String> {
	private List<String> allowedValues;
	@Override
	public void initialize(In constraintAnnotation) {
		allowedValues = Arrays.stream(constraintAnnotation.value()).collect(Collectors.toList());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		return allowedValues.contains(value);
	}
}
