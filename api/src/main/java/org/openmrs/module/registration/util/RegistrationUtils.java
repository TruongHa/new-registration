package org.openmrs.module.registration.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;

public class RegistrationUtils {

	private static Log logger = LogFactory.getLog(RegistrationUtils.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * Parse Date
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date) throws ParseException {
		return sdf.parse(date);
	}

	/**
	 * Format date
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return sdf.format(date);
	}

	/**
	 * Generate person name
	 * 
	 * @param personName
	 *            TODO
	 * @param name
	 * 
	 * @return
	 */
	public static PersonName getPersonName(PersonName personName, String name) {

		if (personName == null)
			personName = new PersonName();

		@SuppressWarnings("deprecation")
		String fullname = StringUtils.capitaliseAllWords(name).trim();
		String[] parts = fullname.split(" ");
		if (parts.length == 1) {
			personName.setGivenName(parts[0]);
		} else if (parts.length == 2) {
			personName.setGivenName(parts[0]);
			personName.setFamilyName(parts[1]);
		} else if (parts.length > 2) {
			personName.setGivenName(parts[0]);
			personName.setMiddleName(parts[1]);

			String familyName = "";
			for (int i = 2; i < parts.length; i++) {
				familyName += parts[i] + " ";
			}
			familyName = familyName.trim();
			personName.setFamilyName(familyName);
		}
		personName.setPreferred(true);
		return personName;
	}

	/**
	 * Generate patient identifier
	 * 
	 * @param identifier
	 * @return
	 */
	public static PatientIdentifier getPatientIdentifier(String identifier) {
		Location location = new Location(GlobalPropertyUtil.getInteger(
				RegistrationConstants.PROPERTY_LOCATION, 1));
		PatientIdentifierType identType = Context
				.getPatientService()
				.getPatientIdentifierType(
						GlobalPropertyUtil
								.getInteger(
										RegistrationConstants.PROPERTY_PATIENT_IDENTIFIER_TYPE,
										1));
		PatientIdentifier patientIdentifier = new PatientIdentifier(identifier,
				identType, location);
		return patientIdentifier;
	}

	/**
	 * Creates a new Patient Identifier: <prefix>YYMMDDhhmmxxx-checkdigit where
	 * prefix = global_prop (registration.identifier_prefix) YY = two char
	 * representation of current year e.g. 2009 - 09 MM = current month. e.g.
	 * January - 1; December - 12 DD = current day of month e.g. 20 hh = hour of
	 * day e.g. 10PM - 22 mm = minustes e.g. 10:12 - 12 xxx = three random
	 * digits e.g. from 0 - 999 checkdigit = using the Lunh Algorithm
	 * 
	 */
	public static String getNewIdentifier() {
		Calendar now = Calendar.getInstance();
		String shortName = GlobalPropertyUtil.getString(
				RegistrationConstants.PROPERTY_IDENTIFIER_PREFIX, "");
		String noCheck = shortName
				+ String.valueOf(now.get(Calendar.YEAR)).substring(2, 4)
				+ String.valueOf(now.get(Calendar.MONTH) + 1)
				+ String.valueOf(now.get(Calendar.DATE))
				+ String.valueOf(now.get(Calendar.MINUTE))
				+ String.valueOf(new Random().nextInt(999));
		return noCheck + "-" + getCheckdigit(noCheck);
	}

	/*
	 * Using the Luhn Algorithm to generate check digits
	 * 
	 * @param idWithoutCheckdigit
	 * 
	 * @return idWithCheckdigit
	 */
	private static int getCheckdigit(String idWithoutCheckdigit) {
		String validChars = "0123456789ACDEFGHJKLMNPRSTUVWXY";
		idWithoutCheckdigit = idWithoutCheckdigit.trim().toUpperCase();
		int sum = 0;
		for (int i = 0; i < idWithoutCheckdigit.length(); i++) {
			char ch = idWithoutCheckdigit.charAt(idWithoutCheckdigit.length()
					- i - 1);
			if (validChars.indexOf(ch) == -1) {
				logger.warn("\"" + ch + "\" is an invalid character");
			}
			int digit = (int) ch - 48;
			int weight;
			if (i % 2 == 0) {
				weight = (2 * digit) - (int) (digit / 5) * 9;
			} else {
				weight = digit;
			}
			sum += weight;
		}
		sum = Math.abs(sum) + 10;
		return (10 - (sum % 10)) % 10;
	}

	/**
	 * Get person address
	 * 
	 * @param address
	 *            TODO
	 * @param district
	 * @param tehsil
	 * @return
	 */
	public static PersonAddress getPersonAddress(PersonAddress address,
			String district, String tehsil) {

		if (address == null)
			address = new PersonAddress();

		address.setCountyDistrict(district);
		address.setCityVillage(tehsil);

		return address;
	}

	/**
	 * Get person attribute
	 * 
	 * @param id
	 * @param value
	 * @return
	 */
	public static PersonAttribute getPersonAttribute(Integer id, String value) {
		PersonAttributeType type = Context.getPersonService()
				.getPersonAttributeType(id);
		PersonAttribute attribute = new PersonAttribute();
		attribute.setAttributeType(type);
		attribute.setValue(value);
		logger.info(String.format(
				"Saving new person attribute [name=%s, value=%s]",
				type.getName(), value));
		return attribute;
	}
}
