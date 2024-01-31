package com.nero.dronetask.controllers;

import com.nero.dronetask.configs.Routes;
import com.nero.dronetask.dtos.requests.LoadDroneRequest;
import com.nero.dronetask.dtos.responses.Wrapper;
import com.nero.dronetask.models.Drone;
import com.nero.dronetask.services.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.nero.dronetask.helpers.ResponseBuilder.success;

@Tag(name = "Drone Operations")
@RequiredArgsConstructor
@RestController
public class DroneOperationController {
    private final DroneService droneService;

    @Operation(summary = "Load drone", description = "Load a drone with medications")
    @PostMapping(Routes.Drone.Operation.LOAD)
    public ResponseEntity<Wrapper<Drone>> loadDrone(@PathVariable Long id, @Valid @RequestBody LoadDroneRequest request) {
        return success("Success! Drone loaded.", droneService.loadDrone(id, request));
    }

    @Operation(summary = "Charge drone", description = "Charge drone battery")
    @PutMapping(Routes.Drone.Operation.CHARGE)
    public ResponseEntity<Wrapper<Drone>> chargeDrone(@PathVariable Long id) {
        return success("Drone battery charged (100%).", droneService.chargeDrone(id));
    }

    @Operation(summary = "Update drone state", description = "Update drone state")
    @PutMapping(Routes.Drone.Operation.STATE)
    public ResponseEntity<Wrapper<Drone>> updateDroneState(@PathVariable Long id) {
        return success(droneService.updateDroneState(id));
    }
}
