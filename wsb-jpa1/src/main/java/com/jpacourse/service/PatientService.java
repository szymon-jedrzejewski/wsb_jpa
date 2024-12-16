package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;

import java.util.List;

public interface PatientService {
    PatientTO findById(final Long id);

    List<VisitTO> findVisitsForPatient(final Long patientId);
    List<PatientTO> findPatientsByLastName(String lastName);

    List<PatientTO> findPatientsThatHadMoreVisitsThan(Long numberOfVisits);

    List<PatientTO> findPatientsWithGivenBloodTypes(List<Integer> bloodTypes);
}
