package org.openmrs.module.registration.web.controller.util;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.module.hospitalcore.util.PatientUtil;
import org.openmrs.module.registration.util.RegistrationUtils;

public class PatientModel {

	private String patientId;
	private String identifier;
	private String fullname;
	private String age;
	private String gender;
	private String category;
	private String address;
	private String birthdate;
	private Map<Integer, String> attributes = new HashMap<Integer, String>();

	public PatientModel(Patient patient) {
		setPatientId(patient.getPatientId().toString());
		setIdentifier(patient.getPatientIdentifier().getIdentifier());
		setFullname(PatientUtil.getFullName(patient));		
		if (patient.getAge() > 1) {
			setAge(String.format("%s years old, %s", patient.getAge(), PatientUtil.getAgeCategory(patient)));
		} else {
			setAge(String.format("~ 1 year old, %s", PatientUtil.getAgeCategory(patient)));
		}

		if (patient.getGender().equalsIgnoreCase("M")) {
			setGender("Male");
		} else {
			setGender("Female");
		}

		setAddress(patient.getPersonAddress().getCityVillage() + ", "
				+ patient.getPersonAddress().getCountyDistrict());
		
		setBirthdate(RegistrationUtils.formatDate(patient.getBirthdate()));

		Map<String, PersonAttribute> attributes = patient.getAttributeMap();
		for (String key : attributes.keySet()) {
			getAttributes().put(attributes.get(key).getAttributeType().getId(),
					attributes.get(key).getValue());
		}
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Map<Integer, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<Integer, String> attributes) {
		this.attributes = attributes;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
}
