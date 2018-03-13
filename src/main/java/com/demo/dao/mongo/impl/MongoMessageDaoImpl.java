package com.demo.dao.mongo.impl;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.dao.mongo.base.impl.MongoConfig;
import com.demo.dao.mongo.base.impl.MongodbBaseDaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.demo.dao.mongo.base.MongodbCreateIndexListener;

@Service
public class MongoMessageDaoImpl extends MongodbBaseDaoImpl {

	private static final Logger logger = LogManager.getLogger(MongoMessageDaoImpl.class);
	
	@Autowired
	MongoConfig mongoConfig;
	
	@Value("${mongo.enable}") 
	boolean enable;
	
	public MongoMessageDaoImpl() {
	}
	
	@PostConstruct
	public void init() {
		
		if(!enable){
			return;
		}
		
		try {
			super.init(
					MongoConfig.Config.CONNENTSTRING,
					MongoConfig.Config.DATABASENAME,
					MongoConfig.TableName.MSG_TABLE, 
					new MongodbCreateIndexListener(){

						@Override
						public void createUniqueIndex(DBCollection table) {
							// TODO Auto-generated method stub
							table.createIndex(
					                new BasicDBObject()
					                	.append("chatID", 1)
					                	.append("logDate", 1), "UniquePK", true);
						}

						@Override
						public void createIndex(DBCollection table) {
							// TODO Auto-generated method stub
							table.createIndex(
					                new BasicDBObject()
					                	.append("chatID", 1)
					                	.append("id", 1), "chatIDAndIdIndex");
						}
						
					});
		} catch (Exception e) {
			logger.error("init fail!");
		}
	}

}
