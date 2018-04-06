package org.myApps.myShoppingCart.service;

import java.util.Date;
import java.util.List;

import org.myApps.myShoppingCart.dto.CartDTO;
import org.myApps.myShoppingCart.dto.ShoppingGroupDTO;
import org.myApps.myShoppingCart.model.CartProducts;
import org.myApps.myShoppingCart.model.Product;
import org.myApps.myShoppingCart.model.ShoppingCart;
import org.myApps.myShoppingCart.model.ShoppingGroup;
import org.myApps.myShoppingCart.model.ShoppingGroupMember;

public class CartService {

	private final CartDTO cartDTO;

	public CartService() {
		super();
		cartDTO = new CartDTO();
	}

	public ShoppingCart getCurrentCart(long groupId, String language) {
		return cartDTO.getCurrentCart(groupId, language);
	}

	public List<ShoppingCart> getAllCart(long groupId) {
		return cartDTO.getAllCart(groupId);
	}

	public ShoppingCart addProductsToCart(long groupId, long cartId, List<CartProducts> products) {// List<Product>
																									// products
		return cartDTO.addProductsToCart(groupId, cartId, products);
	}

	public boolean addOneProductToCart(long groupId, long cartId, Product product) {
		return cartDTO.addOneProductToCart(groupId, cartId, product);
	}

	public boolean updateProductsInCart(long groupId, long cartId, List<Product> products) {
		return cartDTO.updateProductsInCart(groupId, cartId, products);
	}

	public boolean updateProductInCart(long groupId, long cartId, Product product) {
		return cartDTO.updateProductInCart(groupId, cartId, product);
	}

	public boolean removeProductFromCart(long groupId, long cartId, long productId) {
		return cartDTO.removeProductFromCart(groupId, cartId, productId);
	}

	public ShoppingCart addCart(long groupId, ShoppingCart newCart) {
		Date created = new Date();
		newCart.setCreated(created);
		return cartDTO.addCart(groupId, newCart);
	}

	public boolean archiveCart(long cartId) {
		return cartDTO.archiveCart(cartId);
	}

	public List<Product> getCurrentList(long cartId, String language) {
		return cartDTO.getCurrentList(cartId, language);
	}

	public List<ShoppingGroupMember> fetchReceiverList(ShoppingGroupMember sender, ShoppingGroup group) {
		ShoppingGroupDTO shoppingGpDTO = new ShoppingGroupDTO();
		return shoppingGpDTO.fetchReceiverList(sender, group);
	}

	public List<ShoppingGroupMember> fetchReceiverListByIds(long senderId, long groupId) {
		ShoppingGroupDTO shoppingGpDTO = new ShoppingGroupDTO();
		return shoppingGpDTO.fetchReceiverListByIds(senderId, groupId);
	}

}
