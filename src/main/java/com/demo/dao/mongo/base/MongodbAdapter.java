package com.demo.dao.mongo.base;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public interface MongodbAdapter {

	public void openConnect(String host , int port, String username, String password, String databaseName) throws Exception;
	public void openConnect(String connentString, String databaseName) throws Exception;
	public void openConnect() throws Exception;

	public void closeConnect();
	
	public MongoClient getMongoClient() throws Exception;
	public DB getDB() throws Exception;
	//public void setDatabaseName(String databaseName) throws Exception;
	public String getDatabaseName() throws Exception;
}
