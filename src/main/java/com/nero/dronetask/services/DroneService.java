package com.nero.dronetask.services;

import com.nero.dronetask.dtos.requests.DroneRequest;
import com.nero.dronetask.dtos.requests.LoadDroneRequest;
import com.nero.dronetask.enums.BatteryLevelStatus;
import com.nero.dronetask.enums.DroneModel;
import com.nero.dronetask.enums.DroneState;
import com.nero.dronetask.enums.ErrorType;
import com.nero.dronetask.exceptions.DroneApplicationException;
import com.nero.dronetask.models.BatteryLevelAudit;
import com.nero.dronetask.models.Drone;
import com.nero.dronetask.models.Medication;
import com.nero.dronetask.repositories.BatteryLevelAuditRepository;
import com.nero.dronetask.repositories.DroneRepository;
import com.nero.dronetask.repositories.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final BatteryLevelAuditRepository batteryLevelAuditRepository;

    @Value("${drone.low-battery-threshold}")
    private int lowBatteryThreshold;

    public Page<Drone> getDrones(Pageable pageable) {
        return droneRepository.findAll(pageable);
    }

    public Drone createDrone(DroneRequest droneRequest) {

        if (droneRepository.existsBySerialNumber(droneRequest.getSerialNumber()))
            throw new DroneApplicationException("A drone with this serial number already exist.");

        final Drone drone = new Drone();
        drone.setSerialNumber(droneRequest.getSerialNumber());
        drone.setModel(DroneModel.valueOf(droneRequest.getModel()));
        drone.setWeightLimit(droneRequest.getWeightLimit());

        return droneRepository.save(drone);
    }

    public Drone getDrone(Long id) {
        return droneRepository.findById(id).orElseThrow(() -> new DroneApplicationException("Drone not found.", ErrorType.RESOURCE_NOT_FOUND));
    }

    public Drone loadDrone(Long id, LoadDroneRequest request) {
        final Drone drone = droneRepository.findById(id).orElseThrow(() -> new DroneApplicationException("Drone not found.", ErrorType.RESOURCE_NOT_FOUND));

        // Check if medications are valid
        final Collection<Medication> medications = medicationRepository.findByIdIn(Arrays.asList(request.getMedicationIds()));
        if (medications.size() != request.getMedicationIds().length)
            throw new DroneApplicationException("A selected medication is not valid.");

        // check drone battery level
        if (drone.getBatteryCapacity() < lowBatteryThreshold)
            throw new DroneApplicationException("Drone battery level is low. Please charge.", ErrorType.GENERIC);

        // check drone availability
        if (!drone.isAvailable())
            throw new DroneApplicationException("Selected drone is currently not available. Please choose another.");

        // Check
        final double medicationsWeight = medications.parallelStream().mapToDouble(Medication::getWeight).sum();
        final double loadedMedicationsWeight = drone.getMedicationsWeight();
        if (medicationsWeight + loadedMedicationsWeight > drone.getWeightLimit())
            throw new DroneApplicationException("Drone capacity limit exceeded. Capacity: " + drone.getWeightLimit() + "g. Current weight on drone: " + loadedMedicationsWeight + "g");

        drone.setState(drone.getWeightLimit() == medicationsWeight + loadedMedicationsWeight ? DroneState.LOADED : DroneState.LOADING);
        drone.addMedications(medications);
        return droneRepository.save(drone);
    }

    @Transactional
    public Drone chargeDrone(Long id) {
        final Drone drone = droneRepository.findById(id).orElseThrow(() -> new DroneApplicationException("Drone not found.", ErrorType.RESOURCE_NOT_FOUND));

        // Only IDLE, LOADING & LOADED drones can be charged.
        if (!drone.isAvailable() && !drone.getState().equals(DroneState.LOADED))
            throw new DroneApplicationException("Drone is currently not available.");

        if (drone.getBatteryCapacity() == 100)
            throw new DroneApplicationException("Battery full", ErrorType.GENERIC);

        drone.setBatteryCapacity(100);
        droneRepository.save(drone);

        // Update BatteryLevelAudit
        batteryLevelAuditRepository.updateByDroneAndStatus(drone);

        return drone;
    }

    public Drone updateDroneState(Long id) {
        final Drone drone = droneRepository.findById(id).orElseThrow(() -> new DroneApplicationException("Drone not found.", ErrorType.RESOURCE_NOT_FOUND));

        if (drone.getState().equals(DroneState.IDLE))
            throw new DroneApplicationException("Cannot update the state of an idle drone.", ErrorType.GENERIC);

        switch (drone.getState()) {
            case LOADING -> drone.setState(DroneState.LOADED);
            case LOADED -> drone.setState(DroneState.DELIVERING);
            case DELIVERING -> {
                reduceBatteryLevel(drone);
                drone.setState(DroneState.DELIVERED);
                drone.setMedications(new ArrayList<>()); // offload drone
            }
            case DELIVERED -> drone.setState(DroneState.RETURNING);
            case RETURNING -> {
                reduceBatteryLevel(drone);
                drone.setState(DroneState.IDLE);
            }
        }

        return droneRepository.save(drone);
    }

    private void reduceBatteryLevel(Drone drone) {
        // reduce battery level when state is updated to DELIVERED or IDLE
        if (!drone.getState().equals(DroneState.DELIVERING) && !drone.getState().equals(DroneState.RETURNING))
            return;

        /*
        * The following assumptions were made:
        * - The bigger the drone the higher the power consumption
        * - a delivering drone consumes more power than a returning drone
        * - The battery capacity of a drone MUST be able to deliver a medication and return successfully without running out of battery
        * */
        switch (drone.getModel()) {
            case LIGHTWEIGHT ->
                    drone.setBatteryCapacity(drone.getBatteryCapacity() - (drone.getState().equals(DroneState.DELIVERING) ? 4 : 2));
            case MIDDLEWEIGHT ->
                    drone.setBatteryCapacity(drone.getBatteryCapacity() - (drone.getState().equals(DroneState.DELIVERING) ? 6 : 4));
            case CRUISERWEIGHT ->
                    drone.setBatteryCapacity(drone.getBatteryCapacity() - (drone.getState().equals(DroneState.DELIVERING) ? 8 : 6));
            case HEAVYWEIGHT ->
                    drone.setBatteryCapacity(drone.getBatteryCapacity() - (drone.getState().equals(DroneState.DELIVERING) ? 10 : 8));
        }
    }

    @Scheduled(cron = "0 */5 * * * *") // every 5 minutes
    public void checkDroneBatteryLevel() {

        final List<BatteryLevelAudit> batteryLevelAudits = droneRepository.findByBatteryCapacityLessThan(25).parallelStream().map(drone -> {
            final BatteryLevelAudit batteryLevelAudit = new BatteryLevelAudit();
            batteryLevelAudit.setDrone(drone);
            batteryLevelAudit.setStatus(BatteryLevelStatus.LOW);
            batteryLevelAudit.setBatteryLevel(drone.getBatteryCapacity());
            return batteryLevelAudit;
        }).collect(Collectors.toList());

        if (!batteryLevelAudits.isEmpty()) {
            batteryLevelAuditRepository.saveAll(batteryLevelAudits);
            // Assumed Notification was sent
        }
    }
}
