package com.demo.service;

import java.util.List;

import com.demo.model.Message;

public interface MessageService {
	List<Message> getbyType(String type);
}
