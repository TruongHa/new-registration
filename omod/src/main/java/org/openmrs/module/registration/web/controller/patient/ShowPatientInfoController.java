package org.openmrs.module.registration.web.controller.patient;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.registration.util.RegistrationConstants;
import org.openmrs.module.registration.web.controller.util.PatientModel;
import org.openmrs.module.registration.web.controller.util.RegistrationWebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("RegistrationShowPatientInfoController")
@RequestMapping("/module/registration/showPatientInfo.form")
public class ShowPatientInfoController {

	private static Log logger = LogFactory
			.getLog(ShowPatientInfoController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showPatientInfo(
			@RequestParam("patientId") Integer patientId,
			@RequestParam(value = "encounterId", required = false) Integer encounterId,
			Model model) throws IOException {

		Patient patient = Context.getPatientService().getPatient(patientId);
		PatientModel patientModel = new PatientModel(patient);
		model.addAttribute("patient", patientModel);		
		model.addAttribute("OPDs", RegistrationWebUtils
				.getSubConcepts(RegistrationConstants.CONCEPT_NAME_OPD_WARD));

		// Get current date
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yyyy kk:mm");
		model.addAttribute("currentDateTime", sdf.format(new Date()));

		return "/module/registration/patient/showPatientInfo";
	}

	@RequestMapping(method = RequestMethod.POST)
	public void savePatientInfo(
			@RequestParam("patientId") Integer patientId,
			@RequestParam(value = "encounterId", required = false) Integer encounterId,
			HttpServletRequest request, HttpServletResponse response)
			throws ParseException, IOException {

		Map<String, String> parameters = RegistrationWebUtils
				.optimizeParameters(request);

		// get patient
		Patient patient = Context.getPatientService().getPatient(patientId);

		/*
		 * SAVE ENCOUNTER
		 */
		Encounter encounter = null;
		if (encounterId != null) {
			encounter = Context.getEncounterService().getEncounter(encounterId);
		} else {
			encounter = RegistrationWebUtils.createEncounter(patient, true);

			// create OPD obs
			Concept opdWardConcept = Context.getConceptService().getConcept(
					RegistrationConstants.CONCEPT_NAME_OPD_WARD);
			Concept selectedOPDConcept = Context
					.getConceptService()
					.getConcept(
							Integer.parseInt(parameters
									.get(RegistrationConstants.FORM_FIELD_PATIENT_OPD_WARD)));
			Obs opd = new Obs();
			opd.setConcept(opdWardConcept);
			opd.setValueCoded(selectedOPDConcept);
			encounter.addObs(opd);
			RegistrationWebUtils.sendPatientToOPDQueue(patient,
					selectedOPDConcept);
		}

		// create temporary attributes
		for (String name : parameters.keySet()) {
			if ((name.contains(".attribute."))
					&& (!StringUtils.isBlank(parameters.get(name)))) {
				String[] parts = name.split("\\.");
				String idText = parts[parts.length - 1];
				Integer id = Integer.parseInt(idText);
				Concept temporaryAttributeConcept = Context.getConceptService()
						.getConcept(id);
				Obs temporaryAttribute = new Obs();
				temporaryAttribute.setConcept(temporaryAttributeConcept);
				logger.info("concept: " + temporaryAttributeConcept);
				logger.info("value: " + parameters.get(name));
				temporaryAttribute.setValueAsString(parameters.get(name));
				encounter.addObs(temporaryAttribute);
			}
		}

		// save encounter
		Context.getEncounterService().saveEncounter(encounter);
		logger.info(String
				.format("Save encounter for the visit of patient [encounterId=%s, patientId=%s]",
						encounter.getId(), patient.getId()));

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("success");
	}

}
