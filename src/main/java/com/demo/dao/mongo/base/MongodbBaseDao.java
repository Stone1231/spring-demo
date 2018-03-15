package com.demo.dao.mongo.base;

import java.util.List;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;

public interface MongodbBaseDao extends MongodbAdapter {

	void resetTable() throws Exception;

	void init(String connentString, String databaseName, String tableName, MongodbCreateIndexListener listener)
			throws Exception;

	DBCollection getTable() throws Exception;

	void alterTable(List<MongodbCreateIndexListener> listeners) throws Exception;

	<T> WriteResult insert(T item) throws Exception;

	<T> WriteResult insert(List<T> list) throws Exception;

	<T> WriteResult update(String collKeys, T item) throws Exception;

	<T> WriteResult update(String collKeys, List<T> list) throws Exception;

	<T> WriteResult save(T item) throws Exception;

	<T> WriteResult save(String tableName, T item) throws Exception;

	<T> WriteResult save(List<T> list) throws Exception;

	<T> WriteResult remove(T item) throws Exception;

	<T> List<T> transFormList(DBCursor dbCursor, Class<T> clazz);
}
