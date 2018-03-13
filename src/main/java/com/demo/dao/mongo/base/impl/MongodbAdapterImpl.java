package com.demo.dao.mongo.base.impl;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.springframework.util.Assert;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.demo.dao.mongo.base.MongodbAdapter;

public class MongodbAdapterImpl implements MongodbAdapter{


	private MongoClient _mongoClient = null;
	private DB _db = null;
	
	private String _databaseName = null;
	private String _connentString = null;
	
	public void openConnect(String host , int port, String username, String password, String databaseName) throws Exception{
	
		ServerAddress server = new ServerAddress(host, port);
		MongoCredential credential =  MongoCredential.createMongoCRCredential(username, databaseName, password.toCharArray());
		_mongoClient = new MongoClient(server,Arrays.asList(credential), new MongoClientOptions.Builder().build());
		_mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		_db = _mongoClient.getDB(databaseName);
		
		_databaseName = databaseName;
		
		// mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]		 
		_connentString = String.format("mongodb://%s:%s@%s:%s/?authSource=%s", username,password,host,port,databaseName);
	}
	
	public void openConnect(String connentString, String databaseName) throws Exception{
		
    	MongoClientURI uri =new MongoClientURI(connentString);
    	
    	try{
    		_mongoClient = new MongoClient(uri);
    		
//    		for (String s : _mongoClient.getDatabaseNames()) {
//          System.out.println(s);
//      }

    		_mongoClient.setWriteConcern(WriteConcern.JOURNALED);
    		//銝����遣蝡�
    		_db = _mongoClient.getDB(databaseName);    		
    		_databaseName = databaseName;    		
    		
    		_connentString = connentString;
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}

	}		
	
	public void openConnect() throws Exception{
		openConnect(_connentString, _databaseName);
	}

	public void closeConnect() {
		if (null != _mongoClient) {
			_mongoClient.close();
			_mongoClient = null;
			_db = null;
		}
	}

	public MongoClient getMongoClient() throws Exception{
		if (null == _mongoClient) 
			if (_connentString == null) 
				Assert.notNull(_mongoClient, "connect not be null");
			else
				openConnect();
		return _mongoClient;
	}
	
	public DB getDB() throws Exception{
		getMongoClient();
		return _db;
	}	
	
	public void setDatabaseName(String databaseName) throws Exception{
		_db = getMongoClient().getDB(databaseName);
		_databaseName = databaseName;
	}
	
	public String getDatabaseName() throws Exception{
		return _databaseName;
	}
}
