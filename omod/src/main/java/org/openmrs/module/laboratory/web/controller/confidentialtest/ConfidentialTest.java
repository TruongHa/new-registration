package org.openmrs.module.laboratory.web.controller.confidentialtest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("LaboratoryConfidentialTestController")
@RequestMapping("/module/laboratory/confidentialTest.form")
public class ConfidentialTest {
	
	@RequestMapping(method = RequestMethod.GET)
	public String showForm(Model model) {		
		return "/module/laboratory/confidentialtest/list";
	}
}
