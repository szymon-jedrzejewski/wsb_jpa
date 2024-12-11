package com.jpacourse.persistence.dao.impl;

import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.enums.Specialization;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class PatientDaoImplTest {

    @Autowired
    private PatientDaoImpl patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    void addVisitToPatient() {
        // given: assume there are existing doctor and patient records in the database
        Long doctorId = 1L; // Assume the doctor with ID 1 exists in the database
        Long patientId = 1L; // Assume the patient with ID 1 exists in the database

        // Fetch the doctor from the database
        DoctorEntity doctor = doctorDao.findOne(doctorId);
        assertThat(doctor).isNotNull();

        // Fetch the patient from the database
        PatientEntity patient = patientDao.findOne(patientId);
        assertThat(patient).isNotNull();

        // when: add a visit to the patient
        LocalDateTime visitDate = LocalDateTime.of(2024, 12, 8, 15, 45);
        String description = "Routine checkup";

        patientDao.addVisitToPatient(patient.getId(), doctor.getId(), visitDate, description);

        // then: verify the visit was added
        PatientEntity updatedPatient = patientDao.findOne(patient.getId());
        assertThat(updatedPatient).isNotNull();
        assertThat(updatedPatient.getVisits()).isNotNull();

        // Assert the visit has the correct details
        VisitEntity visit = updatedPatient.getVisits().get(0);
        assertThat(visit.getTime()).isNotNull();
        assertThat(visit.getDescription()).isEqualTo(description);
        assertThat(visit.getDoctor().getId()).isEqualTo(doctor.getId());
    }
}