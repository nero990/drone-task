package com.nero.dronetask.repositories;

import com.nero.dronetask.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {

    boolean existsBySerialNumber(String serialNumber);

    @Query("SELECT d FROM Drone d WHERE d.batteryCapacity < :batteryCapacity " +
            "AND NOT EXISTS (SELECT b FROM BatteryLevelAudit b " +
            "WHERE b.status = com.nero.dronetask.enums.BatteryLevelStatus.LOW " +
            "AND b.drone = d)")
    List<Drone> findByBatteryCapacityLessThan(@Param("batteryCapacity") int batteryCapacity);
}
