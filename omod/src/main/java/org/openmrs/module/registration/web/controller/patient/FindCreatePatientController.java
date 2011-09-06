package org.openmrs.module.registration.web.controller.patient;

import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonName;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.registration.util.RegistrationConstants;
import org.openmrs.module.registration.util.RegistrationUtils;
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
			throws MalformedURLException, JaxenException, DocumentException {
		String form = GlobalPropertyUtil.getString(
				RegistrationConstants.PROPERTY_FORM,
				"Please define registering form");
		model.addAttribute("patientIdentifier",
				RegistrationUtils.getNewIdentifier());
		model.addAttribute("form", form);
		getAddressData(model);


		return "/module/registration/patient/findCreatePatient";
	}

	/*
	 * Get address list from xml file
	 */
	private void getAddressData(Model model) throws MalformedURLException,
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

	@RequestMapping(method = RequestMethod.POST)
	public String savePatient(HttpServletRequest request, Model model)
			throws ParseException {
		
		// list all parameter submitted
		String parameters = "";
		for (@SuppressWarnings("rawtypes")
		Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String parameterName = (String) e.nextElement();
			parameters += parameterName + ", ";
		}
		parameters = parameters.substring(0, parameters.length()-2);
		logger.info("Submited parameters: " + parameters);

		// create patient
		Patient patient = generatePatient(request);
		logger.info(String
				.format("Saving new patient[identifier=%s, name=%s, birthdate=%s, gender=%s, address=%s]",
						patient.getPatientIdentifier().getIdentifier(),
						patient.getGivenName() + " " + patient.getMiddleName()
								+ " " + patient.getFamilyName(),
						patient.getBirthdate(), patient.getGender(), patient.getPersonAddress().getCountyDistrict() + " " + patient.getPersonAddress().getCityVillage()));
		// Context.getPatientService().savePatient(patient);

		// create init encounter
		return "/module/registration/patient/findCreatePatient";
	}

	/*
	 * Generate patient from request
	 * 
	 * @param request
	 * 
	 * @return
	 */
	private Patient generatePatient(HttpServletRequest request)
			throws ParseException {

		Patient patient = new Patient();

		// get person name
		if (!StringUtils.isBlank(request
				.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_NAME))) {
			PersonName personName = RegistrationUtils
					.getPersonName(request
							.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_NAME));
			patient.addName(personName);
		}

		// get identifier
		if (!StringUtils
				.isBlank(request
						.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_IDENTIFIER))) {
			PatientIdentifier identifier = RegistrationUtils
					.getPatientIdentifier(request
							.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_IDENTIFIER));
			patient.addIdentifier(identifier);
		}

		// get birthdate
		if (!StringUtils
				.isBlank(request
						.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE))) {
			patient.setBirthdate(RegistrationUtils.parseDate(request
					.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE)));
			if (request
					.getParameter(
							RegistrationConstants.FORM_FIELD_PATIENT_BIRTHDATE_ESTIMATED)
					.contains("true")) {
				patient.setBirthdateEstimated(true);
			}
		}

		// get gender
		if (!StringUtils.isBlank(request
				.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_GENDER))) {
			patient.setGender(request
					.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_GENDER));
		}

		// get address
		if (!StringUtils
				.isBlank(request
						.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_DISTRICT))) {
			patient.addAddress(RegistrationUtils.getPersonAddress(
					request.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_DISTRICT),
					request.getParameter(RegistrationConstants.FORM_FIELD_PATIENT_ADDRESS_TEHSIL)));
		}
		return patient;
	}

}
