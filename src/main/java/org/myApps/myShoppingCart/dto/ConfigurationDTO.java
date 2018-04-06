package org.myApps.myShoppingCart.dto;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ConfigurationDTO {

	private static SessionFactory sessionFactory = null;
	private static Session session;

	private static ConfigurationDTO singelInstance = new ConfigurationDTO();

	public ConfigurationDTO() {
		// try {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		/*
		 * } catch (HibernateException e) { System.out.println(e.getMessage());
		 * }
		 */
	}

	public static synchronized ConfigurationDTO getInstance() {
		if (singelInstance == null) {
			singelInstance = new ConfigurationDTO();
		}
		return singelInstance;
	}

	protected Session getSession() {
		try {
			return session = sessionFactory.openSession();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return session;

	}

	public void closeSession() {
		try {
			session.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
