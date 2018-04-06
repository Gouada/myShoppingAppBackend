package org.myApps.myShoppingCart.ressources;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.myApps.myShoppingCart.model.ShoppingGroup;
import org.myApps.myShoppingCart.model.ShoppingGroupMember;
import org.myApps.myShoppingCart.service.GroupService;

@Path("/group")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class GroupRessources {
	GroupService gpService = new GroupService();

	@POST
	public Response addGroup(ShoppingGroup group, @Context UriInfo uriInfo) {
		Response response = null;
		ShoppingGroup currentGroup = gpService.addGroup(group);
		if (currentGroup != null) {
			URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(currentGroup.getGroupId())).build();
			response = Response.created(uri).entity(currentGroup).build();
		} else {
			response = Response.status(Status.EXPECTATION_FAILED).build();
		}
		return response;
	}

	@POST
	@Path("/{groupId}")
	public Response addGroupMember(@PathParam("groupId") long groupId, ShoppingGroupMember groupMember) {
		Response response = null;
		if (gpService.addGroupMember(groupId, groupMember)) {
			response = Response.status(Status.CREATED).build();
		} else {
			response = Response.status(Status.EXPECTATION_FAILED).build();
		}
		return response;
	}

	@POST
	@Path("/groupOwnerPhoneNumber/{groupOwnerPhoneNumber}")
	public Response addNewAdherentToGroup(@PathParam("groupOwnerPhoneNumber") String groupOwnerPhoneNumber,
			ShoppingGroupMember groupMember) {

		Response response = null;

		ShoppingGroup createdNewMember = gpService.addNewAdherentToGroup(groupOwnerPhoneNumber, groupMember);
		if (createdNewMember != null) {
			response = Response.status(Status.CREATED).entity(createdNewMember).build();
		} else {
			response = Response.status(Status.NO_CONTENT).build();
		}
		return response;
	}

	@GET
	@Path("/instanceId/{instanceId}")
	public Response getGroupMember(@PathParam("instanceId") String instanceId) {
		Response response = null;
		try {
			ShoppingGroupMember groupMember = new ShoppingGroupMember();
			groupMember = gpService.getGroupMember(instanceId);

			if (groupMember != null) {
				groupMember.getShoppingGroup();
				response = Response.status(Status.OK).entity(groupMember).build();
			} else {
				response = Response.status(Status.NO_CONTENT).build();
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace().toString() + "\n\n\n\n" + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	@GET
	@Path("/groupId/{groupId}")
	public Response getShoppingGroup(@PathParam("groupId") Long groupId) {
		Response response = null;
		try {
			ShoppingGroup group = new ShoppingGroup();
			group = gpService.getShoppingGroup(groupId);
			if (group != null) {
				response = Response.status(Status.OK).entity(group).build();
			} else {
				response = Response.status(Status.NO_CONTENT).build();
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace().toString() + "\n\n\n\n" + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	@GET
	@Path("/memberShoppingGroup")
	// @QueryParam("instanceId")
	public Response getMemberShoppingGroup(@QueryParam("instanceId") String instanceId) {
		Response response = null;
		try {
			ShoppingGroup group = gpService.getMemberShoppingGroup(instanceId);
			if (group != null) {
				response = Response.status(Status.OK).entity(group).build();
			} else {
				response = Response.status(Status.NO_CONTENT).build();
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace().toString() + "\n\n\n\n" + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	@PUT
	@Path("/{groupId}")
	public Response updateGroupMemberData(ShoppingGroupMember groupMember) {
		Response response = null;
		ShoppingGroupMember updatedGroupMember = gpService.updateGroupMemberData(groupMember);
		if (updatedGroupMember != null) {
			response = Response.status(Status.OK).entity(updatedGroupMember).build();
		} else {
			response = Response.status(Status.NOT_MODIFIED).build();
		}
		return response;
	}

	@DELETE
	@Path("/{groupId}")
	public Response deleteGroupMemberData(ShoppingGroupMember groupMember) {
		Response response = null;
		if (gpService.deleteGroupMember(groupMember)) {
			response = Response.status(Status.OK).build();
		} else {
			Response.status(Status.NO_CONTENT).build();
		}
		return response;
	}
}