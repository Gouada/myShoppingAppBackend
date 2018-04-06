package org.myApps.myShoppingCart.exceptionHandling;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.myApps.myShoppingCart.model.ErrorMessage;

@Provider
public class TransactionExceptionProvider implements ExceptionMapper<TransactionExceptionHandler> {

	@Override
	public Response toResponse(TransactionExceptionHandler exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 500, "www.....");
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
		return response;
	}

}
