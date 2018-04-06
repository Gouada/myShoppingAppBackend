package org.myApps.myShoppingCart.model;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@XmlRootElement
@Entity
@Table(name = "tbl_product")
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Product implements Serializable { //

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private long productId;
	private String productName;
	private int productAmount;
	private int productType;
	private Date created;
	private int is_standard_product;
	private String lang;
	/*
	 * @ManyToMany(mappedBy = "products") private List<ShoppingCart>
	 * shoppingCart; // public static final Long serialVersionUID = 1L;
	 */
	private List<CartProducts> cartProducts;
	private List<ProductNameInLang> productNamesInLangs;

	public Product() {
	}

	public Product(String productName, Date created, int productType) {
		this.productName = productName;
		this.productAmount = 1;
		this.created = created;
		this.productType = productType;
	}

	public void addCartProduct(CartProducts cartProduct) {
		this.cartProducts.add(cartProduct);
	}

	public void addProductName(ProductNameInLang productname) {
		if (productNamesInLangs == null) {
			productNamesInLangs = new ArrayList<ProductNameInLang>();
		}
		if (productname != null) {
			productname.setProduct(this);
		}
		this.productNamesInLangs.add(productname);
	}

	@OneToMany(mappedBy = "primaryKey.product", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JsonBackReference // (name = "productNameBackReference") // @JsonIgnore
	public List<CartProducts> getCartProducts() {
		return cartProducts;
	}

	public void setCartProducts(List<CartProducts> cartProducts) {
		this.cartProducts = cartProducts;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "fd_product_id", unique = true, nullable = false)
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	@Column(name = "fd_created", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "fd_product_name", unique = true, nullable = false)
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "fd_product_amount", nullable = true)
	public int getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(int productAmount) {
		this.productAmount = productAmount;
	}

	@Column(name = "fd_product_type")
	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	@Column(name = "fd_is_standard_product")
	public int getIs_standard_product() {
		return is_standard_product;
	}

	public void setIs_standard_product(int is_standard_product) {
		this.is_standard_product = is_standard_product;
	}

	@Column(name = "fd_lang")
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference(value = "productNameInLang") // @JsonBackReference
	public List<ProductNameInLang> getProductNamesInLangs() {
		return productNamesInLangs;
	}

	public void setProductNamesInLangs(List<ProductNameInLang> productNamesInLangs) {
		this.productNamesInLangs = productNamesInLangs;
	}

	public String getProductNameByLang(String language) {
		String productname = "";
		if (this.productNamesInLangs != null && this.productNamesInLangs.size() > 0) {
			for (ProductNameInLang pdtNInL : this.productNamesInLangs) {
				if (pdtNInL.getLang().equals(language)) {
					productname = pdtNInL.getProductName();
				}
			}
		}
		return productname;
	}
}
