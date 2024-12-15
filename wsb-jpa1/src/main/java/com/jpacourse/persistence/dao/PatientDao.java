package com.jpacourse.persistence.dao;

import com.jpacourse.persistence.entity.PatientEntity;

import java.util.List;

public interface PatientDao extends Dao<PatientEntity, Long> {
    List<PatientEntity> findPatientByLastName(String lastName);

    List<PatientEntity> findPatientsThatHadMoreVisitsThan(Long numberOfVisits);

    List<PatientEntity> findPatientsWithGivenBloodTypes(List<Integer> bloodTypes);
}
