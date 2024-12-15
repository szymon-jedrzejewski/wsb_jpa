package com.jpacourse.persistence.dao.impl;

import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {

    @Transactional
    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitDate, String description) {
        PatientEntity patient = entityManager.find(PatientEntity.class, patientId);
        DoctorEntity doctor = entityManager.find(DoctorEntity.class, doctorId);
        VisitEntity visit = new VisitEntity();

        visit.setDescription(description);
        visit.setTime(visitDate);
        visit.setDoctor(doctor);
        visit.setPatient(patient);


        entityManager.merge(patient);

    }

    @Override
    public List<PatientEntity> findPatientByLastName(String lastName) {
        return List.of();
    }

    @Override
    public List<PatientEntity> findPatientsThatHadMoreVisitsThan(int numberOfVisits) {
        return List.of();
    }

    @Override
    public List<PatientEntity> findPatientsOlderThan(int age) {
        return List.of();
    }
}