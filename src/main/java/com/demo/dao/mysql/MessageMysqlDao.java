package com.demo.dao.mysql;

import com.demo.model.Message;

public interface MessageMysqlDao {
	int insert(Message message);

	int insert2(Message message);
}
