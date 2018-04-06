package org.myApps.myShoppingCart.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@XmlRootElement
@Entity
@Table(name = "tbl_user")
public class ShoppingGroupMember {

	private Long userId;
	private String userName;
	private String deviceId;
	private String instanceId;
	private String firebaseDeviceToken;
	private String phoneNumber;
	private Date created;
	private ShoppingGroup shoppingGroup;

	@ManyToOne
	@JoinColumn(name = "fd_group_id")
	@JsonBackReference
	public ShoppingGroup getShoppingGroup() {
		return shoppingGroup;
	}

	public void setShoppingGroup(ShoppingGroup shoppingGroup) {
		this.shoppingGroup = shoppingGroup;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public ShoppingGroupMember() {
		super();
	}

	public ShoppingGroupMember(Long userId, String userName, Date created, String phoneNumber) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.created = created;
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "fd_created")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	public Date getCreated() {
		return created;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "fd_username", nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "fd_phone_number", nullable = false, unique = true)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "fd_device_id", nullable = true)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "fd_instanceid", nullable = true)
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Column(name = "fd_device_token")
	public String getFirebaseDeviceToken() {
		return firebaseDeviceToken;
	}

	public void setFirebaseDeviceToken(String firebaseDeviceToken) {
		this.firebaseDeviceToken = firebaseDeviceToken;
	}

}
