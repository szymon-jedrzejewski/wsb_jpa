package com.jpacourse.Repository;

import com.jpacourse.persistence.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {
}