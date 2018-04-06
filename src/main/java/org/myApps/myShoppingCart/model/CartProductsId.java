package org.myApps.myShoppingCart.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

@XmlRootElement
@Embeddable
public class CartProductsId implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Product product;
	private ShoppingCart cart;

	@ManyToOne(cascade = CascadeType.ALL)
	// @JsonManagedReference(value = "productBackreference")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JsonBackReference(value = "cartBackreference") // @JsonManagedReference
	public ShoppingCart getCart() {
		return cart;
	}

	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}

	@Override
	public boolean equals(Object arg0) {

		if (this == arg0) {
			return true;
		}
		if (!(arg0 instanceof CartProductsId)) {
			return false;
		}

		final CartProductsId cartPdtsId = (CartProductsId) arg0;

		if (!cartPdtsId.getProduct().equals(getProduct())) {
			return false;
		}
		if (!cartPdtsId.getCart().equals(getCart())) {
			return false;
		}

		return true;
		// return super.equals(arg0);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	@Override
	public int hashCode() {
		int result;
		result = getCart().hashCode();// + getProduct().hashCode();
		result = 29 * result; // + getLitterId();
		return result;
		// return super.hashCode();
	}

}
