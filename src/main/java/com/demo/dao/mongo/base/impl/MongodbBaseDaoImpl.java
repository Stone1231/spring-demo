package com.demo.dao.mongo.base.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.demo.dao.mongo.base.MongodbBaseDao;
import com.demo.dao.mongo.base.MongodbCreateIndexListener;
import com.demo.utils.StringUtil;


public class MongodbBaseDaoImpl extends MongodbAdapterImpl implements MongodbBaseDao {

	private static final Logger LOGGER = LogManager.getLogger(MongodbBaseDaoImpl.class);
	
	@Value("${mongo.enable}") 
	boolean enable;
	
	private String tableNameString = null;
	private MongodbCreateIndexListener createTablelistener = null;
	private DBCollection dbCollectionTable = null;
	
	@Override
	public void init(String connentString, String databaseName,
			String tableName, MongodbCreateIndexListener listener) throws Exception{
		
		if(!enable){
			return;
		}
		
		try {
			super.openConnect(connentString, databaseName);
			createTable(tableName, listener);
		} catch (Exception e) {
			LOGGER.error("MongodbBaseDaoImpl",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}

	/**
	 * close Connect
	 */
	@Override
	public void closeConnect() {
		super.closeConnect();
		dbCollectionTable = null;
	}
	
	/**
	 * Get Table Name
	 * @return
	 * @throws Exception
	 */
	private String getTableName() {
		return tableNameString;
	}	
	
	/**
	 * Get Create Table Listener
	 * @return
	 * @throws Exception
	 */
	private MongodbCreateIndexListener getCreateTablelistener() {
		return createTablelistener;
	}	

	
	/**
	 * Check Table Exists
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private boolean tableExists(String tableName) throws Exception{
		return super.getDB().collectionExists(tableName);
	}
	
	/**
	 * Get Table List
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private DBCollection getTable(String tableName) throws Exception{
	
		if (!tableExists(tableName)) {
			Assert.notNull(dbCollectionTable, "table not be null");
		}else{
			dbCollectionTable = super.getDB().getCollection(tableName);
		}
		
		return dbCollectionTable;
	}	
	
	/**
	 * Get Table List
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public DBCollection getTable() throws Exception{
		if (dbCollectionTable ==null)
			return getTable(getTableName());
		else
			return dbCollectionTable;
	}	
	
	/**
	 * Reset Table
	 */
	public void resetTable() throws Exception{
		super.getDB().getCollection(getTableName()).drop();
		createTable(getTableName(), getCreateTablelistener());
	}
	
	/**
	 * create Table
	 * 
	 * @param tableName
	 * @param listener
	 * @throws Exception
	 */
	private void createTable(String tableName, MongodbCreateIndexListener listener) throws Exception{
		
		
		if (!tableExists(tableName)) {
			super.getDB().createCollection(tableName, new BasicDBObject("capped", true).append("size", 5242880).append("max" , 5000 ));
			dbCollectionTable = super.getDB().getCollection(tableName);
	
	        // drop all the data in it
			dbCollectionTable.drop();
	
			createIndex(getTable(), listener);
		}
		
		createTablelistener = listener;
		tableNameString = tableName;

	}

	/**
	 * Alter Table
	 * 
	 * @param listeners
	 * @throws Exception
	 */
	@Override
	public void alterTable(List<MongodbCreateIndexListener> listeners) throws Exception{
		
		try {
			for(MongodbCreateIndexListener listener :listeners){
				createIndex(getTable(), listener);
			}
		} catch (Exception e) {
			LOGGER.error("createIndex",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}
	
	/**
	 * create Table Index
	 * 
	 * @param table
	 * @param listener
	 * @throws Exception
	 */
	private void createIndex(DBCollection table, MongodbCreateIndexListener listener) {
		
		if (listener != null) {
			listener.createUniqueIndex(table);;
			listener.createIndex(table);
		}

	}
	
	/**
	 * insert entity to Table row
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@Override
	public <T> WriteResult insert (T item) throws Exception{
		
		try {
			String json = StringUtil.writeJSON(item);
			return insertData(Arrays.asList((DBObject)JSON.parse(json)));
		} catch (Exception e) {
			LOGGER.error("insert",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}
	
	/**
	 * insert entity to Table rows
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@Override
	public <T> WriteResult insert (List<T> list) throws Exception{
				
		try {
			List<DBObject> dbObjects = new ArrayList<>();
			for(T item:list){
				String json = JSON.serialize(item);
				dbObjects.add((DBObject)JSON.parse(json));
			}
			return insertData(dbObjects);
		} catch (Exception e) {
			LOGGER.error("insert List",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}
	
	/**
	 * insert Table rows
	 * 
	 * @param dbObjects
	 * @return
	 * @throws Exception
	 */
	private WriteResult insertData(List<DBObject> dbObjects) throws Exception{
		return getTable().insert(dbObjects);
	}
	
	/**
	 * Update entity to Table rows
	 * 
	 * @param collKeys: PK coll
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@Override
	public <T> WriteResult update (String collKeys,T item) throws Exception{
		
		try {
			String json = StringUtil.writeJSON(item);
			DBObject obj = (DBObject)JSON.parse(json);
			BasicDBObject original = new BasicDBObject();
			for(String collKey : collKeys.split(","))
				original = original.append(collKey, obj.get(collKey));
			
			return updateData(original, obj, false ,false);
		} catch (Exception e) {
			LOGGER.error("update",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}
	
	/**
	 * Update entity to Table rows
	 * 
	 * @param collKeys: PK coll
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@Override
	public <T> WriteResult update (String collKeys,List<T> list) throws Exception{
		
		try {
			WriteResult result = null;
			for(T item:list){
				String json = JSON.serialize(item);
				DBObject obj = (DBObject)JSON.parse(json);
				BasicDBObject original = new BasicDBObject();
				for(String collKey : collKeys.split(","))
					original = original.append(collKey, obj.get(collKey));

				result = updateData(original, obj, false ,false);
			}
			
			return result;
		} catch (Exception e) {
			LOGGER.error("update list",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}
	
	/**
	 * Update Table rows
	 * 
	 * @param original: original PK data
	 * @param modify: modify PK data
	 * @param upsert
	 * @param multi
	 * @return
	 * @throws Exception
	 */
	private WriteResult updateData( DBObject original , DBObject modify , boolean upsert , boolean multi) throws Exception{
		return getTable().update(original, modify, upsert, multi);		
	}	
	
	/**
	 * Save entity to Table rows (UK for ID)
	 * 	1. Update
	 * 	2. Insert
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@Override
	public <T> WriteResult save (T item) throws Exception{
		
		try {
			String json = StringUtil.writeJSON(item);
			return saveData((DBObject)JSON.parse(json));
		} catch (Exception e) {
			LOGGER.error("save",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}
	
	@Override
	public <T> WriteResult save (String tableName, T item) throws Exception {		
		try {
			String json = StringUtil.writeJSON(item);
			return saveData(tableName, (DBObject)JSON.parse(json));
		} catch (Exception e) {
			LOGGER.error("save",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}
	
	@Override
	public <T> WriteResult save (List<T> list) throws Exception{
		
		try {
			WriteResult result = null;
			for(T item:list){
				String json = JSON.serialize(item);
				result = saveData((DBObject)JSON.parse(json));
			}
			
			return result;
		} catch (Exception e) {
			LOGGER.error("save list",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}

	private WriteResult saveData(DBObject dbObjects) throws Exception{
		return getTable().save(dbObjects);		
	}
	
	private WriteResult saveData(String tableName, DBObject dbObjects) throws Exception{
		return getTable(tableName).save(dbObjects);		
	}
	
	/**
	 * remove entity to Table rows
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	@Override
	public <T> WriteResult remove (T item) throws Exception{
		
		try {
			String json = StringUtil.writeJSON(item);			
			return removeData((DBObject)JSON.parse(json));
		} catch (Exception e) {
			LOGGER.error("remove",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}
	
	private WriteResult removeData(DBObject dbObjects) throws Exception{
		return getTable().remove(dbObjects);		
	}
	
	@Override
	public <T> List<T> transFormList(DBCursor dbCursor, Class<T> clazz) {
	
		LOGGER.info(String.format("Query(%s) :%s %s", dbCursor.count(), clazz.getName(), "\n"));

		try {
			List<T> transform = new ArrayList<>();
			while (dbCursor.hasNext()) {
				String json = dbCursor.next().toString();
				LOGGER.info(String.format("value(%s) :%s %s", transform.size(), json, "\n"));
				T data = StringUtil.readJSON(json, clazz);
				transform.add(data);
			}
					
			return transform;
		} catch (Exception e) {
			LOGGER.error("trans form List",e);
			throw e; 
		} finally{
			//closeConnect();			
		}
	}


}
