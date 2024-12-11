package com.jpacourse.persistance.service;



import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.dao.VisitDao;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.enums.Specialization;
import com.jpacourse.service.impl.PatientServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceTest {
    @Autowired
    private PatientServiceImpl patientService;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private VisitDao visitDao;

    @Autowired
    private DoctorDao doctorDao;


    @Test
    public void testFindById_returnsPatientTO() {
        // Given
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setFirstName("John");
        patientEntity.setLastName("Doe");
        patientEntity.setTelephoneNumber("123456789");
        patientEntity.setEmail("john.doe@example.com");
        patientEntity.setAge(30);
        patientEntity.setPatientNumber("P123");
        patientEntity.setDateOfBirth(LocalDate.of(1993, 1, 15));

        // Save patient to the database
        PatientEntity savedPatient = patientDao.save(patientEntity);
        Long savedPatientId = savedPatient.getId();

        // When
        PatientTO patientTO = patientService.findById(savedPatientId);

        // Then
        assertThat(patientTO).isNotNull();
        assertThat(patientTO.getId()).isEqualTo(savedPatientId);
        assertThat(patientTO.getFirstName()).isEqualTo("John");
        assertThat(patientTO.getLastName()).isEqualTo("Doe");
        assertThat(patientTO.getTelephoneNumber()).isEqualTo("123456789");
        assertThat(patientTO.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(patientTO.getAge()).isEqualTo(30);
        assertThat(patientTO.getPatientNumber()).isEqualTo("P123");
        assertThat(patientTO.getDateOfBirth()).isEqualTo(LocalDate.of(1993, 1, 15));

        assertThat(patientTO.getVisits()).isNullOrEmpty();

    }

    @Test
    public void testDeletePatient_cascadesVisitsButNotDoctors() {
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
        //patient.setId(12L);
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

        assertThat(visitDao.findAll().size()).isGreaterThan(0);

        // When
        patientDao.delete(savedPatient.getId());

        // Then
        assertThat(visitDao.findAll()).isNotEmpty();

        // And
        assertThat(doctorDao.findOne(doctor.getId())).isNotNull();
    }
}