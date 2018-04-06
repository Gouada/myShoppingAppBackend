package org.myApps.myShoppingCart.service;

import java.util.Date;

import org.myApps.myShoppingCart.dto.ShoppingGroupDTO;
import org.myApps.myShoppingCart.model.ShoppingGroup;
import org.myApps.myShoppingCart.model.ShoppingGroupMember;

public class GroupService {

	private final ShoppingGroupDTO shoppingGpDTO;
	private final Date jetzt = new Date();

	public GroupService() {
		super();
		shoppingGpDTO = new ShoppingGroupDTO();
	}

	public ShoppingGroup addGroup(ShoppingGroup group) { // ,
															// ShoppingGroupMember
		// groupMember) {
		group.setCreated(jetzt);
		// groupMember.setCreated(jetzt);
		return shoppingGpDTO.addGroup(group);// , groupMember);
	}

	public boolean addGroupMember(long groupId, ShoppingGroupMember groupMember) {
		groupMember.setCreated(jetzt);
		return shoppingGpDTO.addGroupMember(groupId, groupMember);
	}

	public ShoppingGroupMember getGroupMember(String instanceId) {
		return shoppingGpDTO.getGroupMember(instanceId);
	}

	public ShoppingGroupMember updateGroupMemberData(ShoppingGroupMember groupMember) {
		return null;
	}

	public boolean deleteGroupMember(ShoppingGroupMember groupMember) {
		// TODO Auto-generated method stub
		return false;
	}

	public ShoppingGroup getShoppingGroup(Long groupId) {
		return shoppingGpDTO.getShoppingGroup(groupId);
	}

	public ShoppingGroup getMemberShoppingGroup(String instanceId) {
		return shoppingGpDTO.getMemberShoppingGroup(instanceId);
	}

	public ShoppingGroup addNewAdherentToGroup(String groupOwnerPhoneNumber, ShoppingGroupMember groupMember) {
		return shoppingGpDTO.addNewAdherentToGroup(groupOwnerPhoneNumber, groupMember);
	}

}
