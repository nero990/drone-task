package com.nero.dronetask.services;

import com.nero.dronetask.dtos.requests.MedicationRequest;
import com.nero.dronetask.models.Medication;
import com.nero.dronetask.repositories.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    public Page<Medication> getMedications(Pageable pageable) {
        return medicationRepository.findAll(pageable);
    }

    public Medication createMedication(MedicationRequest request) {
        final Medication medication = new Medication();
        medication.setName(request.getName());
        medication.setCode(request.getCode());
        medication.setWeight(request.getWeight());

        return medicationRepository.save(medication);
    }
}
