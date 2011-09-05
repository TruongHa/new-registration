package org.openmrs.module.laboratory.web.controller.department;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.Role;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.LaboratoryCoreService;
import org.openmrs.module.hospitalcore.model.Lab;
import org.openmrs.module.laboratory.LaboratoryService;
import org.openmrs.module.laboratory.web.controller.propertyeditor.LaboratoryLabPropertyEditor;
import org.openmrs.module.laboratory.web.controller.propertyeditor.RolePropertyEditor;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("LaboratoryEditDepartmentController")
@RequestMapping("/module/laboratory/editDepartment.form")
public class EditDepartmentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Lab.class,
				new LaboratoryLabPropertyEditor());
		binder.registerCustomEditor(Role.class, new RolePropertyEditor());
		binder.registerCustomEditor(Set.class, "investigationsToDisplay",
				new CustomCollectionEditor(Set.class) {
					ConceptService conceptService = Context.getConceptService();

					protected Object convertElement(Object element) {
						String conceptName = null;
						if (element instanceof String)
							conceptName = (String) element;
						return conceptName != null ? conceptService
								.getConcept(conceptName) : null;
					}
				});
		binder.registerCustomEditor(Set.class, "confidentialTestsToDisplay",
				new CustomCollectionEditor(Set.class) {
					ConceptService conceptService = Context.getConceptService();

					protected Object convertElement(Object element) {
						String conceptName = null;
						if (element instanceof String)
							conceptName = (String) element;
						return conceptName != null ? conceptService
								.getConcept(conceptName) : null;
					}
				});
	}

	@ModelAttribute("department")
	public Lab getDepartment(
			@RequestParam(value = "id", required = false) Integer id) {
		Lab department = new Lab();
		if (id != null) {
			LaboratoryService ls = (LaboratoryService) Context
					.getService(LaboratoryService.class);
			department = ls.getLaboratoryDepartment(id);
		}
		return department;
	}

	@ModelAttribute("roles")
	public List<Role> getRoles(
			@RequestParam(value = "id", required = false) Integer id) {		
		List<Role> roles = Context.getUserService().getAllRoles();
		LaboratoryCoreService lcs = Context.getService(LaboratoryCoreService.class);
		List<Lab> departments = lcs.getAllDepartments();

		// get all existing roles
		Set<Role> existingRoles = new HashSet<Role>();
		for (Lab d : departments) {
			if (!d.getLabId().equals(id)) {
				existingRoles.add(d.getRole());
			} else {
				System.out.println("department ==> " + d);
			}
		}

		// eliminate them from all roles list
		for (Role r : existingRoles) {
			roles.remove(r);
		}

		return roles;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showDepartment(
			@RequestParam(value = "id", required = false) Integer id,
			Model model) {
		return "/module/laboratory/department/edit";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String saveForm(
			@ModelAttribute("department") Lab department,
			Model model) {
		LaboratoryService ls = (LaboratoryService) Context
				.getService(LaboratoryService.class);
		ls.saveLaboratoryDepartment(department);
		return "redirect:/module/laboratory/listDepartment.form";
	}
}
