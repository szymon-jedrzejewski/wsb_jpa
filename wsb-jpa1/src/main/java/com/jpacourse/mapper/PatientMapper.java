package com.jpacourse.mapper;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistence.entity.PatientEntity;

public final class PatientMapper {

    public static PatientTO mapToTO(final PatientEntity patientEntity) {
        if (patientEntity == null) {
            return null;
        }

        final PatientTO patientTO = new PatientTO();
        patientTO.setId(patientEntity.getId());
        patientTO.setFirstName(patientEntity.getFirstName());
        patientTO.setLastName(patientEntity.getLastName());
        patientTO.setTelephoneNumber(patientEntity.getTelephoneNumber());
        patientTO.setEmail(patientEntity.getEmail());
        patientTO.setBloodType(patientEntity.getBloodType());
        patientTO.setPatientNumber(patientEntity.getPatientNumber());
        patientTO.setDateOfBirth(patientEntity.getDateOfBirth());

        // If you have visits, map them as well
        if (patientEntity.getVisits() != null) {
            patientTO.setVisits(VisitMapper.mapToTOList(patientEntity.getVisits()));
        }

        return patientTO;
    }
}
