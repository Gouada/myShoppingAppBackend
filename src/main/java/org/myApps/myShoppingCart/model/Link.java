package org.myApps.myShoppingCart.model;

public class Link {
	private String link;
	private String description;

	public Link(String link, String description) {
		super();
		this.link = link;
		this.description = description;
	}

	public Link() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
