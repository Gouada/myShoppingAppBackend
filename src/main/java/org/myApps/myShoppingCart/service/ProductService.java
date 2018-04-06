package org.myApps.myShoppingCart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.myApps.myShoppingCart.dto.ProductDTO;
import org.myApps.myShoppingCart.exceptionHandling.MyDataNotFoundExceptionHandler;
import org.myApps.myShoppingCart.model.Product;

public class ProductService {

	public static List<Product> list = new ArrayList<Product>();

	private final Date jetzt = new Date();

	public ProductService() {
	}

	public List<Product> getAllProducts() {

		// replace later on by db operation
		return list;
	}

	public Product addProduct(Product product) {
		// replace later on by db operation
		product.setCreated(jetzt);
		ProductDTO pdtDTO = new ProductDTO();
		return pdtDTO.addProduct(product);
	}

	public boolean removeProduct(Product product) {
		ProductDTO pdtDTO = new ProductDTO();
		return pdtDTO.deleteProduct(product);
	}

	public boolean updateProduct(String productOldName, Product updatedProduct) {
		ProductDTO pdtDTO = new ProductDTO();
		return pdtDTO.updateProductByName(productOldName, updatedProduct);
	}

	public boolean updateProduct(Product updatedProduct) {
		ProductDTO pdtDTO = new ProductDTO();
		return pdtDTO.updateProduct(updatedProduct);
	}

	public Product getProductById(long productId) {
		ProductDTO pdtDTO = new ProductDTO();
		try {
			return pdtDTO.getProductByProductId(productId);
		} catch (Exception exception) {
			throw new MyDataNotFoundExceptionHandler("Product with id " + productId + " Doesn't exixts");
		}
	}

	public Product getProductByName(String productName) {
		ProductDTO pdtDTO = new ProductDTO();
		try {
			return pdtDTO.getProductByProductName(productName);
		} catch (Exception exception) {
			throw new MyDataNotFoundExceptionHandler("Product with id " + productName + " Doesn't exixts");
		}

	}

	public List<Product> getProductFromType(int productType) {
		ProductDTO pdtDTO = new ProductDTO();
		return pdtDTO.loadByType(productType);
	}

	public List<Product> loadProducts(String language) {
		// List<Product> productTypeSubList = new ArrayList<Product>();
		ProductDTO pdtDTO = new ProductDTO();
		return pdtDTO.load(language);
	}

}
