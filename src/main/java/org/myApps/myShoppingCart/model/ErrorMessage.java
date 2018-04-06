package org.myApps.myShoppingCart.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {
	private int erorCode;
	private String message;
	private String documentation;

	public ErrorMessage() {
	}

	public ErrorMessage(String message, int erorCode, String documentation) {
		setErorCode(erorCode);
		setDocumentation(documentation);
		setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErorCode() {
		return erorCode;
	}

	public void setErorCode(int erorCode) {
		this.erorCode = erorCode;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

}
