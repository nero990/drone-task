package com.nero.dronetask.controllers;

import com.nero.dronetask.configs.Routes;
import com.nero.dronetask.dtos.requests.MedicationRequest;
import com.nero.dronetask.dtos.responses.Wrapper;
import com.nero.dronetask.models.Medication;
import com.nero.dronetask.services.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.nero.dronetask.helpers.ResponseBuilder.success;

@Tag(name = "Medication Management")
@RequiredArgsConstructor
@RestController
public class MedicationController {
    private final MedicationService medicationService;

    @Operation(summary = "Fetch all medications", description = "Fetch all medications")
    @GetMapping(Routes.Medication.INDEX)
    public ResponseEntity<Wrapper<Collection<Medication>>> getMedications(@ParameterObject Pageable pageable) {
        return success(medicationService.getMedications(pageable));
    }

    @Operation(summary = "Create medication", description = "Create a new medication")
    @PostMapping(Routes.Medication.INDEX)
    public ResponseEntity<Wrapper<Medication>> createMedication(@Valid @RequestBody MedicationRequest droneRequest) {
        return success(medicationService.createMedication(droneRequest));
    }
}
