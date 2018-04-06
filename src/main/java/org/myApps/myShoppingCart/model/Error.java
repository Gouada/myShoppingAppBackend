package org.myApps.myShoppingCart.model;

public class Error {

	public static final String INSERTERROR = "an error occured during the hibernate insert operation, then the transaction got rolled back";
	public static final String DELETEERROR = "an error occured during the hibernate delete operation, then the transaction got rolled back";
	public static final String UPDATEERROR = "an error occured during the hibernate update operation, then the transaction got rolled back";
	public static final String SELECTERROR = "an error occured during the hibernate select operation, then the transaction got rolled back";
	public static final String TRANSACTIONERROR = "an error occured dured the transaction, then the transaction got rolled back";
	public static final String NORESULTERROR = "no result was found for this request";

	public Error() {
		super();
		// TODO Auto-generated constructor stub
	}

}
