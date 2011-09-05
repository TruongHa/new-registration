package org.openmrs.module.laboratory.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.ConceptWord;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.concept.TestTree;
import org.openmrs.module.hospitalcore.model.Lab;
import org.openmrs.module.hospitalcore.model.LabTest;
import org.openmrs.module.hospitalcore.util.PatientUtil;
import org.openmrs.module.laboratory.LaboratoryService;
import org.springframework.ui.Model;

public class LaboratoryUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static Map<Concept, String> conceptNames = new HashMap<Concept, String>();

	/**
	 * Generate list of test model using orders
	 * 
	 * @param orders
	 * @return
	 */
	public static List<TestModel> generateModelsFromOrders(List<Order> orders,
			Map<Concept, Set<Concept>> testTreeMap) {

		List<TestModel> models = new ArrayList<TestModel>();
		for (Order order : orders) {
			TestModel tm = generateModel(order, testTreeMap);
			models.add(tm);
		}
		return models;
	}

	/**
	 * Generate list of test models using tests
	 * 
	 * @param tests
	 * @return
	 */
	public static List<TestModel> generateModelsFromTests(List<LabTest> tests,
			Map<Concept, Set<Concept>> testTreeMap) {

		List<TestModel> models = new ArrayList<TestModel>();
		for (LabTest test : tests) {
			TestModel tm = generateModel(test, testTreeMap);
			models.add(tm);
		}
		return models;
	}

	/**
	 * Generate a single test model
	 * 
	 * @param order
	 * @return
	 */
	public static TestModel generateModel(Order order,
			Map<Concept, Set<Concept>> testTreeMap) {
		LaboratoryService ls = (LaboratoryService) Context
				.getService(LaboratoryService.class);
		LabTest test = ls.getLaboratoryTest(order);
		return generateModel(order, test, testTreeMap);
	}

	/**
	 * Generate a single test model
	 * 
	 * @param test
	 * @return
	 */
	public static TestModel generateModel(LabTest test,
			Map<Concept, Set<Concept>> testTreeMap) {
		return generateModel(test.getOrder(), test, testTreeMap);
	}

	/**
	 * Format a date
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return sdf.format(date);
	}

	/**
	 * Parse a string into a date
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr) throws ParseException {
		return sdf.parse(dateStr);
	}

	private static Set<Concept> getAllInvestigations() {
		LaboratoryService ls = (LaboratoryService) Context
				.getService(LaboratoryService.class);
		Lab department = ls.getCurrentDepartment();
		if (department != null) {
			Set<Concept> investigations = department
					.getInvestigationsToDisplay();
			return investigations;
		}
		return null;
	}

	/**
	 * Get all test trees for all investigation belongs to a department
	 * 
	 * @return
	 */
	public static List<TestTree> getAllTestTrees() {
		List<TestTree> trees = new ArrayList<TestTree>();
		Set<Concept> concepts = getAllInvestigations();
		for (Concept concept : concepts) {
			TestTree tree = new TestTree(concept);
			trees.add(tree);
		}
		return trees;
	}

	/*
	 * REFACTORING
	 */
	private static TestModel generateModel(Order order, LabTest test,
			Map<Concept, Set<Concept>> testTreeMap) {

		TestModel tm = new TestModel();
		tm.setStartDate(sdf.format(order.getStartDate()));
		tm.setPatientIdentifier(order.getPatient().getPatientIdentifier()
				.getIdentifier());
		tm.setPatientName(PatientUtil.getFullName(order.getPatient()));
		tm.setGender(order.getPatient().getGender());
		tm.setAge(order.getPatient().getAge());
		tm.setTestName(order.getConcept().getName().getName());
		tm.setOrderId(order.getOrderId());

		if (test != null) {
			tm.setStatus(test.getStatus());
			tm.setTestId(test.getLabTestId());
			tm.setAcceptedDate(sdf.format(test.getAcceptDate()));
			tm.setConceptId(test.getConcept().getConceptId());
			tm.setSampleId(test.getSampleNumber());
			if(test.getEncounter()!=null)
				tm.setEncounterId(test.getEncounter().getEncounterId());
		} else {
			tm.setStatus(null);
		}

		// get investigation from test tree map
		if (testTreeMap != null) {
			tm.setInvestigation(getInvestigationName(order.getConcept(), testTreeMap));
		}

		return tm;
	}
	
	public static String getInvestigationName(Concept concept, Map<Concept, Set<Concept>> testTreeMap){
		for (Concept investigationConcept : testTreeMap.keySet()) {
			Set<Concept> set = testTreeMap.get(investigationConcept);
			if (set.contains(concept)) {
				if (conceptNames.containsKey(investigationConcept)) {
					return conceptNames.get(investigationConcept);
				} else {
					Concept newInvestigationConcept = Context
							.getConceptService()
							.getConcept(investigationConcept.getConceptId());
					conceptNames.put(newInvestigationConcept, newInvestigationConcept.getName().getName());
					return conceptNames.get(newInvestigationConcept);
				}
			}
		}
		return null;
	}
	

	/**
	 * Search for concept using name
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Concept searchConcept(String name) {
		Concept concept = Context.getConceptService().getConcept(name);
		if (concept != null) {
			return concept;
		} else {
			List<ConceptWord> cws = Context.getConceptService().findConcepts(
					name, new Locale("en"), false);
			if (!cws.isEmpty())
				return cws.get(0).getConcept();
		}
		return null;
	}

	/**
	 * generate data for form from an existing encounter
	 * 
	 * @param model
	 * @param encounter
	 */
	public static void generateDataFromEncounter(Model model,
			Encounter encounter) {
		if (encounter != null) {
			List<String> inputNames = new ArrayList<String>();
			List<String> inputValues = new ArrayList<String>();
			for (Obs obs : getOrderedObs(encounter)) {
				inputNames.add(obs.getConcept().getName().getName());
				inputValues.add(getObsValue(obs));
			}
			model.addAttribute("inputNames", inputNames);
			model.addAttribute("inputValues", inputValues);
			model.addAttribute("inputLength", inputValues.size());
		}
	}

	private static List<Obs> getOrderedObs(Encounter encounter) {
		List<Obs> obs = new ArrayList<Obs>();
		obs.addAll(encounter.getAllObs());
		Collections.sort(obs, new Comparator<Obs>() {

			public int compare(Obs o1, Obs o2) {
				String o1n = o1.getConcept().getName().getName();
				String o2n = o2.getConcept().getName().getName();
				return o1n.compareToIgnoreCase(o2n);
			}
		});
		return obs;
	}

	private static String getObsValue(Obs obs) {
		Concept concept = obs.getConcept();
		if (concept.getDatatype().getName().equalsIgnoreCase("Text")) {
			return obs.getValueText();
		} else if (concept.getDatatype().getName().equalsIgnoreCase("Numeric")) {
			return obs.getValueNumeric().toString();
		} else if (concept.getDatatype().getName().equalsIgnoreCase("Coded")) {
			return obs.getValueCoded().getName().getName();
		}
		return null;
	}
	
	/**
	 * Get name of a detached by hibernate session concept
	 * 
	 * @param searchConcept
	 * @return
	 */
	public static String getConceptName(Concept searchConcept) {
		if (conceptNames.containsKey(searchConcept)) {
			return conceptNames.get(searchConcept);
		} else {
			Concept concept = Context.getConceptService().getConcept(
					searchConcept.getConceptId());
			conceptNames.put(searchConcept, concept.getName().getName());
			return conceptNames.get(searchConcept);
		}
	}
}
