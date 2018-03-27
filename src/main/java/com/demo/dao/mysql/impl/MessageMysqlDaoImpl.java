package com.demo.dao.mysql.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import com.demo.dao.mysql.MessageMysqlDao;
import com.demo.dao.mysql.base.AbstractShardingDao;
import com.demo.model.Message;


@Repository
public class MessageMysqlDaoImpl extends AbstractShardingDao implements MessageMysqlDao {
//public class MessageMysqlDaoImpl implements MessageMysqlDao {

	@Override
	public int insert(Message message) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder()//
				.append("INSERT INTO message")
				.append(" (id, body, log_date, msg_id, receiver, sender, type)")
				.append(" VALUES ")//
				.append("(:id, :body, :log_date, :msg_id, :receiver, :sender, :type)");

		//
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("id", message.getId())
				.addValue("body", message.getBody())//
				.addValue("log_date", message.getLogDate())//
				.addValue("msg_id", message.getMsgId())//
				.addValue("receiver", message.getReceiver())//
				.addValue("sender", message.getSender())//
				.addValue("type", message.getType());
		//
		return getJdbcTemplate(1).update(sql.toString(), params);
	}
	
	@Override
	public int insert2(Message message) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder()//
				.append("INSERT INTO message")
				.append(" (id, body, log_date, msg_id, receiver, sender, type)")
				.append(" VALUES ")//
				.append("(:id, :body, :log_date, :msg_id, :receiver, :sender, :type)");

		//
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("id", message.getId())
				.addValue("body", message.getBody())//
				.addValue("log_date", message.getLogDate())//
				.addValue("msg_id", message.getMsgId())//
				.addValue("receiver", message.getReceiver())//
				.addValue("sender", message.getSender())//
				.addValue("type", message.getType());
		//
		return getJdbcTemplate(2).update(sql.toString(), params);
	}	

//	@Autowired
//	private BasicDataSource shardingDataSourceTarget;	
	
//	@Autowired
//	private BasicDataSource shardingDataSource2Target;
	
//	private NamedParameterJdbcTemplate getJdbcTemplate(int dbId) {
//		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(shardingDataSourceTarget);
//		
//		
//		
//		
//		
//		return jdbcTemplate;
//	}

}
