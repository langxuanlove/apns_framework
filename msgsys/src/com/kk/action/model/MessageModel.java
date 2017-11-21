package com.kk.action.model;

public class MessageModel {
	
	private String tokenId;
	private String message;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void MessgeModel(String tokenId, String message) {
		this.tokenId = tokenId;
		this.message = message;
	}

	public void MessgeModel() {
	}
}
