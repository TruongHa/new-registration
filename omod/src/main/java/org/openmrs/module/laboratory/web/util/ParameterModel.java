package org.openmrs.module.laboratory.web.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ParameterModel implements Comparable<ParameterModel> {
	private String id;
	private String type;
	private String title;
	private List<String> optionValues = new ArrayList<String>();
	private List<String> optionLabels = new ArrayList<String>();
	private String unit;
	private String validator;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getOptionValues() {
		return optionValues;
	}

	public void setOptionValues(List<String> optionValues) {
		this.optionValues = optionValues;
	}

	public List<String> getOptionLabels() {
		return optionLabels;
	}

	public void setOptionLabels(List<String> optionLabels) {
		this.optionLabels = optionLabels;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}	

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public int compareTo(ParameterModel o) {
		if (StringUtils.isBlank(o.getId()))
			return 1;
		if (StringUtils.isBlank(this.getId()))
			return -1;
		String thisId = id;
		String oId = o.getId();
		return thisId.compareToIgnoreCase(oId);
	}

	public String toString() {
		return "ParameterModel [id=" + id + "]";
	}
}
