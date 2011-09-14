package org.openmrs.module.registration;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.registration.model.RegistrationFee;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RegistrationService extends OpenmrsService {
	
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
	 * Get registration fee by patient
	 * @param patient
	 * @return
	 */
	public List<RegistrationFee> getRegistrationFees(Patient patient);

	/**
	 * Delete a registration fee
	 * @param fee
	 */
	public void deleteRegistrationFee(RegistrationFee fee);
}
