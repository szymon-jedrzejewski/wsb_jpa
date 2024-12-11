package com.jpacourse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jpacourse.persistence.entity.VisitEntity;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {
}