package com.demo.dao.mongo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.demo.dao.mongo.base.impl.MongoConfig;
import com.demo.dao.mongo.base.impl.MongodbBaseDaoImpl;
import com.demo.model.Message;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.demo.dao.mongo.MessageMongoDao;
import com.demo.dao.mongo.base.MongodbCreateIndexListener;

//@Service
@Repository
public class MessageMongoDaoImpl extends MongodbBaseDaoImpl implements MessageMongoDao {

	private static final Logger logger = LogManager.getLogger(MessageMongoDaoImpl.class);
	
	@Autowired
	MongoConfig mongoConfig;
	
	public MessageMongoDaoImpl() {
	}
	
	@PostConstruct
	public void init() {
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
					                	.append("msgID", 1)
					                	.append("logDate", 1), "UniquePK", true);
						}

						@Override
						public void createIndex(DBCollection table) {
							// TODO Auto-generated method stub
							table.createIndex(
					                new BasicDBObject()
					                	.append("msgID", 1)
					                	.append("logDate", 1), "msgIDAndLogDateIndex");
						}
						
					});
		} catch (Exception e) {
			logger.error("init fail!");
		}
	}
	
	public List<Message> queryById(String id) throws Exception {
		
		BasicDBObject query = new BasicDBObject();
		query = new BasicDBObject("msgID", id);

		DBCursor dbCursor = super.getTable().find(query);

		return super.transFormList(dbCursor, Message.class);
	}		
	
	@Override
	public List<Message> queryTypeAfter(
			String type, long timestamp, int skip, int limit) throws Exception {
		
		BasicDBObject query = new BasicDBObject();
		query = new BasicDBObject("type", type).append("logDate", new BasicDBObject("$gt", timestamp));
		DBCursor dbCursor = super.getTable().find(query).skip(skip).limit(limit).sort(new BasicDBObject("msgID", -1));

		return super.transFormList(dbCursor, Message.class);
	}
	
	public List<Message> queryTypeBefore(
			String type, long timestamp, int skip, int limit) throws Exception {
		
		BasicDBObject query = new BasicDBObject();
		ArrayList<BasicDBObject> arrayList = new ArrayList<BasicDBObject>();
		arrayList.add(new BasicDBObject().append("type", type));
		arrayList.add(new BasicDBObject().append("logDate", new BasicDBObject().append("$le", timestamp)));
		query.put("$and", arrayList);		
		DBCursor dbCursor = super.getTable().find(query).skip(skip).limit(limit).sort(new BasicDBObject("msgID", -1));
		
		return super.transFormList(dbCursor, Message.class);
	}

}
