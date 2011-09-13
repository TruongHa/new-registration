package org.openmrs.module.registration.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.registration.RegistrationService;
import org.openmrs.module.registration.db.RegistrationDAO;

public class RegistrationServiceImpl extends BaseOpenmrsService implements
		RegistrationService {

	private Log logger = LogFactory.getLog(getClass());
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public RegistrationServiceImpl() {
	}

	protected RegistrationDAO dao;

	public void setDao(RegistrationDAO dao) {
		this.dao = dao;
	}

	public List<Patient> searchPatient(String hql) {
		return dao.searchPatient(hql);
	}
	
	public BigInteger getPatientSearchResultCount(String hql){
		return dao.getPatientSearchResultCount(hql);
	}
}
