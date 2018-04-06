package org.myApps.myShoppingCart.exceptionHandling;

import org.hibernate.TransactionException;

public class TransactionExceptionHandler extends TransactionException {

	public TransactionExceptionHandler(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
