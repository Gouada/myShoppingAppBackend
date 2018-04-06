package org.myApps.myShoppingCart.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.myApps.myShoppingCart.exceptionHandling.MyDataNotFoundExceptionHandler;
import org.myApps.myShoppingCart.exceptionHandling.TransactionExceptionHandler;
import org.myApps.myShoppingCart.model.Error;
import org.myApps.myShoppingCart.model.ShoppingCart;
import org.myApps.myShoppingCart.model.ShoppingGroup;
import org.myApps.myShoppingCart.model.ShoppingGroupMember;

public class ShoppingGroupDTO {

	private final Date jetzt = new Date();
	// private static SessionFactory mySessionFactory;
	private static ConfigurationDTO configDTO;

	public ShoppingGroupDTO() {
		configDTO = ConfigurationDTO.getInstance();
		// mySessionFactory = new
		// Configuration().configure().buildSessionFactory();
	}

	public ShoppingGroup addGroup(ShoppingGroup group) {
		Transaction myTransaction = null;
		Session mySession = null;
		boolean added = false;

		ShoppingGroup currentGroup = new ShoppingGroup();
		currentGroup.setGroupName(group.getGroupName());
		currentGroup.setCreated(jetzt);

		ShoppingGroupMember member = new ShoppingGroupMember();

		member.setUserName(group.getGroupMembers().get(0).getUserName());
		member.setPhoneNumber(group.getGroupMembers().get(0).getPhoneNumber());
		member.setDeviceId(group.getGroupMembers().get(0).getDeviceId());
		member.setInstanceId(group.getGroupMembers().get(0).getInstanceId());
		member.setFirebaseDeviceToken(group.getGroupMembers().get(0).getFirebaseDeviceToken());

		member.setCreated(jetzt);
		currentGroup.addMember(member);

		// group.getGroupMembers().get(0).setShoppingGroup(group);
		try {
			mySession = configDTO.getSession();
			// mySession = mySessionFactory.openSession();
			myTransaction = mySession.beginTransaction();

			mySession.persist(currentGroup);
			// mySession.flush();
			myTransaction.commit();
			// mySession.refresh(currentGroup);

			added = true;
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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
		return currentGroup;
	}

	public boolean addGroupMember(long groupId, ShoppingGroupMember groupMember) {
		Transaction myTransaction = null;
		Session mySession = null;
		boolean added = false;
		ShoppingGroup group;
		try {
			// mySession = mySessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			group = mySession.get(ShoppingGroup.class, groupId);
			groupMember.setDeviceId("111111111222222");
			groupMember.setInstanceId("222222223323");
			groupMember.setShoppingGroup(group);
			group.addMember(groupMember);

			// groupMember.setShoppingGroup(group);
			mySession.merge(group);
			// mySession.persist(groupMember);

			myTransaction.commit();
			added = true;
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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
		return added;
	}

	public ShoppingGroupMember getGroupMember(String instanceId) {
		Transaction myTransaction = null;
		Session mySession = null;
		ShoppingGroupMember groupMember = null;
		try {
			// mySession = mySessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			Query query = mySession.createQuery("FROM ShoppingGroupMember WHERE instanceId=:instanceId")
					.setParameter("instanceId", instanceId);

			// groupMember = (ShoppingGroupMember) query.getSingleResult();
			ArrayList<ShoppingGroupMember> results = (ArrayList<ShoppingGroupMember>) query.getResultList();
			if (!results.isEmpty()) {
				groupMember = results.get(0);

				if (groupMember != null) {
					groupMember.getShoppingGroup();
					if (groupMember.getShoppingGroup().getCurrentShoppingCart() != null) {
						groupMember.getShoppingGroup().getCurrentShoppingCart().getProductsInCart();
					} else {
						ShoppingCart newCart = new ShoppingCart();
						newCart.setStatus(true);
						newCart.setCreated(new Date());

						CartDTO cartDTO = new CartDTO();
						cartDTO.addCart(groupMember.getShoppingGroup().getGroupId(), newCart);
						groupMember.getShoppingGroup().setCurrentShoppingCart(newCart);
					}
				}
			}

			myTransaction.commit();

		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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
		/*
		 * if (groupMember != null) { // ???groupMember.getShoppingGroup(); long
		 * groupId = groupMember.getShoppingGroup().getGroupId();
		 * groupMember.getShoppingGroup().setCurrentShoppingCart(
		 * getCurrentShoppingCart(groupId)); }
		 */
		return groupMember;
	}

	public ShoppingCart getCurrentShoppingCart(long groupId) {
		Transaction myTransaction = null;
		Session mySession = null;
		ShoppingCart currentCart;
		try {
			// mySession = mySessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			Query query = mySession.createQuery("FROM ShoppingCart WHERE groupId=:groupId AND status=:status")
					.setParameter("groupId", groupId).setParameter("status", true);

			currentCart = (ShoppingCart) query.getSingleResult();
			currentCart.getProductsInCart();
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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

		return currentCart;
	}

	public ShoppingGroup getShoppingGroup(Long groupId) {
		ShoppingGroup group = null;
		Transaction myTransaction = null;
		Session mySession = null;
		try {
			// mySession = mySessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			Query query = mySession.createQuery("FROM ShoppingGroup WHERE  groupId=:groupId ").setParameter("groupId",
					groupId);
			if (!query.getResultList().isEmpty()) {
				group = (ShoppingGroup) query.getResultList().get(0);
			}
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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
		return group;
	}

	public ShoppingGroup getMemberShoppingGroup(String instanceId) {
		ShoppingGroup group = new ShoppingGroup();
		ShoppingGroupMember member = new ShoppingGroupMember();
		Transaction myTransaction = null;
		Session mySession = null;
		ShoppingGroup preparedGroup = null;
		try {
			// mySession = mySessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			Query query = mySession.createQuery(" FROM ShoppingGroupMember WHERE instanceId=:instanceId")
					.setParameter("instanceId", instanceId);
			if (!query.getResultList().isEmpty()) {
				member = (ShoppingGroupMember) query.getResultList().get(0);
				group = member.getShoppingGroup();
				preparedGroup = new ShoppingGroup(group.getGroupId(), group.getGroupName());
				preparedGroup.addMember(member);
			}
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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
		return preparedGroup;

	}

	public List<ShoppingGroupMember> fetchReceiverList(ShoppingGroupMember sender, ShoppingGroup group) {

		Transaction myTransaction = null;
		Session mySession = null;
		List<ShoppingGroupMember> members = null;
		try {
			// mySession = mySessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			Query query = mySession
					.createQuery(
							" FROM ShoppingGroupMember AS gm WHERE gm.userId <>senderId AND gm.shoppingGroup :=group")
					.setParameter("senderId", sender.getUserId()).setParameter("group", group);
			members = query.list();
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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
		return members;
	}

	public List<ShoppingGroupMember> fetchReceiverListByIds(long senderId, long groupId) {
		Transaction myTransaction = null;
		Session mySession = null;
		List<ShoppingGroupMember> members = null;
		try {
			// mySession = mySessionFactory.openSession();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();
			Query query = mySession
					.createQuery(
							" FROM ShoppingGroupMember AS gm WHERE gm.userId <> :senderId  AND gm.shoppingGroup = (FROM ShoppingGroup WHERE groupId=:groupId)")
					.setParameter("groupId", groupId).setParameter("senderId", senderId);
			members = query.list();
			// System.out.println("This is the device token" +
			// members.get(0).getFirebaseDeviceToken());
			myTransaction.commit();
		} catch (Exception e) {
			if (myTransaction != null) {
				myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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
		return members;
	}

	public ShoppingGroup addNewAdherentToGroup(String groupOwnerPhoneNumber, ShoppingGroupMember groupMember) {
		Transaction myTransaction = null;
		Transaction myTransaction2 = null;

		ShoppingCart currentCarrt = null;
		Session mySession = null;
		ShoppingGroupMember groupOwner = null;
		ShoppingGroup preparedGroup = null;
		try {
			// mySession = mySessionFactory.openSession();
			ShoppingGroup group = new ShoppingGroup();
			mySession = configDTO.getSession();
			myTransaction = mySession.beginTransaction();

			Query getGroupOwner = mySession
					.createQuery("FROM ShoppingGroupMember WHERE phoneNumber=:groupOwnerPhoneNumber")
					.setParameter("groupOwnerPhoneNumber", groupOwnerPhoneNumber);

			if (getGroupOwner.list() != null && getGroupOwner.list().size() > 0) {
				groupOwner = (ShoppingGroupMember) getGroupOwner.list().get(0);
				group = groupOwner.getShoppingGroup();
				groupMember.setCreated(jetzt);
				group.addMember(groupMember);
				mySession.merge(group);
				currentCarrt = group.getCurrentShoppingCart();
				myTransaction.commit();
			}
			if (group != null && groupMember != null) {
				preparedGroup = new ShoppingGroup(group.getGroupId(), group.getGroupName(), group.getCreated());
				ShoppingGroupMember preparedGroupMember = null;
				myTransaction2 = mySession.beginTransaction();
				List resultList = mySession
						.createQuery("FROM ShoppingGroupMember WHERE  phoneNumber LIKE :newUserPhoneNumber")
						.setParameter("newUserPhoneNumber", groupMember.getPhoneNumber()).list();
				if (!resultList.isEmpty()) {
					preparedGroupMember = (ShoppingGroupMember) resultList.get(0);
					preparedGroup.addMember(preparedGroupMember);
				}
				if (currentCarrt != null) {
					preparedGroup.setCurrentShoppingCart(currentCarrt);
					preparedGroup.addCart(currentCarrt);
				}

				myTransaction2.commit();
			}

		} catch (Exception e) {
			if (myTransaction != null || myTransaction2 == null) {
				// myTransaction.rollback();
			}
			if (e.getClass().isInstance(MyDataNotFoundExceptionHandler.class)) {
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
		return preparedGroup;
	}

}
