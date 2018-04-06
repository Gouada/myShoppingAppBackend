package org.myApps.myShoppingCart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

@XmlRootElement
@Entity
@Table(name = "tbl_product_name")
public class ProductNameInLang implements Serializable { // extends Product {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String productName;
	private String lang;
	private Product product;
	private long id;

	public ProductNameInLang(String lang, String name) {
		this.lang = lang;
		if (lang == null || lang.equals("")) {
			this.lang = "de";
		}
		this.productName = name;
	}

	public ProductNameInLang() {
		super();
	};

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "fd_id", unique = true, nullable = true)
	public long getId() {
		return id;
	}

	public void setId(long fd_id) {
		this.id = fd_id;
	}

	@Column(name = "fd_product_name", nullable = false)
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "fd_lang", nullable = false)
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@ManyToOne
	@JoinColumn(name = "fd_product_id") // , referencedColumnName =
	// "fd_product_id", insertable = false, updatable = false)
	@JsonBackReference(value = "productNameInLang") // @JsonManagedReference
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
