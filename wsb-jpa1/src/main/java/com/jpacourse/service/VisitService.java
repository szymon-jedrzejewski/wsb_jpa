package com.jpacourse.service;

import com.jpacourse.dto.VisitTO;

public interface VisitService {
    VisitTO findById(final Long id);
}
