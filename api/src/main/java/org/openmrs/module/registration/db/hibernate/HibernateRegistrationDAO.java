package org.openmrs.module.registration.db.hibernate;

import org.hibernate.SessionFactory;
import org.openmrs.module.registration.db.RegistrationDAO;

public class HibernateRegistrationDAO implements RegistrationDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}