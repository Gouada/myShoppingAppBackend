package org.myApps.myShoppingCart.helpers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.myApps.myShoppingCart.model.ShoppingGroup;
import org.myApps.myShoppingCart.model.ShoppingGroupMember;
import org.myApps.myShoppingCart.service.CartService;

public class FirebaseTrigger {

	private ShoppingGroupMember sender;
	private List<ShoppingGroupMember> receiver;
	private ShoppingGroup group;
	private String language;

	public final static String NEW_CART_CREATED_TITLE_FR = "un nouveau panier crée/modifié";
	public final static String NEW_CART_CREATED_TITLE_DE = "Einkaufskorb angelegt/geändert";

	public final static String NEW_CART_CREATED_FR = "un nouveau panier a été crée ou le panier actuel a été modifié";
	public final static String NEW_CART_CREATED_DE = "ein neuer Einkaufskorb wurde angelegt oder der aktuelle Einkaufskorb wurde geändert";

	public final static String CART_ARCHIVED_TITLE_FR = "achats terminés";
	public final static String CART_ARCHIVED_TITLE_DE = "Einkäufe erledigt";

	public final static String CART_ARCHIVED_FR = "le panier actuel a été archivé";
	public final static String CART_ARCHIVED_DE = "der aktuelle Einkaufskorb wurde archiviert";

	private long groupId;
	private long senderId;

	CartService cs = new CartService();

	public FirebaseTrigger(ShoppingGroup group, ShoppingGroupMember sender) {
		super();
		this.group = group;
		this.sender = sender;
	}

	public FirebaseTrigger(long groupId, long senderId, String language) {
		super();
		this.groupId = groupId;
		this.senderId = senderId;
		this.language = language;

	}

	public FirebaseTrigger() {
	}

	public ShoppingGroup getGroup() {
		return group;
	}

	public void setGroup(ShoppingGroup group) {
		this.group = group;
	}

	public ShoppingGroupMember getSender() {
		return sender;
	}

	public void setSender(ShoppingGroupMember sender) {
		this.sender = sender;
	}

	public List<ShoppingGroupMember> getReceiver() {
		return receiver;
	}

	public void setReceiver(List<ShoppingGroupMember> receiver) {
		this.receiver = receiver;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void fetchReceiverList(ShoppingGroupMember sender, ShoppingGroup group) {
		setReceiver(cs.fetchReceiverList(sender, group));
	}

	public void fetchReceiverListByIds(long memberId, long groupId) {
		setReceiver(cs.fetchReceiverListByIds(memberId, groupId));
	}

	public void trigerFirebase(int pourpose) {
		// fetchReceiverList(sender, group);
		fetchReceiverListByIds(this.senderId, this.groupId);
		buildRequest(receiver, pourpose);
		// fireRequest();
	}

	private void buildRequest(List<ShoppingGroupMember> receiver, int pourpose) {
		String notification_title = "";
		String notification_body = "";

		switch (pourpose) {
		case 1:
			if (this.getLanguage().equalsIgnoreCase("fr")) {
				notification_title = NEW_CART_CREATED_TITLE_FR;
				notification_body = NEW_CART_CREATED_FR;
			} else {
				notification_title = NEW_CART_CREATED_TITLE_DE;
				notification_body = NEW_CART_CREATED_DE;
			}
			break;
		case 2:
			if (this.getLanguage().equalsIgnoreCase("fr")) {
				notification_title = CART_ARCHIVED_TITLE_FR;
				notification_body = CART_ARCHIVED_FR;
			} else {
				notification_title = CART_ARCHIVED_TITLE_DE;
				notification_body = CART_ARCHIVED_DE;
			}
			break;
		default:
			break;
		}
		HttpURLConnection connection = null;
		String receiverTokenList = "";
		String message;
		final String firebaseTargetURL = "https://fcm.googleapis.com/fcm/send";
		final String SERVER_KEY = "key=...";
		SERVER_KEY.replace("\n", "");
		String notification = "";
		// if (pourpose == 1) {
		notification = "{ \"notification\": {" + "\"title\": \"" + notification_title + "\"," + "\"body\": \""
				+ notification_body + "\"" + "},";
		// } else if (pourpose == 2) {
		// notification = "{ \"notification\": {" + "\"title\": \" CURRENT CART
		// ARCHIVED \","
		// + "\"body\": \"SHOPPING DONE, CURRENT CART ARCHIEVED\"" + "},";
		// }

		if (receiver != null) {
			int rs = receiver.size();
			receiverTokenList = "\"to\" :";
			if (rs == 1) {
				receiverTokenList += "\"" + receiver.get(0).getFirebaseDeviceToken() + "\"";
			} else if (rs > 1) {
				receiverTokenList = "\"registration_ids\": [";
				// receiverTokenList += "\"/topics/MyShoppingCartApp\"";

				for (int i = 0; i < rs; i++) {
					// if(i == rs-1) //
					receiverTokenList += "\"" + receiver.get(i).getFirebaseDeviceToken() + "\"";
					// else
					receiverTokenList += "\"" + receiver.get(i).getFirebaseDeviceToken() + "\", ";
				}
				receiverTokenList = receiverTokenList.substring(0, receiverTokenList.length() - 2) + "]";

			}
			receiverTokenList += "}";
		}
		message = notification + receiverTokenList;

		System.out.println(" ......       " + message + "    ........");
		try {
			// Create connection
			URL url = new URL(firebaseTargetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			connection.setRequestProperty("Authorization", SERVER_KEY);

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(message.getBytes("UTF-8"));

			// System.out.println("this the request i sent" + message);

			wr.close();

			// Get Response
			String line;
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuffer response = new StringBuffer(); // or StringBuffer if
														// Java version 5+
														// String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			System.out.println("this the response i got" + response.toString());
			rd.close();
			// return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
