package com.nero.dronetask.dtos.requests;


import com.nero.dronetask.utils.customvalidations.constraints.In;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;


@Getter
public class DroneRequest {
    @Size(min = 10, max = 100)
    @NotEmpty
    private String serialNumber;

    @In({"LIGHTWEIGHT", "MIDDLEWEIGHT", "CRUISERWEIGHT", "HEAVYWEIGHT"})
    @NotEmpty
    private String model;

    @Range(min = 20, max = 500)
    @Positive
    @NotNull
    private Double weightLimit;
}
