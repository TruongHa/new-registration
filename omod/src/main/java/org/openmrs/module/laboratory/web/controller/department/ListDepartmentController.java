package org.openmrs.module.laboratory.web.controller.department;

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.LaboratoryCoreService;
import org.openmrs.module.hospitalcore.model.Lab;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("LaboratoryListDepartmentController")
@RequestMapping("/module/laboratory/listDepartment.form")
public class ListDepartmentController {
	
	
	@ModelAttribute("departments")
	public List<Lab> getDepartments(){
		LaboratoryCoreService rs = (LaboratoryCoreService) Context.getService(LaboratoryCoreService.class);
		return rs.getAllDepartments();
	}	

	@RequestMapping(method = RequestMethod.GET)
	public String listForms(
			Model model) {
		return "/module/laboratory/department/list";
	}
}
