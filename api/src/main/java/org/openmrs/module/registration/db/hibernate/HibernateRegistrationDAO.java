package org.openmrs.module.registration.db.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.module.registration.db.RegistrationDAO;
import org.openmrs.module.registration.model.RegistrationFee;

public class HibernateRegistrationDAO implements RegistrationDAO {

	private SessionFactory sessionFactory;
	private static SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	private static SimpleDateFormat mysqlDateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static Log logger = LogFactory
	.getLog(HibernateRegistrationDAO.class);

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
	public List<RegistrationFee> getRegistrationFees(Patient patient,
			Integer numberOfLastDate) throws ParseException {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				RegistrationFee.class);
		criteria.add(Restrictions.eq("patient", patient));
		Calendar afterDate = Calendar.getInstance();
		afterDate.add(Calendar.DATE, -numberOfLastDate);
		String afterDateFormat = mysqlDateFormatter.format(afterDate
				.getTime()) + " 00:00:00";
		logger.info(String.format("getRegistrationFees(patientId=%s, afterDate=%s)", patient.getId(), afterDateFormat));
		criteria.add(Expression.ge("createdOn",
				mysqlDateTimeFormatter.parse(afterDateFormat)));
		criteria.addOrder(Order.desc("createdOn"));

		return criteria.list();
	}

	public void deleteRegistrationFee(RegistrationFee fee) {
		sessionFactory.getCurrentSession().delete(fee);
	}
}