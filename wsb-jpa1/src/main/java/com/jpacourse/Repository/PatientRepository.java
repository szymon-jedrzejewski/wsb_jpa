package com.jpacourse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jpacourse.persistence.entity.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
}