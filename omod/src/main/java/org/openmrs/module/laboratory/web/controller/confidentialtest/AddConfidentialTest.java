package org.openmrs.module.laboratory.web.controller.confidentialtest;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.model.Lab;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.laboratory.LaboratoryService;
import org.openmrs.module.laboratory.web.util.LaboratoryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("LaboratoryAddConfidentialTestController")
@RequestMapping("/module/laboratory/addConfidentialTest.form")
public class AddConfidentialTest {

	private Log logger = LogFactory.getLog(getClass());

	@ModelAttribute("confidentialTests")
	public Set<Concept> getConfidentialTests() {
		LaboratoryService ls = (LaboratoryService) Context
				.getService(LaboratoryService.class);
		Lab department = ls.getCurrentDepartment();
		if (department != null) {
			Set<Concept> investigations = department
					.getConfidentialTestsToDisplay();
			return investigations;
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showForm(
			@RequestParam("patientIdentifier") String patientIdentifider,
			Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = sdf.format(new Date());
		List<Patient> patients = Context.getPatientService().getPatients(
				patientIdentifider);
		if (!CollectionUtils.isEmpty(patients)) {
			Patient patient = patients.get(0);
			model.addAttribute("patient", patient);
		}
		model.addAttribute("currentDate", dateStr);
		return "/module/laboratory/confidentialtest/show";
	}

	@RequestMapping(method = RequestMethod.POST)
	public void saveOrder(
			@RequestParam("patientId") Integer patientId,
			@RequestParam(value = "confidentialTests", required = false) int[] confidentialTests,
			@RequestParam("date") String dateStr, Model model,
			HttpServletResponse response) throws IOException, ParseException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String error = null;
		if (!ArrayUtils.isEmpty(confidentialTests)) {
			if (validateDate(dateStr)) {

				Date date = LaboratoryUtil.parseDate(dateStr);
				Patient patient = Context.getPatientService().getPatient(
						patientId);

				Encounter encounter = getEncounter(patient);
				Integer labOrderTypeId = GlobalPropertyUtil.getInteger(
						BillingConstants.GLOBAL_PROPRETY_LAB_ORDER_TYPE, 2);
				OrderType orderType = Context.getOrderService().getOrderType(
						labOrderTypeId);

				for (int confidentialTestId : confidentialTests) {
					Concept concept = Context.getConceptService().getConcept(
							confidentialTestId);
					addOrder(encounter, concept, patient, orderType, date);
					String info = String
							.format("Saving confidential test[concept=%d, patient=%d, date=%s]",
									concept.getConceptId(),
									patient.getPatientId(), dateStr);
					logger.info(info);
				}

				Context.getEncounterService().saveEncounter(encounter);				
			} else {
				error = "Invalid date of confidential test.";
			}
		} else {
			error = "Please choose at least a confidential test.";
		}

		if (error == null) {
			out.print("success");
		} else {
			out.print(error);
		}
	}

	private Encounter getEncounter(Patient patient) {
		String encounterTypeText = GlobalPropertyUtil.getString(
				BillingConstants.GLOBAL_PROPRETY_LAB_ENCOUNTER_TYPE,
				"LABENCOUNTER");
		EncounterType encounterType = Context.getEncounterService()
				.getEncounterType(encounterTypeText);

		Encounter encounter = new Encounter();
		encounter.setCreator(Context.getAuthenticatedUser());
		Location location = Context.getLocationService().getLocation(1);
		encounter.setLocation(location);
		encounter.setDateCreated(new Date());
		encounter.setEncounterDatetime(new Date());
		encounter.setEncounterType(encounterType);
		encounter.setPatient(patient);
		encounter.setProvider(Context.getAuthenticatedUser().getPerson());
		return encounter;
	}

	private boolean validateDate(String dateStr) throws ParseException {
		Date date = LaboratoryUtil.parseDate(dateStr);
		Date now = new Date();
		Date currentDate = LaboratoryUtil.parseDate(LaboratoryUtil
				.formatDate(now));
		return date.compareTo(currentDate) >= 0;
	}

	private void addOrder(Encounter encounter, Concept concept,
			Patient patient, OrderType orderType, Date startDate)
			throws ParseException {

		if (checkExistingOrder(startDate, concept, patient)) {
			Order order = new Order();
			order.setConcept(concept);
			order.setCreator(Context.getAuthenticatedUser());
			order.setDateCreated(new Date());
			order.setOrderer(Context.getAuthenticatedUser());
			order.setPatient(patient);
			order.setStartDate(startDate);
			order.setAccessionNumber("0");
			order.setOrderType(orderType);
			order.setEncounter(encounter);
			encounter.addOrder(order);
		} else {
			String warning = String
					.format("Duplicated order already found[patient=%d, concept=%d, startDate=%s]",
							patient.getPatientId(), concept.getConceptId(),
							LaboratoryUtil.formatDate(startDate));
			logger.warn(warning);
		}
	}

	private boolean checkExistingOrder(Date startDate, Concept concept,
			Patient patient) throws ParseException {
		LaboratoryService ls = (LaboratoryService) Context
				.getService(LaboratoryService.class);
		List<Order> orders = ls.getOrders(patient, startDate, concept);
		return CollectionUtils.isEmpty(orders);
	}
}
