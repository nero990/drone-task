package com.nero.dronetask.repositories;

import com.nero.dronetask.models.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Collection<Medication> findByIdIn(Collection<Long> id);
}
