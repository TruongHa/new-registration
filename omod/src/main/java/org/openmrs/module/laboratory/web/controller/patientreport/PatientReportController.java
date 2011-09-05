package org.openmrs.module.laboratory.web.controller.patientreport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.Lab;
import org.openmrs.module.laboratory.LaboratoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("LaboratoryPatientReportController")
@RequestMapping("/module/laboratory/patientReport.form")
public class PatientReportController {
	
	@ModelAttribute("investigations")
	public Set<Concept> getAllInvestigations(){
		LaboratoryService ls = (LaboratoryService) Context.getService(LaboratoryService.class);
		Lab department = ls.getCurrentDepartment();
		if(department!=null){
			Set<Concept> investigations = department.getInvestigationsToDisplay();
			return investigations;
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showView(
			Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = sdf.format(new Date());
		model.addAttribute("currentDate", dateStr);
		return "/module/laboratory/patientreport/list";
	}
}
