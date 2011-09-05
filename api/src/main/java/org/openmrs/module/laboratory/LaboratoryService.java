package org.openmrs.module.laboratory;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.hospitalcore.model.Lab;
import org.openmrs.module.hospitalcore.model.LabTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LaboratoryService extends OpenmrsService {

	/**
	 * Save laboratory department
	 * 
	 * @param department
	 * @return
	 */
	public Lab saveLaboratoryDepartment(Lab department);

	/**
	 * Get laboratory department by id
	 * 
	 * @param id
	 * @return
	 */
	public Lab getLaboratoryDepartment(Integer id);

	/**
	 * Delete a laboratory department
	 * 
	 * @param department
	 */
	public void deleteLaboratoryDepartment(Lab department);

	/**
	 * get the default laboratory department using current user's role
	 * 
	 * @return
	 */
	public Lab getCurrentDepartment();

	/**
	 * Find orders
	 * 
	 * @param startDate
	 * @param phrase
	 * @param tests
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	public List<Order> getOrders(Date startDate, String phrase,
			Set<Concept> tests, int page) throws ParseException;

	/**
	 * Count found orders
	 * 
	 * @param startDate
	 * @param phrase
	 * @param tests
	 * @return
	 * @throws ParseException
	 */
	public Integer countOrders(Date startDate, String phrase, Set<Concept> tests)
			throws ParseException;

	/**
	 * Accept a laboratory test
	 * 
	 * @param order
	 * @param sampleId TODO
	 * @return id of accepted laboratory test
	 */
	public Integer acceptTest(Order order, String sampleId) throws ParseException;

	/**
	 * Save laboratory test
	 * 
	 * @param test
	 * @return
	 */
	public LabTest saveLaboratoryTest(LabTest test);

	/**
	 * Get laboratory test by id
	 * 
	 * @param id
	 * @return
	 */
	public LabTest getLaboratoryTest(Integer id);

	/**
	 * Delete laboratory test
	 * 
	 * @param test
	 */
	public void deleteLaboratoryTest(LabTest test);

	/**
	 * Get laboratory test by order
	 * 
	 * @param order
	 * @return
	 */
	public LabTest getLaboratoryTest(Order order);

	/**
	 * Reschedule a test
	 * 
	 * @param order
	 * @param rescheduledDate
	 * @return reschedule status
	 */
	public String rescheduleTest(Order order, Date rescheduledDate);

	/**
	 * Find laboratory tests for worklist
	 * 
	 * @param date
	 * @param phrase
	 * @param investigation
	 * @return
	 * @throws ParseException
	 */
	public List<LabTest> getAcceptedLaboratoryTests(Date date, String phrase,
			Set<Concept> allowableTests) throws ParseException;

	/**
	 * Complete a test then return whether the test is successfully completed.
	 * 
	 * @param test
	 * @return
	 */
	public String completeTest(LabTest test);

	/**
	 * Get all laboratory tests by date
	 * 
	 * @param date
	 * @param phrase
	 * @param allowableTests
	 * @return
	 * @throws ParseException
	 */
	public List<LabTest> getAllLaboratoryTestsByDate(Date date, String phrase,
			Set<Concept> allowableTests) throws ParseException;

	/**
	 * Get all completed laboratory tests
	 * 
	 * @param date
	 * @param phrase
	 * @param allowableTests
	 * @return
	 * @throws ParseException
	 */
	public List<LabTest> getCompletedLaboratoryTests(Date date, String phrase,
			Set<Concept> allowableTests) throws ParseException;

	/**
	 * Get all laboratory tests by date and patient
	 * 
	 * @param date
	 * @param patient
	 * @return
	 * @throws ParseException
	 */
	public List<LabTest> getLaboratoryTestsByDateAndPatient(Date date,
			Patient patient) throws ParseException;

	/**
	 * Get laboratory tests
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public List<LabTest> getLaboratoryTests(Date date) throws ParseException;
	
	/**
	 * Get default sample id
	 * @param investigationName TODO
	 * @return
	 * @throws ParseException
	 */
	public String getDefaultSampleId(String investigationName) throws ParseException;

	/**
	 * Get orders
	 * 
	 * @param patient
	 * @param date
	 * @param concept
	 * @return
	 * @throws ParseException
	 */
	public List<Order> getOrders(Patient patient, Date date, Concept concept)
			throws ParseException;
}
