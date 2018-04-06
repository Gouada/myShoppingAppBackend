package org.myApps.myShoppingCart.exceptionHandling;

import org.hibernate.HibernateException;

public class DataTabaseOperationException extends HibernateException {

	public DataTabaseOperationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
