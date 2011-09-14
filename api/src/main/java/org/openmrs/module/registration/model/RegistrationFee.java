package org.openmrs.module.registration.model;

import java.math.BigDecimal;
import java.util.Date;

import org.openmrs.Patient;
import org.openmrs.User;

public class RegistrationFee {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Patient patient;
	private Date createdOn;
	private User createdBy;
	private BigDecimal fee;	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}
