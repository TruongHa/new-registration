package org.openmrs.module.laboratory.web.controller.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.laboratory.web.util.LaboratoryUtil;

public class ConceptPropertyEditor extends PropertyEditorSupport {
	private Log log = LogFactory.getLog(this.getClass());

	public ConceptPropertyEditor() {
	}

	public void setAsText(String text) throws IllegalArgumentException {
		Context.getConceptService();
		if (text != null && text.trim().length() > 0) {
			try {
				Concept concept = LaboratoryUtil.searchConcept(text);
				setValue(concept);
			} catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("Concept not found: "
						+ ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}

	public String getAsText() {
		Concept concept = (Concept) getValue();
		if (concept == null) {
			return null;
		} else {
			return concept.getName().getName();
		}
	}
}
