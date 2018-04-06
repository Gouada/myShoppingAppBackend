package org.myApps.myShoppingCart.ressources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.myApps.myShoppingCart.helpers.FirebaseTrigger;
import org.myApps.myShoppingCart.model.CartProducts;
import org.myApps.myShoppingCart.model.Product;
import org.myApps.myShoppingCart.model.ShoppingCart;
import org.myApps.myShoppingCart.service.CartService;

@Path("/group/{groupId}/cart")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class CartRessources {
	CartService cs = new CartService();

	@GET
	@Path("/currentCart/language/{language}")
	public Response getCurrentCart(@PathParam("groupId") long groupId, @PathParam("language") String language) {
		// public Response getCurrentCart(@PathParam("groupId") long groupId,
		// @QueryParam("option") String option) {
		Response response = null;
		ShoppingCart currentCart = null;
		try {
			currentCart = cs.getCurrentCart(groupId, language);
			if (currentCart != null) {
				response = Response.status(Status.OK).entity(currentCart).build();
				System.out.println("SHOPPINGCART-ID......" + currentCart.getCartId() + "IN CartRessources");
			} else {
				response = Response.status(Status.NO_CONTENT).build();
			}
			return response;

		} catch (Exception e) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
			return response;
		}

	}

	@GET
	@Path("/{cartId}/currentList/language/{language}")
	public Response getCurrentList(@PathParam("groupId") long groupId, @PathParam("cartId") long cartId,
			@PathParam("language") String language) {
		Response response;
		List<Product> currentCartPdtList = cs.getCurrentList(cartId, language);

		if (currentCartPdtList != null) {
			response = Response.status(Status.OK).entity(currentCartPdtList).build();
		} else {
			response = Response.status(Status.NO_CONTENT).build();
		}
		return response;
	}

	@GET
	@Path("/all")
	public Response getAllCarts(@PathParam("groupId") long groupId) {
		List<ShoppingCart> carts = cs.getAllCart(groupId);

		Response response;
		if (carts != null) {
			response = Response.status(Status.OK).entity(new GenericEntity<List<ShoppingCart>>(carts) {
			}).build();
		} else {
			response = Response.status(Status.NO_CONTENT).build();
		}
		return response;
	}

	@GET
	@Path("/archive/{cartId}/member/{memberId}/language/{language}")
	public boolean archiveCart(@PathParam("cartId") long cartId, @PathParam("groupId") long groupId,
			@PathParam("memberId") long memberId, @PathParam("language") String language) {
		boolean archived = false;
		archived = cs.archiveCart(cartId);
		FirebaseTrigger fbtg = new FirebaseTrigger(groupId, memberId, language);
		fbtg.trigerFirebase(2);
		return archived;
	}

	@POST
	@Path("/{cartId}/member/{memberId}/language/{language}")
	public Response addProductsToCart(@PathParam("groupId") long groupId, @PathParam("cartId") long cartId,
			@PathParam("memberId") long memberId, List<CartProducts> products, @PathParam("language") String language) { // List<Product>
		// products)
		Response response;
		ShoppingCart savedCart = cs.addProductsToCart(groupId, cartId, products);
		if (savedCart != null) {
			response = Response.status(Status.OK).entity(savedCart).build();
			FirebaseTrigger fbtg = new FirebaseTrigger(groupId, memberId, language);
			System.out.println("Callin firebase trigger");
			fbtg.trigerFirebase(1);
		} else {
			response = Response.status(Status.NOT_MODIFIED).build();
		}
		return response;
	}

	@POST
	@Path("/{cartId}/{productId}")
	public Response addOneProductToCart(@PathParam("groupId") long groupId, @PathParam("cartId") long cartId,
			@PathParam("productId") long productId, Product product) {
		Response response;
		product.setProductId(productId);
		if (cs.addOneProductToCart(groupId, cartId, product)) {
			response = Response.status(Status.CREATED).build();
		} else {
			response = Response.status(Status.EXPECTATION_FAILED).build();
		}
		return response;
	}

	@PUT
	@Path("/{cartId}")
	public Response updateProductsInCart(@PathParam("groupId") long groupId, @PathParam("cartId") long cartId,
			List<Product> products) {
		Response response;
		if (cs.updateProductsInCart(groupId, cartId, products)) {
			response = Response.status(Status.OK).build();
		} else {
			response = Response.status(Status.NOT_MODIFIED).build();
		}
		return response;
	}

	@PUT
	@Path("/{cartId}/{productId}")
	public Response updateProductInCart(@PathParam("groupId") long groupId, @PathParam("cartId") long cartId,
			@PathParam("productId") long productId, Product product) {
		Response response;
		product.setProductId(productId);
		if (cs.updateProductInCart(groupId, cartId, product)) {
			response = Response.status(Status.ACCEPTED).build();
		} else {
			response = Response.status(Status.NOT_MODIFIED).build();
		}
		return response;
	}

	@DELETE
	@Path("/{cartId}/{productId}")
	public Response removeProductFromCart(@PathParam("groupId") long groupId, @PathParam("cartId") long cartId,
			@PathParam("productId") long productId) {
		Response response;
		if (cs.removeProductFromCart(groupId, cartId, productId)) {
			response = Response.status(Status.OK).build();
		} else {
			response = Response.status(Status.NOT_MODIFIED).build();
		}
		return response;
	}

	@POST
	@Path("/createNewCart")
	public Response createCart(@PathParam("groupId") long groupId, ShoppingCart newCart) {
		Response response = null;
		ShoppingCart createdCart = cs.addCart(groupId, newCart);
		if (createdCart != null) {
			System.out.print("IF TRUE");
			System.out.println(createdCart.getCartId() + " createdCart  " + createdCart.getCartId());
			response = Response.status(Status.OK).entity(createdCart).build();
		} else {
			response = Response.status(Status.EXPECTATION_FAILED).build();
			System.out.print("IF FALSE");
		}
		return response;
	}
}
