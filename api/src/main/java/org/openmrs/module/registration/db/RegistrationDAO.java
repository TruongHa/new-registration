package org.openmrs.module.registration.db;

import java.math.BigInteger;
import java.util.List;

import org.openmrs.Patient;


public interface RegistrationDAO {
	
	/**
	 * Search patients
	 * @param hql
	 * @return
	 */
	public List<Patient> searchPatient(String hql);
	
	/**
	 * Get patient search result count
	 * @param hql
	 * @return
	 */
	public BigInteger getPatientSearchResultCount(String hql);
}
