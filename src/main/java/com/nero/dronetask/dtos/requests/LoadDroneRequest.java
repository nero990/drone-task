package com.nero.dronetask.dtos.requests;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoadDroneRequest {

    @NotEmpty
    private Long[] medicationIds;
}
