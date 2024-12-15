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

        if (patient.getVisits() == null) {
            patient.setVisits(List.of(visit));
        } else {
            patient.getVisits().add(visit);
        }

        save(patient);

    }

    @Override
    public List<PatientEntity> findPatientByLastName(String lastName) {
        return entityManager.createQuery("SELECT p FROM PatientEntity p WHERE p.lastName = :lastName", PatientEntity.class)
                .setParameter("lastName", lastName)
                .getResultList();
    }

    @Override
    public List<PatientEntity> findPatientsThatHadMoreVisitsThan(Long numberOfVisits) {
        String query = "SELECT p " +
                "FROM PatientEntity p " +
                "WHERE (SELECT COUNT(v) FROM VisitEntity v WHERE v.patient.id = p.id) > :numberOfVisits";

        return entityManager.createQuery(query, PatientEntity.class)
                .setParameter("numberOfVisits", numberOfVisits)
                .getResultList();
    }

    @Override
    public List<PatientEntity> findPatientsWithGivenBloodTypes(List<Integer> bloodTypes) {
        return entityManager.createQuery(
                        "SELECT p  FROM PatientEntity p WHERE p.bloodType IN :bloodTypes",
                        PatientEntity.class
                )
                .setParameter("bloodTypes", bloodTypes)
                .getResultList();
    }
}