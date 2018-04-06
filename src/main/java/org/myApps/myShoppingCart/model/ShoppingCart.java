package org.myApps.myShoppingCart.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

@XmlRootElement
@Entity
@Table(name = "tbl_shopping_cart")
public class ShoppingCart {

	private long cartId;
	private Date created;
	private boolean status;
	// @Transient
	// private String cartName;

	private ShoppingGroup shoppingGroup;
	/*
	 * @ManyToMany(fetch = FetchType.EAGER)
	 *
	 * @JoinTable(name = "tbl_carts_products", joinColumns = @JoinColumn(name =
	 * "cart_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	 * private List<Product> products; // = new ArrayList<Product>();
	 */

	private List<CartProducts> productsInCart;

	@OneToMany(mappedBy = "primaryKey.cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	// @JsonManagedReference(value = "cartProductReference") //
	// @JsonBackReference
	public List<CartProducts> getProductsInCart() {
		return productsInCart;
	}

	public void setProductsInCart(List<CartProducts> productsInCart) {
		this.productsInCart = productsInCart;

	}

	public ShoppingCart() {
	}

	public void addProductInCart(CartProducts cartProduct) {
		cartProduct.setCart(this);
		if (this.productsInCart == null) {
			this.productsInCart = new ArrayList<CartProducts>();
		}
		productsInCart.add(cartProduct);
	}

	@Column(name = "fd_created", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	/*
	 * public void setProducts(List<Product> products) { this.products =
	 * products; }
	 *
	 * public Collection<Product> getProducts() { return products; }
	 */

	@ManyToOne
	@JoinColumn(name = "fd_group_id")
	@JsonBackReference
	public ShoppingGroup getShoppingGroup() {
		return shoppingGroup;
	}

	public void setShoppingGroup(ShoppingGroup shoppingGroup) {
		this.shoppingGroup = shoppingGroup;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "fd_cart_id")
	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	@Column(name = "fd_status", nullable = false)
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Transient
	public List<Product> getCurrentCartProducts(String language) {
		List<Product> pdctList = null;
		if (getProductsInCart() != null) {
			if (getProductsInCart().size() > 0) {
				pdctList = new ArrayList<Product>();
				Product pdt = new Product();
				for (CartProducts pdctInCart : this.productsInCart) {
					pdt = pdctInCart.getPrimaryKey().getProduct();
					pdt.setProductAmount(pdctInCart.getProductAmount());
					// if (language.equals(pdt.getLang())) {
					String pdtName = pdt.getProductNameByLang(language);
					if (pdtName != null && !pdtName.equals("")) {
						pdt.setProductName(pdtName);
						pdctList.add(pdt);
					}
				}
			}
		}

		return pdctList;
	}

	/*
	 * public void addProduct(Product product) {
	 * this.getProducts().add(product); }
	 */
}
