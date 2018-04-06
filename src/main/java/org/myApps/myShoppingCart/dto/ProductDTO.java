package org.myApps.myShoppingCart.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.myApps.myShoppingCart.exceptionHandling.MyDataNotFoundExceptionHandler;
import org.myApps.myShoppingCart.exceptionHandling.TransactionExceptionHandler;
import org.myApps.myShoppingCart.model.Error;
import org.myApps.myShoppingCart.model.Product;
import org.myApps.myShoppingCart.model.ProductNameInLang;

public class ProductDTO {
	private static ConfigurationDTO configDTO;
	private final Date jetzt = new Date();
	// private static SessionFactory sessionFactory;

	public ProductDTO() {
		try {
			configDTO = ConfigurationDTO.getInstance();
		} catch (Exception e) {
			System.out.println(e.getStackTrace().toString());
		}
		// sessionFactory = new
		// Configuration().configure().buildSessionFactory();
	}

	public Product addProduct(Product product) {
		Session mySession = null;
		// Product insertedProduct = null;
		Transaction trx = null;
		Serializable obj = null;
		long saved = 2L;
		Product foundProduct = getProductByProductName(product.getProductName());
		if (foundProduct == null) {
			try {
				ProductNameInLang pdctNInL = null;
				if (product != null) {
					pdctNInL = new ProductNameInLang(product.getLang(), product.getProductName());
				}
				// mySession = sessionFactory.openSession();
				product.addProductName(pdctNInL);
				product.setCreated(jetzt);
				mySession = configDTO.getSession();
				trx = mySession.beginTransaction();
				mySession.save(product);
				// insertedProduct = (Product) mySession.save(product);
				trx.commit();
				saved = 1L;
				return product;
			} catch (Exception e) {
				if (trx != null) {
					saved = 0L;
					trx.rollback();
					return null;
				}
				if (e.getClass() == HibernateException.class) {
					throw new MyDataNotFoundExceptionHandler(Error.INSERTERROR);
				} else if (e.getClass() == HibernateException.class) {
					throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
				} else {
					throw e;
				}

			} finally {
				if (mySession != null) {
					mySession.close();
				}
				// sessionFactory.close();
			}
		} else {
			return foundProduct;
		}
	}

	public boolean deleteProduct(Product product) {
		boolean deleted = true;
		long id;// = getProductId((Serializable)product);
		// product.setId(4L);
		Transaction trx = null;
		Session mySession = null;
		Product identifiedProduct = getProductByProductName(product.getProductName());
		try {
			// mySession = sessionFactory.openSession();
			mySession = configDTO.getSession();
			trx = mySession.beginTransaction();
			if (identifiedProduct != null) {
				mySession.delete(identifiedProduct);
				deleted = true;
				Product pdt = mySession.get(Product.class, identifiedProduct.getProductId());
			} else {
				deleted = false;
			}
			trx.commit();
		} catch (Exception e) {
			if (trx != null) {
				trx.rollback();
				deleted = false;
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.DELETEERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else {
				throw e;
			}
		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
		return deleted;
	}

	public boolean updateProduct(Product product) {
		Transaction trx = null;
		boolean updated = false;
		Session mySession = null;
		Product pdt = getProductByProductId(product.getProductId());
		// if (!product.getProductName().equals("")) {
		if (product.getProductName() != "") {
			pdt.setProductName(product.getProductName());
		}

		if (product.getProductAmount() != 0) {
			pdt.setProductAmount(product.getProductAmount());
		}

		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			trx = mySession.beginTransaction();
			mySession.update(pdt);
			trx.commit();
			updated = true;
		} catch (Exception e) {
			if (trx != null) {
				trx.rollback();
				updated = false;
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.UPDATEERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else {
				throw e;
			}
		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
		return updated;
	}

	public boolean updateProductByName(String productOldName, Product updatedProduct) {
		Transaction trx = null;
		boolean updated = false;
		Product productToBeUpdated = getProductByProductName(productOldName);
		productToBeUpdated.setProductName(updatedProduct.getProductName());

		if (!updatedProduct.getProductName().equals("")) {
			productToBeUpdated.setProductName(updatedProduct.getProductName());
		}

		if (updatedProduct.getProductAmount() != 0) {
			productToBeUpdated.setProductAmount(updatedProduct.getProductAmount());
		}

		Session mySession = configDTO.getSession();
		// Session mySession = sessionFactory.openSession();
		try {
			trx = mySession.beginTransaction();
			mySession.update(productToBeUpdated);
			trx.commit();
			updated = true;
		} catch (Exception e) {
			if (trx != null) {
				trx.rollback();
				updated = false;
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.UPDATEERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else {
				throw e;
			}
		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
		return updated;
	}

	public Product getProductByProductName(String productName) {
		Transaction trx = null;
		Product retrivedProduct = null;
		Session mySession = configDTO.getSession();
		// Session mySession = sessionFactory.openSession();
		try {
			trx = mySession.beginTransaction();
			// myProduct = mySession.get(Product.class,
			// product.getProductName());

			Query<Product> query = mySession.createQuery("FROM Product WHERE upper(productName) LIKE ?");
			query.setParameter(0, productName.toUpperCase());

			List<Product> pdtList = query.list();
			if (pdtList != null && pdtList.size() > 0) {
				retrivedProduct = pdtList.get(0);
			}
			trx.commit();

		} catch (Exception e) {
			if (trx != null) {
				trx.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.SELECTERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else if (e.getClass().isInstance(NoResultException.class)) {
				// throw e;
			} else {
				e.getMessage();
			}
		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
		return retrivedProduct;
	}

	public Product getProductByProductId(long productId) {
		Transaction trx = null;
		Product retrivedProduct = null;
		Session mySession = null;
		try {
			// mySession = sessionFactory.openSession();
			mySession = configDTO.getSession();
			trx = mySession.beginTransaction();
			Query<Product> query = mySession.createQuery("FROM Product WHERE productId = :productId")
					.setParameter("productId", productId);
			trx.commit();
			retrivedProduct = query.getSingleResult();
		} catch (Exception e) {
			if (trx != null) {
				trx.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.SELECTERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else {
				throw e;
			}
		} finally {
			if (mySession != null) {
				mySession.close();
				// sessionFactory.close();
			}
		}
		return retrivedProduct;
	}

	public List<Product> load(String language) {
		Transaction trx = null;
		List<Product> retrivedProducts = null;
		Session mySession = null;
		if (!language.equalsIgnoreCase("de") && !language.equalsIgnoreCase("fr")) {
			language = "de";
		}

		try {
			mySession = configDTO.getSession();
			trx = mySession.beginTransaction();
			// Query<ProductNameInLang> query = mySession
			// .createQuery(" FROM "
			// + " ProductNameInLang pNLg WHERE pNLg.lang LIKE :language AND
			// pNLg.product.is_standard_product= :is_standard ")
			// .setParameter("is_standard", 1).setParameter("language",
			// language);

			// Query<Product> queryProduct = mySession
			// .createQuery(" FROM "
			// + " Product pdt, ProductNameInLang pNLg WHERE pNLg.product = pdt
			// AND pNLg.lang LIKE :language AND pdt.is_standard_product=
			// :is_standard ")
			// .setParameter("is_standard", 1).setParameter("language",
			// language);

			Query<Product> queryProduct = mySession
					.createQuery(" FROM  "
							+ " Product pdt WHERE EXISTS ( FROM ProductNameInLang pNLg WHERE pNLg.product = pdt AND pNLg.lang LIKE :language AND pdt.is_standard_product= :is_standard) ")
					.setParameter("is_standard", 1).setParameter("language", language);

			int listSize = queryProduct.getResultList().size();
			retrivedProducts = queryProduct.getResultList();
			if (retrivedProducts != null) {
				for (Product pNlg : retrivedProducts) {
					System.out.println(pNlg.getProductAmount());
				}
			}

			trx.commit();

		} catch (Exception e) {
			if (trx != null) {
				trx.rollback();
			}
			System.out.println(e.getStackTrace());
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.SELECTERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else if (e.getClass().isInstance(NoResultException.class)) {
				throw e;
			} else {
				System.out.println(e.getMessage());
			}
		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
		// System.out.println("Test .......... " + language + " ....." +
		// retrivedProducts.size() + "....");

		return retrivedProducts;
	}

	public List<Product> loadByType(int productType) {
		Transaction trx = null;
		List<Product> retrivedProducts = null;
		Session mySession = null;
		try {
			// mySession = sessionFactory.openSession();
			mySession = configDTO.getSession();
			trx = mySession.beginTransaction();

			Query<Product> query = mySession.createQuery("FROM Product WHERE productType = :productType")
					.setParameter(productType, productType);

			retrivedProducts = query.list();
			trx.commit();
		} catch (Exception e) {
			if (trx != null) {
				trx.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.SELECTERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else if (e.getClass().isInstance(NoResultException.class)) {
				// throw e;
			} else {
				e.getMessage();
			}
		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
		return retrivedProducts;
	}
}
