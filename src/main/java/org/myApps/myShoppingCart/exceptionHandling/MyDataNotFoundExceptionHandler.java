package org.myApps.myShoppingCart.exceptionHandling;

public class MyDataNotFoundExceptionHandler extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -6517940926414099523L;

	/*
	 * public String getErrorMessage(String message) { return this.ErrorMessage;
	 * }
	 */
	public MyDataNotFoundExceptionHandler(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
