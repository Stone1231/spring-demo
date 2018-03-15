package com.demo.dao.mongo;

import java.util.List;

import com.demo.dao.mongo.base.MongodbBaseDao;
import com.demo.model.Message;

public interface MessageMongoDao extends MongodbBaseDao {
	List<Message> queryById(String id) throws Exception;
	List<Message> queryTypeAfter(String type, long timestamp, int skip, int limit) throws Exception;
	List<Message> queryTypeBefore(String type, long timestamp, int skip, int limit) throws Exception;
}
