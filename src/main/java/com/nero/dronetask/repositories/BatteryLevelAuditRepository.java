package com.nero.dronetask.repositories;

import com.nero.dronetask.models.BatteryLevelAudit;
import com.nero.dronetask.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BatteryLevelAuditRepository extends JpaRepository<BatteryLevelAudit, Long> {

    @Modifying
    @Query("UPDATE BatteryLevelAudit SET status = com.nero.dronetask.enums.BatteryLevelStatus.CHARGED WHERE drone = :drone")
    void updateByDroneAndStatus(@Param("drone") Drone drone);
}
