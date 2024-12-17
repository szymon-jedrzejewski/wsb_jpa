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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void testOptimisticLocking() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("John");
        patient.setLastName("Lincoln");
        patient.setTelephoneNumber("123456789");
        patient.setPatientNumber("PN123456");
        patient.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));

        em.persist(patient);
        em.getTransaction().commit();
        em.close();

        Long patientId = patient.getId();

        EntityManager em1 = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        em1.getTransaction().begin();
        em2.getTransaction().begin();

        PatientEntity patient1 = em1.find(PatientEntity.class, patientId);
        PatientEntity patient2 = em2.find(PatientEntity.class, patientId);

        patient1.setTelephoneNumber("987654321");
        em1.getTransaction().commit();
        em1.close();

        patient2.setTelephoneNumber("555555555");

        assertThatThrownBy(() -> em2.getTransaction().commit())
                .isInstanceOf(RollbackException.class)
                .hasCauseInstanceOf(OptimisticLockException.class);

        em2.close();
    }

    @Test
    public void findByLasName_ShouldReturnPatientsWithGivenLastName() {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("john.doe@example.com");
        patient.setBloodType(30);
        patient.setPatientNumber("P123");
        patient.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("William");
        patient1.setLastName("Doe");
        patient1.setTelephoneNumber("987654321");
        patient1.setEmail("william.doe@example.com");
        patient1.setBloodType(30);
        patient1.setPatientNumber("P321");
        patient1.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("William");
        patient2.setLastName("Murray");
        patient2.setTelephoneNumber("987654321");
        patient2.setEmail("william.doe@example.com");
        patient2.setBloodType(30);
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
        patient1.setBloodType(30);
        patient1.setPatientNumber("P321");
        patient1.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("William");
        patient2.setLastName("Murray");
        patient2.setTelephoneNumber("987654321");
        patient2.setEmail("william.doe@example.com");
        patient2.setBloodType(30);
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
    public void findPatientsWithGivenBloodTypes_shouldReturnPatientsThatHaveBloodType1or2() {
        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("William");
        patient1.setLastName("Doe");
        patient1.setTelephoneNumber("987654321");
        patient1.setEmail("william.doe@example.com");
        patient1.setBloodType(1);
        patient1.setPatientNumber("P321");
        patient1.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("William");
        patient2.setLastName("Murray");
        patient2.setTelephoneNumber("987654321");
        patient2.setEmail("william.doe@example.com");
        patient2.setBloodType(2);
        patient2.setPatientNumber("P321");
        patient2.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity patient3 = new PatientEntity();
        patient3.setFirstName("William");
        patient3.setLastName("Murray");
        patient3.setTelephoneNumber("987654321");
        patient3.setEmail("william.doe@example.com");
        patient3.setBloodType(4);
        patient3.setPatientNumber("P321");
        patient3.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity savedPatient1 = patientDao.save(patient1);
        PatientEntity savedPatient2 = patientDao.save(patient2);
        PatientEntity savedPatient3 = patientDao.save(patient3);

        List<PatientEntity> patients = patientDao.findPatientsWithGivenBloodTypes(List.of(1, 2));

        assertThat(patients)
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(savedPatient1, savedPatient2)
                .doesNotContain(savedPatient3);
    }

    @Test
    void addVisitToPatient_shouldAddVisitToPatient() {
        DoctorEntity doctor = createDoctor();

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("john.doe@example.com");
        patient.setBloodType(30);
        patient.setPatientNumber("P123");
        patient.setDateOfBirth(LocalDate.of(1993, 1, 15));

        PatientEntity savedPatient = patientDao.save(patient);

        patientDao.addVisitToPatient(savedPatient.getId(), doctor.getId(), LocalDateTime.now(), "Have fun");

        assertThat(savedPatient.getVisits()).isNotNull().isNotEmpty().hasSize(1);
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
        patient.setBloodType(30);
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