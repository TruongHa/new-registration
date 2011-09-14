package org.openmrs.module.registration.db;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.module.registration.model.RegistrationFee;



public interface RegistrationDAO {
	
	/**
	 * Save registration fee
	 * @param fee
	 * @return
	 */
	public RegistrationFee saveRegistrationFee(RegistrationFee fee);

	/**
	 * Get registration fee by id
	 * @param id
	 * @return
	 */
	public RegistrationFee getRegistrationFee(Integer id);
	
	/**
	 * Get registration fees by patient
	 * @param patient
	 * @return
	 */
	public List<RegistrationFee> getRegistrationFees(Patient patient);

	/**
	 * Delete registration fee
	 * @param fee
	 */
	public void deleteRegistrationFee(RegistrationFee fee);
}
