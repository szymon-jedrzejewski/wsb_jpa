package com.jpacourse.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
import javax.print.Doc;

@Entity
@Table(name = "VISIT")
public class VisitEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Column(nullable = false)
	private LocalDateTime time;

	// Relacja jednostronna od strony dziecka: Visit -> Doctor (rodzic)
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ID", nullable = false)
	private DoctorEntity doctor;

	// Relacja jednostronna od strony dziecka: Visit -> Patient (rodzic)
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID", nullable = false)
	private PatientEntity patient;

	//// Relacja jednostronna od strony rodzica: Visit -> MedicalTreatment (dziecko)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "VISIT_ID")
	private List<MedicalTreatmentEntity> medicalTreatment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

}
