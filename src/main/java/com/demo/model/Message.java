package com.demo.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="msgs")   
@CompoundIndexes({//複合索引查詢將大大提高速度
    @CompoundIndex(name = "com_idx", def = "{'type': 1, 'sender': -1}")//排序 1為正序，-1為倒序
})
public class Message {
	
	private String msgId;	
	private String type;
	private String sender;
	@Transient //將不會被錄入到數據庫中。只作為普通的javaBean屬性
	private String receiver;
	@Indexed
	private String body;
	@Field("log")
	private Long logDate;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Long getLogDate() {
		return logDate;
	}
	public void setLogDate(Long logDate) {
		this.logDate = logDate;
	}
}
