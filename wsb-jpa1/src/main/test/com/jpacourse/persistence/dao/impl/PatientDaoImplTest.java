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

import static org.assertj.core.api.Assertions.assertThat;

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
    public void findByLasName_ShouldReturnPatientsWithGivenLastName() {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("john.doe@example.com");
        patient.setAge(30);
        patient.setPatientNumber("P123");
        patient.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("William");
        patient1.setLastName("Doe");
        patient1.setTelephoneNumber("987654321");
        patient1.setEmail("william.doe@example.com");
        patient1.setAge(30);
        patient1.setPatientNumber("P321");
        patient1.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("William");
        patient2.setLastName("Murray");
        patient2.setTelephoneNumber("987654321");
        patient2.setEmail("william.doe@example.com");
        patient2.setAge(30);
        patient2.setPatientNumber("P321");
        patient2.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity savedPatient = patientDao.save(patient);
        PatientEntity savedPatient1 = patientDao.save(patient1);
        patientDao.save(patient2);

        assertThat(patientDao.findPatientByLastName("Doe"))
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(savedPatient1, savedPatient);
    }

    @Test
    public void findPatientsThatHadMoreVisitsThan_shouldReturnPatientsThatHaveMoreThan3Visits() {
        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("William");
        patient1.setLastName("Doe");
        patient1.setTelephoneNumber("987654321");
        patient1.setEmail("william.doe@example.com");
        patient1.setAge(30);
        patient1.setPatientNumber("P321");
        patient1.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("William");
        patient2.setLastName("Murray");
        patient2.setTelephoneNumber("987654321");
        patient2.setEmail("william.doe@example.com");
        patient2.setAge(30);
        patient2.setPatientNumber("P321");
        patient2.setDateOfBirth(LocalDate.of(1993, 1, 15));

        DoctorEntity doctor = createDoctor();

        List<VisitEntity> visits = prepareVisits(doctor, patient1);

        patient1.setVisits(visits);
        patient2.setVisits(List.of(visits.get(0)));

        PatientEntity savedPatient1 = patientDao.save(patient1);
        PatientEntity savedPatient2 = patientDao.save(patient2);

        List<PatientEntity> patients = patientDao.findPatientsThatHadMoreVisitsThan(3L);

        assertThat(patients)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(savedPatient1)
                .doesNotContain(savedPatient2);
    }

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
        DoctorEntity doctor = createDoctor();

        // Given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("john.doe@example.com");
        patient.setAge(30);
        patient.setPatientNumber("P123");
        patient.setDateOfBirth(LocalDate.of(1993, 1, 15));

        List<VisitEntity> visits = prepareVisits(doctor, patient);
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

    private static List<VisitEntity> prepareVisits(DoctorEntity doctor, PatientEntity patient) {
        List<VisitEntity> visits = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            VisitEntity visit = new VisitEntity();
            visit.setTime(LocalDateTime.now().minusDays(i));
            visit.setDoctor(doctor);
            visit.setPatient(patient);
            visits.add(visit);
        }
        return visits;
    }

    private DoctorEntity createDoctor() {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Dr.");
        doctor.setLastName("Smith");
        doctor.setSpecialization(Specialization.DERMATOLOGIST);
        doctor.setDoctorNumber("123123");
        doctor.setEmail("email@example.com");
        doctor.setId(doctor.getId());
        doctor.setTelephoneNumber("1212121");

        doctor = doctorDao.merge(doctor);
        return doctor;
    }
}