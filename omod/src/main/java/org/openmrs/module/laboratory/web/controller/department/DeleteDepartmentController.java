package org.openmrs.module.laboratory.web.controller.department;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.Lab;
import org.openmrs.module.laboratory.LaboratoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("LaboratoryDeleteDepartmentController")
@RequestMapping("/module/laboratory/deleteDepartment.form")
public class DeleteDepartmentController {

	@RequestMapping(method = RequestMethod.GET)
	public String deleteForm(@RequestParam("id") Integer id,
			Model model) {
		LaboratoryService ls = (LaboratoryService) Context.getService(LaboratoryService.class);
		Lab department = ls.getLaboratoryDepartment(id);
		if(department!=null){
			ls.deleteLaboratoryDepartment(department);
		}
		return "redirect:/module/laboratory/listDepartment.form";
	}

}
