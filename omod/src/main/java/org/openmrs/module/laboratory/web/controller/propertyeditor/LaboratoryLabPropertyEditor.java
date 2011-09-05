package org.openmrs.module.laboratory.web.controller.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.RadiologyDepartment;
import org.openmrs.module.laboratory.LaboratoryService;

public class LaboratoryLabPropertyEditor extends PropertyEditorSupport {
	private Log log = LogFactory.getLog(this.getClass());

	public LaboratoryLabPropertyEditor() {
	}

	public void setAsText(String text) throws IllegalArgumentException {
		LaboratoryService ls = (LaboratoryService) Context.getService(LaboratoryService.class);
		if (text != null && text.trim().length() > 0) {
			try {
				setValue(ls.getLaboratoryDepartment(NumberUtils.toInt(text)));
			} catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("Radiology department not found: "
						+ ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}

	public String getAsText() {
		RadiologyDepartment department = (RadiologyDepartment) getValue();
		if (department == null) {
			return null;
		} else {
			return department.getId() + "";
		}
	}
}
