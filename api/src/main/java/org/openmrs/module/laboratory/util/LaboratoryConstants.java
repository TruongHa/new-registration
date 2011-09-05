package org.openmrs.module.laboratory.util;

public class LaboratoryConstants {
	
	public static final String MODULE_ID = "laboratory";
	public static final String TEST_STATUS_ACCEPTED = "accepted";
	public static final String TEST_STATUS_COMPLETED = "completed";
	public static final String UNACCEPT_TEST_RETURN_STATUS_SUCCESS = "success";	
	public static final String UNACCEPT_TEST_RETURN_STATUS_NOT_FOUND = "not found";
	public static final String UNACCEPT_TEST_RETURN_STATUS_PRINTED = "printed";
	public static final String RESCHEDULE_TEST_RETURN_STATUS_SUCCESS = "success";
	public static final String RESCHEDULE_TEST_RETURN_STATUS_PRINTED = "printed";
	public static final String RESCHEDULE_TEST_RETURN_STATUS_ENTERED = "entered";
	public static final String COMPLETE_TEST_RETURN_STATUS_SUCCESS = "success";
	public static final String COMPLETE_TEST_RETURN_STATUS_NOT_ACCEPTED = "not accepted";
	public static final String PROPERTY_PRINTWORKLIST_PRINTALLTEST = MODULE_ID + ".printWorkList.printAllTest";
	public static final String PROPERTY_PRINTWORKLIST_TESTOPTION_DISPLAY = MODULE_ID + ".printWorkList.testOption.display";
	public static final String PROPERTY_PRINTWORKLIST_PATIENTSEARCHBOX_DISPLAY = MODULE_ID + ".printWorkList.patientSearchBox.display";
	public static final String PROPERTY_RADIOLOGY_ENCOUNTER = MODULE_ID + ".radiologyEncounterType";
	public static final String PROPERTY_MAINTAINCODE = MODULE_ID + ".maintainCode";	
	public static final String SESSION_TEST_TREE_MAP = MODULE_ID + ".testTreeMap";
	public static final Integer PAGESIZE = 20;
	public static final Integer ACCEPT_TEST_RETURN_ERROR_EXISTING_SAMPLEID = -2;
	public static final Integer ACCEPT_TEST_RETURN_ERROR_EXISTING_TEST = -1;
	
}
