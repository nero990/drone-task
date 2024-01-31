package com.nero.dronetask.controllers;

import com.nero.dronetask.configs.Routes;
import com.nero.dronetask.dtos.requests.DroneRequest;
import com.nero.dronetask.dtos.responses.Wrapper;
import com.nero.dronetask.helpers.ResponseBuilder;
import com.nero.dronetask.models.Drone;
import com.nero.dronetask.services.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.nero.dronetask.helpers.ResponseBuilder.success;

@Tag(name = "Drone Management")
@RequiredArgsConstructor
@RestController
public class DroneController {
    private final DroneService droneService;

    @Operation(summary = "Fetch all registered drone", description = "This endpoint fetches all registered and their availability status")
    @GetMapping(Routes.Drone.INDEX)
    public ResponseEntity<Wrapper<Collection<Drone>>> getDrones(@ParameterObject Pageable pageable) {
        return success(droneService.getDrones(pageable));
    }

    @Operation(summary = "Register a drone", description = "This endpoint registers a new drone")
    @PostMapping(Routes.Drone.INDEX)
    public ResponseEntity<Wrapper<Drone>> createDrone(@Valid @RequestBody DroneRequest droneRequest) {
        return success(droneService.createDrone(droneRequest), ResponseBuilder.Type.CREATED);
    }

    @Operation(summary = "Fetch details of a drone", description = "This provide detail information about a drone. E.g: Availability, drone state, loaded medications, available weight, etc.")
    @GetMapping(Routes.Drone.SHOW)
    public ResponseEntity<Wrapper<Drone>> getDrone(@PathVariable Long id) {
        return success(droneService.getDrone(id));
    }
}
