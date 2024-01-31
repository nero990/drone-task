package com.nero.dronetask.dtos.requests;

import com.nero.dronetask.utils.customvalidations.constraints.Regex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MedicationRequest {
    @Size(min = 2, max = 70)
    @Regex(value = "^[A-Za-z0-9_-]*$", message = "not valid. Valid name can only be letters, numbers, - or _")
    @NotNull
    private String name;

    @Positive
    @NotNull
    private Double weight;

    @Size(min = 2, max = 10)
    @Regex(value = "^[A-Z0-9_]*$", message = "not valid. Valid code can only be uppercase letters, underscore or numbers")
    @NotNull
    private String code;

    private String image;
}
