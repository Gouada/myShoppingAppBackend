package org.myApps.myShoppingCart.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

@XmlRootElement
@Entity
@Table(name = "tbl_cart_products")
@AssociationOverrides({ @AssociationOverride(name = "primaryKey.cart", joinColumns = @JoinColumn(name = "fd_cart_id")),
		@AssociationOverride(name = "primaryKey.product", joinColumns = @JoinColumn(name = "fd_product_id")) })
public class CartProducts implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	CartProductsId primaryKey = new CartProductsId();

	private int productAmount;
	private Date updatedDate;
	private Date addedDate;
	private boolean productInCart;

	@EmbeddedId
	public CartProductsId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(CartProductsId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Transient
	@JsonBackReference(value = "productBackreference")
	public Product getProduct() {
		return getPrimaryKey().getProduct();
	}

	public void setProduct(Product product) {
		getPrimaryKey().setProduct(product);
	}

	@Transient
	@JsonBackReference(value = "cartProductReference")
	public ShoppingCart getCart() {
		return getPrimaryKey().getCart();
	}

	public void setCart(ShoppingCart cart) {
		getPrimaryKey().setCart(cart);
	}

	@Column(name = "fd_product_amount", nullable = false)
	public int getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(int productAmount) {
		this.productAmount = productAmount;
	}

	@Column(name = "fd_addedDate", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	@Column(name = "fd_updatedDate", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
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
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Column(name = "fd_IsProductInCart")
	public boolean isProductInCart() {
		return productInCart;
	}

	public void setProductInCart(boolean productInCart) {
		this.productInCart = productInCart;
	}

}
