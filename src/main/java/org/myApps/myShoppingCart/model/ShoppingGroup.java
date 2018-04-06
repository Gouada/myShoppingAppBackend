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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@XmlRootElement
@Entity
@Table(name = "tbl_shopping_group")
public class ShoppingGroup {

	private long groupId;
	private String groupName;
	private Date created;
	private List<ShoppingGroupMember> groupMembers;
	private List<ShoppingCart> carts;

	public ShoppingGroup() {
		super();
	}

	public ShoppingGroup(long groupId, String groupName, Date created) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.created = created;
	}

	public ShoppingGroup(long groupId, String groupName) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "fd_group_id")
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "fd_group_name")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "fd_created")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	// a z")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@OneToMany(mappedBy = "shoppingGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	public List<ShoppingGroupMember> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<ShoppingGroupMember> groupMembers) {
		if (groupMembers != null) {
			int groupSize = groupMembers.size();
			for (int i = 0; i < groupSize; i++) {
				groupMembers.get(i).setShoppingGroup(this);

			}
		}

		this.groupMembers = groupMembers;
	}

	@Transient
	public ShoppingCart getCurrentShoppingCart() {
		ShoppingCart currCarrt = null;
		List<ShoppingCart> localCarts = new ArrayList<ShoppingCart>();
		localCarts = this.getCarts();
		if (localCarts != null) {
			for (ShoppingCart cart : this.carts) {
				if (cart.isStatus() == true) {
					currCarrt = cart;
					this.setCurrentShoppingCart(cart);
				}
			}
		}
		return currCarrt;
	}

	public void setCurrentShoppingCart(ShoppingCart currentShoppingCart) {
	}

	@OneToMany(mappedBy = "shoppingGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	public List<ShoppingCart> getCarts() {
		return carts;
	}

	public void setCarts(List<ShoppingCart> carts) {
		/*
		 * if (carts != null && carts.size() > 0) { for (ShoppingCart cart :
		 * carts) { cart.setShoppingGroup(this); } }
		 */
		this.carts = carts;
	}

	public void addMember(ShoppingGroupMember member) {
		member.setShoppingGroup(this);
		if (this.groupMembers == null) {
			this.groupMembers = new ArrayList<ShoppingGroupMember>();
		}
		this.groupMembers.add(member);
	}

	public void addCart(ShoppingCart cart) {
		cart.setShoppingGroup(this);
		if (this.carts == null) {
			this.carts = new ArrayList<ShoppingCart>();
		}
		this.carts.add(cart);
	}

}
