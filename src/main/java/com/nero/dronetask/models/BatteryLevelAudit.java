package com.nero.dronetask.models;

import com.nero.dronetask.enums.BatteryLevelStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "battery_level_audits")
public class BatteryLevelAudit extends BaseModel {
    @ManyToOne
    private Drone drone;
    private int batteryLevel;

    @Enumerated(EnumType.STRING)
    private BatteryLevelStatus status;
}
