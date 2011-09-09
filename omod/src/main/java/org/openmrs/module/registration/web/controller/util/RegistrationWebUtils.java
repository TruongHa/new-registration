package org.openmrs.module.registration.web.controller.util;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.OpdPatientQueue;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.registration.util.RegistrationConstants;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.ui.Model;

public class RegistrationWebUtils {
	
	/**
	 * Optimize the request's parameters
	 * @param request
	 * @return
	 */
	public static Map<String, String> optimizeParameters(HttpServletRequest request) {
		Map<String, String> parameters = new HashMap<String, String>();
		for (@SuppressWarnings("rawtypes")
		Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String parameterName = (String) e.nextElement();
			String[] values = request.getParameterValues(parameterName);			
			String value = StringUtils.join(values, ',');
			parameters.put(parameterName, value);
		}
		return parameters;
	}
	
	/**
	 * Get the list of concepts which are answered for a concept
	 * @param conceptName
	 * @return
	 */
	public static String getSubConcepts(String conceptName){
		Concept opdward = Context.getConceptService().getConcept(conceptName);
		StringBuilder sb = new StringBuilder();
		for (ConceptAnswer ca : opdward.getAnswers()) {
			sb.append(ca.getAnswerConcept().getConceptId() + "," + ca
					.getAnswerConcept().getName().getName() + "|");
		}
		return sb.toString();
	}
	
	/**
	 * Send patient for OPD Queue
	 * @param patient
	 * @param selectedOPDConcept
	 */
	public static void sendPatientToOPDQueue(Patient patient, Concept selectedOPDConcept){
		String gpNewPatient = Context.getAdministrationService().getGlobalProperty("registration.newPatientConcept");
        Concept conNewPatient = Context.getConceptService().getConcept(gpNewPatient);
        if( conNewPatient == null ){        	
			
			ConceptDatatype datatype = Context.getConceptService().getConceptDatatypeByName("N/A");
			ConceptClass conceptClass = Context.getConceptService().getConceptClassByName("Misc");
			conNewPatient = new Concept();
			ConceptName name = new ConceptName(gpNewPatient, Context.getLocale());
			conNewPatient.addName(name);
			conNewPatient.setDatatype(datatype);
			conNewPatient.setConceptClass(conceptClass);
			Context.getConceptService().saveConcept(conNewPatient);
        }
        OpdPatientQueue queue = new OpdPatientQueue();
        queue.setPatient(patient);
        queue.setCreatedOn(new Date());
        queue.setBirthDate(patient.getBirthdate());
        queue.setPatientIdentifier(patient.getPatientIdentifier().getIdentifier());
        queue.setOpdConcept(selectedOPDConcept);
        queue.setOpdConceptName(selectedOPDConcept.getName().getName());
        queue.setPatientName(patient.getGivenName()+" "+patient.getMiddleName() + " "+ patient.getFamilyName());
        queue.setReferralConcept(conNewPatient);
        queue.setReferralConceptName(conNewPatient.getName().getName());
        queue.setSex(patient.getGender());
        PatientQueueService queueService = Context.getService(PatientQueueService.class);
        queueService.saveOpdPatientQueue(queue);
	}
	
	/**
	 * Get the list of address
	 * @param model
	 * @throws MalformedURLException
	 * @throws DocumentException
	 * @throws JaxenException
	 */
	public static void getAddressData(Model model) throws MalformedURLException,
			DocumentException, JaxenException {
		File addressFile = new File(OpenmrsUtil.getApplicationDataDirectory()
				+ "addresshierarchy.xml");
		if (addressFile.exists()) {
			SAXReader reader = new SAXReader();
			Document document = reader.read(addressFile.toURI().toURL());
			XPath distSelector = new Dom4jXPath("//state/district");
			@SuppressWarnings("rawtypes")
			List distList = distSelector.selectNodes(document);
			String[] distArr = new String[distList.size()];
			String[] tehsilArr = new String[distList.size()];
			if (distList.size() > 0) {
				for (int i = 0; i < distList.size(); i++) {
					distArr[i] = ((Element) distList.get(i))
							.attributeValue("name");
					@SuppressWarnings("rawtypes")
					List tehsilList = ((Element) distList.get(i))
							.elements("tehsil");
					tehsilArr[i] = ((Element) tehsilList.get(0))
							.attributeValue("name") + ",";
					for (int j = 1; j < (tehsilList.size() - 1); j++) {
						tehsilArr[i] += ((Element) tehsilList.get(j))
								.attributeValue("name") + ",";
					}
					tehsilArr[i] += ((Element) tehsilList
							.get(tehsilList.size() - 1)).attributeValue("name");
				}
			}
			model.addAttribute("districts", distArr);
			model.addAttribute("tehsils", tehsilArr);
		}
	}
	
	/**
	 * Create a new encounter
	 * @param patient
	 * @param revisit
	 * @return
	 */
	public static Encounter createEncounter(Patient patient, boolean revisit){
		EncounterType encounterType = null;
		if (!revisit) {
			String encounterTypeName = GlobalPropertyUtil.getString(
					RegistrationConstants.PROPERTY_ENCOUNTER_TYPE_REGINIT,
					"REGINITIAL");
			encounterType = Context.getEncounterService().getEncounterType(
					encounterTypeName);
		} else {
			String encounterTypeName = GlobalPropertyUtil.getString(
					RegistrationConstants.PROPERTY_ENCOUNTER_TYPE_REVISIT,
					"REGREVISIT");
			encounterType = Context.getEncounterService().getEncounterType(
					encounterTypeName);
		}
		
		// get location
		Location location = new Location(GlobalPropertyUtil.getInteger(
				RegistrationConstants.PROPERTY_LOCATION, 1));		

		// create encounter
		Encounter encounter = new Encounter();
		encounter.setEncounterType(encounterType);
		encounter.setCreator(Context.getAuthenticatedUser());
		encounter.setProvider(Context.getAuthenticatedUser().getPerson());
		encounter.setEncounterDatetime(new Date());
		encounter.setPatient(patient);
		encounter.setLocation(location);	
		
		return encounter;
	}
}
