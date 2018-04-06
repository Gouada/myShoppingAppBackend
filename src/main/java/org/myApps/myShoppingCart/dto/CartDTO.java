package org.myApps.myShoppingCart.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.myApps.myShoppingCart.exceptionHandling.MyDataNotFoundExceptionHandler;
import org.myApps.myShoppingCart.exceptionHandling.NoResultForQueryException;
import org.myApps.myShoppingCart.exceptionHandling.TransactionExceptionHandler;
import org.myApps.myShoppingCart.model.CartProducts;
import org.myApps.myShoppingCart.model.Error;
import org.myApps.myShoppingCart.model.Product;
import org.myApps.myShoppingCart.model.ShoppingCart;
import org.myApps.myShoppingCart.model.ShoppingGroup;

public class CartDTO {

	// private static SessionFactory sessionFactory;
	private static ConfigurationDTO configDTO;
	private final Date jetzt = new Date();

	public CartDTO() {
		super();
		configDTO = ConfigurationDTO.getInstance();
		// sessionFactory = new
		// Configuration().configure().buildSessionFactory();
	}

	public List<ShoppingCart> getAllCart(long groupId) {
		List<ShoppingCart> carts = null;
		Transaction myTransaction = null;
		Session mySession = null;
		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();
			Query createQuery = mySession.createQuery("FROM ShoppingCart WHERE groupId=?");
			Query<ShoppingCart> query = createQuery;
			query.setParameter(0, groupId);

			carts = query.list();
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
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
		}
		return carts;
	}

	public List<Product> getCurrentList(long cartId, String language) {
		List<Product> productList = null;
		Transaction myTransaction = null;
		Session mySession = null;
		ShoppingCart currentCart = null;

		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();
			currentCart = mySession.get(ShoppingCart.class, cartId);
			if (currentCart.getProductsInCart().size() > 0) {
				currentCart.getProductsInCart();
			}
			myTransaction.commit();

		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
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
				productList = currentCart.getCurrentCartProducts(language);
			}
		}

		return productList;
	}

	public ShoppingCart getCurrentCart(long groupId, String language) {

		ShoppingCart currentCart = null;
		Transaction myTransaction = null;
		Session mySession = null;
		ShoppingGroup group = new ShoppingGroup();
		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();
			// group = mySession.get(ShoppingGroup.class, groupId);
			Query query = mySession.createQuery(" FROM ShoppingCart sc "
					+ " WHERE sc.status=true AND sc.shoppingGroup = (FROM ShoppingGroup sg WHERE sg.groupId=:gId" // +
					// groupId
					+ ")");
			query.setParameter("gId", groupId);
			// .setParameter("sc.status",
			// true);
			// Query<ShoppingCart> query = createQuery;
			// query.setParameter("groupId", groupId);
			// query.setParameter("status", true);

			// currentCart = (ShoppingCart) query.getSingleResult();
			List<ShoppingCart> cartList = query.getResultList();
			if (!cartList.isEmpty()) {
				currentCart = cartList.get(0);
				currentCart.getShoppingGroup();
				currentCart.getProductsInCart();
				currentCart.getCurrentCartProducts(language);
			} else {
				currentCart = new ShoppingCart();
				currentCart.setStatus(true);
				currentCart.setCreated(new Date());
				// currentCart.setShoppingGroup(
				addCart(groupId, currentCart);
			}
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
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
		}
		System.out.println("SHOPPINGCART-ID FROM DAO........................" + currentCart.getCartId() + "IN CARTDAO");
		return currentCart;
	}

	public ShoppingCart addProductsToCart(long groupId, long cartId, List<CartProducts> products) { // List<Product>
																									// products
		ShoppingCart cart = new ShoppingCart();
		ShoppingGroup group;

		List<CartProducts> cartProducts = new ArrayList<CartProducts>();

		Transaction myTransaction = null;
		Session mySession = null;
		boolean saved = false;
		try {
			cleanupCart(cartId);

			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();

			cart = mySession.get(ShoppingCart.class, cartId);
			// cartProducts = cart.getProductsInCart();
			// cart.getProductsInCart();
			// for (Product product : products) {

			ProductDTO pDTO = new ProductDTO();

			for (CartProducts cartProduct : products) {

				/*
				 * CartProducts cartProduct = new CartProducts();
				 * cartProduct.setProduct(product);
				 */
				/* cartProduct.setProductAmount(product.getProductAmount()); */

				if (pDTO.getProductByProductName(cartProduct.getProduct().getProductName()) == null) {
					pDTO.addProduct(cartProduct.getProduct());
				}

				cartProduct.setAddedDate(jetzt);
				cart.addProductInCart(cartProduct);
				/*
				 * if (i % 20 == 0) { // 20, same as the JDBC batch size //
				 * flush a batch of inserts and release memory:
				 * mySession.flush(); mySession.clear(); }
				 */
			}
			// cart.setProductsInCart(products);
			// cart.setShoppingGroup(group);
			// if (cart.getCartId() > 0) {
			mySession.merge(cart);
			// }
			myTransaction.commit();
			// mySession.refresh(cart);
			saved = true;
		} catch (Exception e) {
			saved = false;
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.INSERTERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else if (e.getClass().isInstance(NoResultForQueryException.class)) {
				cart.setStatus(true);
				group = mySession.get(ShoppingGroup.class, groupId);
				cart.setShoppingGroup(group);

				// for (Product product : products) {
				for (CartProducts cartProduct : products) {
					/*
					 * CartProducts cartProduct = new CartProducts();
					 * cartProduct.setProduct(product);
					 */
					cartProduct.setCart(cart);

					/*
					 * cartProduct.setProductAmount(product.getProductAmount());
					 */
					cartProduct.setAddedDate(jetzt);

					cartProducts.add(cartProduct);
					cart.addProductInCart(cartProduct);
				}
				cart.setShoppingGroup(group);
				mySession.save(cart);

				myTransaction.commit();
				mySession.refresh(cart);

				// throw new NoResultForQueryException(Error.NORESULTERROR);
			} else {
				throw e;
			}

		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
		if (saved == true) {
			cart.setProductsInCart(null);
			return cart;
		} else {
			return null;
		}
	}

	private void cleanupCart(long cartId) {

		Transaction myTransaction = null;
		Session mySession = null;

		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();
			// group = mySession.get(ShoppingGroup.class, groupId);
			ShoppingCart cart = mySession.get(ShoppingCart.class, cartId);
			// List<CartProducts> cartProducts = cart.getProductsInCart();
			Query query = mySession.createQuery("DELETE CartProducts WHERE primaryKey.cart.cartId=:cartId")
					.setParameter("cartId", cartId);

			query.executeUpdate();
			myTransaction.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
	}

	public boolean addOneProductToCart(long groupId, long cartId, Product product) {
		ShoppingCart cart = new ShoppingCart(); // = findCartById(cartId);
		ShoppingGroup group = findShoppingGroupById(groupId);

		CartProducts cartProduct = new CartProducts();

		// cart.getProducts().add(product);
		// cart.addProduct(product);
		Transaction myTransaction = null;
		Session mySession = null;
		boolean saved = false;
		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();

			cartProduct.setProduct(product);
			cart = mySession.get(ShoppingCart.class, cartId);
			cartProduct.setCart(cart);
			cartProduct.setProductAmount(product.getProductAmount());
			cartProduct.setAddedDate(jetzt);
			cart.addProductInCart(cartProduct);
			cart.setShoppingGroup(group);

			mySession.save(cartProduct);

			myTransaction.commit();
			saved = true;
		} catch (Exception e) {
			saved = false;
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.INSERTERROR);
			} else if (e.getClass().isInstance(HibernateException.class)) {
				throw new TransactionExceptionHandler(Error.TRANSACTIONERROR);
			} else if (e.getClass().isInstance(NoResultForQueryException.class)) {
				cart.setStatus(true);
				cart.setShoppingGroup(group);

				cartProduct.setCart(cart);
				cart.addProductInCart(cartProduct);

				cartProduct.setProductAmount(product.getProductAmount());
				cartProduct.setAddedDate(jetzt);

				mySession.save(cart);
				myTransaction.commit();

				// throw new NoResultForQueryException(Error.NORESULTERROR);
			} else {
				throw e;
			}

		} finally {
			if (mySession != null) {
				mySession.close();
			}
		}
		return saved;
	}

	public boolean updateProductsInCart(long groupId, long cartId, List<Product> products) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateProductInCart(long groupId, long cartId, Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeProductFromCart(long groupId, long cartId, long productId) {
		Transaction myTransaction = null;
		Session mySession = null;
		boolean deleted = false;
		ShoppingGroup group = findShoppingGroupById(groupId);
		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();
			Query<Boolean> query = mySession
					.createQuery("DELETE FROM tbl_carts_products WHERE cartId=:cartId AND productId=:productId");
			query.setParameter("cartId", cartId);
			query.setParameter("productId", productId);
			myTransaction.commit();
			deleted = true;
		} catch (Exception e) {
			deleted = false;
			if (myTransaction != null) {
				myTransaction.rollback();
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
		}
		return deleted;

	}

	public ShoppingCart addCart(long groupId, ShoppingCart newCart) {
		Transaction myTransaction = null;
		Session mySession = null;
		boolean saved = false;
		ShoppingGroup group; // = findShoppingGroupById(groupId);
		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();
			group = mySession.get(ShoppingGroup.class, groupId);
			group.addCart(newCart);
			// newCart.setShoppingGroup(group);

			mySession.persist(newCart);
			myTransaction.commit();
			mySession.refresh(newCart);
			System.out.println(newCart.getCartId() + "   " + newCart.getCartId());
			saved = true;
		} catch (Exception e) {
			saved = false;
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.INSERTERROR);
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
		return newCart;
	}

	public ShoppingGroup findShoppingGroupById(long groupId) {
		ShoppingGroup shoppingGroup = null;
		Transaction myTransaction = null;
		Session mySession = null;
		try {
			// mySession = sessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			shoppingGroup = mySession.get(ShoppingGroup.class, groupId);
			/*
			 * Query<ShoppingGroup> query =
			 * mySession.createQuery("FROM ShoppingGroup WHERE groupId=?");
			 * query.setParameter(0, groupId); shoppingGroup =
			 * query.getSingleResult();
			 */
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.INSERTERROR);
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
		return shoppingGroup;
	}

	public ShoppingCart findCartById(long cartId) {
		ShoppingCart cart = null;
		Transaction myTransaction = null;
		Session mySession = null;
		try {
			mySession = configDTO.getSession();
			// mySession = sessionFactory.openSession();
			myTransaction = mySession.beginTransaction();

			Query<ShoppingCart> query = mySession.createQuery(" FROM ShoppingCart WHERE cartId = ?");
			query.setParameter(0, cartId);
			cart = query.getSingleResult();
			// cart.getProducts();
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.INSERTERROR);
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
		return cart;
	}

	public boolean archiveCart(long cartId) {
		boolean archived = false;
		ShoppingCart cart = null;
		Transaction myTransaction = null;
		Session mySession = null;
		try {
			// mySession = sessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			cart = mySession.get(ShoppingCart.class, cartId);
			cart.setStatus(false);
			mySession.update(cart);

			myTransaction.commit();
			archived = true;
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(HibernateException.class)) {
				throw new MyDataNotFoundExceptionHandler(Error.INSERTERROR);
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
		return archived;

	}

}
