package org.openmrs.module.laboratory.web.controller.worklist;

import java.util.Date;
import java.util.UUID;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.model.LabTest;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.laboratory.LaboratoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("LaboratoryEnterResultController")
@RequestMapping("/module/laboratory/enterResult.form")
public class EnterResultController {

	@RequestMapping(method = RequestMethod.GET)
	public String enterResult(
			@RequestParam(value = "testId") Integer testId,
			Model model) {
		LaboratoryService ls = (LaboratoryService) Context.getService(LaboratoryService.class);
		LabTest test = ls.getLaboratoryTest(testId);
		String encounterTypeStr = GlobalPropertyUtil.getString(BillingConstants.GLOBAL_PROPRETY_LAB_ENCOUNTER_TYPE, "LABENCOUNTER");
		EncounterType encounterType = Context.getEncounterService().getEncounterType(encounterTypeStr);
		Encounter enc = new Encounter();
		enc.setCreator(Context.getAuthenticatedUser());
		enc.setDateCreated(new Date());
		Location loc = Context.getLocationService().getLocation(1);		
		enc.setLocation(loc);
		enc.setPatient(test.getPatient());
		enc.setPatientId(test.getPatient().getId());		
		enc.setEncounterType(encounterType);
		enc.setVoided(false);
		enc.setProvider(Context.getAuthenticatedUser().getPerson());
		enc.setUuid(UUID.randomUUID().toString());		
		enc.setEncounterDatetime(new Date());		
		enc = Context.getEncounterService().saveEncounter(enc);		
		test.setEncounter(enc);
		ls.saveLaboratoryTest(test);
		return "redirect:/module/laboratory/showForm.form?encounterId=" + enc.getEncounterId() + "&conceptId=" + test.getConcept().getConceptId();
	}
}
