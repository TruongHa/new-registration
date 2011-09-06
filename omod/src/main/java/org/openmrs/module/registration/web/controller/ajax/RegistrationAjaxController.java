package org.openmrs.module.registration.web.controller.ajax;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.hospitalcore.util.DateUtils;
import org.openmrs.module.registration.util.RegistrationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("RegistrationAjaxController")
public class RegistrationAjaxController {

	private static Log logger = LogFactory
			.getLog(RegistrationAjaxController.class);

	/**
	 * process patient birth date
	 * @param birthdate
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/module/registration/ajax/processPatientBirthDate.htm", method = RequestMethod.GET)
	public String processPatientBirthDate(@RequestParam("birthdate") String birthdate, Model model) throws ParseException {		
		
		Map<String, Object> json = new HashMap<String, Object>();
		
		// try to parse date
		// if success -> it's a birthdate
		// otherwise -> it's an age
		Date date = null;
		try {
			date = RegistrationUtils.parseDate(birthdate);	
		} catch (ParseException e) {
			
		}
		
		if(date!=null){
			
			// the user entered the correct birthdate
			json.put("estimated", false);
			json.put("birthdate", birthdate);	
			json.put("age", estimateAge(birthdate));
			logger.info("User entered the correct birthdate.");
			
		} else {
			
			// the user entered an age			
			Integer age = Integer.parseInt(birthdate);
			json.put("estimated", true);
			String estimatedBirthdate = getEstimatedBirthdate(age);
			json.put("birthdate", estimatedBirthdate);
			json.put("age", estimateAge(estimatedBirthdate));
			logger.info("User entered an estimated age.");
		}
		model.addAttribute("json", json);
		return "/module/registration/ajax/processPatientBirthDate";
	}
	
	/*
	 * Estimate the birthdate by age
	 * @param age
	 * @return
	 */
	private String getEstimatedBirthdate(int age){
		Calendar date = Calendar.getInstance();
		date.add(Calendar.YEAR, -age);
		return "01/01/" + date.get(Calendar.YEAR);		
	}
	
	/*
	 * Estimate the year by birthdate 
	 * @param birthdate
	 * @return
	 * @throws ParseException
	 */
	private String estimateAge(String birthdate) throws ParseException{
		Date date = RegistrationUtils.parseDate(birthdate);
		int years = DateUtils.getAgeFromBirthday(date);
		if(years>1){
			return String.format("~ %s years old", years);
		} else {
			return "~ 1 year old";
		}
	}
}
