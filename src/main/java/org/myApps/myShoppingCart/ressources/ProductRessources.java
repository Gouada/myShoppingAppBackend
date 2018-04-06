package org.myApps.myShoppingCart.ressources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.myApps.myShoppingCart.model.Product;
import org.myApps.myShoppingCart.service.ProductService;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductRessources {

	ProductService ps = new ProductService();

	@GET
	@Path("/all/language/{language}")
	public Response loadProducts(@PathParam("language") String language) {
		Response response = null;
		List<Product> products = new ArrayList<Product>();
		products = ps.loadProducts(language);

		if (products != null) {

			// for (ProductNameInLang product : products) {
			// product.getProduct();
			// System.out.println("..................................." +
			// product.getProduct().getProductId());
			// }

			response = Response.status(Status.OK).entity(new GenericEntity<List<Product>>(products) {
			}).build();
		} else {
			response = Response.status(Status.NO_CONTENT).build();
		}
		return response;
	}

	@GET
	@Path("/productType/{productType}")
	public Response getProductsByType(@PathParam("productType") int productType) {
		Response response = null;
		List<Product> products = ps.getProductFromType(productType);
		if (products != null) {
			response = Response.status(Status.OK).entity(new GenericEntity<List<Product>>(products) {
			}).build();
		} else {
			response = Response.status(Status.NO_CONTENT).build();
		}
		return response;
	}

	@GET
	public Response getProductById(@QueryParam("productId") long productId) {
		Response response;
		Product product = ps.getProductById(productId);
		if (product != null) {
			response = Response.status(Status.OK).entity(product).build();
		} else {
			response = Response.status(Status.NO_CONTENT).build();
		}
		return response;
	}

	@GET
	@Path("/{productName}")
	public Response getProductByName(@PathParam("productName") String name) {
		Response response = null;
		Product product = ps.getProductByName(name);
		if (product != null) {
			response = Response.status(Status.OK).entity(product).build();
		} else {
			response = Response.status(Status.NO_CONTENT).build();
		}
		return response;
	}

	@POST
	public Response addProduct(@Context UriInfo uriInfo, Product product) {
		Product insertedProduct = ps.addProduct(product);
		if (insertedProduct != null) {
			URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getProductId())).build();
			return Response.created(uri).entity(insertedProduct).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}

	}

	@PUT
	// @QueryParam("productName=productName")
	public Response updateProduct(@QueryParam("productOldName") String productOldName, Product updatedProduct) {
		// product.setId(id);
		if (ps.updateProduct(productOldName, updatedProduct)) {
			return Response.status(Status.OK).build();
		} else {
			return null;
		}
	}

	@PUT
	@Path("/productId/{productId}")
	public Response updateProduct(@PathParam("productId") long productId, Product updatedProduct) {
		updatedProduct.setProductId(productId);
		if (ps.updateProduct(updatedProduct)) {
			return Response.status(Status.OK).build();
		} else {
			return null;
		}
	}

	@DELETE
	public Response deleteProduct(Product product) {
		Response response;
		if (ps.removeProduct(product)) {
			response = Response.status(Status.OK).build();
		} else {
			response = Response.status(Status.NOT_MODIFIED).build();
		}
		return response;
	}

}
