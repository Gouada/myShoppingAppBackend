package org.myApps.myShoppingCart.exceptionHandling;

import javax.persistence.NoResultException;

public class NoResultForQueryException extends NoResultException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NoResultForQueryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
