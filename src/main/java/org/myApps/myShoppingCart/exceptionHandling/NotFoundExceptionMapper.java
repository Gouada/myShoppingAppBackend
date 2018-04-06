package org.myApps.myShoppingCart.exceptionHandling;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.myApps.myShoppingCart.model.ErrorMessage;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<MyDataNotFoundExceptionHandler> {

	@Override
	public Response toResponse(MyDataNotFoundExceptionHandler exception) {
		// TODO Auto-generated method stub
		ErrorMessage error = new ErrorMessage(exception.getMessage(), 500, "www......");
		Response response = Response.status(Status.NOT_FOUND).entity(error).build();
		return response;
	}

}
