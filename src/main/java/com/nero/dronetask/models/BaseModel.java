package com.nero.dronetask.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nero.dronetask.configs.CustomZonedDateTimeSerializer;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
