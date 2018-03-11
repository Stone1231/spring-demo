package com.demo.model;

public class Message {

	private String type;
	private String chatID;
	private String sender;
	private String receiver;
	private Long logDate;
	private String body;
	private String category;
	private String datatype;
	private String id;
	private String[] args;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChatID() {
		return chatID;
	}

	public void setChatID(String chatID) {
		this.chatID = chatID;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Long getLogDate() {
		return logDate;
	}

	public void setLogDate(Long logDate) {
		this.logDate = logDate;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}
}
