package org.myApps.myShoppingCart.exceptionHandling;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.myApps.myShoppingCart.model.ErrorMessage;

@Provider
public class DataBaseOperationMapper implements ExceptionMapper<DataTabaseOperationException> {

	@Override
	public Response toResponse(DataTabaseOperationException exception) {
		ErrorMessage error = new ErrorMessage(exception.getMessage(), 500, "www......");
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		return response;
	}

}
