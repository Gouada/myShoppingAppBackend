package org.myApps.myShoppingCart.exceptionHandling;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.myApps.myShoppingCart.model.ErrorMessage;

@Provider
public class NoResultForQueryExceptionMapper implements ExceptionMapper<NoResultForQueryException> {

	@Override
	public Response toResponse(NoResultForQueryException exception) {
		Response response;
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 500, "www......");
		response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
		return response;
	}

}
