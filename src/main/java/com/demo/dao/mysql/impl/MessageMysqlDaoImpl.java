package com.demo.dao.mysql.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Override
	public int update(Message message) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder()//
				.append("UPDATE message")
				.append(" SET body = :body")
				.append(" ,log_date = :log_date")
				.append(" ,msg_id = :msg_id")
				.append(" ,receiver = :receiver")
				.append(" ,sender = :sender")
				.append(" ,type = :type")
				.append(" WHERE id = :id");	
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
	public List<Message> getbyType(String type) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * from message")
				.append(" WHERE type = :type");

		List<Message> result = new ArrayList<>();

		try {
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("type", type);
			
			List<Message> list = getJdbcTemplate(1).query(
					sql.toString(), 
					params,
					new BeanPropertyRowMapper<Message>(Message.class));
			
			result.addAll(list);
		} catch (EmptyResultDataAccessException e) {
		}

		return result;
	}
	
	@Override
	public List<Message> getSender(String sender, String type) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * from message")
				.append(" WHERE sender = :sender and type = :type");

		List<Message> result = new ArrayList<>();

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("sender", sender);
			params.addValue("type", type);
			
			List<Message> list = getJdbcTemplate(1).query(
					sql.toString(), 
					params,
					new BeanPropertyRowMapper<Message>(Message.class));
			
			result.addAll(list);
		} catch (EmptyResultDataAccessException e) {
		}

		return result;
	}

	@Override
	public List<Message> getReceiver(String receiver, String type) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * from message")
				.append(" WHERE receiver = :receiver and type = :type");

		List<Message> result = new ArrayList<>();

		try {
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("receiver", receiver);
			params.addValue("type", type);
			
			List<Message> list = getJdbcTemplate(1).query(
					sql.toString(), 
					params,
					new BeanPropertyRowMapper<Message>(Message.class));
			
			result.addAll(list);
		} catch (EmptyResultDataAccessException e) {
		}

		return result;
	}
}
