package com.demo.dao.mysql;

import java.util.List;

import com.demo.model.Message;

public interface MessageMysqlDao {
	int insert(Message message);

	int insert2(Message message);

	List<Message> getbyType(String type);
}
