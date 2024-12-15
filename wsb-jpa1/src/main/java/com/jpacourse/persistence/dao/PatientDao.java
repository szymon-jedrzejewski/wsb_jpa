package com.jpacourse.persistence.dao;

import com.jpacourse.persistence.entity.PatientEntity;

import java.util.List;

public interface PatientDao extends Dao<PatientEntity, Long> {
    List<PatientEntity> findPatientByLastName(String lastName);

    List<PatientEntity> findPatientsThatHadMoreVisitsThan(int numberOfVisits);

    List<PatientEntity> findPatientsOlderThan(int age);
}
