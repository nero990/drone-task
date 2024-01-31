package com.nero.dronetask.models;

import com.nero.dronetask.enums.DroneModel;
import com.nero.dronetask.enums.DroneState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "drones")
public class Drone extends BaseModel {
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;
    private double weightLimit;
    private int batteryCapacity = 100;

    @Enumerated(EnumType.STRING)
    private DroneState state = DroneState.IDLE;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "drone_medication",
            joinColumns = { @JoinColumn(name = "drone_id") },
            inverseJoinColumns = { @JoinColumn(name = "medication_id") })
    private Collection<Medication> medications = new ArrayList<>();

    public void addMedications(Collection<Medication> medications) {
        this.medications.addAll(medications);
    }

    public boolean isAvailable() {
        return state.equals(DroneState.IDLE) || state.equals(DroneState.LOADING);
    }

    public double getMedicationsWeight() {
        return medications.parallelStream().mapToDouble(Medication::getWeight).sum();
    }

    public double getAvailableWeight() {
        return getWeightLimit() - getMedicationsWeight();
    }
}
