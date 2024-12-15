package com.jpacourse.persistence.dao.impl;

import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.dao.VisitDao;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.enums.Specialization;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class PatientDaoImplTest {

    @Autowired
    private PatientDaoImpl patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private VisitDao visitDao;

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


    @Test
    public void deletePatient_cascadesVisitsButNotDoctors() {
        // Given
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Dr.");
        doctor.setLastName("Smith");
        doctor.setSpecialization(Specialization.DERMATOLOGIST);
        doctor.setDoctorNumber("123123");
        doctor.setEmail("email@example.com");
        doctor.setId(doctor.getId());
        doctor.setTelephoneNumber("1212121");

        doctor = doctorDao.merge(doctor);


        // Given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("john.doe@example.com");
        patient.setAge(30);
        patient.setPatientNumber("P123");
        patient.setDateOfBirth(LocalDate.of(1993, 1, 15));

        List<VisitEntity> visits = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            VisitEntity visit = new VisitEntity();
            visit.setTime(LocalDateTime.now().minusDays(i));
            visit.setDoctor(doctor);
            visit.setPatient(patient);
            visits.add(visit);
        }
        patient.setVisits(visits);
        PatientEntity savedPatient = patientDao.save(patient);

        Assertions.assertThat(visitDao.findAll().size()).isGreaterThan(0);

        // When
        patientDao.delete(savedPatient.getId());

        // Then
        Assertions.assertThat(visitDao.findAll()).isNotEmpty();

        // And
        Assertions.assertThat(doctorDao.findOne(doctor.getId())).isNotNull();
    }
}