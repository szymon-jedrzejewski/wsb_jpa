package com.jpacourse.rest;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/{id}")
    public PatientTO getPatientById(@PathVariable Long id) {
        return patientService.findById(id);
    }

    @GetMapping("/{id}/visits")
    public ResponseEntity<List<VisitTO>> getPatientsVisits(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findVisitsForPatient(id));
    }

    @GetMapping("/ln/{lastName}")
    public ResponseEntity<List<PatientTO>> getPatientsVisits(@PathVariable String lastName) {
        return ResponseEntity.ok(patientService.findPatientsByLastName(lastName));
    }

    @GetMapping("/nov/{numberOfVisits}")
    public ResponseEntity<List<PatientTO>> getPatientsWithMoreThanVisits(@PathVariable Long numberOfVisits) {
        return ResponseEntity.ok(patientService.findPatientsThatHadMoreVisitsThan(numberOfVisits));
    }

    @GetMapping("/bloodType")
    public ResponseEntity<List<PatientTO>> getPatientsWithBloodType(@RequestParam List<Integer> bloodType) {
        return ResponseEntity.ok(patientService.findPatientsWithGivenBloodTypes(bloodType));
    }
}
