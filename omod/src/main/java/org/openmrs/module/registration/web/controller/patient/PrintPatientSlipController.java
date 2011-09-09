package org.openmrs.module.registration.web.controller.patient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("PrintPatientSlipController")
@RequestMapping("/module/registration/printPatientSlip.form")
public class PrintPatientSlipController {

	private static Log logger = LogFactory
			.getLog(PrintPatientSlipController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String showPatientSlip(Model model){
		return "/module/registration/patient/printPatientSlip";
	}
}
