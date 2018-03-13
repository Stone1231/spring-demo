package com.demo.dao.mongo.base;

import com.mongodb.DBCollection;

public interface MongodbCreateIndexListener {
	public void createUniqueIndex(DBCollection table);
	public void createIndex(DBCollection table);

}
