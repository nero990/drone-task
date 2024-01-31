package com.nero.dronetask.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "medications")
public class Medication extends BaseModel {
    private String name;
    private double weight;
    private String code;
    private String image;
}
