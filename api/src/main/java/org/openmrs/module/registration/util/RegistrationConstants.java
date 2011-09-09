package org.openmrs.module.registration.util;

public class RegistrationConstants {

	public static final String MODULE_ID = "registration";
	public static final String PROPERTY_FORM = MODULE_ID
			+ ".form.patient.register";
	public static final String PROPERTY_IDENTIFIER_PREFIX = MODULE_ID
			+ ".identifier_prefix";
	public static final String PROPERTY_PATIENT_IDENTIFIER_TYPE = MODULE_ID
			+ ".patientIdentifierType";
	public static final String PROPERTY_LOCATION = MODULE_ID + ".location";
	public static final String PROPERTY_PATIENT_INFO_FORM = MODULE_ID
			+ ".form.patient.info";
	public static final String PROPERTY_ENCOUNTER_TYPE_REGINIT = MODULE_ID
			+ ".encounterType.init";
	public static final String PROPERTY_ENCOUNTER_TYPE_REVISIT = MODULE_ID
			+ ".encounterType.revisit";

	// field names
	public static final String FORM_FIELD_PATIENT_NAME = "patient.name";
	public static final String FORM_FIELD_PATIENT_IDENTIFIER = "patient.identifier";
	public static final String FORM_FIELD_PATIENT_BIRTHDATE = "patient.birthdate";
	public static final String FORM_FIELD_PATIENT_BIRTHDATE_ESTIMATED = "patient.birthdateEstimate";
	public static final String FORM_FIELD_PATIENT_GENDER = "patient.gender";
	public static final String FORM_FIELD_PATIENT_ADDRESS_DISTRICT = "patient.address.district";
	public static final String FORM_FIELD_PATIENT_ADDRESS_TEHSIL = "patient.address.tehsil";
	public static final String FORM_FIELD_PATIENT_TEMPORARY_ATTRIBUTE = "patient.temporary.attribute";
	public static final String FORM_FIELD_PATIENT_OPD_WARD = "patient.opdWard";
	public static final String FORM_FIELD_PATIENT_REFERRED = "patient.referred";
	public static final String FORM_FIELD_PATIENT_REFERRED_FROM = "patient.referred.from";
	public static final String FORM_FIELD_PATIENT_REFERRED_REASON = "patient.referred.reason";

	// concept name
	public static final String CONCEPT_NAME_TEMPORARY_CATEGORY = "TEMPORARY CATEGORY";
	public static final String CONCEPT_NAME_OPD_WARD = "OPD WARD";
	public static final String CONCEPT_NAME_PATIENT_REFERRED_TO_HOSPITAL = "PATIENT REFERRED TO HOSPITAL?";
	public static final String CONCEPT_NAME_REASON_FOR_REFERRAL = "REASON FOR REFERRAL";
	public static final String CONCEPT_NAME_PATIENT_REFERRED_FROM = "PATIENT REFERRED FROM";

}
