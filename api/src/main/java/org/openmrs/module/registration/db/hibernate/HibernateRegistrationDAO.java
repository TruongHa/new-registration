package org.openmrs.module.registration.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.module.registration.db.RegistrationDAO;
import org.openmrs.module.registration.model.RegistrationFee;

public class HibernateRegistrationDAO implements RegistrationDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public RegistrationFee saveRegistrationFee(RegistrationFee fee) {
		return (RegistrationFee) sessionFactory.getCurrentSession().merge(fee);
	}

	public RegistrationFee getRegistrationFee(Integer id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				RegistrationFee.class);
		criteria.add(Restrictions.eq("id", id));
		return (RegistrationFee) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<RegistrationFee> getRegistrationFees(Patient patient) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				RegistrationFee.class);
		criteria.add(Restrictions.eq("patient", patient));
		return criteria.list();
	}

	public void deleteRegistrationFee(RegistrationFee fee) {
		sessionFactory.getCurrentSession().delete(fee);
	}	
}