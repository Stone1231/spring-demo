package com.demo.service;

import java.util.List;

import com.demo.model.Message;

public interface MessageService {
	List<Message> getType(String type);

	List<Message> getTypeCache(String type);

	List<Message> getSenderAndType(Message message);

	List<Message> getSenderAndTypeCache(Message message);

	List<Message> getReceiverAndType(Message message);

	List<Message> getReceiverAndTypeCache(Message message);
	
	void create(Message message);

	List<Message> getReceiverAndTypeProxy(Message message);

	Message convertMsg(Message messsage) throws Exception;
}
