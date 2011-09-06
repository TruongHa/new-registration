package org.openmrs.module.registration.web.controller.util;

public class PatientRegistrerException extends Exception {

	private static final long serialVersionUID = 1L;
	private String error;

	public PatientRegistrerException(String error) {
		super();
		this.error = error;
	}

	public PatientRegistrerException() {
		super();
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
