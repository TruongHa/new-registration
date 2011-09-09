package org.openmrs.module.registration.web.controller.patient;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.jaxen.JaxenException;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.registration.util.RegistrationConstants;
import org.openmrs.module.registration.util.RegistrationUtils;
import org.openmrs.module.registration.web.controller.util.RegistrationWebUtils;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("RegistrationFindCreatePatientController")
@RequestMapping("/findPatient.htm")
public class FindCreatePatientController {

	private static Log logger = LogFactory
			.getLog(FindCreatePatientController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showForm(HttpServletRequest request, Model model)
			throws JaxenException, DocumentException, IOException {
		String formFilename = GlobalPropertyUtil.getString(
				RegistrationConstants.PROPERTY_FORM,
				"Please define registering form");
		File file = new File(OpenmrsUtil.getApplicationDataDirectory()
				+ File.separator + "registration" + File.separator
				+ formFilename);
		String form = FileUtils.readFileToString(file);
		model.addAttribute("patientIdentifier",
				RegistrationUtils.getNewIdentifier());
		model.addAttribute("form", form);
		model.addAttribute("OPDs", RegistrationWebUtils
				.getSubConcepts(RegistrationConstants.CONCEPT_NAME_OPD_WARD));
		model.addAttribute(
				"referralHospitals",
				RegistrationWebUtils
						.getSubConcepts(RegistrationConstants.CONCEPT_NAME_PATIENT_REFERRED_FROM));
		model.addAttribute(
				"referralReasons",
				RegistrationWebUtils
						.getSubConcepts(RegistrationConstants.CONCEPT_NAME_REASON_FOR_REFERRAL));
		RegistrationWebUtils.getAddressData(model);
		return "/module/registration/patient/findCreatePatient";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String savePatient(HttpServletRequest request, Model model)
			throws ParseException {

		// list all parameter submitted
		Map<String, String> parameters = RegistrationWebUtils
				.optimizeParameters(request);
		logger.info("Submited parameters: " + parameters);

		// create patient
		Patient patient = generatePatient(parameters);
		patient = Context.getPatientService().savePatient(patient);
		logger.info(String.format("Saved new patient [id=%s]", patient.getId()));

		// create encounter for the visit
		Encounter encounter = createEncounter(patient, parameters);
		encounter = Context.getEncounterService().saveEncounter(encounter);
		logger.info(String.format("Saved encounter for the visit of patient [id=%s, patient=%s]", encounter.getId(), patient.getId()));
		
		return "redirect:/module/registration/showPatientInfo.form?patientId="
				+ patient.getPatientId() + "&encounterId=" + encounter.getId();
	}

	/*
	 * Generate Patient From Parameters
	 * 
	 * @param request
	 * 
	 * @return
	 */
	private Patient generatePatient(Map<String, String> parameters)
			throws ParseException {

		Patient patient = new Patient();

		// get person name
		if (!StringUtils.isBlank(parameters
				.get(RegistrationConstants.FORM_FIELD_PATIENT_NAME))) {
			PersonName personName = RegistrationUtils.getPersonName(parameters
					.get(RegistrationConstants.FORM_FIELD_PATIENT_NAME));
			patient.addName(personName);
		}

		// get identifier
		if (!StringUtils.isBlank(parameters
				.get(RegistrationConstants.FORM_FIELD_PATIENT_IDENTIFIER))) {
			PatientIdentifier identifier = RegistrationUtils
					.getPatientIdentifier(parameters
							.get(RegistrationConstants.FORM_FIELD_PATIENT_IDENTIFIER));
			patient.addIdentifier(identifier);
		}

		// get birthdate
		if (!StringUtils.isBlank(parameters
				.get(RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE))) {
			patient.setBirthdate(RegistrationUtils.parseDate(parameters
					.get(RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE)));
			if (parameters
					.get(RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE_ESTIMATED)
					.contains("true")) {
				patient.setBirthdateEstimated(true);
			}
		}

		// get gender
		if (!StringUtils.isBlank(parameters
				.get(RegistrationConstants.FORM_FIELD_PATIENT_GENDER))) {
			patient.setGender(parameters
					.get(RegistrationConstants.FORM_FIELD_PATIENT_GENDER));
		}

		// get address
		if (!StringUtils
				.isBlank(parameters
						.get(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_DISTRICT))) {
			patient.addAddress(RegistrationUtils.getPersonAddress(
					parameters
							.get(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_DISTRICT),
					parameters
							.get(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_TEHSIL)));
		}

		// get custom person attribute
		for (String name : parameters.keySet()) {
			if ((name.contains(".attribute."))
					&& (!StringUtils.isBlank(parameters.get(name)))) {
				String[] parts = name.split("\\.");
				String idText = parts[parts.length - 1];
				Integer id = Integer.parseInt(idText);
				PersonAttribute attribute = RegistrationUtils
						.getPersonAttribute(id, parameters.get(name));
				patient.addAttribute(attribute);
			}
		}

		return patient;
	}

	/**
	 * Create Encouunter For The Visit Of Patient
	 * 
	 * @param patient
	 * @param parameters
	 * @return
	 */
	private Encounter createEncounter(Patient patient,
			Map<String, String> parameters) {

		Encounter encounter = RegistrationWebUtils.createEncounter(patient,
				false);

		/*
		 * ADD OPD ROOM
		 */
		Concept opdWardConcept = Context.getConceptService().getConcept(
				RegistrationConstants.CONCEPT_NAME_OPD_WARD);
		Concept selectedOPDConcept = Context
				.getConceptService()
				.getConcept(
						Integer.parseInt(parameters
								.get(RegistrationConstants.FORM_FIELD_PATIENT_OPD_WARD)));
		Obs opdObs = new Obs();
		opdObs.setConcept(opdWardConcept);
		opdObs.setValueCoded(selectedOPDConcept);
		encounter.addObs(opdObs);

		/*
		 * REFERRAL INFORMATION
		 */
		Obs referralObs = new Obs();
		Concept referralConcept = Context
				.getConceptService()
				.getConcept(
						RegistrationConstants.CONCEPT_NAME_PATIENT_REFERRED_TO_HOSPITAL);
		referralObs.setConcept(referralConcept);
		encounter.addObs(referralObs);
		if (!StringUtils.isBlank(parameters
				.get(RegistrationConstants.FORM_FIELD_PATIENT_REFERRED))) {
			referralObs.setValueCoded(Context.getConceptService().getConcept(
					"YES"));

			// referred from
			Obs referredFromObs = new Obs();
			Concept referredFromConcept = Context
					.getConceptService()
					.getConcept(
							RegistrationConstants.CONCEPT_NAME_PATIENT_REFERRED_FROM);
			referredFromObs.setConcept(referredFromConcept);
			referredFromObs
					.setValueCoded(Context
							.getConceptService()
							.getConcept(
									Integer.parseInt(parameters
											.get(RegistrationConstants.FORM_FIELD_PATIENT_REFERRED_FROM))));
			encounter.addObs(referredFromObs);

			// referred reason
			Obs referredReasonObs = new Obs();
			Concept referredReasonConcept = Context
					.getConceptService()
					.getConcept(
							RegistrationConstants.CONCEPT_NAME_REASON_FOR_REFERRAL);
			referredReasonObs.setConcept(referredReasonConcept);
			referredReasonObs
					.setValueCoded(Context
							.getConceptService()
							.getConcept(
									Integer.parseInt(parameters
											.get(RegistrationConstants.FORM_FIELD_PATIENT_REFERRED_REASON))));
			encounter.addObs(referredReasonObs);
		} else {
			referralObs.setValueCoded(Context.getConceptService().getConcept(
					"NO"));
		}
		return encounter;
	}

}
