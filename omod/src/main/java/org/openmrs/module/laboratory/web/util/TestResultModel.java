package org.openmrs.module.laboratory.web.util;

import org.openmrs.Concept;

public class TestResultModel implements Comparable<TestResultModel> {

	public static String LEVEL_INVESTIGATION = "LEVEL_INVESTIGATION";
	public static String LEVEL_SET = "LEVEL_SET";
	public static String LEVEL_TEST = "LEVEL_TEST";
	public static String LEVEL_RESULT = "LEVEL_RESULT";

	public String investigation;
	public String set;
	public String test;
	public String value;
	public String hiNormal;
	public String lowNormal;
	public String unit;
	public String level = LEVEL_TEST;
	public Concept concept;
	public Integer encounterId;
	public Integer testId;

	public String getInvestigation() {
		return investigation;
	}

	public void setInvestigation(String investigation) {
		this.investigation = investigation;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getHiNormal() {
		return hiNormal;
	}

	public void setHiNormal(String hiNormal) {
		this.hiNormal = hiNormal;
	}

	public String getLowNormal() {
		return lowNormal;
	}

	public void setLowNormal(String lowNormal) {
		this.lowNormal = lowNormal;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Concept getConcept() {
		return concept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	@Override
	public int compareTo(TestResultModel o) {
		if (o == null)
			return 1;
		if (this == null)
			return -1;

		String tInvestigation = this.getInvestigation();
		String tSet = this.getSet();
		String tTest = this.getTest();
		String oInvestigation = o.getInvestigation();
		String oSet = o.getSet();
		String oTest = o.getTest();
		int investigationCompare = tInvestigation
				.compareToIgnoreCase(oInvestigation);
		int setCompare = tSet.compareToIgnoreCase(oSet);
		int testCompare = tTest.compareToIgnoreCase(oTest);
		if (investigationCompare != 0) {
			return investigationCompare;
		} else if (setCompare != 0) {
			return setCompare;
		} else {
			return testCompare;
		}
	}
}
